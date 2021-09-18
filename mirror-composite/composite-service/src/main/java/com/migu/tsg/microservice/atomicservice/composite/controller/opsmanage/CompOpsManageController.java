/**
 * 项目名： composite-service
 * <p/>
 * <p>
 * 文件名:  CompOpsManageController.java
 * <p/>
 * <p>
 * 功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @date 2019年11月26日
 * @version V1.0 <p/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 */
package com.migu.tsg.microservice.atomicservice.composite.controller.opsmanage;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.aspire.mirror.common.util.ZipUtil;
import com.aspire.mirror.composite.service.opsmanage.ICompOpsManageService;
import com.aspire.mirror.ops.api.domain.*;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel;
import com.aspire.mirror.ops.api.domain.OpsLabel.OpsLabelQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO.OpsPipelineQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob.OpsPipelineRunJobQueryModel;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;
import com.aspire.mirror.ops.api.util.EncryptUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.module.ModuleClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsFileClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsManageClient;
import com.migu.tsg.microservice.atomicservice.composite.common.excel2pdf.POIModuleUtils;
import com.migu.tsg.microservice.atomicservice.composite.config.CommonSftpServerConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.EasyPoiUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.SshUtil.SshResultWrap;
import com.migu.tsg.microservice.atomicservice.composite.helper.SshdHelper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目名称: composite-service
 * <p/>
 * <p>
 * 类名: CompOpsManageController
 * <p/>
 * <p>
 * 类功能描述: TODO
 * <p/>
 *
 * @author pengguihua
 * @version V1.0
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有
 * @date 2019年11月26日
 */
