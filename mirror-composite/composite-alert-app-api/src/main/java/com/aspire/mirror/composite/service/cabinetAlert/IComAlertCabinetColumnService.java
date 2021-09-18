package com.aspire.mirror.composite.service.cabinetAlert;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnConfig;
import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnConfigData;
import com.aspire.mirror.composite.payload.cabinetAlert.ComAlertCabinetColumnReq;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping(value = "/${version}/alerts/CabinetColumn")
public interface IComAlertCabinetColumnService {

	/**
	 * 管理列头柜告警配置
	 */
	@PostMapping(value = "/manageConfig")
	@ApiOperation(value = "管理列头柜告警配置", notes = "管理列头柜告警配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> manageConfig(@RequestBody List<ComAlertCabinetColumnConfig> configList);

	@PostMapping(value = "/getConfig")
	@ApiOperation(value = "查询列头柜告警配置", notes = "查询列头柜告警配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ComAlertCabinetColumnConfig getConfig(@RequestBody ComAlertCabinetColumnReq req);

	@PostMapping(value = "/updateStatus")
	@ApiOperation(value = "禁用/启用列头柜配置", notes = "禁用/启用列头柜配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> updateStatus(@RequestBody ComAlertCabinetColumnReq req);
	
	
	
	@PostMapping(value = "/queryCabinetColumnAlertPageList")
	@ApiOperation(value = "查询列头柜列表", notes = "查询列头柜列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<ComAlertCabinetColumnConfigData> queryCabinetColumnAlertPageList(@RequestBody ComAlertCabinetColumnReq req);
	
	@PostMapping(value = "/exportCabinetColumnAlert", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列头柜下电故障列表导出", notes = "列头柜下电故障列表导出", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	void exportCabinetColumnAlert(@RequestBody ComAlertCabinetColumnReq req) throws IllegalAccessException, InvocationTargetException, IntrospectionException;
	
	@PostMapping(value = "/queryCabinetAlertPageList")
	@ApiOperation(value = "查询机柜下电故障列表", notes = "查询机柜下电故障列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryCabinetAlertPageList(@RequestBody ComAlertCabinetColumnReq req);

	@PostMapping(value = "/queryCabinetAlert")
	List<Map<String, Object>> queryCabinetAlert(@RequestBody ComAlertCabinetColumnReq req);
	
	@GetMapping(value = "/getScheduleConfig")
	@ApiOperation(value = "查询配置信息", notes = "查询配置信息", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	String getScheduleConfig(@RequestParam(value = "indexType", required = true) String indexType);
	
	@PostMapping(value = "/queryDeviceAlertPageList")
	@ApiOperation(value = "查询电源设备故障列表", notes = "查询电源设备故障列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryDeviceAlertPageList(@RequestBody ComAlertCabinetColumnReq req);

	@PostMapping(value = "/queryBizSystemPageList")
	@ApiOperation(value = "查询影响业务列表", notes = "查询影响业务列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryBizSystemPageList(@RequestBody ComAlertCabinetColumnReq req);
	
	@PostMapping(value = "/exportBizSystemList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "查询影响业务列表", notes = "查询影响业务列表", tags = { "Cabinet_Column_API" })
	void exportBizSystemList(@RequestBody ComAlertCabinetColumnReq req);
	
}
