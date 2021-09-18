/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsStepInstance.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspire.mirror.ops.api.domain.*;
import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.OpsIndexValueCollectRequest.IndexValueCollectAgentInfo;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsStepInstance
 * <p/>
 *
 * 类功能描述: 作业步骤点运行实例
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class OpsStepInstance extends OpsStepInstanceDTO {
	public static final long	NO_STEP_ID	= -1L;
	
	@JsonIgnore
	public String getTargetHostsJson() {
		return targetHosts == null ? null : JsonUtil.toJacksonJson(targetHosts);
	}

	@JsonIgnore
	public String getTargetExecObjectJson() {
		return targetExecObject == null ? null : JsonUtil.toJacksonJson(targetExecObject);
	}
	public void setTargetHostsJson(String targetHostsString) {
		if (StringUtils.isBlank(targetHostsString)) {
			this.targetHosts = new ArrayList<>();
			return;
		}
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
		this.targetHosts = JsonUtil.jacksonConvert(targetHostsString, typeRef);
	}
	public void setTargetExecObjectJson(String targetExecObjectString) {
		if (StringUtils.isBlank(targetExecObjectString)) {
			this.targetExecObject = null;
			return;
		}
		TypeReference<List<TargetExecObject>> typeRef = new TypeReference<List<TargetExecObject>>(){};
		this.targetExecObject = JsonUtil.jacksonConvert(targetExecObjectString, typeRef);
	}
	@JsonIgnore
	public String getBadHostsJson() {
		return badHosts == null ? null : JsonUtil.toJacksonJson(badHosts);
	}
	
	public void setBadHostsJson(String badHostsString) {
		if (StringUtils.isBlank(badHostsString)) {
			this.badHosts = null;
			return;
		}
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
		this.badHosts = JsonUtil.jacksonConvert(badHostsString, typeRef);
	}
	
	@JsonIgnore
	public String getFileSourceJson() {
		return fileSource == null ? null : JsonUtil.toJacksonJson(fileSource);
	}
	
	public void setFileSourceJson(String filesourceString) {
		if (StringUtils.isBlank(filesourceString)) {
			this.fileSource = null;
			return;
		}
		TypeReference<List<OpsFileSource>> typeRef = new TypeReference<List<OpsFileSource>>(){};
		this.fileSource = JsonUtil.jacksonConvert(filesourceString, typeRef);
	}
	
	@JsonIgnore
	public String getFileStoreSourceJson() {
		return fileStoreSource == null ? null : JsonUtil.toJacksonJson(fileStoreSource);
	}
	
	public void setFileStoreSourceJson(String fileStoreSource) {
		if (StringUtils.isBlank(fileStoreSource)) {
			this.fileStoreSource = null;
			return;
		}
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
		this.fileStoreSource = JsonUtil.jacksonConvert(fileStoreSource, typeRef);
	}
	
	@JsonIgnore
	public boolean isStepOverAndSuccess() {
		return getRunHostsNum() == 0 && getSuccessHostsNum().equals(getTotalHostsNum())
				&& OpsStatusEnum.STATUS_9.getStatusCode().equals(getStatus());
	}
	
	/** 
	 * 功能描述: 从agent的step结果，构造更新对象  
	 * <p>
	 * @param agentResult
	 * @return
	 */
	public OpsStepInstance constructUpdateData(AgentOpsResultDetail agentResult) {
		OpsStepInstance updateData = new OpsStepInstance();
		updateData.setPipelineInstanceId(getPipelineInstanceId());
		updateData.setStepId(getStepId());
		updateData.setStepInstanceId(getStepInstanceId());
		updateData.setBlockOrd(getBlockOrd());
		updateData.setOrd(getOrd());
		updateData.setTotalHostsNum(getTotalHostsNum());
		updateData.setRunHostsNum(getRunHostsNum());
		updateData.setBadHostsNum(getBadHostsNum());
		updateData.setSuccessHostsNum(getSuccessHostsNum());
		updateData.setBadHosts(getBadHosts());
		updateData.setStatus(getStatus());
		updateData.setPauseFlag(getPauseFlag());
		updateData.setStartTime(getStartTime());
		
		int badChangeNum = agentResult.isSuccess() ? 0 : 1;
		int sucChangeNum = agentResult.isSuccess() ? 1 : 0;
		
		updateData.setRunHostsNum(updateData.getRunHostsNum() - 1);
		updateData.setBadHostsNum(updateData.getBadHostsNum() + badChangeNum);
		updateData.setSuccessHostsNum(updateData.getSuccessHostsNum() + sucChangeNum);
		
		if (!agentResult.isSuccess()) {
			updateData.getBadHosts().add(agentResult.getConcatHost());
			if (OpsStatusEnum.STATUS_100.getStatusCode().equals(updateData.getStatus())) {
				updateData.setStatus(OpsStatusEnum.STATUS_101.getStatusCode());
			}
		} 
		// 可能存在超时但所有agent反馈为success的情况
		else if (updateData.getSuccessHostsNum().equals(updateData.getTotalHostsNum())
				&& updateData.getStatus().equals(OpsStatusEnum.STATUS_100.getStatusCode())) {
			updateData.setStatus(OpsStatusEnum.STATUS_9.getStatusCode());
		}
		
		if (updateData.getRunHostsNum() == 0) {
			updateData.setEndTime(new Date());
			float costSeconds = (updateData.getEndTime().getTime() - updateData.getStartTime().getTime()) / 1000f;
			updateData.setTotalTime(costSeconds);
		}
		return updateData;
	}
	
	/** 
	 * 功能描述: 构建脚本执行stepInstance  
	 * <p>
	 * @param scriptExecuteData
	 * @return
	 */
	public static OpsStepInstance buildScriptExecuteStepInstance(OpsScriptExecuteActionModel scriptExecuteData) {
		OpsStepInstance onetimeInstance = new OpsStepInstance();
		onetimeInstance.setStepId(OpsStepInstance.NO_STEP_ID);
		onetimeInstance.setStepInstanceName(scriptExecuteData.getRunName());
		onetimeInstance.setBlockName(scriptExecuteData.getRunName());
		onetimeInstance.setBlockOrd(1);
		onetimeInstance.setOrd(1);
		onetimeInstance.setCreateTime(new Date());
		onetimeInstance.setOpsTimeout(scriptExecuteData.getScript().getTimeout());
		// TODO stepInstance.setOperator(operator);
		// stepInstance.setPipelineInstanceId(pipelineInstanceId);
		onetimeInstance.setOpsType(OpsStep.OPS_TYPE_SCRIPT);
		onetimeInstance.setScriptContentType(scriptExecuteData.getScript().getContentType());
		onetimeInstance.setScriptContent(scriptExecuteData.getScript().decodeScriptContent());
		onetimeInstance.setScriptParam(scriptExecuteData.getScript().getScriptParams());
		onetimeInstance.setScriptSudo(scriptExecuteData.getScript().getScriptSudo());
		onetimeInstance.setParamSensiveFlag(scriptExecuteData.getScript().getParamSensiveFlag());
		//自定义参数相关
		onetimeInstance.setOpsParamReferenceList(scriptExecuteData.getScript().getOpsParamReferenceList());
		onetimeInstance.setPackagePassword(scriptExecuteData.getScript().getPackagePassword());


		onetimeInstance.setPauseFlag(OpsStep.NO_PAUSE);
		onetimeInstance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		onetimeInstance.setTargetOpsUser(scriptExecuteData.getTargetOpsUser());
		onetimeInstance.setTargetHosts(scriptExecuteData.getTargetHostList());
		onetimeInstance.setTargetExecObject(scriptExecuteData.getTargetExecObject());
		onetimeInstance.setTotalHostsNum(scriptExecuteData.getTargetHostList().size());
		onetimeInstance.setRunHostsNum(scriptExecuteData.getTargetHostList().size());
		onetimeInstance.setBadHostsNum(0);
		onetimeInstance.setSuccessHostsNum(0);
		onetimeInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return onetimeInstance;
	}
	
	public static OpsStepInstance buildScriptExecuteStepInstance(OpsFileTransferActionModel fileTransferData) {
		OpsStepInstance onetimeInstance = new OpsStepInstance();
		onetimeInstance.setStepId(OpsStepInstance.NO_STEP_ID);
		onetimeInstance.setStepInstanceName(fileTransferData.getRunName());
		onetimeInstance.setBlockName(fileTransferData.getRunName());
		onetimeInstance.setBlockOrd(1);
		onetimeInstance.setOrd(1);
		onetimeInstance.setCreateTime(new Date());
		onetimeInstance.setOpsTimeout(60 * 60 * 24);	// 超时设置成1天
		// TODO stepInstance.setOperator(operator);
		// stepInstance.setPipelineInstanceId(pipelineInstanceId);
		onetimeInstance.setOpsType(OpsStep.OPS_TYPE_FILE_TRANSFER);
		onetimeInstance.setFileSource(fileTransferData.getFileSourceList());
		onetimeInstance.setFileTargetPath(fileTransferData.getTargetFilePath());
		onetimeInstance.setFileType(fileTransferData.getFileType());
		onetimeInstance.setPauseFlag(OpsStep.NO_PAUSE);
		onetimeInstance.setStatus(OpsStatusEnum.STATUS_100.getStatusCode());
		onetimeInstance.setTargetOpsUser(fileTransferData.getTargetOpsUser());
		onetimeInstance.setTargetHosts(fileTransferData.getTargetHostList());
		onetimeInstance.setTargetExecObject(fileTransferData.getTargetExecObject());
		onetimeInstance.setTotalHostsNum(fileTransferData.getTargetHostList().size());
		onetimeInstance.setRunHostsNum(fileTransferData.getTargetHostList().size());
		onetimeInstance.setBadHostsNum(0);
		onetimeInstance.setSuccessHostsNum(0);
		onetimeInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return onetimeInstance;
	}
	
	public static OpsStepInstance buildScriptExecuteStepInstance(OpsIndexValueCollectRequest opsBizData) {
		OpsStepInstance onetimeInstance = new OpsStepInstance();
		OpsScript scriptData = opsBizData.getScriptData();
		onetimeInstance.setStepId(OpsStepInstance.NO_STEP_ID);
		onetimeInstance.setStepInstanceName(scriptData.getScriptName());
		onetimeInstance.setBlockName(scriptData.getScriptName());
		onetimeInstance.setBlockOrd(1);
		onetimeInstance.setOrd(1);
		onetimeInstance.setCreateTime(new Date());
		onetimeInstance.setOpsTimeout(opsBizData.getTimeout());
		// TODO stepInstance.setOperator(operator);
		// stepInstance.setPipelineInstanceId(pipelineInstanceId);
		onetimeInstance.setOpsType(OpsStep.OPS_TYPE_SCRIPT);
		onetimeInstance.setScriptContentType(scriptData.getContentType());
		onetimeInstance.setScriptContent(scriptData.getScriptContent());
		onetimeInstance.setScriptParam(opsBizData.getScriptParam());
		// 巡检脚本执行设置自定义参数
		List<OpsParamReference> paramReferenceList = opsBizData.getScriptData().getOpsParamReferenceList();
		TypeReference<List<OpsParamReference>> typeRef = new TypeReference<List<OpsParamReference>>() {};
		List<OpsParamReference> paramReferenceNewList = JsonUtil.jacksonConvert(opsBizData.getCustomizeParam(), typeRef);
		if (!CollectionUtils.isEmpty(paramReferenceNewList)) {
			Map<String, String> refMap = paramReferenceNewList.stream().collect(Collectors.toMap(OpsParamReference::getEntityParamCode, OpsParamReference::getParamValue));
			if (!CollectionUtils.isEmpty(paramReferenceList)) {
				for (OpsParamReference opsParamReference : paramReferenceList) {
					if (refMap.get(opsParamReference.getEntityParamCode()) != null) {
						opsParamReference.setParamValue(refMap.get(opsParamReference.getEntityParamCode()));
					}
				}
			}
		}
		onetimeInstance.setOpsParamReferenceList(paramReferenceList);
//		onetimeInstance.setOpsParamCode();
//		onetimeInstance.setParamSensiveFlag(scriptExecuteData.getScript().getParamSensiveFlag());
		onetimeInstance.setPauseFlag(OpsStep.NO_PAUSE);
		onetimeInstance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		onetimeInstance.setTargetOpsUser(opsBizData.getTargetOpsUser());
		List<IndexValueCollectAgentInfo> collectTargetList = opsBizData.getCollectTargetList();
		List<String> agentList = collectTargetList.stream().map(agent -> {
			return agent.getAgentHost();
		}).collect(Collectors.toList());
		onetimeInstance.setTargetHosts(agentList);
		onetimeInstance.setTotalHostsNum(agentList.size());
		onetimeInstance.setRunHostsNum(agentList.size());
		onetimeInstance.setBadHostsNum(0);
		onetimeInstance.setSuccessHostsNum(0);
		onetimeInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return onetimeInstance;
	}
}
