/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsPipelineInstance.java 
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.AspNodeResultEnum;
import com.aspire.mirror.ops.api.domain.OpsFileTransferActionModel;
import com.aspire.mirror.ops.api.domain.OpsIndexValueCollectRequest;
import com.aspire.mirror.ops.api.domain.OpsIndexValueCollectRequest.IndexValueCollectAgentInfo;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.api.domain.OpsScriptExecuteActionModel;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsPipelineInstance
 * <p/>
 *
 * 类功能描述: 作业流水实例
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
public class OpsPipelineInstance extends OpsPipelineInstanceDTO {
	
	public Float calcTotalSeconds() {
		if (endTime == null || startTime == null) {
			return null;
		}
		return (endTime.getTime() - startTime.getTime()) / 1000f;
	}
	
	@JsonIgnore
	public String getBizMetaJson() {
		return JsonUtil.toJacksonJson(bizMeta);
	}
	
	public void setBizMetaJson(String bizMetaJson) {
		if (StringUtils.isBlank(bizMetaJson)) {
			super.bizMeta.clear();
		}
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>(){};
		bizMeta.putAll(JsonUtil.jacksonConvert(bizMetaJson, typeRef));
	}
	
	/** 
	 * 功能描述: 构造实时脚本执行作业实例  
	 * <p>
	 * @param scriptExecuteData
	 * @return
	 */
	public static OpsPipelineInstance buildScriptExecutePipelineInstance(OpsScriptExecuteActionModel scriptExecuteData) {
		OpsPipelineInstance pipelineInstance = new OpsPipelineInstance();
		pipelineInstance.setPipelineId(NO_PIPELINE_ID);
		pipelineInstance.setPipelineInstanceName(scriptExecuteData.getRunName());
		pipelineInstance.setInstanceClassify(CLASSIFY_REALTIME_SCRIPT);
		pipelineInstance.setBizClassify(BIZ_CLASSIFY_OPS);
		pipelineInstance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		pipelineInstance.setCreateTime(new Date());
		pipelineInstance.setStartTime(new Date());
		pipelineInstance.setOperator(RequestAuthContext.getRequestHeadUserName());
		pipelineInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return pipelineInstance;
	}
	
	/** 
	 * 功能描述: 构造巡检指标采集作业实例  
	 * <p>
	 * @param indexCollectData
	 * @return
	 */
	public static OpsPipelineInstance buildIndexCollectPipelineInstance(OpsIndexValueCollectRequest indexCollectData) {
		OpsPipelineInstance pipelineInstance = new OpsPipelineInstance();
		pipelineInstance.setPipelineId(NO_PIPELINE_ID);
		pipelineInstance.setPipelineInstanceName(indexCollectData.getScriptData().getScriptName());
		pipelineInstance.setInstanceClassify(CLASSIFY_REALTIME_SCRIPT);
		pipelineInstance.setBizClassify(BIZ_CLASSIFY_INDEX_COLLECT);
		pipelineInstance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		// 记录巡检业务的meta信息，因为一个巡检采集脚本对应的指标是一样的，所以只需要取第一个就可以
		IndexValueCollectAgentInfo targetInfo = indexCollectData.getCollectTargetList().get(0);
		Map<String, Object> bizMetaInfo = targetInfo.getResultMessage();
		if (MapUtils.isNotEmpty(bizMetaInfo)) {
			Map<String, Object> copy = new HashMap<>(bizMetaInfo);
			copy.remove("hostId");	// 移除hostId属性
			pipelineInstance.putAllBizMetaAttr(copy);
		}
		pipelineInstance.setCreateTime(new Date());
		pipelineInstance.setStartTime(new Date());
		pipelineInstance.setOperator(RequestAuthContext.getRequestHeadUserName());
		pipelineInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return pipelineInstance;
	}
	
	/** 
	 * 功能描述: 构造实时脚本执行作业实例  
	 * <p>
	 * @param scriptExecuteData
	 * @return
	 */
	public static OpsPipelineInstance buildFileTransferPipelineInstance(OpsFileTransferActionModel fileTransferData) {
		OpsPipelineInstance pipelineInstance = new OpsPipelineInstance();
		pipelineInstance.setPipelineId(NO_PIPELINE_ID);
		pipelineInstance.setPipelineInstanceName(fileTransferData.getRunName());
		pipelineInstance.setInstanceClassify(CLASSIFY_FILE_TRANSFER);
		pipelineInstance.setBizClassify(BIZ_CLASSIFY_OPS);
		pipelineInstance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		pipelineInstance.setCreateTime(new Date());
		pipelineInstance.setStartTime(new Date());
		pipelineInstance.setOperator(RequestAuthContext.getRequestHeadUserName());
		pipelineInstance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return pipelineInstance;
	}
}
