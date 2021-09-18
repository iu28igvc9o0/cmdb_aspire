package com.aspire.mirror.ops.biz.whitelist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.OpsSpectreHostExt;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistConstraint;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck.OpsWhitelistCruiseCheckQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost.OpsWhitelistHostQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability.OpsWhitelistVulnerabilityQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline.OpsWhitelistBaselineQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.ops.dao.AgentDataDao;
import com.aspire.mirror.ops.dao.OpsWhitelistDao;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsWhitelistBiz
 * <p/>
 *
 * 类功能描述: 白名单管理业务类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年3月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
@Service
@Transactional
public class OpsWhitelistBiz {
	@Autowired
	private OpsWhitelistDao		whitelistDao;
	@Autowired
	private	AgentDataDao		agentDataDao;
	
	/** 
	 * 功能描述: 根据查询参数分页查询主机白名单列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(OpsWhitelistHostQueryParam queryParam) {
		PageListQueryResult<OpsWhitelistHost> pageResult = new PageListQueryResult<OpsWhitelistHost>();
		List<OpsWhitelistHost> queryList = whitelistDao.queryWhitelistHostList(queryParam);
		pageResult.setDataList(queryList);
		pageResult.setTotalCount(CollectionUtils.isEmpty(queryList) ? 0 : whitelistDao.queryWhitelistHostTotalCount(queryParam));
		return pageResult;
	}
	
	/** 
	 * 功能描述: 根据关键信息查询主机白名单  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public OpsWhitelistHost queryOpsWhitelistHostByKeys(OpsWhitelistHost queryParam) {
		return whitelistDao.queryWhitelistHostById(queryParam.refreshIdByKeys());
	}
	
	/** 
	 * 功能描述: 保存主机白名单  
	 * <p>
	 * @param whitelistHost
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistHost(OpsWhitelistHost whitelistHost) {
		Pair<Boolean, String> checkResult = whitelistHost.selfCheck();
		if (!checkResult.getLeft()) {
			return new GeneralResponse(false, checkResult.getRight());
		}
		
		
		String whitelistId = whitelistHost.refreshIdByKeys();
		OpsWhitelistHost existRecord = whitelistDao.queryWhitelistHostById(whitelistId);
		if (existRecord == null) {
			whitelistHost.setCreater(RequestAuthContext.getRequestHeadUserName());
			whitelistHost.setCreateTime(new Date());
			whitelistHost.setUpdater(whitelistHost.getCreater());
			whitelistHost.setLastUpdateTime(whitelistHost.getCreateTime());
			whitelistDao.insertOpsWhitelistHost(whitelistHost);
		} else {
			whitelistHost.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistHost.setLastUpdateTime(new Date());
			whitelistDao.updateOpsWhitelistHost(whitelistHost);
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(whitelistHost.getWhitelistType(), whitelistId);
		}
		
		/*
		 * 白名单关联约束采用单独的接口保存 , 参考 saveWhitelistLinkConstraints(Set<OpsWhitelistConstraint>) 方法
		 * 
		List<OpsWhitelistConstraint> allConstraintList = new ArrayList<>();
		allConstraintList.addAll(whitelistHost.getScriptConstraintList());
		allConstraintList.addAll(whitelistHost.getPipelineConstraintList());
		if (CollectionUtils.isNotEmpty(allConstraintList)) {
			whitelistDao.batchInsertWhitelistConstraint(allConstraintList);
		}
		 */
		return new GeneralResponse(true, null, whitelistHost);
	}
	
	@Transactional
	public GeneralResponse batchSaveWhitelistHost(@RequestBody List<OpsWhitelistHost> batchWhitelistHost) {
		GeneralResponse response = new GeneralResponse();
		for (OpsWhitelistHost host : batchWhitelistHost) {
			GeneralResponse result = saveWhitelistHost(host);
			if (!result.isFlag()) {
				response = result;
			}
		}
		return response;
	}
	
	/** 
	 * 功能描述: 保存白名单关联约束 <p/>
	 * 注意：保存约束时，采用先删除后插入的方式  
	 * 
	 * <p>
	 * @param linkConstraints
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistLinkConstraints(@RequestBody Set<OpsWhitelistConstraint> linkConstraints) {
		for (OpsWhitelistConstraint constraint : linkConstraints) {
			if (!constraint.selfCheck()) {
				return new GeneralResponse(false, "保存的数据存在字段不完整.");
			}
		}
		
		List<Pair<WhitelistTypeEnum, String>> uniqueWhitelistIdList = new ArrayList<>();
		for (OpsWhitelistConstraint c : linkConstraints) {
			Pair<WhitelistTypeEnum, String> uniqueWhitelistId = Pair.of(c.getWhitelistType(), c.getWhitelistId());
			if (!uniqueWhitelistIdList.contains(uniqueWhitelistId)) {
				uniqueWhitelistIdList.add(uniqueWhitelistId);
			}
		}
		
		// 先删除旧的关联约束
		for (Pair<WhitelistTypeEnum, String> uniqueId : uniqueWhitelistIdList) {
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(uniqueId.getLeft(), uniqueId.getRight());
		}
		// 插入新的关联约束
		whitelistDao.batchInsertWhitelistConstraint(new ArrayList<>(linkConstraints));
		return new GeneralResponse();
	}
	
	@Transactional(readOnly=true)
	public List<OpsWhitelistConstraint> queryWhitelistConstraintListByTypeAndId(WhitelistTypeEnum whitelistType, String whitelistId) {
		return whitelistDao.queryWhitelistConstraintListByTypeAndId(whitelistType, whitelistId);
	}
	
	/** 
	 * 功能描述: 根据id移除主机白名单  
	 * <p>
	 * @param whitelistHostId
	 * @return
	 */
	@Transactional
	public void removeWhitelistHostById(String whitelistHostId) {
		whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(WhitelistTypeEnum.host, whitelistHostId);
		whitelistDao.deleteWhitelistHostById(whitelistHostId);
	}
	
	/**
	 * 功能描述: 批量导入主机白名单
	 * <p>
	 * 
	 * @param poolNameMapAgentIp
	 * @return
	 */
	@Transactional
	public GeneralResponse batchImportOpsWhitelistHost(List<MutablePair<String, String>> poolNameMapAgentIp) {
		// List<Map<String, Object>> sourceDataList = cmdbClient.getRefModuleDict("ed9ae050263746e1a4fff685c25734eb");
		// List<String> poolNameList = sourceDataList.stream().map(item -> (String)item.get("key")).collect(Collectors.toList());
		GeneralResponse response = new GeneralResponse();
		List<OpsWhitelistHost> errorHostList = new ArrayList<>();
		List<OpsWhitelistHost> successHostList = new ArrayList<>();
		Map<String, List<OpsWhitelistHost>> importResult = new HashMap<>();
		importResult.put("errorHostList", errorHostList);
		importResult.put("successHostList", successHostList);
		response.setBizData(importResult);
		
		for (MutablePair<String, String> entry : poolNameMapAgentIp) {
			OpsSpectreHostExt cmdbExt = agentDataDao.queryHostExtByPoolNameAndAgentIp(entry.getLeft(), entry.getRight());
			OpsWhitelistHost whitelistHost = new OpsWhitelistHost();
			whitelistHost.setPoolName(entry.getLeft());
			whitelistHost.setHostIp(entry.getRight());
			if (cmdbExt == null) {
				errorHostList.add(whitelistHost);
				continue;
			} 
			
			whitelistHost.setPoolId(cmdbExt.getPool());
			OpsWhitelistHost existWhitelistHost = whitelistDao.queryWhitelistHostById(whitelistHost.refreshIdByKeys());
			if (existWhitelistHost != null) {
				successHostList.add(existWhitelistHost);
				continue;
			}
			whitelistHost.setDepartment1(cmdbExt.getDepartment1());
			whitelistHost.setDepartment2(cmdbExt.getDepartment2());
			whitelistHost.setBizSystem(cmdbExt.getBizSystem());
			successHostList.add(whitelistHost);
		}
		return response;
	}
	
	/** 
	 * 功能描述: 巡检白名单业务方法  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	/** 
	 * 功能描述: 根据查询参数分页查询巡检白名单列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(OpsWhitelistCruiseCheckQueryParam queryParam) {
		PageListQueryResult<OpsWhitelistCruiseCheck> pageResult = new PageListQueryResult<OpsWhitelistCruiseCheck>();
		List<OpsWhitelistCruiseCheck> queryList = whitelistDao.queryWhitelistCruiseCheckList(queryParam);
		pageResult.setDataList(queryList);
		pageResult.setTotalCount(CollectionUtils.isEmpty(queryList) ? 0 : whitelistDao.queryWhitelistCruiseCheckTotalCount(queryParam));
		return pageResult;
	}
	
	/** 
	 * 功能描述: 根据关键信息查询巡检白名单  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	@Transactional(readOnly=true)
	public OpsWhitelistCruiseCheck queryOpsWhitelistCruiseCheckByKeys(OpsWhitelistCruiseCheck queryParam) {
		return whitelistDao.queryWhitelistCruiseCheckById(queryParam.refreshIdByKeys());
	}
	
	/** 
	 * 功能描述: 保存巡检白名单  
	 * <p>
	 * @param whitelistCruiseCheck
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistCruiseCheck(OpsWhitelistCruiseCheck whitelistCruiseCheck) {
		Pair<Boolean, String> checkResult = whitelistCruiseCheck.selfCheck();
		if (!checkResult.getLeft()) {
			return new GeneralResponse(false, checkResult.getRight());
		}
		
		String whitelistId = whitelistCruiseCheck.refreshIdByKeys();
		OpsWhitelistCruiseCheck existRecord = whitelistDao.queryWhitelistCruiseCheckById(whitelistId);
		if (existRecord == null) {
			whitelistCruiseCheck.setCreater(RequestAuthContext.getRequestHeadUserName());
			whitelistCruiseCheck.setCreateTime(new Date());
			whitelistCruiseCheck.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistCruiseCheck.setLastUpdateTime(whitelistCruiseCheck.getCreateTime());
			whitelistDao.insertOpsWhitelistCruiseCheck(whitelistCruiseCheck);
		} else  {
			whitelistCruiseCheck.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistCruiseCheck.setLastUpdateTime(new Date());
			whitelistDao.updateOpsWhitelistCruiseCheck(whitelistCruiseCheck);
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(whitelistCruiseCheck.getWhitelistType(), whitelistId);
		}
		
		List<OpsWhitelistConstraint> allConstraintList = new ArrayList<>();
		allConstraintList.addAll(whitelistCruiseCheck.getHostConstraintList());
		allConstraintList.addAll(whitelistCruiseCheck.getOsTypeConstraintList());
		if (CollectionUtils.isNotEmpty(allConstraintList)) {
			whitelistDao.batchInsertWhitelistConstraint(allConstraintList);
		}
		return new GeneralResponse(true, null, whitelistCruiseCheck);
	}
	
	/** 
	 * 功能描述: 根据id移除巡检白名单  
	 * <p>
	 * @param whitelistCruiseCheckId
	 * @return
	 */
	@Transactional
	public GeneralResponse removeWhitelistCruiseCheckById(String whitelistCruiseCheckId) {
		whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(WhitelistTypeEnum.cruisecheck, whitelistCruiseCheckId);
		whitelistDao.deleteWhitelistCruiseCheckById(whitelistCruiseCheckId);
		return new GeneralResponse(true, null, "删除成功");
	}
	
	

	
	/** 
	 * 功能描述: 漏洞白名单业务方法  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	/** 
	 * 功能描述: 根据查询参数分页查询漏洞白名单列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsWhitelistVulnerability> queryWhitelistVulnerabilityList(OpsWhitelistVulnerabilityQueryParam queryParam) {
		PageListQueryResult<OpsWhitelistVulnerability> pageResult = new PageListQueryResult<OpsWhitelistVulnerability>();
		List<OpsWhitelistVulnerability> queryList = whitelistDao.queryWhitelistVulnerabilityList(queryParam);
		pageResult.setDataList(queryList);
		pageResult.setTotalCount(CollectionUtils.isEmpty(queryList) ? 0 : whitelistDao.queryWhitelistVulnerabilityTotalCount(queryParam));
		return pageResult;
	}
	
	/** 
	 * 功能描述: 根据关键信息查询漏洞白名单  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	@Transactional(readOnly=true)
	public OpsWhitelistVulnerability queryOpsWhitelistVulnerabilityByKeys(OpsWhitelistVulnerability queryParam) {
		return whitelistDao.queryWhitelistVulnerabilityById(queryParam.refreshIdByKeys());
	}
	
	/** 
	 * 功能描述: 保存漏洞白名单  
	 * <p>
	 * @param whitelistVulnerability
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistVulnerability(OpsWhitelistVulnerability whitelistVulnerability) {
		Pair<Boolean, String> checkResult = whitelistVulnerability.selfCheck();
		if (!checkResult.getLeft()) {
			return new GeneralResponse(false, checkResult.getRight());
		}
		
		String whitelistId = whitelistVulnerability.refreshIdByKeys();
		OpsWhitelistVulnerability existRecord = whitelistDao.queryWhitelistVulnerabilityById(whitelistId);
		if (existRecord == null) {
			whitelistVulnerability.setCreater(RequestAuthContext.getRequestHeadUserName());
			whitelistVulnerability.setCreateTime(new Date());
			whitelistVulnerability.setUpdater(whitelistVulnerability.getUpdater());
			whitelistVulnerability.setLastUpdateTime(whitelistVulnerability.getCreateTime());
			whitelistDao.insertOpsWhitelistVulnerability(whitelistVulnerability);
		} else {
			whitelistVulnerability.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistVulnerability.setLastUpdateTime(new Date());
			whitelistDao.updateOpsWhitelistVulnerability(whitelistVulnerability);
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(whitelistVulnerability.getWhitelistType(), whitelistId);
		}
		
		List<OpsWhitelistConstraint> allConstraintList = new ArrayList<>();
		allConstraintList.addAll(whitelistVulnerability.getHostConstraintList());
		allConstraintList.addAll(whitelistVulnerability.getOsTypeConstraintList());
		if (CollectionUtils.isNotEmpty(allConstraintList)) {
			whitelistDao.batchInsertWhitelistConstraint(allConstraintList);
		}
		return new GeneralResponse(true, null, whitelistVulnerability);
	}
	
	/** 
	 * 功能描述: 根据id移除漏洞白名单  
	 * <p>
	 * @param whitelistVulnerabilityId
	 * @return
	 */
	@Transactional
	public GeneralResponse removeWhitelistVulnerabilityById(String whitelistVulnerabilityId) {
		whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(WhitelistTypeEnum.vulnerability, whitelistVulnerabilityId);
		whitelistDao.deleteWhitelistVulnerabilityById(whitelistVulnerabilityId);
		return new GeneralResponse(true, null, "删除成功");
	}
	
	
	
	
	/** 
	 * 功能描述: 基线白名单业务方法  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	/** 
	 * 功能描述: 根据查询参数分页查询基线白名单列表  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	@Transactional(readOnly=true)
	public PageListQueryResult<OpsWhitelistBaseline> queryWhitelistBaselineList(OpsWhitelistBaselineQueryParam queryParam) {
		PageListQueryResult<OpsWhitelistBaseline> pageResult = new PageListQueryResult<OpsWhitelistBaseline>();
		List<OpsWhitelistBaseline> queryList = whitelistDao.queryWhitelistBaselineList(queryParam);
		pageResult.setDataList(queryList);
		pageResult.setTotalCount(CollectionUtils.isEmpty(queryList) ? 0 : whitelistDao.queryWhitelistBaselineTotalCount(queryParam));
		return pageResult;
	}
	
	/** 
	 * 功能描述: 根据关键信息查询基线白名单  
	 * <p>
	 * @param queryParam
	 * @return
	 */
	
	@Transactional(readOnly=true)
	public OpsWhitelistBaseline queryOpsWhitelistBaselineByKeys(OpsWhitelistBaseline queryParam) {
		return whitelistDao.queryWhitelistBaselineById(queryParam.refreshIdByKeys());
	}
	
	/** 
	 * 功能描述: 保存基线白名单  
	 * <p>
	 * @param whitelistBaseline
	 * @return
	 */
	@Transactional
	public GeneralResponse saveWhitelistBaseline(OpsWhitelistBaseline whitelistBaseline) {
		Pair<Boolean, String> checkResult = whitelistBaseline.selfCheck();
		if (!checkResult.getLeft()) {
			return new GeneralResponse(false, checkResult.getRight());
		}
		
		String whitelistId = whitelistBaseline.refreshIdByKeys();
		OpsWhitelistBaseline existRecord = whitelistDao.queryWhitelistBaselineById(whitelistId);
		if (existRecord == null) {
			whitelistBaseline.setCreater(RequestAuthContext.getRequestHeadUserName());
			whitelistBaseline.setCreateTime(new Date());
			whitelistBaseline.setUpdater(whitelistBaseline.getUpdater());
			whitelistBaseline.setLastUpdateTime(whitelistBaseline.getCreateTime());
			whitelistDao.insertOpsWhitelistBaseline(whitelistBaseline);
		} else {
			whitelistBaseline.setUpdater(RequestAuthContext.getRequestHeadUserName());
			whitelistBaseline.setLastUpdateTime(new Date());
			whitelistDao.updateOpsWhitelistBaseline(whitelistBaseline);
			whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(whitelistBaseline.getWhitelistType(), whitelistId);
		}
		
		List<OpsWhitelistConstraint> allConstraintList = new ArrayList<>();
		allConstraintList.addAll(whitelistBaseline.getHostConstraintList());
		allConstraintList.addAll(whitelistBaseline.getOsTypeConstraintList());
		if (CollectionUtils.isNotEmpty(allConstraintList)) {
			whitelistDao.batchInsertWhitelistConstraint(allConstraintList);
		}
		return new GeneralResponse(true, null, whitelistBaseline);
	}
	
	/** 
	 * 功能描述: 根据id移除基线白名单  
	 * <p>
	 * @param whitelistBaselineId
	 * @return
	 */
	@Transactional
	public GeneralResponse removeWhitelistBaselineById(String whitelistBaselineId) {
		whitelistDao.deleteWhitelistConstraintsByWhitelistTypeAndId(WhitelistTypeEnum.baseline, whitelistBaselineId);
		whitelistDao.deleteWhitelistBaselineById(whitelistBaselineId);
		return new GeneralResponse(true, null, "删除成功");
	}
}
