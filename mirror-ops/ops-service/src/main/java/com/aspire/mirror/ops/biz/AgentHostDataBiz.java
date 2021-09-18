/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  AgentHostDataBiz.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月18日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.ops.api.domain.AgentHostQueryModel;
import com.aspire.mirror.ops.api.domain.AgentStatusEnum;
import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.NormalAgentHostInfo;
import com.aspire.mirror.ops.api.domain.OpsSpectreHostExt;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo;
import com.aspire.mirror.ops.api.domain.SimpleAgentHostInfo.SimpleAgentHostQueryModel;
import com.aspire.mirror.ops.biz.model.AgentHostSyncData;
import com.aspire.mirror.ops.clientservice.CmdbServiceClient;
import com.aspire.mirror.ops.clientservice.model.CmdbInstanceQueryParam;
import com.aspire.mirror.ops.clientservice.model.CmdbListResponse;
import com.aspire.mirror.ops.clientservice.model.CmdbListResponse.CmdbInstance;
import com.aspire.mirror.ops.dao.AgentDataDao;
import com.aspire.mirror.ops.domain.AgentHostInfo;
import com.aspire.mirror.ops.domain.AgentHostInfo.AgentCmdbSyncData;
import com.aspire.mirror.ops.domain.AgentProxyInfo;
import com.aspire.mirror.ops.util.JsonUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AgentHostDataBiz
 * <p/>
 *
 * 类功能描述: agent主机数据处理业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@Service
@Transactional
public class AgentHostDataBiz {
	private static final String				CMDB_NOTIFY_TOPIC	= "Ci_Zbx";
	private static final String				LOAD_FROM_CDMB		= "cmdb";
	@Value("${agent.loadfrom:local}")
	private String							agentLoadType;					// cmdb: cmdb服务加载        local: 本地服务加载
	@Value("${agent.localFilterHosts:}")
	private String filterHosts;
	@Autowired
	private AgentDataDao					agentDao;
	@Autowired
	private CmdbServiceClient				cmdbClient;
	@Autowired
	@Qualifier("kafka2Template")
	private KafkaTemplate<String, String>	kafkaTemplate;
	
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public void processAgentHostDataSync(AgentHostSyncData syncData) {
		AgentProxyInfo proxyInfo = handleAgentProxyInfo(syncData);
		AgentHostInfo updateData = syncData.retrieveHostInfo(proxyInfo);
		updateData.setLastSyncTime(new Date());
		
		// agent被移除
		if (syncData.getStatus() == AgentStatusEnum.REMOVE) {
			agentDao.deleteAgentDataByProxyIdAndIP(updateData.getProxyId(), updateData.getAgentIp());
			syncAgentInfo2Cmdb(updateData);
			return;
		}
		
		AgentHostInfo existHost = agentDao.queryAgentDataByProxyIdAndIP(updateData.getProxyId(), updateData.getAgentIp());
		if (existHost == null) {
			agentDao.insertAgentData(updateData);
		} else {
			agentDao.updateAgentData(updateData);
		}
		if (agentLoadType != null && LOAD_FROM_CDMB.equals(agentLoadType.trim())) {
			OpsSpectreHostExt opsSpectreHostExt = agentDao.queryHostExtByProxyIdAndAgentIp(updateData.getProxyId(), updateData.getAgentIp());
			if (opsSpectreHostExt == null) {
				CmdbInstance cmdbInstance = null;
				try {
					cmdbInstance = this.getCmdbInstance(proxyInfo.getPool(), updateData.getAgentIp());
				} catch (Exception e) {
					log.error("Error when fetch cmdbInstance info with params poolName:{}, agentIp: {}", 
							proxyInfo.getPool(), updateData.getAgentIp(), e);
				}
				if (cmdbInstance != null) {
					OpsSpectreHostExt hostExt = CmdbInstance.generateHostExtFromCMDBIntance(cmdbInstance);
					hostExt.setProxyId(updateData.getProxyId());
					agentDao.insertAgentExtData(hostExt);
				}
			}
		}
		if (existHost == null || existHost.getStatus() != updateData.getStatus()) {
			syncAgentInfo2Cmdb(updateData);
		}
	}
	
	/** 
	 * 功能描述: 同步agent信息到CMDB
	 * <p>
	 * @param updateData
	 */
	private void syncAgentInfo2Cmdb(AgentHostInfo updateData) {
		try {
			AgentCmdbSyncData cmdbSyncData = updateData.resolveAgentCmdbSyncData();
			kafkaTemplate.send(CMDB_NOTIFY_TOPIC, JsonUtil.toJacksonJson(cmdbSyncData));
		} catch (Throwable e) {
			log.error(null, e);
		}
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, propagation=Propagation.REQUIRES_NEW)
	public AgentProxyInfo handleAgentProxyInfo(AgentHostSyncData syncData) {
		AgentProxyInfo proxyInfo = agentDao.queryAgentProxyByPoolAndIdentity(syncData.getProxyIdentity());
		if (proxyInfo == null) {
			proxyInfo = syncData.retrieveProxyInfo();
			proxyInfo.setUpdateTime(new Date());
			agentDao.insertProxyData(proxyInfo);
		}
		return proxyInfo;
	}
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public GeneralResponse getAgentHostInfoLoadSource() {
		return new GeneralResponse(true, null, agentLoadType);
	}
	
