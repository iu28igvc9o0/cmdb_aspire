package com.aspire.mirror.ops.biz.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.tuple.Pair;

/** 
 *
 * 项目名称: ops-proxy 
 * <p/>
 * 
 * 类名: AbstractOpsMessageBody
 * <p/>
 *
 * 类功能描述: Ops数据定义模型抽象父类
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
public abstract class AbstractOpsMessageBody {
	@JsonProperty("target_ops_user")
	protected String				targetOpsUser;
	@JsonProperty("target_host_list")
	protected List<OpsAgentInfo>	targetHostList;

	@JsonProperty("auth_list")
	protected List<String> authList;

	@JsonProperty("customize_params")
	private Map<String, Map<String, Pair<String, String>>> customizeParams;

	public Map<String, Map<String, Pair<String, String>>> getCustomizeParams() {
		return customizeParams;
	}

	public void setCustomizeParams(Map<String, Map<String, Pair<String, String>>> customizeParams) {
		this.customizeParams = customizeParams;
	}

	public List<String> getAuthList() {
		return authList;
	}

	public void setAuthList(List<String> authList) {
		this.authList = authList;
	}

	public final String getTargetOpsUser() {
		return targetOpsUser;
	}

	public final void setTargetOpsUser(String targetOpsUser) {
		this.targetOpsUser = targetOpsUser;
	}
	
	public final void setTargetHostList(List<OpsAgentInfo> targetHostList) {
		this.targetHostList = targetHostList;
	}
	
	public final List<OpsAgentInfo> getTargetHostList() {
		return targetHostList;
	}
}
