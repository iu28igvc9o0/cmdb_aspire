/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  SimpleAgentInfo.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月4日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: SimpleAgentHostInfo
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月4日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_EMPTY)
public class SimpleAgentHostInfo {
	@JsonProperty("pool")
	private String			pool;

	@JsonProperty("proxy_id")
	private Long			proxyId;
	
	@JsonProperty("proxy_identity")
	private String			proxyIdentity;

	@JsonProperty("agent_ip")
	private String			agentIp;

	@JsonProperty("host_os_type")
	private String			hostOsType;

	@JsonProperty("status")
	private AgentStatusEnum	status;

	@JsonProperty("error_reason")
	private String errorReason;
	
	@JsonIgnore
	protected final Map<String, Object>	extendAttrMap	= new HashMap<String, Object>();	// 其它属性


	@JsonAnyGetter
	public Map<String, Object> getExtendAttrMap() {
		return extendAttrMap;
	}
	
	@JsonAnySetter
	public void addExtendAttr(String key, Object value) {
		this.extendAttrMap.put(key, value);
	}
	
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public static class SimpleAgentHostQueryModel extends AbsPageQueryParamsAware {
		@JsonProperty("agent_ip")
		private String	agentIp;
		
		@JsonProperty("host_os_type")
		private String	hostOsType;
		
		@JsonProperty("status")
		private String	status;

		@JsonProperty("pool")
		private String pool;

		@JsonProperty("agent_ip_list")
		private List<String> agentIpList;

		@JsonIgnore
		protected final Map<String, Object>	extendParams	= new HashMap<String, Object>();	// 其它查询参数

		@JsonAnyGetter
		public Map<String, Object> getExtendParams() {
			return extendParams;
		}
		
		@JsonAnySetter
		public void addExtendParam(String key, Object value) {
			this.extendParams.put(key, value);
		}
	}
}