	@Transactional(readOnly=true)
	public PageListQueryResult<SimpleAgentHostInfo> fetchUserAuthedAgentHostList(SimpleAgentHostQueryModel queryParam) {
//		Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
		// 从cmdb服务加载 
		if (agentLoadType != null && LOAD_FROM_CDMB.equals(agentLoadType.trim())) {
			// 从cmdb查询数据
			return fetchAgentHostInfoFromCmdb(queryParam);
		}
		if (StringUtils.isNotBlank(filterHosts)) {
			queryParam.setAgentIpList(Arrays.asList(filterHosts.split(",")));
		}
		List<SimpleAgentHostInfo> resultList = agentDao.querySimpleAgentHostList(queryParam);
		if (CollectionUtils.isEmpty(resultList)) {
			return new PageListQueryResult<SimpleAgentHostInfo>(0, resultList);
		}
		Integer totalCount = agentDao.querySimpleAgentHostTotalSize(queryParam);
		return new PageListQueryResult<SimpleAgentHostInfo>(totalCount, resultList);
	}
	
	/** 
	 * 功能描述: 从CMDB加载  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	private PageListQueryResult<SimpleAgentHostInfo> fetchAgentHostInfoFromCmdb(SimpleAgentHostQueryModel queryParam) {
//		// just for test
//		queryParam.addExtendParam("idcType", "呼和浩特资源池");
//		queryParam.addExtendParam("ip", "10.12.70");
		
		CmdbListResponse response = cmdbClient.queryCmdbInstanceList(CmdbInstanceQueryParam.from(queryParam));
		if (response == null || CollectionUtils.isEmpty(response.getDataList())) {
			return new PageListQueryResult<SimpleAgentHostInfo>(0, new ArrayList<SimpleAgentHostInfo>());
		}
		
		List<SimpleAgentHostInfo> resultList = new ArrayList<>();
		response.getDataList().stream().forEach(cmdb -> {
			SimpleAgentHostInfo simpleAgent = agentDao.queryAgentInfoByPoolAndAgentIP((String) cmdb.getExtendAttrs().get("idcType_idc_name_name"), cmdb.getIp());
			if (simpleAgent == null) {
				log.warn("There is no agent host with pool: {} and agentIp: {}", cmdb.getIdcType(), cmdb.getIp());
				return;
			}
			if (MapUtils.isNotEmpty(cmdb.getExtendAttrs())) {
				simpleAgent.getExtendAttrMap().putAll(cmdb.toMap());
			}
			resultList.add(simpleAgent);
		});
		return new PageListQueryResult<SimpleAgentHostInfo>(response.getTotalSize(), resultList);
	}
	
	@Transactional(readOnly=true)
	public SimpleAgentHostInfo queryAgentInfoByProxyIdConcatIP(String proxyIdConcatIP) {
		SimpleAgentHostInfo simpleAgent = agentDao.queryAgentInfoByProxyIdConcatIP(proxyIdConcatIP);
		if (simpleAgent == null) {
			return null;
		}
		// 从cmdb服务加载, 额外填充cmdb信息
		if (agentLoadType != null && LOAD_FROM_CDMB.equals(agentLoadType.trim())) {
			CmdbInstance response = getCmdbInstance(simpleAgent.getPool(), simpleAgent.getAgentIp());
			if (response == null) {
				return simpleAgent;
			}
			CmdbInstance cmdb = response;
			if (MapUtils.isNotEmpty(cmdb.getExtendAttrs())) {
				simpleAgent.getExtendAttrMap().putAll(cmdb.getExtendAttrs());
			}
		}
		return simpleAgent;
	}

	private CmdbInstance getCmdbInstance(String pool, String agentIp) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("idcType", pool);
		param.put("ip", agentIp);
		param.put("is_cn", true);
		return cmdbClient.queryDeviceByRoomIdAndIP(param);
	}

	public AgentHostInfo queryAgentDataByPoolAndAgentIP(String pool, String agentIp) {
		return agentDao.queryAgentDataByPoolAndAgentIP(pool, agentIp);
	}

	public PageListQueryResult<NormalAgentHostInfo> queryNormalAgentHostList(AgentHostQueryModel queryParam) {
		List<NormalAgentHostInfo> resultList = agentDao.queryAgentHost(queryParam);
		if (CollectionUtils.isEmpty(resultList)) {
			return new PageListQueryResult<>(0, resultList);
		}
		Integer totalCount = agentDao.queryAgentHostTotalSize(queryParam);
		return new PageListQueryResult<>(totalCount, resultList);
	}
	public OpsSpectreHostExt queryHostExtByProxyIdAndAgentIp(String proxyId, String agentIp) {
		return agentDao.queryHostExtByProxyIdAndAgentIp(Long.parseLong(proxyId), agentIp);
	}
}
