package com.aspire.cdn.esdatawrap.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.cdn.esdatawrap.biz.report.OpsReportBiz;
import com.aspire.cdn.esdatawrap.biz.report.model.AlertStatisticReportItem;
import com.aspire.cdn.esdatawrap.biz.report.model.CustomOpsReportParam;
import com.aspire.cdn.esdatawrap.biz.report.model.CustomOpsReportResponse;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllNosyncStatistics;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllSyncStatistics;
import com.aspire.cdn.esdatawrap.biz.report.model.DropDownEntry;
import com.aspire.cdn.esdatawrap.biz.report.model.ReqDomainQueryParams;
import com.aspire.cdn.esdatawrap.biz.report.model.ReqDomainReportByHourParams;
import com.aspire.cdn.esdatawrap.controller.authcontext.RequestAuthContext;
import com.aspire.cdn.esdatawrap.controller.authcontext.RequestAuthContext.RequestHeadUser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "运营报表")
@RequestMapping(value = "/v1/cdnIntegrate/opsBizReport/")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}') && ${opsReport.switch:false}")
@RestController
public class OpsBizReportController {
	@Autowired
	private OpsReportBiz opsReportBiz;
	
	@PostMapping(value = "/fetchBandwidthReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省、全国带宽", notes = "各省、全国带宽", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchBandwidthReport() {
		return opsReportBiz.fetchBandwidthReport();
	}
	
	@PostMapping(value = "/fetchServiceSuccPercentReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省、全国服务成功率", notes = "各省、全国服务成功率", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchServiceSuccPercentReport() {
		return opsReportBiz.fetchServiceSuccPercentReport();
	}
	
	@PostMapping(value = "/fetchFileHitSucPercentReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省、全国请求命中率", notes = "各省、全国请求命中率", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchFileHitSucPercentReport() {
		return opsReportBiz.fetchFileHitSucPercentReport();
	}
	
	@PostMapping(value = "/fetchRequestCountReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省、全国请求数", notes = "各省、全国请求数", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchRequestCountReport() {
		return opsReportBiz.fetchRequestCountReport();
	}
	
	@PostMapping(value = "/fetchReturnStatusSucPercentReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省回包成功率", notes = "各省回包成功率", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchReturnStatusSucPercentReport() {
		return opsReportBiz.fetchReturnStatusSucPercentReport();
	}
	
	@PostMapping(value = "/fetchInitBitDelayTimeReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省首包延迟 ", notes = "各省首包延迟 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchInitBitDelayTimeReport() {
		return opsReportBiz.fetchInitBitDelayTimeReport();
	}
	
	@PostMapping(value = "/fetchDownloadRateByProvinceReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "各省/全国下载速率 ", notes = "各省/全国下载速率 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchDownloadRateByProvinceReport() {
		return opsReportBiz.fetchDownloadRateByProvinceReport();
	}
	
	@PostMapping(value = "/wholeCountryHttpStatusWeightReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "全国状态码占比 ", notes = "全国状态码占比 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics wholeCountryHttpStatusWeightReport() {
		return opsReportBiz.fetchWholeCountryHttpStatusWeight();
	}
	
	@PostMapping(value = "/fetch5xxPercentRankByProvince", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "全国各省5XX排名和占比 ", notes = "全国各省5XX排名和占比 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetch5xxPercentRankByProvince() {
		return opsReportBiz.fetch5xxPercentRankByProvince();
	}
	
	@PostMapping(value = "/concentrateStatistic01Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表1 ", notes = "集中监控报表1", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic01Report() {
		return opsReportBiz.concentrateStatistic01Report();
	}
	
	@PostMapping(value = "/concentrateStatistic02Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表2 ", notes = "集中监控报表2", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic02Report() {
		return opsReportBiz.concentrateStatistic02Report();
	}
	
	@PostMapping(value = "/concentrateStatistic03Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表3 ", notes = "集中监控报表3", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic03Report() {
		return opsReportBiz.concentrateStatistic03Report();
	}
	
	@PostMapping(value = "/concentrateStatistic04Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表4 ", notes = "集中监控报表4", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic04Report() {
		return opsReportBiz.concentrateStatistic04Report();
	}
	
	@PostMapping(value = "/concentrateStatistic05Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表5", notes = "集中监控报表5", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic05Report() {
		return opsReportBiz.concentrateStatistic05Report();
	}
	
	@PostMapping(value = "/concentrateStatistic06Report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "集中监控报表6", notes = "集中监控报表6", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics concentrateStatistic06Report() {
		return opsReportBiz.concentrateStatistic06Report();
	}
	
	/////////////////// 地市维度报表  ///////////////////
	@PostMapping(value = "/fetchUserAuthedProvinceList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取用户拥有权限的省份名称列表 ", notes = "获取用户拥有权限的省份名称列表 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = List.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<String> fetchUserAuthedProvinceList() {
		RequestHeadUser headUser = RequestAuthContext.getRequestHeadUser();
		if (headUser == null) {
			throw new RuntimeException("There is no header user info.");
		}
		if (headUser.isAdmin() || headUser.isSuperUser()) {
			return opsReportBiz.getWholeProvinceNamelist();
		}
		Map<String, List<String>> resFilterMap = headUser.getResFilterConfig();
		if (resFilterMap != null) {
			List<String> authProvList = resFilterMap.get("idcType_name");
			return  CollectionUtils.isNotEmpty(authProvList) ? authProvList : new ArrayList<String>();
		}
		return new ArrayList<String>();
	}
	
	/** 
	 * 功能描述: 验证当前用户是否有该省份的权限  
	 * <p>
	 * @param targetProvince
	 */
	private void validHeadUserProvinceAuth(String targetProvince) {
		List<String> userAuthProvList = fetchUserAuthedProvinceList();
		boolean isAuth = CollectionUtils.isEmpty(userAuthProvList) ? false : userAuthProvList.contains(targetProvince);
		if (!isAuth) {
			throw new RuntimeException("User '" + RequestAuthContext.getRequestHeadUser().getUsername() 
					+ "' has no privilige for province: " + targetProvince);
		}
	}
	
	@PostMapping(value = "/fetchCitybaseServiceSuccPercent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "地市维度服务成功率 ", notes = "地市维度服务成功率 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchCitybaseServiceSuccPercent(@RequestParam("provinceName") String provinceName) {
		validHeadUserProvinceAuth(provinceName);
		return opsReportBiz.fetchCitybaseServiceSuccPercent(provinceName);
	}
	
	@PostMapping(value = "/fetchCitybaseFileHitSucPercentReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "地市维度请求命中率 ", notes = "地市维度请求命中率 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics fetchCitybaseFileHitSucPercentReport(@RequestParam("provinceName") String provinceName) {
		validHeadUserProvinceAuth(provinceName);
		return opsReportBiz.fetchCitybaseFileHitSucPercentReport(provinceName);
	}
	
	@PostMapping(value = "/fetchCitybaseBandwidthReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "地市维度带宽 ", notes = "地市维度带宽 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchCitybaseBandwidthReport(@RequestParam("provinceName") String provinceName) {
		validHeadUserProvinceAuth(provinceName);
		return opsReportBiz.fetchCitybaseBandwidthReport(provinceName);
	}
	
	@PostMapping(value = "/fetchCitybaseDownloadRateReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "地市/全省下载速率 ", notes = "地市/全省下载速率 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchCitybaseDownloadRateReport(@RequestParam("provinceName") String provinceName) {
		validHeadUserProvinceAuth(provinceName);
		return opsReportBiz.fetchCitybaseDownloadRateReport(provinceName);
	}
	
	// "服务统计" 改成   "请求数统计"
	@PostMapping(value = "/fetchCitybaseReqCountReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "地市/全省请求数量统计 ", notes = "地市/全省请求数量统计 ", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllSyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllSyncStatistics fetchCitybaseReqCountReport(@RequestParam("provinceName") String provinceName) {
		validHeadUserProvinceAuth(provinceName);
		return opsReportBiz.fetchCitybaseReqCountReport(provinceName);
	}
	
	@PostMapping(value = "/queryTop10AlertStatisticByReqDomain", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据请求域名统计的top10数量告警   ", notes = "根据请求域名统计的top10数量告警", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertStatisticReportItem.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<AlertStatisticReportItem> queryTop10AlertStatisticByReqDomain() {
		return opsReportBiz.queryTop10AlertStatisticByReqDomain();
	}
	
	@PostMapping(value = "/queryTop10AlertStatisticReport", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "top10数量告警   ", notes = "top10数量告警", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertStatisticReportItem.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<AlertStatisticReportItem> queryTop10AlertStatisticReport() {
		return opsReportBiz.queryTop10AlertStatisticReport();
	}
	
	@PostMapping(value = "/queryTop10AlertStatisticByManufacture", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据平面统计的top10数量告警   ", notes = "根据平面统计的top10数量告警", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = AlertStatisticReportItem.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<AlertStatisticReportItem> queryTop10AlertStatisticByManufacture() {
		return opsReportBiz.queryTop10AlertStatisticByManufacture();
	}
	
	
	@PostMapping(value = "/loadCustomReportKeyValList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "加载自定义报表选项列表", notes = "加载自定义报表选项列表", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DropDownEntry.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<DropDownEntry> loadCustomReportKeyValList() {
		return opsReportBiz.loadCustomReportKeyValList();
	}
	
	@PostMapping(value = "/loadProvinceKeyValList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "加载省份下拉列表", notes = "加载省份下拉列表", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DropDownEntry.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<DropDownEntry> loadProvinceKeyValList() {
		return opsReportBiz.loadProvinceKeyValList();
	}
	
	@PostMapping(value = "/loadProvinceCityKeyValList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据省份加载地市下拉列表", notes = "根据省份加载地市下拉列表", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DropDownEntry.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<DropDownEntry> loadProvinceCityKeyValList(@RequestParam("provinceCode") String provinceCode) {
		return opsReportBiz.loadProvinceCityKeyValList(provinceCode);
	}
	
	@PostMapping(value = "/queryCustomReportDataList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "自定义报表查询", notes = "自定义报表查询", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = CustomOpsReportResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public CustomOpsReportResponse queryCustomReportDataList(@RequestBody CustomOpsReportParam queryParam) {
		return opsReportBiz.queryCustomReportDataList(queryParam);
	}
	
	@PostMapping(value = "/loadReqDomainReportKeyValList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "域名指标下拉列表", notes = "域名指标下拉列表", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<DropDownEntry> loadReqDomainReportKeyValList() {
		return opsReportBiz.loadReqDomainReportKeyValList();
	}
	
	@PostMapping(value = "/queryReqDomainListByKey", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "根据关键字查询域名", notes = "根据关键字查询域名", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = String.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public List<String> queryReqDomainListByKey(@RequestBody ReqDomainQueryParams queryParam) {
		return opsReportBiz.queryReqDomainListByKey(queryParam);
	}
	
	@PostMapping(value = "/queryReqDomainListCompareReportByKey", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "针对指定域名查询指标报表数据", notes = "针对指定域名查询指标报表数据", tags = {"Report service API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = DistrictWholeInAllNosyncStatistics.class),
            @ApiResponse(code = 500, message = "Unexpected error")})
	public DistrictWholeInAllNosyncStatistics queryReqDomainListCompareReportByKey(@RequestBody ReqDomainReportByHourParams queryParam) {
		return opsReportBiz.queryReqDomainListCompareReportByKey(queryParam);
	}
}