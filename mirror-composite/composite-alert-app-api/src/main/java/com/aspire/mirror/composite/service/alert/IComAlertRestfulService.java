package com.aspire.mirror.composite.service.alert;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.composite.payload.Result;
import com.aspire.mirror.composite.payload.alert.ComAlertDeviceItemInfo;
import com.aspire.mirror.composite.payload.alert.ComAlertEsDataRequest;
import com.aspire.mirror.composite.payload.alert.ComAlertKpiBook;
import com.aspire.mirror.composite.payload.alert.ComAlertStandardization;
import com.aspire.mirror.composite.payload.dashboard.ComAlertDashboardResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author longfeng
 * @title: IAlertStandardizationService
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1510:56
 */
@RequestMapping("/v1")
@Api(value = "对外接口")
public interface IComAlertRestfulService {

	 @PostMapping(value = "/kpi/restful/beixiang/kpiList")
	    @ApiOperation(value = "指标列表接口", notes = "指标列表接口", response = ComAlertDeviceItemInfo.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	List<HashMap<String,String>> getMonitorList(@RequestParam(value = "device_class",required=false)String deviceClass
			,@RequestParam(value = "device_type",required=false)String deviceType);

	 
	 @PostMapping(value = "/alerts/restful/bookAlerts")
	    @ApiOperation(value = "预定告警输出", notes = "预定告警输出", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 String bookAlerts(@RequestBody ComAlertStandardization stand);


	 @PostMapping(value = "/kpi/restful/beixiang/kpiDataSub")
	    @ApiOperation(value = "订阅指标", notes = "订阅指标", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 String bookKpiList(@RequestBody ComAlertKpiBook regData);

	 @PostMapping(value = "/kpi/restful/getIdcTypeStoreUseRate")
	    @ApiOperation(value = "运营月报-存储利用率", notes = "运营月报-资源池均峰值利用率", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 Map<String, Object> getIdcTypeStoreUseRate(@RequestBody ComAlertEsDataRequest request) throws ParseException;



	 @GetMapping(value = "/alerts/restful/queryServerData")
	    @ApiOperation(value = "服务器大屏监控数据", notes = "服务器大屏监控数据", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 ComAlertDashboardResponse queryServerData(@RequestParam(value = "instanceId",required=true)String instanceId
			, @RequestParam(value = "itermType",required=true)String itermType
			, @RequestParam(value = "deviceClass",required=true)String deviceClass,
											   @RequestParam(value = "countTypeFlag", required = false) Boolean countTypeFlag) throws Exception;


	 @GetMapping(value = "/alerts/restful/queryServerInfo")
	    @ApiOperation(value = "服务器大屏设备信息", notes = "服务器大屏设备信息", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	Map<String, Object> queryServerInfo(@RequestParam(value = "instanceId",required=true)String instanceId
			,@RequestParam(value = "itermType",required=false)String itermType,@RequestParam(value = "deviceClass",required=false)String deviceClass) throws Exception;


	 @PostMapping(value = "/alerts/restful/getCmdbPageList")
	    @ApiOperation(value = "查询设备信息", notes = "查询设备信息", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	PageResponse<Map<String, Object>> getCmdbPageList(Map<String, Object> params);


	 @GetMapping(value = "/alerts/restful/queryNetInfo")
	    @ApiOperation(value = "查询网络设备", notes = "查询网络设备", response = ResponseEntity.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	Map<String, Object> queryNetInfo(@RequestParam(value = "instanceId", required = true) String instanceId,
			@RequestParam(value = "itermType", required = false) String itermType,
			@RequestParam(value = "deviceClass", required = false) String deviceClass) throws Exception;

	/**
	 * 获取设备指标列表,包含告警情况
	 * @param params
	 * @param moduleType
	 * @return
	 */
	@RequestMapping(value = "/kpi/restful/instanceKpi/list", method = RequestMethod.POST)
	@ApiOperation(value = "获取设备指标列表,包含告警情况", notes = "获取设备指标列表,包含告警情况", tags = {"restful API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
			@ApiResponse(code = 500, message = "内部错误")})
	Result<Map<String, Object>> getInstanceKpiList(@RequestBody Map<String, Object> params,
												  @RequestParam(value = "moduleType", required = false) String moduleType);

	/**
	 * 获取告警设备指标列表
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/kpi/restful/alertKpi/list", method = RequestMethod.POST)
	@ApiOperation(value = "获取告警设备指标列表", notes = "获取告警设备指标列表", tags = {"restful API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
			@ApiResponse(code = 500, message = "内部错误")})
	Result<Map<String, Object>> getAlertKpiList(@RequestBody Map<String, Object> params);

	@RequestMapping(value = "/alerts/restful/getInstanceMonitorValueList", method = RequestMethod.POST)
	@ApiOperation(value = "资源设备利用率", notes = "资源设备利用率", tags = {"restful API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误")})
	Result<Map<String, Object>> getInstanceMonitorValueList(@RequestBody Map<String, Object> params,
															@RequestParam(value = "moduleType", required = false) String moduleType);

	@PostMapping(value = "/alerts/restful/exportInstanceMonitorValueList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "导出资源设备利用率", notes = "导出资源设备利用率", tags = {"restful API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误")})
	void  exportInstanceMonitorValueList(@RequestBody Map<String, Object> params
			,@RequestParam String moduleType,
			HttpServletResponse response) throws Exception;

	@PostMapping(value = "/alerts/restful/getIdcTypePerformanceData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "资源池的设备性能分布", notes = "资源池的设备性能分布", tags = {"restful API"})
	@ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误")})
	List<Map<String, Object>> getIdcTypePerformanceData(Map<String, String> params);
}
