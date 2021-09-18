package com.aspire.mirror.ops.biz;

import static com.aspire.mirror.ops.api.domain.OpsStatusEnum.STATUS_100;
import static com.aspire.mirror.ops.api.domain.OpsStatusEnum.STATUS_101;
import static com.aspire.mirror.ops.api.domain.OpsStatusEnum.STATUS_102;
import static com.aspire.mirror.ops.api.domain.OpsStatusEnum.STATUS_6;
import static com.aspire.mirror.ops.api.domain.OpsStatusEnum.STATUS_7;
import static com.aspire.mirror.ops.api.domain.OpsStepDTO.OPS_TYPE_FILE_TRANSFER;
import static com.aspire.mirror.ops.api.domain.OpsStepDTO.OPS_TYPE_RESULT_FILE_STORE;
import static com.aspire.mirror.ops.api.domain.OpsStepDTO.OPS_TYPE_SCRIPT;
import static com.aspire.mirror.ops.api.domain.OpsStepDTO.PAUSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.dao.*;
import com.aspire.mirror.ops.helper.SensitiveHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.util.ZipUtil;
import com.aspire.mirror.ops.api.domain.OpsStepDTO.ReplaceAttrDefine;
import com.aspire.mirror.ops.api.util.PasswordUtil;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail;
import com.aspire.mirror.ops.biz.model.OpsAgentInfo;
import com.aspire.mirror.ops.biz.model.OpsMessage;
import com.aspire.mirror.ops.biz.model.OpsMessageMeta;
import com.aspire.mirror.ops.biz.model.OpsPipelineExecuteTimeoutEvent;
import com.aspire.mirror.ops.clientservice.RbacClient;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.AgentProxyInfo;
import com.aspire.mirror.ops.domain.OpsPipeline;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;
import com.aspire.mirror.ops.domain.OpsStep;
import com.aspire.mirror.ops.domain.OpsStepInstance;
import com.aspire.mirror.ops.helper.SshdHelper;
import com.aspire.mirror.ops.util.JsonUtil;
import com.aspire.mirror.ops.util.SshUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jayway.jsonpath.DocumentContext;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;


/**
 * 项目名称: ops-service
 * <p/>
 * <p>
 * 类名: OpsActionBiz
 * <p/>
 * <p>
 * 类功能描述: ops操作管理业务类
 * <p/>
 *
 * @author pengguihua
 * @version V1.0
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有
 * @date 2019年10月22日
 */
@Slf4j
@Service
@Transactional
public class OpsActionBiz implements ApplicationEventPublisherAware {
    private static final int IN_QUERY_BATCH_COUNT = 1000;
    private static final String STEP_RUN_TIME_OUT_CHECK_LOCK = "LOCK_STEP_RUN_TIMEOUT_CHECK";
    private static final String STEP_INST_PARAM_PATH = "/customize_param_output/";
    private static final String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|", "!"};
    private static final String[] fbsArr2 = {"$", "``", "\""};
    @Autowired
    private AgentDataDao agentDataDao;

    @Autowired
    private AgentHostDataBiz agentHostDataBiz;

    @Autowired
    private OpsDataDao opsBaseDataDao;
    @Autowired
    private CommonSftpServerConfig sftpConfig;

    @Autowired
    private SshdHelper sshdHelper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${ops.ops-message-topic-prefix:ops_message}")
    private String opsMessageTopicPrefix;

    @Autowired(required = false)
    private RedissonClient redissonClient;

//    @Autowired
//    private SensitiveConfigDao sensitiveConfigDao;
//
//    @Autowired
//    private SensitiveRuleDao sensitiveRuleDao;
//
//    @Autowired
//    private SensitiveReviewDao sensitiveReviewDao;
//
//    @Autowired
//    private SensitiveRuleWhiteDao sensitiveRuleWhiteDao;
//
//    @Autowired
//    private SensitiveLevelDao sensitiveLevelDao;

    @Autowired
    private SensitiveHelper sensitiveHelper;



    private ApplicationEventPublisher publisher;



    @Value("${file_regenerate_temp:D://temp/}")
    private String tempPath;

//    @Autowired
//    private SshdHelper sshdHelper;

    @Autowired
    private OpsFileBiz opsFileBiz;

//    @Autowired
//    private OpsBaseDataBiz opsBaseDataBiz;

    /**
     * 功能描述: 处理agent执行ops的step结果
     * <p>
     *
     * @param agentStepResult
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processOpsAgentStepResult(OpsActionAgentResult agentStepResult) {
        OpsMessageMeta meta = agentStepResult.getMeta();
        AgentOpsResultDetail opsResultDetail = agentStepResult.getOpsResultDetail();
        OpsStepInstance stepInstance = opsBaseDataDao.queryStepInstanceById(meta.getStepInstanceId());

        OpsAgentStepInstanceResult agentResult = agentStepResult.retrieveAgentStepInstanceResult();
        if (opsResultDetail.isOver()) {
            agentResult.calculateTotalTime(stepInstance.getStartTime());
        }
        OpsAgentStepInstanceResult existOpsResult = opsBaseDataDao.queryAgentStepInstanceResultByKeys(
                agentResult.getStepInstanceId(), agentResult.getTargetHost());
        if (existOpsResult == null) {
            opsBaseDataDao.insertAgentStepInstanceResult(agentResult);
        } else {
            opsBaseDataDao.updateAgentStepInstanceResult(agentResult);
        }
        // 更新参数值表 ---脚本执行成功则认为生效，否则认为无效
        if (agentResult.getAspNodeResult() != null && agentResult.getAspNodeResult() == 0) {
            OpsParamValue opsParamValue = new OpsParamValue();
            opsParamValue.setAgentIp(agentResult.getTargetHost());
            opsParamValue.setStepInstanceId(agentResult.getStepInstanceId());
            opsParamValue.setIsValid("1");
            opsBaseDataDao.updateValidByHostAndStepInstId(opsParamValue);
        }

        if (!opsResultDetail.isOver()) {
            return;
        }

        OpsStepInstance stepInstanceUpdateData = stepInstance.constructUpdateData(opsResultDetail);

        OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel param = new OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel();
        param.setStepInstanceId(meta.getStepInstanceId());
        List<OpsAgentStepInstanceResult> instanceResultList = opsBaseDataDao.queryOpsStepAgentRunResultList(param);
        Set<Integer> aspNodeResultSet = instanceResultList.stream().filter(item -> item.getAspNodeResult() != null).map(OpsAgentStepInstanceResult::getAspNodeResult).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(aspNodeResultSet)) {
            if (aspNodeResultSet.contains(AspNodeResultEnum.STATUS_1.getStatusCode())) {
                stepInstanceUpdateData.setAspNodeResult(AspNodeResultEnum.STATUS_1.getStatusCode());
            } else {
                stepInstanceUpdateData.setAspNodeResult(AspNodeResultEnum.STATUS_0.getStatusCode());
            }
        }
        opsBaseDataDao.updateStepInstance(stepInstanceUpdateData);

        postProcessOpsStepInstanceUpdate(stepInstanceUpdateData,
                OpsTriggerWayEnum.fromStatusCode(agentStepResult.getMeta().getTriggerWay()), meta.getExtendMeta());
    }

    /**
     * 功能描述: 执行step instance更新后的后续处理;
     * <p>
     * 包含以下逻辑：<br/>
     * 1. 如果当前stepInstance失败或超时, 更新相应的pipeline完成状态;  <br/>
     * 2. 如果当前stepInstance成功, 如果不存在下一个step Instance, 更新pipeline的完成状态;  否则, 判断下一个stepInstance是否暂停, 未暂停则触发执行;
     * 3. 生成密码参数产出zip，上传ftp，并通过文件管理提供下载
     * 4. 处理结果文件汇聚
     *
     * @param stepInstance
     * @param triggerWay
     * @param extendMeta
     */
    private void postProcessOpsStepInstanceUpdate(
            OpsStepInstance stepInstance, OpsTriggerWayEnum triggerWay, OpsMessageExtendMeta extendMeta) {
        if (OpsStatusEnum.STATUS_100.getStatusCode().equals(stepInstance.getStatus())) {
            return;
        }
        // 生成有效密码产出文件
        stepInstance = generateParamResult(stepInstance);

        if (stepInstance.isStepOverAndSuccess()) {
            // 结果保存步骤汇聚文件处理
            OpsStepInstance stepInstance1 = opsBaseDataDao.queryStepInstanceById(stepInstance.getStepInstanceId());
            if (stepInstance1.getOpsType() == 2) {
                OpsStep opsStep = opsBaseDataDao.queryOpsStepById(stepInstance1.getStepId());
                if (opsStep.getFileStoreConvergeType() != null && opsStep.getFileStoreConvergeType() == 1) {
                    // 追加汇聚
                    this.outputPackage(stepInstance1, opsStep);
                } else if (opsStep.getFileStoreConvergeType() != null && opsStep.getFileStoreConvergeType() == 2) {
                    // 分类汇聚
                    this.classifyPackage(stepInstance1, opsStep);
                }
            }

            int nextOrder = stepInstance.getOrd() + 1;
            OpsStepInstance nextStepInstance
                    = opsBaseDataDao.queryStepInstanceByPipelineOrder(stepInstance.getPipelineInstanceId(), nextOrder);
            // 未存在下一个step, 更新整个pipeline状态
            if (nextStepInstance == null) {
                List<OpsStepInstance> stepInstList = opsBaseDataDao.queryStepInstListByPipelineInstId(stepInstance.getPipelineInstanceId());
                Set<Integer> aspNodeResultSet = stepInstList.stream().map(OpsStepInstance::getAspNodeResult).collect(Collectors.toSet());
                Integer aspNodeResult = null;
                if (CollectionUtils.isNotEmpty(aspNodeResultSet)) {
                    if (aspNodeResultSet.contains(AspNodeResultEnum.STATUS_1.getStatusCode())) {
                        aspNodeResult = AspNodeResultEnum.STATUS_1.getStatusCode();
                    } else {
                        aspNodeResult = AspNodeResultEnum.STATUS_0.getStatusCode();
                    }
                }

                updatePipelineInstance(stepInstance.getPipelineInstanceId(), stepInstance.getStatus(), new Date(), aspNodeResult);
            }
            // 存在下一个step, 但当前step配置了"完成后暂停"
            else if (PAUSE == stepInstance.getPauseFlag()) {
                nextStepInstance.setStatus(STATUS_6.getStatusCode());
                opsBaseDataDao.updateStepInstance(nextStepInstance);
            }
            // 激活下一个step
            else {
                activeOpsStepIntance(nextStepInstance, triggerWay, extendMeta);
            }
        } else if (STATUS_101.getStatusCode().equals(stepInstance.getStatus())
                || STATUS_102.getStatusCode().equals(stepInstance.getStatus())
                || STATUS_7.getStatusCode().equals(stepInstance.getStatus())) {
            updatePipelineInstance(stepInstance.getPipelineInstanceId(), stepInstance.getStatus(), new Date(), null);
        }
    }

