/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsAutoRepairOperateLog.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.autorepair.OpsAutoRepairExecuteLogDTO;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsAutoRepairExecuteLog
 * <p/>
 *
 * 类功能描述: 自动修复作业执行结果记录
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
@JsonInclude(Include.NON_EMPTY)
public class OpsAutoRepairExecuteLog extends OpsAutoRepairExecuteLogDTO {
	
	public void setExtendDataMapJson(String extendAttrsJson) {
		if (StringUtils.isBlank(extendAttrsJson)) {
			return;
		}
		TypeReference<List<Map<String, Object>>> typeRef = new TypeReference<List<Map<String, Object>>>() {};
		List<Map<String, Object>> extendDataArr = JsonUtil.jacksonConvert(extendAttrsJson, typeRef);
		extendDataList.clear();
		extendDataList.addAll(extendDataArr);
	}
	
	@JsonIgnore
	public String getExtendDataMapJson() {
		if (extendDataList.isEmpty()) {
			return null;
		}
		return JsonUtil.toJacksonJson(extendDataList);
	}
	
	public void setExecutedPipeInstIdListJson(String executedPipeInstIdListJson) {
		if (StringUtils.isBlank(executedPipeInstIdListJson)) {
			return;
		}
		TypeReference<List<Long>> typeRef = new TypeReference<List<Long>>() {};
		List<Long> pipeInstIdList = JsonUtil.jacksonConvert(executedPipeInstIdListJson, typeRef);
		executedPipeInstIdList.clear();
		executedPipeInstIdList.addAll(pipeInstIdList);
	}
	
	@JsonIgnore
	public String getExecutedPipeInstIdListJson() {
		if (executedPipeInstIdList.isEmpty()) {
			return null;
		}
		return JsonUtil.toJacksonJson(executedPipeInstIdList);
	}
	
	
	public void setExecutedPipeInstNameListJson(String executedPipeInstNameListJson) {
		if (StringUtils.isBlank(executedPipeInstNameListJson)) {
			return;
		}
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
		List<String> pipeInstNameList = JsonUtil.jacksonConvert(executedPipeInstNameListJson, typeRef);
		executedPipeInstNameList.clear();
		executedPipeInstNameList.addAll(pipeInstNameList);
	}
	
	@JsonIgnore
	public String getExecutedPipeInstNameListJson() {
		if (executedPipeInstNameList.isEmpty()) {
			return null;
		}
		return JsonUtil.toJacksonJson(executedPipeInstNameList);
	}
	
	/** 
	 * 功能描述: 执行结果是否已经完结  
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public boolean isExecuteResultOver() {
		if (status == null) {
			return false;
		}
		OpsStatusEnum statusEnum = OpsStatusEnum.of(status);
		if (statusEnum == OpsStatusEnum.STATUS_101 
				|| statusEnum == OpsStatusEnum.STATUS_102 
				|| statusEnum == OpsStatusEnum.STATUS_9) {
			return true;
		}
		return false;
	}
}
