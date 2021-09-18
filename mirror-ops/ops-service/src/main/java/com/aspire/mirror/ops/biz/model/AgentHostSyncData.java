package com.aspire.mirror.ops.biz.model;

import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.ops.api.domain.AgentStatusEnum;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.AgentProxyInfo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AgentHostSyncData
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
@EqualsAndHashCode(of = {"pool", "proxyIdentity", "agentIp"})
@ToString(exclude = "embedSshPass")
public class AgentHostSyncData {
	private String			pool;
	private String			proxyIdentity;
	private String			agentIp;
	private String			agentEnvId;
	private String			hostOsType;
	private String			baseUser;
	private String			embedSshUser;
	private String			embedSshPass;		// 已AES加密
	private Integer			embedSshPort;
	private AgentStatusEnum	status;	
	
	public boolean selfCheck() {
		return StringUtils.isNotBlank(pool) && StringUtils.isNotBlank(proxyIdentity) 
				&& StringUtils.isNotBlank(agentIp);
	}
	
	public AgentProxyInfo retrieveProxyInfo() {
		AgentProxyInfo proxy = new AgentProxyInfo();
		proxy.setPool(pool);
		proxy.setProxyIdentity(proxyIdentity);
		return proxy;
	}
	
	public AgentHostInfo retrieveHostInfo(AgentProxyInfo proxyInfo) {
		AgentHostInfo host = new AgentHostInfo();
		host.setPool(proxyInfo.getPool());
		host.setProxyId(proxyInfo.getId());
		host.setAgentIp(agentIp);
		host.setAgentEnvId(agentEnvId);
		host.setHostOsType(hostOsType);
		host.setBaseUser(baseUser);
		host.setEmbedSshPort(embedSshPort);
		host.setEmbedSshUser(embedSshUser);
		host.setEmbedSshPass(embedSshPass);
		host.setStatus(status);
		return host;
	}
}
