/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsStep.java 
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

import java.util.List;

import com.aspire.mirror.ops.api.domain.TargetExecObject;
import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.OpsFileSource;
import com.aspire.mirror.ops.api.domain.OpsStepDTO;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsStep
 * <p/>
 *
 * 类功能描述: 流水作业步骤定义
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
public class OpsStep extends OpsStepDTO {
	
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
	public String getTargetHostsJson() {
		return targetHosts == null ? null : JsonUtil.toJacksonJson(targetHosts);
	}



	public void setTargetHostsJson(String targetHostsString) {
		if (StringUtils.isBlank(targetHostsString)) {
			this.targetHosts = null;
			return;
		}
		TypeReference<List<String>> typeRef = new TypeReference<List<String>>(){};
		this.targetHosts = JsonUtil.jacksonConvert(targetHostsString, typeRef);
	}

	@JsonIgnore
	public String getTargetExecObjectJson() {
		return targetExecObject == null ? null : JsonUtil.toJacksonJson(targetExecObject);
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
	public String getReplaceAttrListJson() {
		return replaceAttrList == null ? null : JsonUtil.toJacksonJson(replaceAttrList);
	}
	
	public void setReplaceAttrListJson(String replaceAttrListJson) {
		if (StringUtils.isBlank(replaceAttrListJson)) {
			this.replaceAttrList = null;
			return;
		}
		TypeReference<List<ReplaceAttrDefine>> typeRef = new TypeReference<List<ReplaceAttrDefine>>(){};
		this.replaceAttrList = JsonUtil.jacksonConvert(replaceAttrListJson, typeRef);
	}
	
}
