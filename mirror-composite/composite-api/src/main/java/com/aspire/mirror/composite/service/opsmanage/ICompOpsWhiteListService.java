package com.aspire.mirror.composite.service.opsmanage;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.ops.api.domain.GeneralResponse;
import com.aspire.mirror.ops.api.domain.PageListQueryResult;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistBaseline.OpsWhitelistBaselineQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistConstraint;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistCruiseCheck.OpsWhitelistCruiseCheckQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistHost.OpsWhitelistHostQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability;
import com.aspire.mirror.ops.api.domain.whitelist.OpsWhitelistVulnerability.OpsWhitelistVulnerabilityQueryParam;
import com.aspire.mirror.ops.api.domain.whitelist.WhitelistConst.WhitelistTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @projectName: ICompOpsWhiteListService
 * @description: 接口
 * @author: 许熙铉
 * @create: 2021-03-010 15:52
 **/
@Api("白名单管理")
@RequestMapping(value = "/v1/ops-service/whitelist")
public interface ICompOpsWhiteListService {
	
	@PostMapping(value = "/queryWhitelistHostList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "查询主机白名单列表", notes = "查询主机白名单列表", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistHost.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsWhitelistHost> queryWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam);
	
	@PostMapping(value = "/queryOpsWhitelistHostByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据关键属性查询主机白名单", notes = "根据关键属性查询主机白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistHost.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsWhitelistHost queryOpsWhitelistHostByKeys(@RequestBody OpsWhitelistHost queryParam);
	
	@PostMapping(value = "/saveWhitelistHost", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存主机白名单", notes = "保存主机白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse saveWhitelistHost(@RequestBody OpsWhitelistHost whitelistHost);
	
	@PostMapping(value = "/batchSaveWhitelistHost", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "批量保存主机白名单", notes = "批量保存主机白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse batchSaveWhitelistHost(@RequestBody List<OpsWhitelistHost> batchWhitelistHost);

	@DeleteMapping(value = "/removeWhitelistHostById/{whitelistHostId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据id移除主机白名单", notes = "根据id移除主机白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
	void removeWhitelistHostById(@PathVariable("whitelistHostId") String whitelistHostId);
	
	@PostMapping(value = "/importWhitelistHost", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导入主机白名单设备", notes = "导入主机白名单设备", tags = {"Ops whitelist service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
	public Map<String, Object> importWhitelistHost(@RequestParam("file") MultipartFile file);
	
	@PostMapping(value = "/saveWhitelistLinkConstraints", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存白名单关联约束", notes = "保存白名单关联约束", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse saveWhitelistLinkConstraints(@RequestBody Set<OpsWhitelistConstraint> linkConstraints);
	
	@PostMapping(value = "/queryWhitelistConstraintListByTypeAndId/{whitelistType}/{whitelistId}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据白名单类型和白名单id查询关联约束列表", notes = "根据白名单类型和白名单id查询关联约束列表", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	List<OpsWhitelistConstraint> queryWhitelistConstraintListByTypeAndId(
			@PathVariable("whitelistType") WhitelistTypeEnum whitelistType, @PathVariable("whitelistId") String whitelistId);
	
	@PostMapping(value = "/exportWhitelistHostList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出主机白名单列表", notes = "导出主机白名单列表", tags = {"Ops whitelist service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
    void exportWhitelistHostList(@RequestBody OpsWhitelistHostQueryParam queryParam, HttpServletResponse response);
	
	//巡检白名单管理
	//巡检接口
	@PostMapping(value = "/queryWhitelistCruiseCheckList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "查询巡检白名单列表", notes = "查询巡检白名单列表", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistCruiseCheck.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsWhitelistCruiseCheck> queryWhitelistCruiseCheckList(@RequestBody OpsWhitelistCruiseCheckQueryParam  queryParam);
	
	@PostMapping(value = "/queryOpsWhitelistCruiseCheckByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据关键属性查询巡检白名单", notes = "根据关键属性查询巡检白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistCruiseCheck.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsWhitelistCruiseCheck queryOpsWhitelistCruiseCheckByKeys(@RequestBody OpsWhitelistCruiseCheck queryParam);
	
	@PostMapping(value = "/saveWhitelistCruiseCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存巡检白名单", notes = "保存巡检白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse 	saveWhitelistCruiseCheck(@RequestBody 	OpsWhitelistCruiseCheck whitelisCruiseCheck);
	
//	@DeleteMapping(value = "/removeWhitelistCruiseCheckById/{whitelistCruiseCheckId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(value = "根据id移除巡检白名单", notes = "根据id移除巡检白名单", tags = {"Ops whitelist service API"})
//	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
//	void removeWhitelistCruiseCheckById(@PathVariable("whitelistCruiseCheckId") String whitelistCruiseCheckId);
	
	
	@DeleteMapping(value = "/removeWhitelistCruiseCheckById/{whitelistCruiseCheckId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据id移除巡检白名单", notes = "根据id移除巡检白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回",response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse removeWhitelistCruiseCheckById(@PathVariable("whitelistCruiseCheckId") String whitelistCruiseCheckId);
	
	//漏洞白名单管理
	
	@PostMapping(value = "/queryWhitelistVulnerabilityList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "查询漏洞白名单列表", notes = "查询漏洞白名单列表", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistVulnerability.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsWhitelistVulnerability> queryWhitelistVulnerabilityList(@RequestBody OpsWhitelistVulnerabilityQueryParam  queryParam);
	
	@PostMapping(value = "/queryOpsWhitelistVulnerabilityByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据关键属性查询漏洞白名单", notes = "根据关键属性查询漏洞白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistVulnerability.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsWhitelistVulnerability queryOpsWhitelistVulnerabilityByKeys(@RequestBody OpsWhitelistVulnerability queryParam);
	
	@PostMapping(value = "/saveWhitelistVulnerability", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存漏洞白名单", notes = "保存漏洞白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse 	saveWhitelistVulnerability(@RequestBody 	OpsWhitelistVulnerability whitelisVulnerability);
	
//	@DeleteMapping(value = "/removeWhitelistVulnerabilityById/{whitelistVulnerabilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ApiOperation(value = "根据id移除漏洞白名单", notes = "根据id移除漏洞白名单", tags = {"Ops whitelist service API"})
//	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
//	GeneralResponse removeWhitelistVulnerabilityById(@PathVariable("whitelistVulnerabilityId") String whitelistVulnerabilityId);
	
	
	@DeleteMapping(value = "/removeWhitelistVulnerabilityById/{whitelistVulnerabilityId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据id移除漏洞白名单", notes = "根据id移除漏洞白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回",response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse removeWhitelistVulnerabilityById(@PathVariable("whitelistVulnerabilityId") String whitelistVulnerabilityId);
//	
	
	
	
//	
	//基线白名单管理
	
	@PostMapping(value = "/queryWhitelistBaselineList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "查询基线白名单列表", notes = "查询基线白名单列表", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistBaseline.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	PageListQueryResult<OpsWhitelistBaseline> queryWhitelistBaselineList(@RequestBody OpsWhitelistBaselineQueryParam  queryParam);
	
	@PostMapping(value = "/queryOpsWhitelistBaselineByKeys", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据关键属性查询基线白名单", notes = "根据关键属性查询基线白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=OpsWhitelistBaseline.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	OpsWhitelistBaseline queryOpsWhitelistBaselineByKeys(@RequestBody OpsWhitelistBaseline queryParam);
	
	@PostMapping(value = "/saveWhitelistBaseline", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "保存基线白名单", notes = "保存基线白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse 	saveWhitelistBaseline(@RequestBody 	OpsWhitelistBaseline whitelisBaseline);
	
	@DeleteMapping(value = "/removeWhitelistBaselineById/{whitelistBaselineId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据id移除基线白名单", notes = "根据id移除基线白名单", tags = {"Ops whitelist service API"})
	@ApiResponses(value = {@ApiResponse(code = 204, message = "返回",response=GeneralResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error")})
	GeneralResponse removeWhitelistBaselineById(@PathVariable("whitelistBaselineId") String whitelistBaselineId);


	

}
