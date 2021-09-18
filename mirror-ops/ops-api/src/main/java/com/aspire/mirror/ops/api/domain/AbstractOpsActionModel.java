package com.aspire.mirror.ops.api.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: AbstractOpsActionModel
 * <p/>
 *
 * 类功能描述: Ops操作数据定义模型抽象父类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月24日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
public abstract class AbstractOpsActionModel {
	@JsonProperty("run_name")
	protected String		runName;
	
	@JsonProperty("target_ops_user")
	protected String		targetOpsUser;
	
	@JsonProperty("target_host_list")
	protected List<String>	targetHostList;

	@JsonProperty("target_exec_object")
	protected List<TargetExecObject> targetExecObject;


	
	public String getRunName() {
		return runName;
	}
	public void setRunName(String runName) {
		this.runName = runName;
	}

	public final String getTargetOpsUser() {
		return targetOpsUser;
	}

	public final void setTargetOpsUser(String targetOpsUser) {
		this.targetOpsUser = targetOpsUser;
	}
	
	public final void setTargetHostList(List<String> targetHostList) {
		this.targetHostList = targetHostList;
	}
	
	public final List<String> getTargetHostList() {
		return targetHostList;
	}

	public List<TargetExecObject> getTargetExecObject() {
		return targetExecObject;
	}

	public void setTargetExecObject(List<TargetExecObject> targetExecObject) {
		this.targetExecObject = targetExecObject;
	}
}