    private GeneralResponse classifyPackage(OpsStepInstance stepInstance, OpsStep opsStep) {
        // step 1 整理Path
        Map<String, Set<String>> parsePathMap = Maps.newHashMap();
        String classifyName = getClassifyNameAndPasePathMap(stepInstance.getStepInstanceId(), parsePathMap);
        // step 2 创建rootPath
        String rootPath = tempPath + "output" + File.separator + "分类汇聚产出_" + stepInstance.getStepInstanceName() + "_";
        if (classifyName != null) {
            rootPath = rootPath + classifyName + "_" + stepInstance.getStepInstanceId();
        } else {
            rootPath = rootPath + stepInstance.getStepInstanceId();
        }
        File rootFile = new File(rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        try {
            if (!parsePathMap.isEmpty()) {
                // step 3 分类汇聚处理
                classifyConverge(parsePathMap, classifyName, rootPath, rootFile);
                // step 4 5 6
                GeneralResponse uploadResult = uploadAndSaveFileRecode(stepInstance, rootFile, rootPath, opsStep);
                if (uploadResult != null) return uploadResult;
            }
        } catch (Exception e) {
            log.error("OpsActionBiz[classifyPackage] parse output is error!", e);
        }
        return new GeneralResponse();
    }

    private void classifyConverge(Map<String, Set<String>> parsePathMap, String classifyName, String rootPath, File rootFile) throws IOException {
        for (String typeName : parsePathMap.keySet()) {
            File instanceResultPath;
            if (StringUtils.isNotEmpty(typeName)) {
                instanceResultPath = new File(rootPath + File.separator + classifyName + "_" + typeName);
                if (!instanceResultPath.exists()) {
                    instanceResultPath.mkdirs();
                }
            } else {
                instanceResultPath = rootFile;
            }
            for (String filePath : parsePathMap.get(typeName)) {
                SshUtil.SshdServer sftpServer = sshdHelper.getSshdServer();
                Pair<SshUtil.SshResultWrap, Path> downResult
                        = null;
                downResult = SshUtil.sftpDownload(sftpServer, filePath, instanceResultPath.getCanonicalPath());
                SshUtil.SshResultWrap wrap = downResult.getLeft();
                if (!wrap.isSuccess()) {
                    log.error("OpsActionBiz[classifyPackage] sftp download is error path is {} error log is {}", filePath, wrap.getBizLog());
                }
            }

        }
    }

    private String getClassifyNameAndPasePathMap(Long instanceId, Map<String, Set<String>> parsePathMap) {
        OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel param = new OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel();
        param.setStepInstanceId(instanceId);
        List<OpsAgentStepInstanceResult> instanceResultList = opsBaseDataDao.queryOpsStepAgentRunResultList(param);
        String classifyName = null;
        for (OpsAgentStepInstanceResult stepInstanceResult : instanceResultList) {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(stepInstanceResult.getAspNodeMessage())) {
                String[] pathList = stepInstanceResult.getAspNodeMessage().split(",");
                for (String path : pathList) {
                    if (path.indexOf("/") != -1) {
                        String parentDir = path.substring(0, path.lastIndexOf("/"));
                        if (parentDir.indexOf("/") != -1) {
                            classifyName = parentDir.substring(parentDir.lastIndexOf("/") + 1);
                        }
                    }
                    String typeName = "";
                    // 按点区分类型
                    if (path.indexOf(".") != -1) {
                        typeName = path.substring(path.lastIndexOf(".") + 1);
                    }
                    if (parsePathMap.get(typeName) == null) {
                        Set<String> pathSet = Sets.newHashSet();
                        pathSet.add(path);
                        parsePathMap.put(typeName, pathSet);
                    } else {
                        parsePathMap.get(typeName).add(path);
                    }

                }
            }
        }
        return classifyName;
    }

