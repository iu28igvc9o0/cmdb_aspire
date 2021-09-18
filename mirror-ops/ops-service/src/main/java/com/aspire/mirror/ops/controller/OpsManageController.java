/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsManageController.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.ops.domain.AgentHostInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.anno.ResFilterKeysValid;
import com.aspire.mirror.ops.api.domain.AgentHostQueryModel;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.GroupScenes;
import com.aspire.mirror.ops.api.domain.NormalAgentHostInfo;
import com.aspire.mirror.ops.api.domain.OpsAccount;
import com.aspire.mirror.ops.api.domain.OpsAccountQueryModel;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult;
import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult.OpsAgentStepInstanceResultQueryModel;
import com.aspire.mirror.ops.api.domain.OpsFileTransferActionModel;
import com.aspire.mirror.ops.api.domain.OpsIndexValueCollectRequest;
import com.aspire.mirror.ops.api.domain.OpsLabel;
import com.aspire.mirror.ops.api.domain.OpsLabel.OpsLabelQueryModel;
import com.aspire.mirror.ops.api.domain.OpsParam;
import com.aspire.mirror.ops.api.domain.OpsParamQueryModel;
import com.aspire.mirror.ops.api.domain.OpsParamReference;
import com.aspire.mirror.ops.api.domain.OpsParamType;
import com.aspire.mirror.ops.api.domain.OpsParamValue;
import com.aspire.mirror.ops.api.domain.OpsParamValueDetail;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO.OpsPipelineQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineHis;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceLog;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceQueryParam;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob;
import com.aspire.mirror.ops.api.domain.OpsPipelineRunJob.OpsPipelineRunJobQueryModel;
import com.aspire.mirror.ops.api.domain.OpsPipelineScenes;
import com.aspire.mirror.ops.api.domain.OpsScript;
import com.aspire.mirror.ops.api.domain.OpsScriptExecuteActionModel;
import com.aspire.mirror.ops.api.domain.OpsScriptHis;
import com.aspire.mirror.ops.api.domain.OpsScriptQueryModel;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsStepDTO;
import com.aspire.mirror.ops.api.domain.OpsStepHis;
import com.aspire.mirror.ops.api.domain.OpsStepInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsTriggerWayEnum;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;
import com.aspire.mirror.ops.api.service.IOpsManageService;
import com.aspire.mirror.ops.biz.AgentHostDataBiz;
import com.aspire.mirror.ops.biz.CommonSftpServerConfig;
import com.aspire.mirror.ops.biz.OpsActionBiz;
import com.aspire.mirror.ops.biz.OpsBaseDataBiz;
import com.aspire.mirror.ops.util.JsonUtil;
import com.aspire.mirror.ops.util.SshUtil;
import com.aspire.mirror.ops.util.SshUtil.SshResultWrap;
import com.aspire.mirror.ops.util.SshUtil.SshdServer;
import com.fasterxml.jackson.core.type.TypeReference;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsManageController
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@RestController
public class OpsManageController implements IOpsManageService {
	private static final String				OPS_TRANSFER_DIR				= "/ops_transfer/";
	@Autowired
	private OpsBaseDataBiz					opsManageBiz;
	@Autowired
	private OpsActionBiz					opsActionBiz;
	@Autowired
	private AgentHostDataBiz				agentDataBiz;
	@Autowired
	private KafkaTemplate<String, String>	kafkaTemplate;
	@Autowired
	private CommonSftpServerConfig			sftpConfig;
	
	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void mockKafkaMessageSend(@PathVariable("topicName") String topicName, @RequestBody Map<String, Object> messageObj) {
		log.info("MockKafkaMessageSend for topic {}.", topicName);
		kafkaTemplate.send(topicName, JsonUtil.toJacksonJson(messageObj));
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse getAgentHostInfoLoadSource() {
		return agentDataBiz.getAgentHostInfoLoadSource();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<SimpleAgentHostInfo> fetchUserAuthedAgentHostList(@RequestBody SimpleAgentHostQueryModel queryParam) {
		return agentDataBiz.fetchUserAuthedAgentHostList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public SimpleAgentHostInfo queryAgentInfoByProxyIdConcatIP(@PathVariable("proxyIdConcatIP") String proxyIdConcatIP) {
		return agentDataBiz.queryAgentInfoByProxyIdConcatIP(proxyIdConcatIP);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadOpsStatusList() {
		List<Map<Integer, String>> result = new ArrayList<Map<Integer, String>>();
		for (OpsStatusEnum status : OpsStatusEnum.values()) {
			result.add(Collections.singletonMap(status.getStatusCode(), status.getLabel()));
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<Map<Integer, String>> loadOpsTriggerWayList() {
		List<Map<Integer, String>> result = new ArrayList<Map<Integer, String>>();
		for (OpsTriggerWayEnum triggerWay : OpsTriggerWayEnum.values()) {
			result.add(Collections.singletonMap(triggerWay.getStatusCode(), triggerWay.getLabel()));
		}
		return result;
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsAccount(@RequestBody OpsAccount account) {
		return opsManageBiz.saveOpsAccount(account);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeOpsAccount(@PathVariable("accountName") String accountName) {
		return opsManageBiz.removeOpsAccount(accountName);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsAccount> queryOpsAccountList(@RequestBody OpsAccountQueryModel queryParam) {
		return opsManageBiz.queryOpsAccountList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsLabel(@RequestBody OpsLabel label) {
		return opsManageBiz.saveOpsLabel(label);
	}
    
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeOpsLabel(@PathVariable("labelCode") String labelCode) {
		return opsManageBiz.removeOpsLabel(labelCode);
	}
    
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsLabel> queryOpsLabelList(@RequestBody OpsLabelQueryModel queryParam) {
		return opsManageBiz.queryOpsLabelList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsScript(@RequestBody OpsScript script) {
		return opsManageBiz.saveOpsScript(script);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeOpsScript(@PathVariable("scriptId") Long scriptId) {
		return opsManageBiz.removeOpsScript(scriptId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResFilterKeysValid(notAllEmpty = {"opsPipeline", "opsScript", "opsYum", "opsGroup"})
	public PageListQueryResult<OpsScript> queryOpsScriptList(@RequestBody OpsScriptQueryModel queryParam) {
		return opsManageBiz.queryOpsScriptList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsScript queryOpsScriptById(@RequestParam("scriptId") Long scriptId) {
		return opsManageBiz.queryOpsScriptById(scriptId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsPipeline(@RequestBody OpsPipelineDTO pipeline) {
		return opsManageBiz.saveOpsPipeline(pipeline);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeOpsPipeline(@PathVariable("pipelineId") Long pipelineId) {
		return opsManageBiz.removeOpsPipeline(pipelineId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	@ResFilterKeysValid(notAllEmpty = {"opsPipeline", "opsScript", "opsYum", "opsGroup"})
	public PageListQueryResult<OpsPipelineDTO> queryOpsPipelineList(@RequestBody OpsPipelineQueryModel queryParam) {
		return opsManageBiz.queryOpsPipelineList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsPipelineDTO queryOpsPipelineById(@PathVariable("pipelineId") Long pipelineId) {
		return opsManageBiz.queryOpsPipelineById(pipelineId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsStepDTO> queryOpsStepListByPipelineId(@PathVariable("pipelineId") Long pipelineId) {
		return opsManageBiz.queryOpsStepListByPipelineId(pipelineId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
//	@ResFilterKeysValid(notAllEmpty = {"opsPipeline", "opsScript", "opsYum", "opsGroup"})
	public PageListQueryResult<OpsPipelineInstanceDTO> queryOpsPipelineInstanceList(
								@RequestBody OpsPipelineInstanceQueryParam queryParam) {
		return opsManageBiz.queryOpsPipelineInstanceList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsPipelineInstanceDTO queryOpsPipelineInstanceById(@PathVariable("pipelineInstanceId") Long pipelineInstanceId) {
		return opsManageBiz.queryOpsPipelineInstanceById(pipelineInstanceId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsStepInstanceDTO> queryStepInstListByPipelineInstId(
					@PathVariable("pipelineInstanceId") Long pipelineInstanceId) {
		return opsManageBiz.queryStepInstListByPipelineInstId(pipelineInstanceId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsStepInstanceDTO queryStepInstanceById(@RequestParam("stepInstId") Long stepInstId) {
		return opsManageBiz.queryStepInstanceById(stepInstId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsAgentStepInstanceResult> 
			queryOpsStepAgentRunResultList(@RequestBody OpsAgentStepInstanceResultQueryModel param) {
		return opsManageBiz.queryOpsStepAgentRunResultList(param);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse executeRealtimeScript(@RequestBody OpsScriptExecuteActionModel scriptExecuteData) {
		return opsActionBiz.executeRealtimeScript(scriptExecuteData);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse executeRealtimeFileTransfer(@RequestBody OpsFileTransferActionModel requestData) {
		return opsActionBiz.executeRealtimeFileTransfer(requestData);
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
			
			SshdServer sftpServer = new SshdServer();
			sftpServer.setIpAddress(sftpConfig.getIpAddress());
			sftpServer.setPort(sftpConfig.getPort());
			sftpServer.setLoginUser(sftpConfig.getLoginUser());
			sftpServer.setLoginPass(sftpConfig.getLoginPass());
			
			String subDir = OPS_TRANSFER_DIR + tempDir.toFile().getName();
			String remotePath = sftpConfig.getRootDirectory() + subDir;
			SshUtil.executeShellCmd(sftpServer, 5, "mkdir -p " + remotePath + " || ls " + remotePath);
			
			Pair<SshResultWrap, String> uploadResult = SshUtil.sftpUpload(sftpServer, tempFile, remotePath);
			if (!uploadResult.getLeft().isSuccess()) {
				return new GeneralResponse(false, uploadResult.getLeft().getBizLog());
			}
			String fileRelativePath = subDir + "/" + tempFile.toFile().getName();
			return new GeneralResponse(true, null, fileRelativePath);
		} catch (IOException e) {
			return new GeneralResponse(false, e.toString());
		}
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse executePipeline(@PathVariable("pipelineId") Long pipelineId, 
					@RequestBody(required=false) Map<String, Object> params) {
		Object selectStepObj = null;
		List<Long> selectStepIdList = null;
		if (params != null) {
			selectStepObj = params.get("select_step_list");
		}
		if (selectStepObj != null) {
			TypeReference<List<Long>> typeRef = new TypeReference<List<Long>>() {};
			selectStepIdList = JsonUtil.jacksonConvert(selectStepObj, typeRef);
		}
		return opsActionBiz.executePipeline(pipelineId, selectStepIdList, OpsTriggerWayEnum.TRIGGER_BY_MANUAL);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse manualHandleOpsStepInstance(@PathVariable("stepInstanceId") Long stepInstanceId, 
			@PathVariable("status") Integer status) {
		return opsActionBiz.manualHandleOpsStepInstance(stepInstanceId, status);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse executeIndexValueScriptCollect(@RequestBody OpsIndexValueCollectRequest indexCollectData) {
		return opsActionBiz.executeIndexValueScriptCollect(indexCollectData);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveOpsPipelineRunJob(@RequestBody OpsPipelineRunJob runJob) {
		return opsManageBiz.saveOpsPipelineRunJob(runJob);
	}
    
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeOpsPipelineRunJob(@PathVariable("jobId") Long jobId) {
		return opsManageBiz.removeOpsPipelineRunJob(jobId);
	}
    
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsPipelineRunJob> queryOpsPipelineRunJobList(@RequestBody OpsPipelineRunJobQueryModel queryParam) {
		return opsManageBiz.queryOpsPipelineRunJobList(queryParam);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse schedulePipelineCronJob(@PathVariable("jobId") Long jobId) {
		return opsManageBiz.schedulePipelineCronJob(jobId);
	}
    
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse unSchedulePipelineCronJob(@PathVariable("jobId") Long jobId) {
		return opsManageBiz.unSchedulePipelineCronJob(jobId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse executePipelineCronJob(@PathVariable("jobId") Long jobId) {
		return opsActionBiz.executePipelineCronJob(jobId);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse reviewSensitiveApply(@PathVariable("pipelineInstanceId") Long pipelineInstanceId) {
		return opsActionBiz.reviewSensitiveApply(pipelineInstanceId);
	}

	@Override
	public GeneralResponse savePipelineScences(@RequestBody OpsPipelineScenes scenes) {
		return opsManageBiz.savePipelineScences(scenes);
	}

	@Override
	public List<GroupScenes> pipelineScenesAllList() {
		return opsManageBiz.pipelineScenesAllList();
	}

	@Override
	public OpsPipelineScenes pipelineScenesById(@RequestParam("pipeline_scenes_id") Long pipelineScenesId) {
		if (pipelineScenesId == null) {
			return null;
		}
		return opsManageBiz.pipelineScenesById(pipelineScenesId);
	}

	@Override
	public GeneralResponse deletePipelineScenes(@RequestParam("scenes_ids") String scenesIds) {
		if (StringUtils.isEmpty(scenesIds)) {
			log.error("deletePipelineScenes param is empty");
			return new GeneralResponse(false,"场景删除操作参数不能为空");
		}
		return opsManageBiz.deletePipelineScenes(scenesIds);
	}

	@Override
	public GeneralResponse logPackageApply(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
		if (pipelineInstanceId == null) {
			return new GeneralResponse(false, "参数作业实例ID不能为空");
		}
		return opsManageBiz.logPackageApply(pipelineInstanceId);
	}

	@Override
	public OpsPipelineInstanceLog getPipelineInstanceLog(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
		return opsManageBiz.getPipelineInstanceLog(pipelineInstanceId);
	}

	@Override
	public GeneralResponse outputPackage(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
		return opsManageBiz.outputPackage(pipelineInstanceId);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsParam> getParamAllList() {
		return opsManageBiz.getParamAllList(null).getDataList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsParam> searchParamList(@RequestBody OpsParamQueryModel paramModel) {
		return opsManageBiz.getParamAllList(paramModel);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse createOpsParam(@RequestBody OpsParam createModel) {
		return opsManageBiz.saveOpsParam(createModel);
	}
    
    @Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse updateOpsParam(@RequestBody OpsParam updateModel) {
    	return opsManageBiz.saveOpsParam(updateModel);
    }
    
    @Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse deleteOpsParamById(@PathVariable("paramId") Long paramId) {
    	return opsManageBiz.deleteOpsParamById(paramId);
    }
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsParamType> loadAllParamTypeList() {
		return opsManageBiz.loadAllParamTypeList();
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsParamReference> queryReferParamListByEntityId(@PathVariable("entityId") Long entityId) {
		return opsManageBiz.queryReferParamListByEntityId(entityId);
	}

	@Override
	public GeneralResponse auditPipeline(@RequestParam("pipelineId") Long pipelineId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc", required = false) String auditDesc) {
		return opsManageBiz.auditPipeline(pipelineId, auditStatus, auditDesc);
	}

	@Override
	public GeneralResponse auditScript(@RequestParam("scriptId") Long scriptId, @RequestParam("auditStatus") String auditStatus, @RequestParam(name = "auditDesc", required = false) String auditDesc) {
		return opsManageBiz.auditScript(scriptId, auditStatus, auditDesc);
	}

	@Override
	public GeneralResponse continueExecInstance(@RequestParam("pipelineInstanceId") Long pipelineInstanceId) {
		return opsActionBiz.continueExecInstance(pipelineInstanceId);
	}

	@Override
	public List<OpsPipelineHis> getPipelineHisListByPipelineId(@RequestParam("pipelineId") Long pipelineId) {
		return opsManageBiz.getPipelineHisListByPipelineId(pipelineId);
	}

	@Override
	public List<OpsScriptHis> getScriptHisListByScriptId(@RequestParam("scriptId") Long scriptId) {
		return opsManageBiz.getScriptHisListByScriptId(scriptId);
	}

	@Override
	public List<OpsStepHis> queryOpsStepHisListByPipelineHisId(@RequestParam("pipelineHisId") Long pipelineHisId) {
		return opsManageBiz.queryOpsStepHisListByPipelineHisId(pipelineHisId);
	}

	@Override
	public OpsPipelineHis queryOpsPipelineHisById(@RequestParam("pipelineHisId") Long pipelineHisId) {
		return opsManageBiz.queryOpsPipelineHisById(pipelineHisId);
	}

	@Override
	public OpsScriptHis queryOpsScriptHisById(@RequestParam("scriptHisId") Long scriptHisId) {
		return opsManageBiz.queryOpsScriptHisById(scriptHisId);
	}

	@Override
	public GeneralResponse updatePipelineVersion(@RequestParam("pipelineHisId") Long pipelineHisId) {
		return opsManageBiz.updatePipelineVersion(pipelineHisId);
	}

	@Override
	public GeneralResponse updateScriptVersion(@RequestParam("scriptHisId") Long scriptHisId) {
		return opsManageBiz.updateScriptVersion(scriptHisId);
	}

	@Override
	public PageListQueryResult<NormalAgentHostInfo> getNormalAgentHostList(@RequestBody AgentHostQueryModel queryParam) {
		return agentDataBiz.queryNormalAgentHostList(queryParam);
	}

	@Override
	public List<String> getUsernameListByAgentIp(@RequestParam("agentIp") String agentIp) {
		return opsManageBiz.getUsernameListByAgentIp(agentIp);
	}

	@Override
	public List<OpsParamValueDetail> queryParamValueList(@RequestBody OpsParamValue paramValue) {
		return opsManageBiz.queryParamValueList(paramValue);
	}

	@Override
	public List<NormalAgentHostInfo> queryNormalAgentHostByStepId(@PathVariable("step_id") Long stepId) {
		return opsManageBiz.queryNormalAgentHostByStepId(stepId);
	}

	@Override
	public SimpleAgentHostInfo querySimpleHostByPoolAndHostIp(@RequestParam("pool") String pool, @RequestParam("ip") String ip) {
 		AgentHostInfo agentHostInfo = agentDataBiz.queryAgentDataByPoolAndAgentIP(pool, ip);
		SimpleAgentHostInfo hostInfo = AgentHostInfo.parseFromAgentHostInfo(agentHostInfo);
		return hostInfo;
	}

}
