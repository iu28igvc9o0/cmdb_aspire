package com.aspire.mirror.alert.api.service.alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.aspire.mirror.alert.api.dto.alert.AlertKpiBook;
import com.aspire.mirror.alert.api.dto.alert.AlertStandardization;
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
public interface IAlertRestfulService {
	 @PostMapping(value = "/alerts/restful/bookAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation(value = "预定告警输出", notes = "预定告警输出", response = String.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 String bookAlerts(@RequestBody AlertStandardization stand);
	 
	 @PostMapping(value = "/kpi/restful/beixiang/kpiList", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation(value = "指标列表接口", notes = "指标列表接口", response = List.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = List.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
	List<HashMap<String,String>> getMonitorList(@RequestParam(value = "device_class",required=false)String deviceClass
			,@RequestParam(value = "device_type",required=false)String deviceType);

	 @PostMapping(value = "/kpi/restful/beixiang/kpiDataSub", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation(value = "订阅指标", notes = "订阅指标", response = String.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 String bookKpiList(@RequestBody AlertKpiBook regData);

	 //告警type1性能2
	 @PostMapping(value = "/kpi/restful/getAuthField", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation(value = "获取鉴权数据", notes = "获取鉴权数据", response = Map.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	 Map<String,List<String>> getAuthField(@RequestParam(value = "type",required=false)Integer type) ;

	 //告警type1性能2
	 @PostMapping(value = "/alerts/restful/getCmdbPageList", produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation(value = "查询设备信息", notes = "查询设备信息", response = PageResponse.class, tags = {"restful API"})
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
	            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class)})
	PageResponse<Map<String, Object>> getCmdbPageList(Map<String, Object> params);

	/**
	 * 根据设备信息获取设备最新告警
	 * @param deviceIds
	 * @return
	 */
	@PostMapping(value = "/alert/restful/getDeviceNewestAlertLevelList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据设备信息获取设备最新告警", notes = "根据设备信息获取设备最新告警", response = List.class, tags = {"restful API"})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "返回", response = List.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
	List<Map<String, Object>> getDeviceNewestAlertLevelList(@RequestBody List<String> deviceIds);

	/**
	 * 根据监控项获取监控项最新告警
	 * @param itemIds
	 * @return
	 */
	@PostMapping(value = "/alert/restful/getItemNewestAlertLevelList/{prefix}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "根据设备信息获取设备最新告警", notes = "根据设备信息获取设备最新告警", response = List.class, tags = {"restful API"})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "返回", response = List.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
	List<Map<String, Object>> getItemNewestAlertLevelList(@PathVariable(name = "prefix") String prefix, @RequestBody List<String> itemIds);

	@PostMapping(value = "/alerts/restful/getIdcTypePerformanceData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "资源池的设备性能分布", notes = "资源池的设备性能分布", response = List.class, tags = {"restful API"})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "返回", response = List.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = List.class)})
	List<Map<String, Object>> getIdcTypePerformanceData(Map<String, String> params);
}
