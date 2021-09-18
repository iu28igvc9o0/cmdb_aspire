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
package com.aspire.mirror.inspection.server.clientservice.payload;

import com.aspire.mirror.inspection.api.dto.constant.AgentStatusEnum;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

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
public class SimpleAgentHostInfo {
	@JsonProperty("pool")
	private String			pool;

	@JsonProperty("proxy_id")
	private Long			proxyId;

	@JsonProperty("agent_ip")
	private String			agentIp;

	@JsonProperty("host_os_type")
	private String			hostOsType;

	@JsonProperty("status")
	private AgentStatusEnum status;

	@JsonIgnore
	protected final Map<String, Object> extendAttrMap	= new HashMap<String, Object>();	// 其它属性

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
	public static class SimpleAgentHostQueryModel{
		@JsonProperty("agent_ip")
		private String	agentIp;

		@JsonProperty("host_os_type")
		private String	hostOsType;

		@JsonProperty("status")
		private AgentStatusEnum	status;

		@JsonProperty("page_size")
		protected Integer	pageSize;	// 如果为null， 则查询全部数据

		@JsonProperty("page_no")
		protected Integer	pageNo;		// 从0开始


		public Integer getStartIdx() {
			if (pageSize == null) {
				return null;
			}
			return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
		}

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
