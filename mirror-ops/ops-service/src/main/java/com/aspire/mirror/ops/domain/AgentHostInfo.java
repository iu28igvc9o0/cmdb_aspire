package com.aspire.mirror.ops.domain;

import static com.aspire.mirror.ops.api.domain.AgentStatusEnum.REMOVE;

import java.util.Date;

import com.aspire.mirror.ops.api.domain.AgentStatusEnum;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 *
 * 项目名称: phoenix-api 
 * <p/>
 * 
 * 类名: AgentSyncInfo
 * <p/>
 *
 * 类功能描述: agent主机信息上报对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月17日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"proxyId", "agentIp"})
@ToString(exclude = "embedSshPass")
public class AgentHostInfo {
	private String			pool;
	private Long			proxyId;
	private String 			proxyIdentity;
	private String			agentIp;
	private String			agentEnvId;
	private String			hostOsType;
	private String			baseUser;
	private String			embedSshUser;
	@JsonIgnore
	private String			embedSshPass;
	private Integer			embedSshPort;
	private Date			lastSyncTime;
	private AgentStatusEnum	status;

	public static SimpleAgentHostInfo parseFromAgentHostInfo(AgentHostInfo agentHostInfo) {
		if (agentHostInfo == null) {
			return null;
		}
		SimpleAgentHostInfo simpleAgentHostInfo = new SimpleAgentHostInfo();
		simpleAgentHostInfo.setAgentIp(agentHostInfo.getAgentIp());
		simpleAgentHostInfo.setPool(agentHostInfo.getPool());
		simpleAgentHostInfo.setHostOsType(agentHostInfo.getHostOsType());
		simpleAgentHostInfo.setProxyId(agentHostInfo.getProxyId());
		simpleAgentHostInfo.setProxyIdentity(agentHostInfo.getProxyIdentity());
		simpleAgentHostInfo.setStatus(agentHostInfo.getStatus());
		return simpleAgentHostInfo;
	}

	@JsonIgnore
	public String getConcatHost() {
		return proxyId + ":" + agentIp;
	}
	
	public AgentCmdbSyncData resolveAgentCmdbSyncData() {
		AgentCmdbSyncData syncData = new AgentCmdbSyncData();
		syncData.setPool(pool);
		syncData.setIp(agentIp);
		String lastVal = status == REMOVE ? AgentCmdbSyncData.AGENT_NOT_INSTALL : AgentCmdbSyncData.AGENT_IS_INSTALL;
		syncData.setLastvalue(lastVal);
		return syncData;
	}
	
	@Data
	@JsonInclude(Include.NON_NULL)
	public static class AgentCmdbSyncData {
		public static final String	AGENT_IS_INSTALL	= "11462";
		public static final String	AGENT_NOT_INSTALL	= "11463";
		
		@JsonProperty("pool")
		private String				pool;

		@JsonProperty("ip")
		private String				ip;

		@JsonProperty("source")
		private final String		source				= "zbx";					// 值规定为zbx

		@JsonProperty("device_type")
		private String				deviceType;
		
		@JsonProperty("key_")
		private final String		key					= "ci[is_install_agent]";
		
		@JsonProperty("lastvalue")
		private String				lastvalue;										// 是|否
	}
}
