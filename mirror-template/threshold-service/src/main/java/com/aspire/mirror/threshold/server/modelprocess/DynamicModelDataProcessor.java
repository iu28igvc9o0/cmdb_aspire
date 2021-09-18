package com.aspire.mirror.threshold.server.modelprocess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.threshold.server.clientservice.TemplateClient;
import com.aspire.mirror.threshold.server.config.DHFtpServerConfig;
import com.aspire.mirror.threshold.server.domain.*;
import com.aspire.mirror.threshold.server.helper.SshdHelper;
import com.aspire.mirror.threshold.server.util.DateUtil;
import com.aspire.mirror.threshold.server.util.ExecuteUtil;
import com.aspire.mirror.threshold.server.util.FtpUtls;
import com.aspire.mirror.threshold.server.util.SshUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

/**
 * 动态模型处理类
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DynamicModelDataProcessor
 * 类描述:    动态模型处理类
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 14:59
 * 版本:      v1.0
 */
@Component
@Slf4j
public class DynamicModelDataProcessor {

    @Autowired
    private DHFtpServerConfig dhFtpServerConfig;

    @Value("${dynamicmodel.sync.pool:信息港资源池}")
    private String pool;

    @Value("${dynamicmodel.sync.localPath:dynamicModelTemp}")
    private String localPath;

    @Value("${dynamicmodel.sync.remotePath:/model/}")
    private String remotePath;

