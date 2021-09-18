package com.aspire.mirror.ops.dao;

import java.util.List;

import com.aspire.mirror.ops.api.domain.AgentHostQueryModel;
import com.aspire.mirror.ops.api.domain.NormalAgentHostInfo;
import com.aspire.mirror.ops.api.domain.OpsSpectreHostExt;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.AgentProxyInfo;

@Mapper
public interface AgentDataDao {
	
	AgentProxyInfo queryAgentProxyByPoolAndIdentity(@Param("proxyIdentity") String proxyIdentity);
	
	AgentProxyInfo queryAgentProxyById(@Param("proxyId") Long proxyId);
	
	void insertProxyData(AgentProxyInfo proxyData);
	
    AgentHostInfo queryAgentDataByProxyIdAndIP(@Param("proxyId") Long proxyId, @Param("agentIp") String hostIp);
    
    AgentHostInfo queryAgentDataByPoolAndAgentIP(@Param("pool") String pool, @Param("agentIp") String agentIp);
    
    List<AgentHostInfo> queryAgentDataListByProxyIdConcatIP(List<String> concatList);
    
    void insertAgentData(AgentHostInfo agentData);
    
    void updateAgentData(AgentHostInfo agentData);
    
    void deleteAgentDataByProxyIdAndIP(@Param("proxyId") Long proxyId, @Param("agentIp") String hostIp);
    
    SimpleAgentHostInfo queryAgentInfoByProxyIdConcatIP(@Param("concactId") String concactId);
    
    SimpleAgentHostInfo queryAgentInfoByPoolAndAgentIP(@Param("pool") String pool, @Param("agentIp") String agentIp);
    
    List<SimpleAgentHostInfo> querySimpleAgentHostList(SimpleAgentHostQueryModel queryParam); 
    
    Integer querySimpleAgentHostTotalSize(SimpleAgentHostQueryModel queryParam);

    OpsSpectreHostExt queryHostExtByProxyIdAndAgentIp(@Param("proxyId") Long proxyId, @Param("agentIp") String agentIp);
    
    OpsSpectreHostExt queryHostExtByPoolIdAndAgentIp(@Param("poolId") String poolId, @Param("agentIp") String agentIp);
    
    OpsSpectreHostExt queryHostExtByPoolNameAndAgentIp(@Param("poolName") String poolName, @Param("agentIp") String agentIp);

    void insertAgentExtData(OpsSpectreHostExt hostExt);

    List<NormalAgentHostInfo> queryAgentHost(AgentHostQueryModel queryParam);

    Integer queryAgentHostTotalSize(AgentHostQueryModel queryModel);

    List<NormalAgentHostInfo> queryyNormalAgentHostByProxyIdConcatIPList(@Param("targetHosts") List<String> targetHosts);
}