    private GeneralResponse uploadAndSaveFileRecode(OpsStepInstance stepInstance, File rootFile, String rootPath, OpsStep opsStep) throws Exception {
        // step 4 汇聚文件夹生成压缩文件
        String generateFileName = rootFile.getParent() + File.separator + rootFile.getName() + ".zip";
        ZipUtil.toZip(rootPath, generateFileName, true);

        // step 5 上传追加汇聚zip文件到sftp
        Pair<SshUtil.SshResultWrap, String> uploadResult = sshdHelper.uploadFile(new File(generateFileName).toPath(), OpsFile.INSTANCE_OUTPUT_DIR);

        // step 6 提供下载  产出写入到文件管理
        if (uploadResult.getLeft().isSuccess()) {
            // 修改实例日志状态
            OpsPipelineInstance pipelineInstance = new OpsPipelineInstance();
            pipelineInstance.setPipelineInstanceId(stepInstance.getPipelineInstanceId());
            pipelineInstance.setOutputPath(uploadResult.getRight());
            opsBaseDataDao.updatePipelineInstance(pipelineInstance);

            // 写入文件管理
            OpsFile opsFile = new OpsFile();
            if (opsStep.getFileStoreSafety() == 0) {
                opsFile.setFileType(OpsFile.FILE_TYPE_1);
            } else {
                opsFile.setFileType(OpsFile.FILE_TYPE_3);
            }
            opsFile.setFileGenerateType(OpsFile.GENERATE_TYPE_2);
            opsFile.setFileClass(OpsFileClassEnum.FILE_CLASS_4.getStatusCode().toString());
            opsFile.setFileVersion(OpsFile.AUTO_OUPUT_VERSION + stepInstance.getStepInstanceId());
            opsFile.setFileName(rootFile.getName() + ".zip");
            opsFile.setFilePath(uploadResult.getRight());
            opsFile.setRelationId(stepInstance.getStepInstanceId().toString());
            opsFile.setRelationName(stepInstance.getStepInstanceName());
            opsFileBiz.saveFile(opsFile);
            return new GeneralResponse(true, null, uploadResult.getRight());
        } else {
            log.error("upload pipeline instance log file to SFTP is failed!");
        }
        return null;
    }

    public GeneralResponse outputPackage(OpsStepInstance stepInstance, OpsStep opsStep) {
//        OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance.getPipelineInstanceId());
        // step 1 生成汇聚文件夹
        String rootPath = tempPath + "output" + File.separator + "追加汇聚产出_" + stepInstance.getStepInstanceName() + "_" + stepInstance.getStepInstanceId();
        File rootFile = new File(rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        // step 2 将结果文件根据文件名做一个汇聚
        Map<String, Set<String>> parsePathMap = getPathSetMap(stepInstance);
        try {
            if (!parsePathMap.isEmpty()) {
                // step 3 处理追加汇聚，按同名文件下载下来后追加汇合一个文件  存放到汇聚文件夹
                appendConverge(rootPath, parsePathMap);
                // step 4 5 6
                GeneralResponse uploadResult = uploadAndSaveFileRecode(stepInstance, rootFile, rootPath, opsStep);
                if (uploadResult != null) return uploadResult;
            }
        } catch (Exception e) {
            log.error("parse output is error!", e);
        }

        return new GeneralResponse();
    }

    private void appendConverge(String rootPath, Map<String, Set<String>> parsePathMap) throws IOException {
        for (String fileName : parsePathMap.keySet()) {
            File instanceResultFile = new File(rootPath + File.separator + fileName);
            if (!instanceResultFile.exists()) {
                instanceResultFile.createNewFile();
            }
            String tableHeader = null;
            for (String filePath : parsePathMap.get(fileName)) {
                SshUtil.SshdServer sftpServer = sshdHelper.getSshdServer();

                Path localDownDir = Files.createTempDirectory("output_file_down");
                Pair<SshUtil.SshResultWrap, Path> downResult
                        = SshUtil.sftpDownload(sftpServer, filePath, localDownDir.toFile().getCanonicalPath());
                SshUtil.SshResultWrap wrap = downResult.getLeft();
                if (!wrap.isSuccess()) {
                    throw new RuntimeException(wrap.getBizLog());
                }
                if (fileName.endsWith(".csv")) {
                    Writer writer = new OutputStreamWriter(new FileOutputStream(instanceResultFile, true), "GB2312");
                    List<String> lineAll;
                    try {
                        lineAll = Files.readAllLines(downResult.getRight());
                    } catch (MalformedInputException malformedInputException) {
                        lineAll = Files.readAllLines(downResult.getRight(), Charset.forName("GB2312"));
                    }
                    if (tableHeader == null) {
                        tableHeader = lineAll.get(0);
                        writer.write(lineAll.get(0));
                        writer.write(System.getProperty("line.separator"));
                    } else {
                        // 如果已经添加表头
                        if (!tableHeader.equals(lineAll.get(0))) {
                            writer.write(lineAll.get(0));
                            writer.write(System.getProperty("line.separator"));
                        }
                    }
                    for (int i = 1; i < lineAll.size(); i++) {
                        writer.write(lineAll.get(i));
                        writer.write(System.getProperty("line.separator"));
                    }
                    writer.flush();
                    IOUtils.closeQuietly(writer);
                } else {
                    OutputStream out = new FileOutputStream(instanceResultFile, true);
                    out.write(Files.readAllBytes(downResult.getRight()));
                    IOUtils.closeQuietly(out);
                }

//                        out.write(Files.readAllBytes(downResult.getRight()));
//                        IOUtils.closeQuietly(out);
            }
        }
    }

    private Map<String, Set<String>> getPathSetMap(OpsStepInstance stepInstance) {
        Map<String, Set<String>> parsePathMap = Maps.newHashMap();
        //文件存储
        OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel param = new OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel();
        param.setStepInstanceId(stepInstance.getStepInstanceId());
        List<OpsAgentStepInstanceResult> instanceResultList = opsBaseDataDao.queryOpsStepAgentRunResultList(param);
        for (OpsAgentStepInstanceResult stepInstanceResult : instanceResultList) {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(stepInstanceResult.getAspNodeMessage())) {
                String[] pathList = stepInstanceResult.getAspNodeMessage().split(",");
                for (String path : pathList) {
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    if (parsePathMap.get(fileName) == null) {
                        Set<String> pathSet = Sets.newHashSet();
                        pathSet.add(path);
                        parsePathMap.put(fileName, pathSet);
                    } else {
                        parsePathMap.get(fileName).add(path);
                    }
                }
            }
        }
        return parsePathMap;
    }