    @Autowired
    private TemplateClient templateClient;
    @Autowired
    private SshdHelper sshdHelper;
    public void handlerDynamicModel() {
//        Map<String, List<DhDynamicMode>> dynamicModelMap = Maps.newHashMap();
        try {
            if (StringUtils.isNotBlank(pool)) {
                // ftp取数据存本地
//                FtpUtls ftpUtls = new FtpUtls(dhFtpServerConfig.getHost(), dhFtpServerConfig.getPort(), dhFtpServerConfig.getUsername(), dhFtpServerConfig.getPassword());
                String[] poolArray = pool.split(",");
                for (String poolItem : poolArray) {
                    // 下载文件
//                    String localRealPath = localPath + "/" + poolItem;
//                    File localDir = new File(localRealPath);
//                    if (!localDir.exists()) {
//                        Files.createDirectory(Paths.get(localDir.getCanonicalPath()));
//                    }
//                    String beforecmd = format(LINUX_CMD_LS_FILE, remotePath + poolItem, remotePath + poolItem, remotePath + poolItem, remotePath + poolItem);
//                    SshUtil.SshResultWrap beforeResult = SshUtil.executeShellCmd(agentSshd, 0, beforecmd);
//                    String[] filePathList = {remoteFile};
//                    if (beforeResult.isSuccess()) {
//                        filePathList = beforeResult.getBizLog().split("\n");
//                    }
                    Pair<String, List<File>> result = sshdHelper.downloadFile(remotePath + poolItem, localPath);
//                    boolean result = ftpUtls.downloadFile(remotePath + poolItem, localRealPath, true);
                    // 解析本地文件为东华动态模型对象数据  后续多线程处理
                    if (result != null) {
                        List<File> fileArray = result.getRight();
                        if (fileArray.size() > 0) {
                            Map<String, List<StandardDynamicModelExt>> modelMap = Maps.newHashMap();
                            for (int i = 0; i < fileArray.size(); i++) {
                                File deviceFile = fileArray.get(i);
                                String ip = deviceFile.getName().split("\\.txt")[0];
                                String fileContent = this.readFileContent(deviceFile);
                                if (StringUtils.isNotBlank(fileContent)) {
                                    fileContent = "[" + fileContent + "]";
                                    // 性能对比 序列化 Jackson>fastjson>gson 反序列化  fastjson>jackson>gson
                                    JSONArray jsonArray = JSONArray.parseArray(fileContent);
                                    List<DhDynamicMode> dhDynamicModes = Lists.newArrayList();
                                    for (Object obj : jsonArray) {
                                        JSONObject jsonObject = (JSONObject) obj;
                                        DhDynamicMode dhDynamicMode = DhDynamicMode.parseByJSONObject(jsonObject);
                                        dhDynamicModes.add(dhDynamicMode);
                                    }
                                    // 解析动态阈值对象
                                    List<StandardDynamicModelExt> modelExtList = parseDynamicDataList(Pair.of(ip + ":" + poolItem, dhDynamicModes));


                                    // 发布动态阈值对象
                                    if (!CollectionUtils.isEmpty(modelExtList)) {
                                        modelMap.put(ip + ":" + poolItem, modelExtList);
                                    } else {
                                        log.error("publishStandardDynamicModel modelListMap is empty, ip is {}", ip);
                                    }
                                }
                            }
                            int i = 0;
                            for (String key : modelMap.keySet()) {
                                publishStandardDynamicModel(modelMap.get(key), i == 0 ? "START" : i == fileArray.size() - 1 ? "END" : "MID");
                                i++;
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("------get dynamic model data is error!------", e);
        }
//        return dynamicModelMap;
    }

//    private Map<String, List<DhDynamicMode>> parseFileContent(File deviceFile, Pair<String, List<DhDynamicMode>> dynamicModelMap) {
//        String fileContent = this.readFileContent(deviceFile);
//        if (StringUtils.isNotBlank(fileContent)) {
//            fileContent = "[" + fileContent + "]";
//            // 性能对比 序列化 Jackson>fastjson>gson 反序列化  fastjson>jackson>gson
//            JSONArray jsonArray = JSONArray.parseArray(fileContent);
//            List<DhDynamicMode> dhDynamicModes = Lists.newArrayList();
//            for (Object obj : jsonArray) {
//                JSONObject jsonObject = (JSONObject) obj;
//                DhDynamicMode dhDynamicMode = DhDynamicMode.parseByJSONObject(jsonObject);
//                dhDynamicModes.add(dhDynamicMode);
//            }
//            String ip = deviceFile.getName().split("\\.txt")[0];
//            dynamicModelMap.put(ip + ":" + pool, dhDynamicModes);
//        }
//        return dynamicModelMap;
//    }

    /**
     * 文件内容读取
     *
     * @param file 读取文件
     * @return 内容
     */
    public static String readFileContent(File file) {
//        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    public List<StandardDynamicModelExt> parseDynamicDataList(Pair<String, List<DhDynamicMode>> modelPair) {
//        List<StandardDynamicModel> standardDynamicModelList = Lists.newArrayList();
//        Map<String, List<StandardDynamicModelExt>> respMap = Maps.newHashMap();
        String key = modelPair.getLeft();
        List<StandardDynamicModelExt> standardDynamicModelExts = Lists.newArrayList();
        String[] keyArr = key.split(":");
        MirrorHostVO mirrorHostVO = templateClient.getHostByIpAndPool(keyArr[0], keyArr[1]);
        if (mirrorHostVO != null) {
            for (DhDynamicMode dhDynamicMode : modelPair.getRight()) {
                StandardDynamicModelExt standardDynamicModelExt = new StandardDynamicModelExt();
                standardDynamicModelExt.setDesc("");
                standardDynamicModelExt.setLastUpdateTime(DateUtil.getDate(dhDynamicMode.getLastUpdateTime(), DateUtil.DATE_TIME_CH_FORMAT));
                standardDynamicModelExt.setUpdateTime(DateUtil.getDate(dhDynamicMode.getUpdateTime(), DateUtil.DATE_TIME_CH_FORMAT));
                standardDynamicModelExt.setModelContent(dhDynamicMode.getModel());
                standardDynamicModelExt.setResourceId(dhDynamicMode.getTaskId());
                standardDynamicModelExt.setModelFailMsg(dhDynamicMode.getErrMsg());
                standardDynamicModelExt.setModelStatus(dhDynamicMode.getStatus());
                standardDynamicModelExt.setSourceType(StandardDynamicModelExt.MODEL_TYPE_DH);
                standardDynamicModelExt.setModelType(dhDynamicMode.getType());


                StandardDynamicModel standardDynamicModel = new StandardDynamicModel();
                standardDynamicModel.setDeviceId(mirrorHostVO.getId().toString());
                standardDynamicModel.setDeviceItemId(dhDynamicMode.getItemId());
                standardDynamicModel.setIdcType(dhDynamicMode.getIdcType());
                standardDynamicModel.setIp(dhDynamicMode.getHost());
                standardDynamicModel.setModelStatus(dhDynamicMode.getStatus().toLowerCase().equals("success") ? "ON" : "OFF");
                standardDynamicModel.setThridSystemId(mirrorHostVO.getProxyIdentity());
                standardDynamicModel.setZabbixItemId(dhDynamicMode.getTemplateId());


                Map<String, Object> resultMap = this.parseModelJson(dhDynamicMode.getModel(), dhDynamicMode.getType(), standardDynamicModel);
                standardDynamicModelExt.setZhixindu((String) resultMap.get("zhixindu"));
                standardDynamicModelExt.setDesc((String) resultMap.get("desc"));

                List<StandardDynamicModel> modelList = Lists.newLinkedList();
                StandardDynamicModel standardDynamicModelPriority2 = new StandardDynamicModel();
                BeanUtils.copyProperties(standardDynamicModel, standardDynamicModelPriority2);
                standardDynamicModelPriority2.setPriority("2");
                JSONObject modelJson = new JSONObject();
                modelJson.put("type", dhDynamicMode.getType());
                if (dhDynamicMode.getType().equals("1")) {
                    modelJson.put("workofdays", resultMap.get("workofdaysMapPriority2"));
                    modelJson.put("noworkofdays", resultMap.get("noworkofdaysMapPriority2"));
                } else {
                    modelJson.put("high", resultMap.get("high"));
                    modelJson.put("low", resultMap.get("low"));
                }
                standardDynamicModelPriority2.setModelJson(JSON.toJSONString(modelJson));
                modelList.add(standardDynamicModelPriority2);

                StandardDynamicModel standardDynamicModelPriority5 = new StandardDynamicModel();
                BeanUtils.copyProperties(standardDynamicModel, standardDynamicModelPriority5);
                standardDynamicModelPriority5.setPriority("5");
                JSONObject modelJson2 = new JSONObject();
                modelJson2.put("type", dhDynamicMode.getType());
                if (dhDynamicMode.getType().equals("1")) {
                    modelJson2.put("workofdays", resultMap.get("workofdaysMapPriority5"));
                    modelJson2.put("noworkofdays", resultMap.get("noworkofdaysMapPriority5"));
                } else {
                    modelJson2.put("high", resultMap.get("serhigh"));
                    modelJson2.put("low", resultMap.get("serlow"));
                }
                standardDynamicModelPriority5.setModelJson(JSON.toJSONString(modelJson2));
                modelList.add(standardDynamicModelPriority5);

                standardDynamicModelExt.setDynamicModelList(modelList);
                standardDynamicModelExts.add(standardDynamicModelExt);
            }
//            respMap.put(key, standardDynamicModelExts);
        }
        return standardDynamicModelExts;
    }

    private Map<String, Object> parseModelJson(String model, String type, StandardDynamicModel standardDynamicModel) {
        Map<String, Object> resultMap = Maps.newHashMap();
        JSONObject modelJson = JSON.parseObject(model);
        // 设置置信度 描述
        String zhixinndu = modelJson.getString("zhixindu");
        String desc = modelJson.getString("desc");
        resultMap.put("zhixindu", zhixinndu);
        resultMap.put("desc", desc);

        JSONObject modelObj = modelJson.getJSONObject("model");
        //周期
        if (type.equals("1")) {
            JSONObject workofdayObj = modelObj.getJSONObject("workOfdays");
            // 解析周期
            JSONArray keysArray = workofdayObj.getJSONArray("keys");
            String begin = (String) keysArray.get(0);
            String end = (String) keysArray.get(1);
            log.debug("===begin={}===, ====end={}===", begin, end);
            long startTime = DateUtil.getDate("2019-11-07 " + begin, DateUtil.DATE_TIME_CH_FORMAT).getTime();
            long endTime = DateUtil.getDate("2019-11-07 " + end, DateUtil.DATE_TIME_CH_FORMAT).getTime();
            Long min = (endTime - startTime) / (1000 * 60);

            Map<String, Object> workofdaysMap = Maps.newHashMap();
            workofdaysMap.put("delay", min.toString());
            workofdaysMap.put("keys", workofdayObj.getJSONArray("keys"));
            workofdaysMap.put("highs", workofdayObj.getJSONArray("highs"));
            workofdaysMap.put("lows", workofdayObj.getJSONArray("lows"));
            resultMap.put("workofdaysMapPriority2", workofdaysMap);

            Map<String, Object> workofdaysMap2 = Maps.newHashMap();
            workofdaysMap2.put("delay", min.toString());
            workofdaysMap2.put("keys", workofdayObj.getJSONArray("keys"));
            workofdaysMap2.put("highs", workofdayObj.getJSONArray("serhighs"));
            workofdaysMap2.put("lows", workofdayObj.getJSONArray("serlows"));
            resultMap.put("workofdaysMapPriority5", workofdaysMap2);

            JSONObject noworkofdayObj = modelObj.getJSONObject("noworkOfdays");

            // 解析周期
            JSONArray keys2Array = noworkofdayObj.getJSONArray("keys");
            String begin2 = (String) keys2Array.get(0);
            String end2 = (String) keys2Array.get(1);
            long startTime2 = DateUtil.getDate("2019-11-07 " + begin2, DateUtil.DATE_TIME_CH_FORMAT).getTime();
            long endTime2 = DateUtil.getDate("2019-11-07 " + end2, DateUtil.DATE_TIME_CH_FORMAT).getTime();
            Long min2 = (endTime2 - startTime2) / (1000 * 60);

            Map<String, Object> noworkofdaysMap = Maps.newHashMap();
            noworkofdaysMap.put("delay", min2.toString());
            noworkofdaysMap.put("keys", noworkofdayObj.getJSONArray("keys"));
            noworkofdaysMap.put("highs", noworkofdayObj.getJSONArray("highs"));
            noworkofdaysMap.put("lows", noworkofdayObj.getJSONArray("lows"));
            resultMap.put("noworkofdaysMapPriority2", noworkofdaysMap);

            Map<String, Object> noworkofdaysMap2 = Maps.newHashMap();
            noworkofdaysMap2.put("delay", min2.toString());
            noworkofdaysMap2.put("keys", noworkofdayObj.getJSONArray("keys"));
            noworkofdaysMap2.put("highs", noworkofdayObj.getJSONArray("serhighs"));
            noworkofdaysMap2.put("lows", noworkofdayObj.getJSONArray("serlows"));
            resultMap.put("noworkofdaysMapPriority5", noworkofdaysMap2);

        } else {
            //平稳
            resultMap.put("high", modelObj.getString("high"));
            resultMap.put("low", modelObj.getString("low"));
            resultMap.put("serhigh", modelObj.getString("serhigh"));
            resultMap.put("serlow", modelObj.getString("serlow"));
        }

        return resultMap;
    }

    public void publishStandardDynamicModel(List<StandardDynamicModelExt> modelList, String parentIndexType) {
        // 分批处理
//        if (CollectionUtils.isEmpty(modelList)) {
//            log.error("publishStandardDynamicModel modelListMap is empty");
//        }
        ExecuteUtil.partitionRun(modelList, 20, (eachPair) -> this.publishPartitonList(eachPair, parentIndexType));
    }

    private void publishPartitonList(Pair<String,List<StandardDynamicModelExt>> eachPair, String parentIndexType) {
        String indexType = "MID";
        if (eachPair.getLeft().equals(parentIndexType)) {
            indexType = parentIndexType;
        }
        DynamicModelBatchCreateVO dynamicModelBatchCreateVO = new DynamicModelBatchCreateVO(indexType, eachPair.getRight());
        templateClient.batchDynamicModelData(dynamicModelBatchCreateVO);
    }
}