@Slf4j
@RestController
public class CompOpsManageController extends CommonResourceController implements ICompOpsManageService {
    private static final String OPS_TRANSFER_DIR = "/ops_transfer/";
    @Autowired
    private OpsManageClient opsClient;
    @Autowired
    private CommonSftpServerConfig sftpConfig;
    @Autowired
    protected ResourceAuthHelper resAuthHelper;
    @Autowired
    private SshdHelper sshdHelper;
    @Autowired
    private OpsFileClient opsFileClient;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse getAgentHostInfoLoadSource() {
        return opsClient.getAgentHostInfoLoadSource();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public PageListQueryResult<SimpleAgentHostInfo> fetchUserAuthedAgentHostList(@RequestBody SimpleAgentHostQueryModel queryParam) {
        return opsClient.fetchUserAuthedAgentHostList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<Integer, String>> loadOpsStatusList() {
        return opsClient.loadOpsStatusList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<Integer, String>> loadOpsTriggerWayList() {
        return opsClient.loadOpsTriggerWayList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse saveOpsAccount(@RequestBody OpsAccount account) {
        return opsClient.saveOpsAccount(account);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse removeOpsAccount(@PathVariable("accountName") String accountName) {
        return opsClient.removeOpsAccount(accountName);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<OpsAccount> queryOpsAccountList(@RequestBody OpsAccountQueryModel queryParam) {
        return opsClient.queryOpsAccountList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse saveOpsLabel(@RequestBody OpsLabel label) {
        return opsClient.saveOpsLabel(label);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse removeOpsLabel(@PathVariable("labelCode") String labelCode) {
        return opsClient.removeOpsLabel(labelCode);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<OpsLabel> queryOpsLabelList(@RequestBody OpsLabelQueryModel queryParam) {
        return opsClient.queryOpsLabelList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "create", resType = "script")
    public GeneralResponse saveOpsScript(@RequestBody OpsScript script) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        if (script.getScriptId() != null) {
            OpsScript opsScript = opsClient.queryOpsScriptById(script.getScriptId());
            if (!opsScript.getCreater().equals(authCtx.getUser().getUsername())) {
                return new GeneralResponse(false, "编辑脚本只允许创建人操作！");
            }
        }
        return opsClient.saveOpsScript(script);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "copy", resType = "script")
    public GeneralResponse copyScript(@RequestBody OpsScript script) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsClient.saveOpsScript(script);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "delete", resType = "script")
    public GeneralResponse removeOpsScript(@PathVariable("scriptId") Long scriptId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsScript opsScript = opsClient.queryOpsScriptById(scriptId);
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && !opsScript.getCreater().equals(authCtx.getUser().getUsername())) {
            return new GeneralResponse(false, "删除脚本只允许创建人操作！");
        }
        return opsClient.removeOpsScript(scriptId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "script", loadResFilter = true)
    public PageListQueryResult<OpsScript> queryOpsScriptList(@RequestBody OpsScriptQueryModel queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
//            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
//
//            if (!super.applyGeneralAuthConstraints(totalConstraints, queryParam)) {
//                return new PageListQueryResult<>();
//            }
//        }
//        
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsClient.queryOpsScriptList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "script")
    public OpsScript queryOpsScriptById(@RequestParam("scriptId") Long scriptId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsScriptById(scriptId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse readLocalScriptFile(@RequestParam("file") MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            OpsScript script = new OpsScript();
            script.setContentType(ScriptContentTypeEnum.SHELL.getCode());
            String fileName = file.getOriginalFilename();
            if (fileName.trim().toLowerCase(Locale.ENGLISH).endsWith(".py")) {
                script.setContentType(ScriptContentTypeEnum.PYTHON.getCode());
            } else if (fileName.trim().toLowerCase(Locale.ENGLISH).endsWith(".bat")) {
                script.setContentType(ScriptContentTypeEnum.BAT.getCode());
            }
            List<String> lines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            script.setScriptContent(StringUtils.join(lines, '\n'));
            script.encodeScriptContent();
            return new GeneralResponse(true, null, script);
        } catch (Exception e) {
            log.error(null, e);
            return new GeneralResponse(false, e.toString());
        }
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "create", resType = "pipeline")
    public GeneralResponse saveOpsPipeline(@RequestBody OpsPipelineDTO pipeline) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        if (pipeline.getPipelineId() != null) {
            OpsPipelineDTO pipelineDTO = opsClient.queryOpsPipelineById(pipeline.getPipelineId());
            if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && !pipelineDTO.getCreater().equals(authCtx.getUser().getUsername())) {
                return new GeneralResponse(false, "编辑作业只允许创建人操作！");
            }
        }
        return opsClient.saveOpsPipeline(pipeline);
    }

    @Override
    @ResAction(action = "copy", resType = "pipeline")
    public GeneralResponse copyOpsPipeline(@RequestBody OpsPipelineDTO pipeline) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        return opsClient.saveOpsPipeline(pipeline);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "delete", resType = "pipeline")
    public GeneralResponse removeOpsPipeline(@PathVariable("pipelineId") Long pipelineId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsPipelineDTO pipelineDTO = opsClient.queryOpsPipelineById(pipelineId);
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && !pipelineDTO.getCreater().equals(authCtx.getUser().getUsername())) {
            return new GeneralResponse(false, "删除作业只允许创建人操作！");
        }
        return opsClient.removeOpsPipeline(pipelineId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipeline", loadResFilter = true)
    public PageListQueryResult<OpsPipelineDTO> queryOpsPipelineList(@RequestBody OpsPipelineQueryModel queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
//        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
//            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
//                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
//
//            if (!super.applyGeneralAuthConstraints(totalConstraints, queryParam)) {
//                return new PageListQueryResult<>();
//            }
//        }

        return opsClient.queryOpsPipelineList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipeline")
    public OpsPipelineDTO queryOpsPipelineById(@PathVariable("pipelineId") Long pipelineId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsPipelineById(pipelineId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipeline")
    public List<OpsStepDTO> queryOpsStepListByPipelineId(@PathVariable("pipelineId") Long pipelineId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsStepListByPipelineId(pipelineId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipelineInstance", loadResFilter = true)
    public PageListQueryResult<OpsPipelineInstanceDTO>
    queryOpsPipelineInstanceList(@RequestBody OpsPipelineInstanceQueryParam queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsPipelineInstanceList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipelineInstance")
    public OpsPipelineInstanceDTO queryOpsPipelineInstanceById(@PathVariable("pipelineInstanceId") Long
                                                                       pipelineInstanceId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsPipelineInstanceById(pipelineInstanceId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipelineInstance")
    public List<OpsStepInstanceDTO> queryStepInstListByPipelineInstId(@PathVariable("pipelineInstanceId") Long
                                                                              pipelineInstanceId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryStepInstListByPipelineInstId(pipelineInstanceId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "pipelineInstance")
    public OpsStepInstanceDTO queryStepInstanceById(@RequestParam("stepInstId") Long stepInstId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryStepInstanceById(stepInstId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public PageListQueryResult<OpsAgentStepInstanceResult> queryOpsStepAgentRunResultList(
            @RequestBody OpsAgentStepInstanceResultQueryModel param) {
        return opsClient.queryOpsStepAgentRunResultList(param);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "exec", resType = "script")
    public GeneralResponse executeRealtimeScript(@RequestBody OpsScriptExecuteActionModel requestData) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.executeRealtimeScript(requestData);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "exec", resType = "fileFistribution")
    public GeneralResponse executeRealtimeFileTransfer(@RequestBody OpsFileTransferActionModel requestData) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.executeRealtimeFileTransfer(requestData);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse uploadFile4Transfer(@RequestParam("file") MultipartFile file) {
        try {
            String dirPrefix = "upload4Transfer_";
            String fileName = file.getOriginalFilename();
            Path tempDir = Files.createTempDirectory(dirPrefix);
            Path tempFile = tempDir.resolve(fileName);
            file.transferTo(tempFile.toFile());

            Pair<SshResultWrap, String> uploadResult = sshdHelper.uploadFile(tempFile, OPS_TRANSFER_DIR);
            // 写入文件管理
            OpsFile opsFile = new OpsFile();
            opsFile.setFileType(OpsFile.FILE_TYPE_1);
            opsFile.setFileGenerateType(OpsFile.GENERATE_TYPE_1);
            opsFile.setFileClass(OpsFileClassEnum.FILE_CLASS_9.getStatusCode().toString());
            opsFile.setFileVersion(OpsFile.FILE_VERSION_V1);
            opsFile.setFileName(file.getName());
            opsFile.setFilePath(uploadResult.getRight());
            opsFileClient.saveFile(opsFile);
            return new GeneralResponse(true, null, uploadResult.getRight());
//            SshdServer sftpServer = new SshdServer();
//            sftpServer.setIpAddress(sftpConfig.getIpAddress());
//            sftpServer.setPort(sftpConfig.getPort());
//            sftpServer.setLoginUser(sftpConfig.getLoginUser());
//            sftpServer.setLoginPass(sftpConfig.getLoginPass());
//
//            String subDir = OPS_TRANSFER_DIR + tempDir.toFile().getName();
//            String remotePath = sftpConfig.getRootDirectory() + subDir;
//            SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);

//            Pair<SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, tempFile, remotePath);
//            if (!uploadResult.getLeft().isSuccess()) {
//                return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
//            }
//            String fileRelativePath = subDir + "/" + tempFile.toFile().getName();
//            return new GeneralResponse(true, null, fileRelativePath);
        } catch (IOException e) {
            return new GeneralResponse(false, e.toString());
        }
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "exec", resType = "pipeline")
    public GeneralResponse executePipeline(@PathVariable("pipelineId") Long pipelineId,
                                           @RequestBody(required = false) Map<String, Object> params) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.executePipeline(pipelineId, params);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "exec", resType = "pipeline")
    public GeneralResponse manualHandleOpsStepInstance(@PathVariable("stepInstanceId") Long stepInstanceId,
                                                       @PathVariable("status") Integer status) {
        return opsClient.manualHandleOpsStepInstance(stepInstanceId, status);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse executeIndexValueScriptCollect(@RequestBody OpsIndexValueCollectRequest indexCollectData) {
        return opsClient.executeIndexValueScriptCollect(indexCollectData);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "create", resType = "timedPipeline")
    public GeneralResponse saveOpsPipelineRunJob(@RequestBody OpsPipelineRunJob runJob) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.saveOpsPipelineRunJob(runJob);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "delete", resType = "timedPipeline")
    public GeneralResponse removeOpsPipelineRunJob(@PathVariable("jobId") Long jobId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.removeOpsPipelineRunJob(jobId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "timedPipeline")
    public PageListQueryResult<OpsPipelineRunJob> queryOpsPipelineRunJobList(@RequestBody OpsPipelineRunJobQueryModel
                                                                                     queryParam) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.queryOpsPipelineRunJobList(queryParam);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "updateStatus", resType = "timedPipeline")
    public GeneralResponse schedulePipelineCronJob(@PathVariable("jobId") Long jobId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.schedulePipelineCronJob(jobId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "updateStatus", resType = "timedPipeline")
    public GeneralResponse unSchedulePipelineCronJob(@PathVariable("jobId") Long jobId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.unSchedulePipelineCronJob(jobId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GeneralResponse executePipelineCronJob(@PathVariable("jobId") Long jobId) {
        return opsClient.executePipelineCronJob(jobId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "reviewAppley", resType = "pipelineInstance")
    public GeneralResponse reviewSensitiveApply(@PathVariable("pipelineInstanceId") Long pipelineInstanceId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsPipelineInstanceDTO opsPipelineInstanceDTO = opsClient.queryOpsPipelineInstanceById(pipelineInstanceId);
        if (opsPipelineInstanceDTO == null) {
            return new GeneralResponse(false, "该作业实例不存在！");
        }
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && !authCtx.getUser().getUsername().equals(opsPipelineInstanceDTO.getOperator())) {
            return new GeneralResponse(false, "指令审核申请必须由启动人发起！");
        }
        return opsClient.reviewSensitiveApply(pipelineInstanceId);
    }

    @Override
    @ResAction(action = "create", resType = "scenes")
    public GeneralResponse savePipelineScences(OpsPipelineScenes scenes, @RequestParam(name = "scenes_picture", required = false)
            MultipartFile picture) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        if (picture != null) {
            BASE64Encoder encoder = new BASE64Encoder();
            // 通过base64来转化图片
            try {
                String data = encoder.encode(picture.getBytes());
                scenes.setScenesPicture(data);
            } catch (IOException e) {
            }
        }
        if (StringUtils.isNotEmpty(scenes.getGroupIdString())) {
            List<String> gorupIdList = Arrays.asList(scenes.getGroupIdString().split(","));
            List<Long> groupIds = gorupIdList.stream().map(item -> Long.parseLong(item)).collect(Collectors.toList());
            scenes.setGroupIdList(groupIds);
        }
        return opsClient.savePipelineScences(scenes);
    }

    @Override
    @ResAction(action = "view", resType = "scenes")
    public List<GroupScenes> pipelineScenesAllList() {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.pipelineScenesAllList();
    }

    @Override
    @ResAction(action = "view", resType = "scenes")
    public OpsPipelineScenes pipelineScenesById(@RequestParam("pipeline_scenes_id") Long pipelineScenesId) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.pipelineScenesById(pipelineScenesId);
    }

    @Override
    @ResAction(action = "delete", resType = "scenes")
    public GeneralResponse deletePipelineScenes(@RequestParam("scenes_ids") String scenesIds) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());

        return opsClient.deletePipelineScenes(scenesIds);
    }

    @Override
    public void downloadLogFile(@RequestBody Map<String, String> downParam) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        sshdHelper.downloadFile(downParam, response, "application/octet-stream", "log_file_down");
//        String relativeFilePath = downParam.get("file_path");

        //"temp_file_down"
//        try (OutputStream os = response.getOutputStream()) {
//            String fileName = Paths.get(relativeFilePath).getFileName().toFile().getName();
//            String fullDownFilePath = sftpConfig.getRootDirectory() + relativeFilePath;
//            SshUtil.SshdServer sftpServer = sftpConfig.getSshdServer();
//
//            Path localDownDir = Files.createTempDirectory("log_file_down");
//            Pair<SshUtil.SshResultWrap, Path> downResult
//                    = SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
//            SshUtil.SshResultWrap wrap = downResult.getLeft();
//            if (!wrap.isSuccess()) {
//                throw new RuntimeException(wrap.getBizLog());
//            }
//
//            response.setHeader("Content-Disposition",
//                    "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//            response.setHeader("Connection", "close");
//            response.setCharacterEncoding("utf-8");
//            response.setHeader("Content-Type", "application/octet-stream");
//            os.write(Files.readAllBytes(downResult.getRight()));
//            os.flush();
//        } catch (Exception e) {
//            log.error("DownLogFile failed!", e);
//        }
    }

    @Override
    public OpsPipelineInstanceLog getPipelineInstanceLog(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        return opsClient.getPipelineInstanceLog(pipelineInstanceId);
    }

    @Override
    public GeneralResponse logPackageApply(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        return opsClient.logPackageApply(pipelineInstanceId);
    }

    @Override
    public void downloadSummaryOutput(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        if (pipelineInstanceId == null) {
            log.warn("CompOpsManageController[downloadSummaryOutput] param is empty");
        }
        OpsPipelineInstanceDTO instanceDTO = opsClient.queryOpsPipelineInstanceById(pipelineInstanceId);
        if (instanceDTO == null) {
            log.error("CompOpsManageController[downloadSummaryOutput] PipelineInstance is not existed");
        }
        String relativeFilePath = null;
        if (StringUtils.isNotEmpty(instanceDTO.getOutputPath())) {
            relativeFilePath = instanceDTO.getOutputPath();
        } else {
            GeneralResponse generalResponse = opsClient.outputPackage(pipelineInstanceId);
            relativeFilePath = (String) generalResponse.getBizData();
        }

        if (relativeFilePath != null) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            Map<String, String> downParam = Maps.newHashMap();
            downParam.put("file_path", relativeFilePath);
            sshdHelper.downloadFile(downParam, response, "application/octet-stream", "output_file_down");
//            try (OutputStream os = response.getOutputStream()) {
//                String fileName = Paths.get(relativeFilePath).getFileName().toFile().getName();
//                String fullDownFilePath = sftpConfig.getRootDirectory() + relativeFilePath;
//                SshUtil.SshdServer sftpServer = sftpConfig.getSshdServer();
//                Path localDownDir = Files.createTempDirectory("log_file_down");
//                Pair<SshUtil.SshResultWrap, Path> downResult
//                        = SshUtil.sftpDownload(sftpServer, fullDownFilePath, localDownDir.toFile().getCanonicalPath());
//                SshUtil.SshResultWrap wrap = downResult.getLeft();
//                if (!wrap.isSuccess()) {
//                    throw new RuntimeException(wrap.getBizLog());
//                }
//
//                response.setHeader("Content-Disposition",
//                        "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
//                response.setHeader("Connection", "close");
//                response.setCharacterEncoding("utf-8");
//                response.setHeader("Content-Type", "application/octet-stream");
//                os.write(Files.readAllBytes(downResult.getRight()));
//                os.flush();
//            } catch (Exception e) {
//                log.error("DownLogFile failed!", e);
//            }
        } else {
            log.error("该作业实例没有可合并产出");
        }
    }

    @Override
    public List<OpsParam> getParamAllList() {
        return opsClient.getParamAllList();
    }

    @Override
    public PageListQueryResult<OpsParam> searchParamList(@RequestBody OpsParamQueryModel paramModel) {
        return opsClient.searchParamList(paramModel);
    }

    @Override
    public GeneralResponse createOpsParam(@RequestBody OpsParam createModel) {
        return opsClient.createOpsParam(createModel);
    }

    @Override
    public GeneralResponse updateOpsParam(@RequestBody OpsParam updateModel) {
        return opsClient.updateOpsParam(updateModel);
    }

    @Override
    public GeneralResponse deleteOpsParamById(@PathVariable("paramId") Long paramId) {
        return opsClient.deleteOpsParamById(paramId);
    }

    @Override
    public List<OpsParamType> loadAllParamTypeList() {
        return opsClient.loadAllParamTypeList();
    }

    @Override
    public List<OpsParamReference> queryReferParamListByEntityId(@PathVariable("entityId") Long entityId) {
        return opsClient.queryReferParamListByEntityId(entityId);
    }

    @Override
    @ResAction(action = "audit", resType = "pipeline")
    public GeneralResponse auditPipeline(@RequestParam("piepeline") Long pipelineId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc") String auditDesc) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsPipelineDTO opsPipelineDTO = opsClient.queryOpsPipelineById(pipelineId);
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && authCtx.getUser().getUsername().equals(opsPipelineDTO.getUpdater())) {
            return new GeneralResponse(false, "不能审核自编辑作业，请联系其他审核人操作！");
        }
        return opsClient.auditPipeline(pipelineId, auditStatus, auditDesc);
    }

    @Override
    @ResAction(action = "audit", resType = "script")
    public GeneralResponse auditScript(@RequestParam("scriptId") Long scriptId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc") String auditDesc) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        OpsScript opsScript = opsClient.queryOpsScriptById(scriptId);
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && authCtx.getUser().getUsername().equals(opsScript.getUpdater())) {
            return new GeneralResponse(false, "不能审核自编辑脚本，请联系其他审核人操作！");
        }
        return opsClient.auditScript(scriptId, auditStatus, auditDesc);
    }

    @Override
    public GeneralResponse continueExecInstance(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
        OpsPipelineInstanceDTO instanceDTO = opsClient.queryOpsPipelineInstanceById(pipelineInstanceId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser() && !authCtx.getUser().getUsername().equals(instanceDTO.getOperator())) {
            return new GeneralResponse(false, "必须发起执行人继续执行敏感指令审核通过的作业！");
        }
        return opsClient.continueExecInstance(pipelineInstanceId);
    }

    @Override
    public List<OpsPipelineHis> getPipelineHisListByPipelineId(@RequestParam("pipelineId") Long pipelineId) {
        return opsClient.getPipelineHisListByPipelineId(pipelineId);
    }

    @Override
    public List<OpsScriptHis> getScriptHisListByScriptId(@RequestParam("scriptId") Long scriptId) {
        return opsClient.getScriptHisListByScriptId(scriptId);
    }

    @Override
    public List<OpsStepHis> queryOpsStepHisListByPipelineHisId(@RequestParam("pipelineHisId") Long pipelineHisId) {
        return opsClient.queryOpsStepHisListByPipelineHisId(pipelineHisId);
    }

    @Override
    public OpsPipelineHis queryOpsPipelineHisById(@RequestParam("pipelineHisId") Long pipelineHisId) {
        return opsClient.queryOpsPipelineHisById(pipelineHisId);
    }

    @Override
    public OpsScriptHis queryOpsScriptHisById(@RequestParam("scriptHisId") Long scriptHisId) {
        return opsClient.queryOpsScriptHisById(scriptHisId);
    }

    @Override
    public GeneralResponse updatePipelineVersion(@RequestParam("pipelineHisId") Long pipelineHisId) {
        return opsClient.updatePipelineVersion(pipelineHisId);
    }

    @Override
    public GeneralResponse updateScriptVersion(@RequestParam("scriptHisId") Long scriptHisId) {
        return opsClient.updateScriptVersion(scriptHisId);
    }

    @Override
    public PageListQueryResult<NormalAgentHostInfo> getNormalAgentHostList(@RequestBody AgentHostQueryModel queryParam) {
        return opsClient.getNormalAgentHostList(queryParam);
    }

    @Override
    public List<String> getUsernameListByAgentIp(@RequestParam("agentIp") String agentIp) {
        return opsClient.getUsernameListByAgentIp(agentIp);
    }

    @Override
    public void exportUserPassword(@PathVariable("downloadPassword") String downloadPassword, @RequestBody AgentHostQueryModel queryParam) {
        String zipPassword = EncryptUtil.base64Decrypt(downloadPassword);

        PageListQueryResult<NormalAgentHostInfo> hostResult = opsClient.getNormalAgentHostList(queryParam);
        if (!CollectionUtils.isEmpty(hostResult.getDataList())) {
            Map<String, NormalAgentHostInfo> mapHost = Maps.newHashMap();
            hostResult.getDataList().stream().forEach(item -> mapHost.put(item.getProxyId() + ":" + item.getAgentIp(), item));
            OpsParamValue paramValue = new OpsParamValue();
            paramValue.setIsExportEffective(true);
            paramValue.setAgentIpList(mapHost.keySet());
            //查询密码参数
            paramValue.setParamCode("PASSWORD");
            List<OpsParamValueDetail> paramValueList = opsClient.queryParamValueList(paramValue);
            if (paramValueList == null) {
                paramValueList = Lists.newArrayList();
            }
            for (OpsParamValueDetail opsParamValueDetail : paramValueList) {
                NormalAgentHostInfo hostInfo = mapHost.get(opsParamValueDetail.getAgentIp());
                if (hostInfo != null) {
                    opsParamValueDetail.setDeviceClass(hostInfo.getDeviceClassName());
                    opsParamValueDetail.setDeviceType(hostInfo.getDeviceTypeName());
                    opsParamValueDetail.setDeviceName(hostInfo.getDeviceName());
                    opsParamValueDetail.setBizSystem(hostInfo.getBizSystemName());
                }
            }

            FileOutputStream fos = null;
            String zipPath = "";
            try {
                // 生成excel，并根据打包密码打成zip包
                Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("实例密码参数列表", "参数列表"),
                        OpsParamValueDetail.class, paramValueList);
                Path tempDir = Files.createTempDirectory("username_download_");
                String filePath = tempDir + File.separator + "实例密码参数列表.xls";
                zipPath = tempDir + File.separator + "密码参数产出.zip";
                fos = new FileOutputStream(new File(filePath));
                workbook.write(fos);
                ZipUtil.zipFileAndEncrypt(filePath, zipPath, zipPassword);
            } catch (Exception e) {
                log.error("处理参数文件失败！", e);
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    log.error("关闭文件流失败！");
                }
            }
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            try (OutputStream os = response.getOutputStream()) {
                response.setHeader("Content-Disposition",
                        "attachment;filename=".concat(String.valueOf(URLEncoder.encode("target.zip", "UTF-8"))));
                response.setHeader("Connection", "close");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Type", "application/octet-stream");
                response.setHeader("Set-Cookie", "cookiename=cookievalue; path=/; Domain=domainvaule; Max- age=seconds; HttpOnly");
                Path target = Paths.get(zipPath);
                os.write(Files.readAllBytes(target));
                os.flush();
            } catch (Exception e) {
                log.error("OpsFileManageController download file {} failed!", zipPath, e);
            }

        }
    }

    @Override
    public void exportStepTargetIp(@PathVariable("step_id") Long stepId) {
        List<NormalAgentHostInfo> hostList = opsClient.queryNormalAgentHostByStepId(stepId);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        EasyPoiUtil.exportExcel(hostList, "目标设备导出列表", "设备列表", NormalAgentHostInfo.class, "host.xls", true, response);
    }

    @Override
    public void downloadTargetHostTemplate(HttpServletResponse response) {
        try {
            List<String> headers = Lists.newArrayList();
            headers.add("资源池[必填]");
            headers.add("设备IP");
            XSSFWorkbook wb = new XSSFWorkbook();
            Map<String, List<Map<String, Object>>> comboDataMap = Maps.newHashMap();
            Map<String, Object> queryParams = new HashMap<>();
            //获取资源池
            List<Map<String, Object>> sourceDataList = moduleClient.getRefModuleDict("ed9ae050263746e1a4fff685c25734eb");
            sourceDataList.stream().forEach(item -> item.put("value", item.get("key")));
            comboDataMap.put("资源池[必填]", sourceDataList);
            poiModuleUtils.generateExcel(wb, "设备列表", headers, null, comboDataMap);
            ServletOutputStream outputStream;
            try {
                outputStream = response.getOutputStream();
                response.setHeader("Content-Disposition", "attachment;filename=11111.xlsx");
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                //写出文件,关闭流
                wb.write(outputStream);
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("导出模板失败.", e);
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (Exception e2) {
            }
        }
    }

    @Override
    public Map<String, Object> importTargetHost(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("请求编码格式 -> {}", request.getCharacterEncoding());
        if (null == file) {
            returnMap.put("flag", "false");
            returnMap.put("message", "文件不能为空！");
            return returnMap;
        }
        log.info("上传文件名称 -> {}", file.getOriginalFilename());
        if (!file.getOriginalFilename().matches("^.+\\.(?i)(xlsx)$") && !file.getOriginalFilename().matches("^.+\\.(?i)(xls)$")) {
            returnMap.put("flag", "false");
            returnMap.put("message", "文件必须是excel格式！");
            return returnMap;
        }
        log.info("上传文件大小 -> {}", file.getSize());
        if (file.getSize() == 0) {
            returnMap.put("flag", "false");
            returnMap.put("message", "文件不能为空！");
            return returnMap;
        }
        InputStream is = null;
        Workbook wb;
        List<Map<String, Object>> sourceDataList = moduleClient.getRefModuleDict("ed9ae050263746e1a4fff685c25734eb");
        List<String> idcTypeList = sourceDataList.stream().map(item -> (String)item.get("key")).collect(Collectors.toList());
        List<SimpleAgentHostInfo> errorHostList = Lists.newArrayList();
        List<SimpleAgentHostInfo> successHostList = Lists.newArrayList();
        try {
            is = file.getInputStream();
            wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
//            Sheet comboSheet = null;
            int totalRows = sheet.getPhysicalNumberOfRows();
            if (totalRows <= 1) {
                returnMap.put("flag", "false");
                returnMap.put("message", "Excel格式不正确, 请先下载Excel模板后, 填写数据再上传!");
                return returnMap;
            }
            for (int r = 1; r < totalRows; r++) {
                if (sheet.getRow(r) == null) {
                    continue;
                }
                String idcType = getDataCellText(sheet.getRow(r).getCell(0));
                String ip = getDataCellText(sheet.getRow(r).getCell(1));
                if (!idcTypeList.contains(idcType)) {
                    SimpleAgentHostInfo host = new SimpleAgentHostInfo();
                    host.setPool(idcType);
                    host.setAgentIp(ip);
                    host.setErrorReason("资源池不存在");
                    errorHostList.add(host);
                } else {
                    SimpleAgentHostInfo simpleAgentHostInfo = opsClient.querySimpleHostByPoolAndHostIp(idcType, ip);
                    if (simpleAgentHostInfo == null) {
                        SimpleAgentHostInfo host = new SimpleAgentHostInfo();
                        host.setPool(idcType);
                        host.setAgentIp(ip);
                        host.setErrorReason("不存在该agent设备信息");
                        errorHostList.add(host);
                    } else {
                        successHostList.add(simpleAgentHostInfo);
                    }
                }
            }
            returnMap.put("flag", "true");
            returnMap.put("errorHostList", errorHostList);
            returnMap.put("successHostList", successHostList);
            return returnMap;
        } catch (Exception e) {
            returnMap.put("flag", "false");
            returnMap.put("message", "解析Excel文件失败:" + e.getMessage());
            returnMap.put("error", e.getMessage());
            return returnMap;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected String getDataCellText(Cell dataCell) {
        String cellValue = "";
        if (dataCell != null) {
            // 判断数据的类型
            switch (dataCell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    if (HSSFDateUtil.isCellDateFormatted(dataCell)) {// 处理日期格式、时间格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = dataCell.getDateCellValue();
                        cellValue = sdf.format(date);
                    } else if (dataCell.getCellStyle().getDataFormat() == 0) {//处理数值格式 General格式
//                        dataCell.setCellType(Cell.CELL_TYPE_STRING);
//                        cellValue = String.valueOf(dataCell.getRichStringCellValue().toString());
//                        if (cellValue != null && cellValue.endsWith(".0")) {
//                            cellValue = cellValue.substring(0, cellValue.length() - 2);
//                        }
                        DecimalFormat df = new DecimalFormat("#.##########");
                        Double  value = dataCell.getNumericCellValue();
                        cellValue = df.format(value);
                    } else {
                        if("#,##0.00".equals(dataCell.getCellStyle().getDataFormatString())) { // 货币格式‘#,##0.00’会在数字后添加随机的浮点数
                            cellValue = String.valueOf(dataCell.getNumericCellValue());
                        } else {
//                            dataCell.setCellType(Cell.CELL_TYPE_STRING);
//                            cellValue = String.valueOf(dataCell.getRichStringCellValue().toString());
                            DecimalFormat df = new DecimalFormat("#.##########");
                            Double  value = dataCell.getNumericCellValue();
                            cellValue = df.format(value);
                        }
                    }
                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    cellValue = String.valueOf(dataCell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BOOLEAN: // Boolean
                    cellValue = String.valueOf(dataCell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA: // 公式
                    cellValue = String.valueOf(dataCell.getCellFormula());
                    break;
                default:
                    break;
            }
        }
        return cellValue;
    }
    @Autowired
    private POIModuleUtils poiModuleUtils;
    @Autowired
    private ModuleClient moduleClient;

}
