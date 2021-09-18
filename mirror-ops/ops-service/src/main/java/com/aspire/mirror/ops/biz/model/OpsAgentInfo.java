package com.aspire.mirror.ops.biz.model;

import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude="sshPass")
public class OpsAgentInfo {
	@JsonProperty("proxy_id")
	private Long proxyId;
	
	@JsonProperty("ip_address")
	private String ipAddress;
	
	@JsonProperty("ssh_user")
	private String sshUser;
	
	@JsonProperty("ssh_pass")
	private String sshPass;
	
	@JsonProperty("ssh_port")
	private Integer sshPort;
	
	@JsonProperty("host_os_type")
	private String hostOsType;
	
	@JsonProperty("base_user")
	private String baseUser;
	
	public static OpsAgentInfo from(AgentHostInfo hostInfo) {
		if (hostInfo == null) {
			return null;
		}
		OpsAgentInfo opsAgent = new OpsAgentInfo();
		opsAgent.setBaseUser(hostInfo.getBaseUser());
		opsAgent.setHostOsType(hostInfo.getHostOsType());
		opsAgent.setIpAddress(hostInfo.getAgentIp());
		opsAgent.setProxyId(hostInfo.getProxyId());
		opsAgent.setSshUser(hostInfo.getEmbedSshUser());
		opsAgent.setSshPass(hostInfo.getEmbedSshPass());
		opsAgent.setSshPort(hostInfo.getEmbedSshPort());
		return opsAgent;
	}
}

