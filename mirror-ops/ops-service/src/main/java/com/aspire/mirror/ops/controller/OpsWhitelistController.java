package com.aspire.mirror.ops.controller;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
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
import com.aspire.mirror.ops.api.service.IOpsWhitelistService;
import com.aspire.mirror.ops.biz.whitelist.OpsWhitelistBiz;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsWhitelistController
 * <p/>
 *
 * 类功能描述: 白名单管理Controller
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
@RestController
public class OpsWhitelistController implements IOpsWhitelistService {
	
	@Autowired
	private OpsWhitelistBiz whitelistBiz;

	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam) {
		return whitelistBiz.queryWhitelistHostList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistHost queryOpsWhitelistHostByKeys(@RequestBody OpsWhitelistHost queryParam) {
		return whitelistBiz.queryOpsWhitelistHostByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistHost(@RequestBody OpsWhitelistHost whitelistHost) {
		return whitelistBiz.saveWhitelistHost(whitelistHost);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse batchSaveWhitelistHost(@RequestBody List<OpsWhitelistHost> batchWhitelistHost) {
		return whitelistBiz.batchSaveWhitelistHost(batchWhitelistHost);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistLinkConstraints(@RequestBody Set<OpsWhitelistConstraint> linkConstraints) {
		return whitelistBiz.saveWhitelistLinkConstraints(linkConstraints);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<OpsWhitelistConstraint> queryWhitelistConstraintListByTypeAndId(
			@PathVariable("whitelistType") WhitelistTypeEnum whitelistType, @PathVariable("whitelistId") String whitelistId) {
		return whitelistBiz.queryWhitelistConstraintListByTypeAndId(whitelistType, whitelistId);
	}

	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removeWhitelistHostById(@PathVariable("whitelistHostId") String whitelistHostId) {
		whitelistBiz.removeWhitelistHostById(whitelistHostId);
	}
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse batchImportOpsWhitelistHost(@RequestBody List<MutablePair<String, String>> poolNameMapAgentIp) {
		return whitelistBiz.batchImportOpsWhitelistHost(poolNameMapAgentIp);
	}
	
	/**
	 * Methor:巡检白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(@RequestBody OpsWhitelistCruiseCheckQueryParam queryParam) {
		return whitelistBiz.queryWhitelistCruiseCheckList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistCruiseCheck queryOpsWhitelistCruiseCheckByKeys(@RequestBody OpsWhitelistCruiseCheck queryParam) {
		return whitelistBiz.queryOpsWhitelistCruiseCheckByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistCruiseCheck(@RequestBody OpsWhitelistCruiseCheck whitelistCruiseCheck) {
		return whitelistBiz.saveWhitelistCruiseCheck(whitelistCruiseCheck);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse removeWhitelistCruiseCheckById(@PathVariable("whitelistCruiseCheckId") String whitelistCruiseCheckId) {
		return whitelistBiz.removeWhitelistCruiseCheckById(whitelistCruiseCheckId);
	}
	
	/**
	 * Methor:漏洞白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistVulnerability> queryWhitelistVulnerabilityList(@RequestBody OpsWhitelistVulnerabilityQueryParam queryParam) {
		return whitelistBiz.queryWhitelistVulnerabilityList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistVulnerability queryOpsWhitelistVulnerabilityByKeys(@RequestBody OpsWhitelistVulnerability queryParam) {
		return whitelistBiz.queryOpsWhitelistVulnerabilityByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistVulnerability(@RequestBody OpsWhitelistVulnerability whitelistVulnerability) {
		return whitelistBiz.saveWhitelistVulnerability(whitelistVulnerability);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse  removeWhitelistVulnerabilityById(@PathVariable("whitelistVulnerabilityId") String whitelistVulnerabilityId) {
		return whitelistBiz.removeWhitelistVulnerabilityById(whitelistVulnerabilityId);
	}
	
	
	/**
	 * Methor:基线白名单Controller
	 */
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public PageListQueryResult<OpsWhitelistBaseline> queryWhitelistBaselineList(@RequestBody OpsWhitelistBaselineQueryParam queryParam) {
		return whitelistBiz.queryWhitelistBaselineList(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public OpsWhitelistBaseline queryOpsWhitelistBaselineByKeys(@RequestBody OpsWhitelistBaseline queryParam) {
		return whitelistBiz.queryOpsWhitelistBaselineByKeys(queryParam);
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public GeneralResponse saveWhitelistBaseline(@RequestBody OpsWhitelistBaseline whitelistBaseline) {
		return whitelistBiz.saveWhitelistBaseline(whitelistBaseline);
	}

	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public GeneralResponse removeWhitelistBaselineById(@PathVariable("whitelistBaselineId") String whitelistBaselineId) {
		return whitelistBiz.removeWhitelistBaselineById(whitelistBaselineId);
		
	}
}