    private OpsStepInstance generateParamResult(OpsStepInstance stepInstance) {
        OpsParamValue queryParam = new OpsParamValue();
//        queryParam.setIsValid("1");
        queryParam.setStepInstanceId(stepInstance.getStepInstanceId());
        List<OpsParamValueDetail> paramValueList = opsBaseDataDao.queryParamValueList(queryParam);
        // 获取设备信息，拼装密码参数产出
        if (CollectionUtils.isNotEmpty(paramValueList)) {
            for (OpsParamValueDetail opsParamValueDetail : paramValueList) {
                SimpleAgentHostInfo hostInfo = agentHostDataBiz.queryAgentInfoByProxyIdConcatIP(opsParamValueDetail.getAgentIp());
                if (hostInfo != null) {
                    String deviceClass = (String) hostInfo.getExtendAttrMap().get("device_class_dict_note_name");
                    String deviceType = (String) hostInfo.getExtendAttrMap().get("device_type_dict_note_name");
                    String deviceName = (String) hostInfo.getExtendAttrMap().get("device_name");
                    String bizSystem = (String) hostInfo.getExtendAttrMap().get("bizSystem_bizSystem_name");
//                    String deviceClass = (String) hostInfo.getExtendAttrMap().get("device_class");
//                    String deviceType = (String) hostInfo.getExtendAttrMap().get("device_type");
//                    String deviceName = (String) hostInfo.getExtendAttrMap().get("device_name");
//                    String bizSystem = (String) hostInfo.getExtendAttrMap().get("bizSystem");
                    opsParamValueDetail.setDeviceClass(deviceClass);
                    opsParamValueDetail.setDeviceType(deviceType);
                    opsParamValueDetail.setDeviceName(deviceName);
                    opsParamValueDetail.setBizSystem(bizSystem);
                }
            }
            FileOutputStream fos = null;
            try {
                OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance.getPipelineInstanceId());
                stepInstance = opsBaseDataDao.queryStepInstanceById(stepInstance.getStepInstanceId());
                // 生成excel，并根据打包密码打成zip包
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("实例密码参数列表", "参数列表"),
                        OpsParamValueDetail.class, paramValueList);
                String version = "stepInst_param_" + stepInstance.getStepInstanceId();
                String rootPath = tempPath + File.separator + version;
                File rootFile = new File(rootPath);
                if (!rootFile.exists()) {
                    rootFile.mkdirs();
                }
                String filePath = rootPath + File.separator + "密码值_" + opsPipelineInstance.getPipelineInstanceName() + "_" + stepInstance.getStepInstanceName() + ".xls";
                fos = new FileOutputStream(new File(filePath));
                workbook.write(fos);
                String zipFileName = "密码参数产出_" + opsPipelineInstance.getPipelineInstanceName() + "_" + stepInstance.getStepInstanceName() + "_" + stepInstance.getStepInstanceId() + ".zip";
                String zipPath = rootPath + File.separator + zipFileName;
                ZipUtil.zipFileAndEncrypt(filePath, zipPath, stepInstance.getPackagePassword());
                //上传sftp并保存到文件管理
                Pair<SshUtil.SshResultWrap, String> result = sshdHelper.uploadFile(new File(zipPath).toPath(), STEP_INST_PARAM_PATH);
                // 写入文件管理
                OpsFile opsFile = new OpsFile();
                opsFile.setFileType(OpsFile.FILE_TYPE_1);
                opsFile.setFileGenerateType(OpsFile.GENERATE_TYPE_2);
                opsFile.setFileClass(OpsFileClassEnum.FILE_CLASS_6.getStatusCode().toString());
                opsFile.setFileVersion(version);
                opsFile.setFileName(zipFileName);
                opsFile.setFilePath(result.getRight());
                opsFile.setRelationId(stepInstance.getStepInstanceId().toString());
                opsFile.setRelationName(stepInstance.getStepInstanceName());
                opsFileBiz.saveFile(opsFile);

            } catch (Exception e) {
                log.error("处理参数文件失败！", e);
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败！");
                }
            }
        }
        return stepInstance;
    }

    /**
     * 功能描述: 发布作业执行超时事件
     * <p>
     *
     * @param stepInstance
     */
    private void publishPipelineInstanceTimeoutEvent(OpsStepInstance stepInstance) {
        OpsPipelineInstance pipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance
                .getPipelineInstanceId());
        OpsPipeline pipeline = opsBaseDataDao.queryPipelineById(pipelineInstance.getPipelineId());
        OpsPipelineExecuteTimeoutEvent event = new OpsPipelineExecuteTimeoutEvent(stepInstance, pipeline,
                pipelineInstance);
        this.publisher.publishEvent(event);
    }

    /**
     * 功能描述: 更新pipeline状态信息
     * <p>
     *
     * @param pipelineInstanceId
     * @param endTime
     * @param statusCode
     */
    protected void updatePipelineInstance(Long pipelineInstanceId, Integer statusCode, Date endTime, Integer aspNodeResult) {
        OpsPipelineInstance pipelineInstance = opsBaseDataDao.queryPipelineInstanceById(pipelineInstanceId);
        OpsPipelineInstance updateData = new OpsPipelineInstance();
        updateData.setPipelineInstanceId(pipelineInstanceId);
        updateData.setStatus(statusCode);
        updateData.setAspNodeResult(aspNodeResult);
        if (endTime != null) {
            updateData.setStartTime(pipelineInstance.getStartTime());
            updateData.setEndTime(endTime);
            updateData.setTotalTime(updateData.calcTotalSeconds());
        }
        opsBaseDataDao.updatePipelineInstance(updateData);
    }

    /**
     * 功能描述: 激活 step
     * <p>
     */
    private void activeOpsStepIntance(OpsStepInstance stepInstance, OpsTriggerWayEnum triggerWay) {
        activeOpsStepIntance(stepInstance, triggerWay, null);
    }

    private void activeOpsStepIntance(
            OpsStepInstance stepInstance, OpsTriggerWayEnum triggerWay, OpsMessageExtendMeta extendMeta) {
        stepInstance.setStartTime(new Date());
        stepInstance.setStatus(STATUS_100.getStatusCode());
        opsBaseDataDao.updateStepInstance(stepInstance);

        sensitiveHelper.generateSudoAuthParam(stepInstance);

        // 更新pipelineInstance
        updatePipelineInstance(stepInstance.getPipelineInstanceId(), stepInstance.getStatus(), null, null);
        // 文件分发类型处理
        if (extendMeta == null && stepInstance.getFileType() != null && stepInstance.getFileType() != 0) {
            extendMeta = new OpsMessageExtendMeta(stepInstance.getFileType() + 10);
        }
        // 发布ops消息
        publishOpsMessage(stepInstance, triggerWay, extendMeta);
    }


    /**
     * 功能描述: 生成并发布ops操作消息
     * <p>
     *
     * @param stepInstance
     * @param triggerWay
     */
    private void publishOpsMessage(OpsStepInstance stepInstance, OpsTriggerWayEnum triggerWay, OpsMessageExtendMeta
            extendMeta) {
        //目标设备重新设置
        List<String> hostList = stepInstance.getTargetHosts();

        List<TargetExecObject> targetExecObjectList = stepInstance.getTargetExecObject();
        if (!CollectionUtils.isEmpty(targetExecObjectList)) {
            for (TargetExecObject targetExecObject : targetExecObjectList) {
                AgentHostQueryModel agentHostQueryModel = new AgentHostQueryModel();
                agentHostQueryModel.setBizSystem(targetExecObject.getBizSystem());
                agentHostQueryModel.setDepartment1(targetExecObject.getDepartment1());
                agentHostQueryModel.setDepartment2(targetExecObject.getDepartment2());
                agentHostQueryModel.setOsType(targetExecObject.getOsType());
                agentHostQueryModel.setOsStatus(targetExecObject.getOsStatus());
                agentHostQueryModel.setPoolName(targetExecObject.getPoolName());
                PageListQueryResult<NormalAgentHostInfo> hostPageResult = agentHostDataBiz.queryNormalAgentHostList(agentHostQueryModel);
                if (!CollectionUtils.isEmpty(hostPageResult.getDataList())) {
                    List<String> itemHost = hostPageResult.getDataList().stream().map(item -> item.getProxyId() + ":" + item.getAgentIp()).collect(Collectors.toList());
                    hostList.addAll(itemHost);
                }
            }
            stepInstance.setTargetHosts(hostList);
        }
        Map<String, AgentHostInfo> agentDetailMap = retriveAgentHostInfoMap(stepInstance);
        //自定义参数生成

        Map<String, Map<String, Pair<String, String>>> customizeParams = retriveCustomizeParams(stepInstance, agentDetailMap.keySet());


        OpsMessage opsMessage = OpsMessage.generateOpsMessage(stepInstance, agentDetailMap, sftpConfig, customizeParams);

        opsMessage.getMeta().setTriggerWay(triggerWay.getStatusCode());
        opsMessage.getMeta().setExtendMeta(extendMeta);
        // 拆分分发到每个PROXY对应的topic
        List<OpsAgentInfo> targetAgentList = null;
        if (OPS_TYPE_SCRIPT == opsMessage.getMeta().getOpsType().intValue()) {
            targetAgentList = opsMessage.getScript().getTargetHostList();
        } else if (OPS_TYPE_FILE_TRANSFER == opsMessage.getMeta().getOpsType().intValue()) {
            targetAgentList = opsMessage.getFileTransfer().getTargetHostList();
        } else if (OPS_TYPE_RESULT_FILE_STORE == opsMessage.getMeta().getOpsType().intValue()) {
            targetAgentList = opsMessage.getFileStore().getTargetHostList();
        }
        Map<Long, List<OpsAgentInfo>> proxyMapAgentList = groupByProxyId(targetAgentList);
        for (Map.Entry<Long, List<OpsAgentInfo>> entry : proxyMapAgentList.entrySet()) {
            OpsMessage clone = new OpsMessage();
            BeanUtils.copyProperties(opsMessage, clone);
            if (OPS_TYPE_SCRIPT == clone.getMeta().getOpsType().intValue()) {
                clone.getScript().setTargetHostList(entry.getValue());
            } else if (OPS_TYPE_FILE_TRANSFER == clone.getMeta().getOpsType().intValue()) {
                clone.getFileTransfer().setTargetHostList(entry.getValue());
            } else if (OPS_TYPE_RESULT_FILE_STORE == opsMessage.getMeta().getOpsType().intValue()) {
                clone.getFileStore().setTargetHostList(entry.getValue());
            }
            AgentProxyInfo proxyInfo = agentDataDao.queryAgentProxyById(entry.getKey());
            String proxyOpsMsgTopic = opsMessageTopicPrefix + "_" + proxyInfo.getProxyIdentity()
                    + "_" + triggerWay.getStatusCode();
            String messageKey = String.valueOf(clone.getMeta().getPipelineInstanceId());

            if (log.isDebugEnabled()) {
                log.debug("Try to send pipeline execute message to kafka, details: topic={} | opsMessageMeta={} ",
                        proxyOpsMsgTopic, JsonUtil.toJacksonJson(clone.getMeta()));
            }
            kafkaTemplate.send(proxyOpsMsgTopic, messageKey, JsonUtil.toJacksonJson(clone));
        }
    }

    private Map<String, Map<String, Pair<String, String>>> retriveCustomizeParams(OpsStepInstance stepInstance, Set<String> ipSet) {
        Map<String, Map<String, Pair<String, String>>> paramsMap = null;
        List<OpsParamReference> opsParamReferences = stepInstance.getOpsParamReferenceList();
        if (CollectionUtils.isNotEmpty(opsParamReferences)) {
            paramsMap = Maps.newHashMap();
//            for (String ip : ipSet) {
//                paramsMap.put(ip, Maps.newHashMap());
//            }
            for (OpsParamReference opsParamReference : opsParamReferences) {
                OpsParam opsParam = opsBaseDataDao.queryOpsParamById(opsParamReference.getParamId());
                opsParamReference.setReferParam(opsParam);
            }
            if (CollectionUtils.isNotEmpty(opsParamReferences)) {
                List<OpsParamValue> paramValueList = Lists.newArrayList();
                 for (OpsParamReference opsParamReference : opsParamReferences) {
                    OpsParam opsParam = opsBaseDataDao.queryOpsParamById(opsParamReference.getParamId());
                    opsParamReference.setReferParam(opsParam);
                    String value = null;
                    for (String ip : ipSet) {
                        OpsSpectreHostExt opsSpectreHostExt = agentHostDataBiz.queryHostExtByProxyIdAndAgentIp(ip.split(":")[0], ip.split(":")[1]);
                        SimpleAgentHostInfo agentHostInfo = agentHostDataBiz.queryAgentInfoByProxyIdConcatIP(ip);
                        if (paramsMap.get(ip) == null) {
                            Map<String, Pair<String, String>> paramItemMap = Maps.newHashMap();
                            paramsMap.put(ip, paramItemMap);
                        }
                        if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_2.getStatusCode())) {
                            value = PasswordUtil.getRandomPassword(20);
                            // 获取上一次生成的生效密码值，可优化为生效的密码值加入redis，这边通过redis获取
                            OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance.getPipelineInstanceId());
                            OpsParamValue oldParamValue = opsBaseDataDao.queryLastValidParamValueByIpAndPipelineIdAndParam(opsPipelineInstance.getPipelineId(), ip, stepInstance.getScriptParam());


                            OpsParamValue opsParamValue = new OpsParamValue(opsParamReference.getEntityParamCode(), value, stepInstance.getStepInstanceId(), stepInstance.getPipelineInstanceId(), ip, "0",
                                    oldParamValue != null ? oldParamValue.getParamValue() : "", stepInstance.getScriptParam());
                            paramValueList.add(opsParamValue);
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_3.getStatusCode())) {
                            if (value == null) {
                                value = PasswordUtil.getRandomPassword(20);
                            }
                            OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance.getPipelineInstanceId());
                            OpsParamValue oldParamValue = opsBaseDataDao.queryLastValidParamValueByIpAndPipelineIdAndParam(opsPipelineInstance.getPipelineId(), ip, stepInstance.getScriptParam());

                            OpsParamValue opsParamValue = new OpsParamValue(opsParamReference.getEntityParamCode(), value, stepInstance.getStepInstanceId(), stepInstance.getPipelineInstanceId(), ip, "0",
                                    oldParamValue != null ? oldParamValue.getParamValue() : "", stepInstance.getScriptParam());
                            paramValueList.add(opsParamValue);
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_4.getStatusCode())) {
                            if (value == null) {
                                value = opsParamReference.getParamValue();
                            }
                            OpsPipelineInstance opsPipelineInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance.getPipelineInstanceId());
                            OpsParamValue oldParamValue = opsBaseDataDao.queryLastValidParamValueByIpAndPipelineIdAndParam(opsPipelineInstance.getPipelineId(), ip, stepInstance.getScriptParam());

                            OpsParamValue opsParamValue = new OpsParamValue(opsParamReference.getEntityParamCode(), value, stepInstance.getStepInstanceId(), stepInstance.getPipelineInstanceId(), ip, "0",
                                    oldParamValue != null ? oldParamValue.getParamValue() : "", stepInstance.getScriptParam());
                            paramValueList.add(opsParamValue);
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_IP.getStatusCode())) {
                            if (agentHostInfo != null && agentHostInfo.getAgentIp() != null) {
                                value = agentHostInfo.getAgentIp();
                            }
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_BIZ_SYS.getStatusCode())) {
                            if (opsSpectreHostExt != null && opsSpectreHostExt.getBizSystemName() != null) {
                                value = opsSpectreHostExt.getBizSystemName();
                            } else {
                                value = "未知";
                            }
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_TASK_ID.getStatusCode())) {
                            if (stepInstance != null && stepInstance.getPipelineInstanceId() != null) {
                                value = String.valueOf(stepInstance.getPipelineInstanceId());
                            } else {
                                value = "未知";
                            }
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_POOL.getStatusCode())) {
                            if (agentHostInfo != null && agentHostInfo.getPool() != null) {
                                value = agentHostInfo.getPool();
                            } else {
                                value = "未知";
                            }
                        } else if (opsParam.getParamType().equals(ParamTypeEnum.TYPE_FUN.getStatusCode())) {  // 函数
                            value = opsParamReference.getParamValue();
                        } else { //常量
                            value = opsParamReference.getParamValue();
                        }
                        for (String key : fbsArr2) {
                            if (value.contains(key)) {
                                value = value.replace(key, "\\" + key);
                            }
                        }
                        paramsMap.get(ip).put(opsParamReference.getEntityParamCode(), Pair.of(opsParam.getParamType().equals(ParamTypeEnum.TYPE_FUN.getStatusCode()) ? "FUN" : "VAR", value));
                    }
                }
                if (CollectionUtils.isNotEmpty(paramValueList)) {
                    opsBaseDataDao.batchInsertParamValue(paramValueList);
                }
            }
        }
        return paramsMap;
    }

    private Map<Long, List<OpsAgentInfo>> groupByProxyId(List<OpsAgentInfo> agentList) {
        Map<Long, List<OpsAgentInfo>> result = new HashMap<>();
        agentList.forEach(agent -> {
            List<OpsAgentInfo> groupList = result.get(agent.getProxyId());
            if (groupList == null) {
                groupList = new ArrayList<>();
                result.put(agent.getProxyId(), groupList);
            }
            groupList.add(agent);
        });
        return result;
    }

    private Map<String, AgentHostInfo> retriveAgentHostInfoMap(OpsStepInstance stepInstance) {
        Map<String, AgentHostInfo> result = new HashMap<>();
        List<String> agentList = new ArrayList<>();
        agentList.addAll(stepInstance.getTargetHosts());

        if (CollectionUtils.isNotEmpty(stepInstance.getFileSource())) {
            stepInstance.getFileSource().parallelStream().filter(file -> {
                return CollectionUtils.isNotEmpty(file.getSourceAgentList());
            }).forEach(file -> {
                agentList.addAll(file.getSourceAgentList());
            });
        }

        for (int i = 0; ; i++) {
            int startIdx = i * IN_QUERY_BATCH_COUNT;
            if (startIdx >= agentList.size()) {
                break;
            }
            int endIdx = (i + 1) * IN_QUERY_BATCH_COUNT > agentList.size() ? agentList.size() : (i + 1) *
                    IN_QUERY_BATCH_COUNT;
            List<String> subQueryList = agentList.subList(startIdx, endIdx);
            if (CollectionUtils.isNotEmpty(subQueryList)) {
                List<AgentHostInfo> agentDetailList = agentDataDao.queryAgentDataListByProxyIdConcatIP(subQueryList);
                agentDetailList.parallelStream().forEach(detail -> {
                    result.put(detail.getConcatHost(), detail);
                });
            }
        }
        return result;
    }

    /**
     * 功能描述: 实时脚本执行
     * <p>
     *
     * @param scriptExecuteData
     * @return
     */
    @Transactional
    public GeneralResponse executeRealtimeScript(OpsScriptExecuteActionModel scriptExecuteData) {
        if (scriptExecuteData.getScript().getScriptId() != null) {
            OpsScript opsScript = this.opsBaseDataDao.queryOpsScriptById(scriptExecuteData.getScript().getScriptId());
            if (!opsScript.getAuditStatus().equals(OpsAuditStatusEnum.STATUS_1.getStatusCode())) {
                log.error("该脚本未审核通过，不能执行");
                return new GeneralResponse(false, "该脚本未审核通过，不能执行");
            }
        }
        OpsPipelineInstance onetimePipeline = OpsPipelineInstance.buildScriptExecutePipelineInstance(scriptExecuteData);
        this.opsBaseDataDao.insertPipelineInstance(onetimePipeline);

        OpsStepInstance onetimeInstance = OpsStepInstance.buildScriptExecuteStepInstance(scriptExecuteData);
        onetimeInstance.setPipelineInstanceId(onetimePipeline.getPipelineInstanceId());
        this.opsBaseDataDao.insertStepInstance(onetimeInstance);
        // 敏感指令处理
        List<OpsStepInstance> opsStepInstancesList = Lists.newArrayList();
        opsStepInstancesList.add(onetimeInstance);
        Integer result = sensitiveHelper.stepInstanceSenstiveProcess(opsStepInstancesList, onetimePipeline, OpsTriggerWayEnum
                .TRIGGER_BY_MANUAL);
        if (!result.equals(OpsStatusEnum.STATUS_5.getStatusCode())) {
            onetimePipeline.setStatus(result);
            opsBaseDataDao.updatePipelineInstance(onetimePipeline);
        } else {
            activeOpsStepIntance(onetimeInstance, OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
        }
        return new GeneralResponse(true, null, onetimeInstance.getStepInstanceId());
    }


    /**
     * 功能描述: 实时文件分发
     * <p>
     *
     * @param fileTransferData
     * @return
     */
    @Transactional
    public GeneralResponse executeRealtimeFileTransfer(OpsFileTransferActionModel fileTransferData) {
        OpsPipelineInstance onetimePipeline = OpsPipelineInstance.buildFileTransferPipelineInstance(fileTransferData);
        this.opsBaseDataDao.insertPipelineInstance(onetimePipeline);

        OpsStepInstance onetimeInstance = OpsStepInstance.buildScriptExecuteStepInstance(fileTransferData);
        onetimeInstance.setPipelineInstanceId(onetimePipeline.getPipelineInstanceId());
        this.opsBaseDataDao.insertStepInstance(onetimeInstance);
        activeOpsStepIntance(onetimeInstance, OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
        return new GeneralResponse(true, null, onetimeInstance.getStepInstanceId());
    }

    /**
     * 功能描述: 执行作业
     * <p>
     *
     * @param pipelineId
     * @param triggerWay 0 页面执行  1  定时执行  2 API调用    参考 OpsPipelineInstanceDTO
     *                   .TRIGGER_BY_MANUAL|TRIGGER_BY_JOB|TRIGGER_BY_API
     * @return
     */
    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, OpsTriggerWayEnum triggerWay) {
        return executePipeline(pipelineId, null, triggerWay);
    }

    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, List<Long> selectStepList, OpsTriggerWayEnum triggerWay) {
        return executePipeline(pipelineId, selectStepList, triggerWay, null);
    }

    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, OpsTriggerWayEnum triggerWay, Map<String, Object>
            replaceAttrs) {
        return executePipeline(pipelineId, null, triggerWay, replaceAttrs);
    }

    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, List<Long> selectStepList,
                                           OpsTriggerWayEnum triggerWay, Map<String, Object> replaceAttrs) {
        return executePipeline(pipelineId, selectStepList, triggerWay, replaceAttrs, null);
    }

    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, OpsTriggerWayEnum triggerWay,
                                           Map<String, Object> replaceAttrs, OpsMessageExtendMeta extendMeta) {
        return executePipeline(pipelineId, null, triggerWay, replaceAttrs, extendMeta);
    }

    /**
     * 功能描述: 执行作业, 如果  pipelineId 指定的作业为模板作业, 则使用replaceAttrs中的键值对替换模板作业的占位参数后执行
     * <p>
     *
     * @param pipelineId
     * @param selectStepList
     * @param triggerWay
     * @param replaceAttrs   要替换的占位参数键值对, 如果指定的作业为模板, 则使用此处的键值对替换占位参数后执行, 针对非模板作业执行，此属性可以为null
     * @param extendMeta     扩展meta信息
     * @return
     */
    @Transactional
    public GeneralResponse executePipeline(Long pipelineId, List<Long> selectStepList,
                                           OpsTriggerWayEnum triggerWay, Map<String, Object> replaceAttrs,
                                           OpsMessageExtendMeta extendMeta) {
        OpsPipeline pipeline = this.opsBaseDataDao.queryPipelineById(pipelineId);
        if (pipeline == null) {
            String tip = "this pipeline is not exist " + pipelineId;
            log.error(tip);
            return new GeneralResponse(false, "该作业不存在");
        }
        if (!pipeline.getAuditStatus().equals(OpsAuditStatusEnum.STATUS_1.getStatusCode())) {
            String tip = "this pipeline audit status is " + pipeline.getAuditStatus();
            log.error(tip);
            return new GeneralResponse(false, "该作业为非可执行状态");
        }
        Integer bizClassify = extendMeta == null ? null : extendMeta.getBizClassify();
        OpsPipelineInstance pipelineInstance = pipeline.newInstance(triggerWay, bizClassify);
        this.opsBaseDataDao.insertPipelineInstance(pipelineInstance);

        List<OpsStep> opsStepList = opsBaseDataDao.queryOpsStepListByPipelineId(pipelineId);
        if (CollectionUtils.isNotEmpty(selectStepList)) {
            opsStepList = opsStepList.stream().filter(step -> {
                return selectStepList.contains(step.getStepId());
            }).collect(Collectors.toList());
        }

        if (MapUtils.isNotEmpty(replaceAttrs)) {
            replaceTemplateAttrValues(opsStepList, replaceAttrs);
        }

        List<OpsStepInstance> stepInstanceList = generateStepInstanceList(pipelineInstance.getPipelineInstanceId(),
                opsStepList);
        for (OpsStepInstance stepInstance : stepInstanceList) {
            opsBaseDataDao.insertStepInstance(stepInstance);
        }
        // 敏感指令校验
        //自愈类型作业  不做敏感指令验证
        Integer status;
        pipelineInstance.setLabelId(pipeline.getLabelId());
        status = sensitiveHelper.stepInstanceSenstiveProcess(stepInstanceList, pipelineInstance, triggerWay);
        if (!status.equals(OpsStatusEnum.STATUS_5.getStatusCode())) {
            pipelineInstance.setStatus(status);
            opsBaseDataDao.updatePipelineInstance(pipelineInstance);
        } else {
            Collections.sort(stepInstanceList);
            activeOpsStepIntance(stepInstanceList.get(0), triggerWay, extendMeta);    // 激活第一个stepInstance
        }
        return new GeneralResponse(true, null, pipelineInstance.getPipelineInstanceId());
    }


    /**
     * 功能描述: 使用 replaceAttrs 键值对替换模板占位属性
     * <p>
     *
     * @param opsStepList
     * @param replaceAttrMap
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void replaceTemplateAttrValues(final List<OpsStep> opsStepList, final Map<String, Object> replaceAttrMap) {
        for (OpsStep step : opsStepList) {
            List<ReplaceAttrDefine> replaceAttrList = step.getReplaceAttrList();
            if (CollectionUtils.isEmpty(replaceAttrList)) {
                continue;
            }
            DocumentContext objCtx = JsonUtil.buildDefaultJsonPathContext(JsonUtil.toJacksonJson(step));
            for (ReplaceAttrDefine attrDefine : replaceAttrList) {
                String attrName = attrDefine.getReplaceAttr();
                Object replaceAttrVal = replaceAttrMap.get(attrName);
                if (replaceAttrVal == null) {
                    log.warn("There is no value to replace for the property {}.", attrName);
                    continue;
                }

                Field attrField = getAttrNameMatchField(OpsStepDTO.class, attrName);
                if (attrField == null) {
                    log.warn("There is no property with name {} in {}.", attrName, OpsStepDTO.class.getSimpleName());
                    continue;
                }

                Class<?> attrFieldClazz = attrField.getType();
                Object originalVal = objCtx.read("$." + attrName);

                boolean directRepalce = ReplaceAttrDefine.REPLACE_TYPE_REPLACE.equalsIgnoreCase(attrDefine
                        .getReplaceType());
                // 如果替换类型为  "直接替换", 或者   之前属性值为null, 则直接替换
                if (originalVal == null || directRepalce) {
                    if (attrFieldClazz.isArray()) {
                        int length = Array.getLength(replaceAttrVal);
                        List<Object> parse = new ArrayList<>(length);
                        for (int i = 0; i < length; i++) {
                            parse.add(Array.get(replaceAttrVal, i));
                        }
                        objCtx.set("$." + attrName, parse);
                    } else {
                        objCtx.set("$." + attrName, replaceAttrVal);
                    }
                    continue;
                }

                // 追加属性值
                if (attrFieldClazz.isArray()) {
                    int length = Array.getLength(replaceAttrVal);
                    for (int i = 0; i < length; i++) {
                        ((List) originalVal).add(Array.get(replaceAttrVal, i));
                    }
                    objCtx.set("$." + attrName, originalVal);
                } else if (List.class.isInstance(originalVal)) {
                    ((List) originalVal).addAll((Collection) replaceAttrVal);
                    objCtx.set("$." + attrName, originalVal);
                } else if (Map.class.isInstance(originalVal)) {
                    ((Map) originalVal).putAll((Map) replaceAttrVal);
                } else {
                    // 其它数据类型, 不考虑追加, 直接替换
                    objCtx.set("$." + attrName, replaceAttrVal);
                }
            }
            BeanUtils.copyProperties(objCtx.read("$", OpsStep.class), step);
        }
    }

    private Field getAttrNameMatchField(Class<?> targetClazz, String attrName) {
        Field[] fields = targetClazz.getDeclaredFields();
        for (Field field : fields) {
            JsonProperty anno = AnnotationUtils.findAnnotation(field, JsonProperty.class);
            if (anno == null || StringUtils.isBlank(anno.value())) {
                continue;
            }
            if (attrName.equals(anno.value())) {
                return field;
            }
        }

        for (Field field : fields) {
            if (field.getName().equals(attrName)) {
                return field;
            }
        }
        return null;
    }

    private List<OpsStepInstance> generateStepInstanceList(Long pipelineInstanceId, List<OpsStep> opsStepList) {
        if (opsStepList == null) {
            return new ArrayList<OpsStepInstance>();
        }
        return opsStepList.stream().map(step -> {
            OpsStepInstance instance = new OpsStepInstance();
            instance.setStepId(step.getStepId());
            instance.setPipelineInstanceId(pipelineInstanceId);
            instance.setStepInstanceName(step.getStepName());
            instance.setOpsType(step.getOpsType());
            instance.setOrd(step.getOrd());
            instance.setBlockOrd(step.getBlockOrd());
            instance.setBlockName(step.getBlockName());
            instance.setTargetOpsUser(step.getTargetOpsUser());
            instance.setTargetHosts(step.getTargetHosts());
            instance.setTargetExecObject(step.getTargetExecObject());
            if (OPS_TYPE_SCRIPT == step.getOpsType()) {
                OpsScript opsScript = this.opsBaseDataDao.queryOpsScriptById(step.getScriptId());
                instance.setScriptContentType(opsScript.getContentType());
                instance.setScriptContent(opsScript.decodeScriptContent());
                instance.setOpsParamReferenceList(opsScript.getOpsParamReferenceList());
                instance.setPackagePassword(opsScript.getPackagePassword());
            }
            instance.setScriptParam(step.getScriptParam());
            instance.setScriptSudo(step.getScriptSudo());
            instance.setParamSensiveFlag(step.getParamSensiveFlag());
            instance.setOpsTimeout(step.getOpsTimeout());
            if (step.getOpsTimeout() == null &&
                    (OPS_TYPE_FILE_TRANSFER == step.getOpsType() || OPS_TYPE_RESULT_FILE_STORE == step.getOpsType())) {
                instance.setOpsTimeout(60 * 60 * 24);    // 文件传输 超时设置成1天
            }
            instance.setFileSource(step.getFileSource());
            instance.setFileTargetPath(step.getFileTargetPath());
            instance.setFileStoreSource(step.getFileStoreSource());
            instance.setFileType(step.getFileType());
            instance.setTipText(step.getTipText());
            instance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
            instance.setTotalHostsNum(step.getTargetHosts().size());
            instance.setRunHostsNum(step.getTargetHosts().size());
            instance.setBadHostsNum(0);
            instance.setSuccessHostsNum(0);
            instance.setCreateTime(new Date());
            instance.setPauseFlag(step.getPauseFlag());
            instance.setOperator(RequestAuthContext.getRequestHeadUserName());
            instance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
            return instance;
        }).collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public GeneralResponse manualHandleOpsStepInstance(Long stepInstanceId, Integer status) {
        OpsStepInstance stepInstance = opsBaseDataDao.queryStepInstanceById(stepInstanceId);
        if (stepInstance == null) {
            return new GeneralResponse(false, "There is no step instance with id {}.", stepInstanceId);
        }
        if (!OpsStatusEnum.STATUS_6.getStatusCode().equals(stepInstance.getStatus())) {
            return new GeneralResponse(false, "The step instance with id {} is not in the status of 'pause'.",
                    stepInstanceId);
        }
        OpsStatusEnum processStatus = OpsStatusEnum.of(status);
        if (OpsStatusEnum.STATUS_100 != processStatus && OpsStatusEnum.STATUS_7 != processStatus) {
            return new GeneralResponse(false, "The mannual status is not valid.");
        }

        OpsPipelineInstance pipeInstance = opsBaseDataDao.queryPipelineInstanceById(stepInstance
                .getPipelineInstanceId());
        if (!OpsStatusEnum.STATUS_100.getStatusCode().equals(pipeInstance.getStatus())) {
            return new GeneralResponse(false, "The status of the pipeline instance which the step instance owned had " +
                    "been over.");
        }

        // 继续执行
        if (OpsStatusEnum.STATUS_100 == processStatus) {
            activeOpsStepIntance(stepInstance, OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
        }
        // 终止执行
        else if (OpsStatusEnum.STATUS_7 == processStatus) {
            stepInstance.setStatus(OpsStatusEnum.STATUS_7.getStatusCode());
            opsBaseDataDao.updateStepInstance(stepInstance);
            postProcessOpsStepInstanceUpdate(stepInstance, OpsTriggerWayEnum.TRIGGER_BY_MANUAL, null);
        }
        return new GeneralResponse();
    }

    @Scheduled(fixedDelayString = "${ops.timeout-check-interval:2000}")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void processRunTimeoutStepInstances() {
        if (redissonClient == null) {
            log.warn("Please init the redis first, the processRunTimeoutStepInstances process will be ignored.");
            return;
        }
        RLock lock = this.redissonClient.getLock(STEP_RUN_TIME_OUT_CHECK_LOCK);
        try {
            if (!lock.tryLock()) {
                return;
            }

            List<OpsStepInstance> timeoutStepInstList
                    = opsBaseDataDao.queryTimeoutStepInstanceList(System.currentTimeMillis() / 1000);
            if (CollectionUtils.isEmpty(timeoutStepInstList)) {
                return;
            }
            log.info("Begin to process run timeout step-instances with size {}", timeoutStepInstList.size());
            for (OpsStepInstance stepInstance : timeoutStepInstList) {
                try {
                    stepInstance.setStatus(OpsStatusEnum.STATUS_102.getStatusCode());
                    opsBaseDataDao.updateStepInstance(stepInstance);
                    postProcessOpsStepInstanceUpdate(stepInstance, null, null);
                    publishPipelineInstanceTimeoutEvent(stepInstance);
                } catch (Exception e) {
                    log.error("Exception when processRunTimeoutStepInstances().", e);
                }
            }
        } catch (Throwable e) {
            log.error("Error when run step timeout check job.", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Transactional
    public GeneralResponse executeIndexValueScriptCollect(OpsIndexValueCollectRequest indexCollectData) {
        OpsScript scriptData = opsBaseDataDao.queryOpsScriptById(indexCollectData.getScriptId());
        if (scriptData == null) {
            return new GeneralResponse(false, String.format("There is no script with id %s.", indexCollectData
                    .getScriptId()));
        }
        indexCollectData.setScriptData(scriptData);

        OpsPipelineInstance pipelineInstance = OpsPipelineInstance.buildIndexCollectPipelineInstance(indexCollectData);
        this.opsBaseDataDao.insertPipelineInstance(pipelineInstance);

        OpsStepInstance stepInstance = OpsStepInstance.buildScriptExecuteStepInstance(indexCollectData);
        stepInstance.setPipelineInstanceId(pipelineInstance.getPipelineInstanceId());
        this.opsBaseDataDao.insertStepInstance(stepInstance);

        // 敏感指令处理
        List<OpsStepInstance> opsStepInstancesList = Lists.newArrayList();
        opsStepInstancesList.add(stepInstance);
        pipelineInstance.setLabelId(NoAuthLabelEnum.INSPECTION.getLabelId());
        Integer status = sensitiveHelper.stepInstanceSenstiveProcess(opsStepInstancesList, pipelineInstance, OpsTriggerWayEnum
                .TRIGGER_BY_API);
        if (!status.equals(OpsStatusEnum.STATUS_5.getStatusCode())) {
            pipelineInstance.setStatus(status);
            opsBaseDataDao.updatePipelineInstance(pipelineInstance);
        } else {
            activeOpsStepIntance(stepInstance, OpsTriggerWayEnum.TRIGGER_BY_API, indexCollectData
                    .constructAgentResultMeta());
        }
        return new GeneralResponse(true, null, stepInstance.getStepInstanceId());
    }

    @Transactional
    public GeneralResponse executePipelineCronJob(Long jobId) {
        OpsPipelineRunJob job = opsBaseDataDao.queryOpsPipelineRunJobById(jobId);
        if (job == null) {
            String tip = "There is no pipelineJob with id: " + jobId;
            log.error(tip);
            return new GeneralResponse(false, tip);
        }
        if (!OpsStatusEnum.STATUS_100.getStatusCode().equals(job.getStatus())) {
            String tip = "As the status of the pipelineJob with id: " + jobId + " is unscheduled, the job execution " +
                    "will be ignored.";
            log.error(tip);
            return new GeneralResponse(false, tip);
        }
        return executePipeline(job.getPipelineId(), OpsTriggerWayEnum.TRIGGER_BY_JOB);
    }

    public void executeReviewedInstance(List<Long> instanceIdList) {
        for (Long instanceId : instanceIdList) {
            List<OpsStepInstance> stepInstanceList = opsBaseDataDao.queryStepInstListByPipelineInstId(instanceId);
            Collections.sort(stepInstanceList);
            activeOpsStepIntance(stepInstanceList.get(0), OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
        }
    }

    /**
     * 指令审核申请
     *
     * @param pipelineInstanceId 实例Id
     * @return
     */
    public GeneralResponse reviewSensitiveApply(Long pipelineInstanceId) {
        //修改执行实例数据
        String currUser = RequestAuthContext.getRequestHeadUserName();
        OpsPipelineInstance opsPipelineInstance = new OpsPipelineInstance();
        opsPipelineInstance.setReviewApplicant(currUser);
        opsPipelineInstance.setReviewApplyTime(new Date());
        opsPipelineInstance.setPipelineInstanceId(pipelineInstanceId);
        opsBaseDataDao.updatePipelineInstance(opsPipelineInstance);
        //修改敏感指令审核记录
        sensitiveHelper.reviewSensitiveApply(pipelineInstanceId);
        return new GeneralResponse(true, null, pipelineInstanceId);
    }

    public GeneralResponse continueExecInstance(Long pipelineInstanceId) {
        if (pipelineInstanceId == null) {
            log.error("continueExecInstance param is null");
            return new GeneralResponse(false, "继续执行作业实例参数为空");
        }
        List<OpsStepInstance> stepInstanceList = opsBaseDataDao.queryStepInstListByPipelineInstId(pipelineInstanceId);

        if (CollectionUtils.isEmpty(stepInstanceList)) {
            log.error("continueExecInstance pipelineInstance step is empty");
            return new GeneralResponse(false, "作业实例未找到相关步骤");
        }
        for(OpsStepInstance opsStepInstance : stepInstanceList) {
            if (opsStepInstance.getOpsType() == OPS_TYPE_SCRIPT) {
                OpsStep step = opsBaseDataDao.queryOpsStepById(opsStepInstance.getStepId());
                if (step != null) {
                    OpsScript opsScript = this.opsBaseDataDao.queryOpsScriptById(step.getScriptId());
//                instance.setScriptContentType(opsScript.getContentType());
//                instance.setScriptContent(opsScript.decodeScriptContent());
                    if (opsScript != null) {
                        opsStepInstance.setOpsParamReferenceList(opsScript.getOpsParamReferenceList());
                    }
                }
//                instance.setPackagePassword(opsScript.getPackagePassword());
            }
        }
        Collections.sort(stepInstanceList);
        activeOpsStepIntance(stepInstanceList.get(0), OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
        return new GeneralResponse(true, null, pipelineInstanceId);
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
