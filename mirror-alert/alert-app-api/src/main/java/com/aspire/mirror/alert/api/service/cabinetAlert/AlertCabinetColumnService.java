package com.aspire.mirror.alert.api.service.cabinetAlert;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnConfigDataDTO;
import com.aspire.mirror.alert.api.dto.cabinetAlert.AlertCabinetColumnReq;
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

@RequestMapping(value = "/v1/alerts/CabinetColumn")
public interface AlertCabinetColumnService {

	/**
	 * 管理列头柜告警配置
	 */
	@PostMapping(value = "/manageConfig")
	@ApiOperation(value = "管理列头柜告警配置", notes = "管理列头柜告警配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> manageConfig(@RequestBody List<AlertCabinetColumnConfigDTO> configList);

	@PostMapping(value = "/getConfig")
	@ApiOperation(value = "查询列头柜告警配置", notes = "查询列头柜告警配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	AlertCabinetColumnConfigDTO getConfig(@RequestBody AlertCabinetColumnReq req);

	@PostMapping(value = "/updateStatus")
	@ApiOperation(value = "禁用/启用列头柜配置", notes = "禁用/启用列头柜配置", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> updateStatus(@RequestBody AlertCabinetColumnReq req);
	
	
	
	@PostMapping(value = "/queryCabinetColumnAlertPageList")
	@ApiOperation(value = "查询列头柜列表", notes = "查询列头柜列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<AlertCabinetColumnConfigDataDTO> queryCabinetColumnAlertPageList(@RequestBody AlertCabinetColumnReq req);
	
	@PostMapping(value = "/queryCabinetColumnAlert")
	@ApiOperation(value = "查询列头柜下电故障列表", notes = "查询列头柜下电故障列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<AlertCabinetColumnConfigDataDTO> queryCabinetColumnAlert(@RequestBody AlertCabinetColumnReq req);
	
	@PostMapping(value = "/queryCabinetAlertPageList")
	@ApiOperation(value = "查询机柜下电故障列表", notes = "查询机柜下电故障列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryCabinetAlertPageList(@RequestBody AlertCabinetColumnReq req);

	@PostMapping(value = "/queryCabinetAlert")
	@ApiOperation(value = "暂时不用", notes = "暂时不用", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	List<Map<String, Object>> queryCabinetAlert(@RequestBody AlertCabinetColumnReq req);
	
	@GetMapping(value = "/getScheduleConfig")
	@ApiOperation(value = "查询配置信息", notes = "查询配置信息", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	String getScheduleConfig(@RequestParam(value = "indexType", required = true) String indexType);
	
	@PostMapping(value = "/queryDeviceAlertPageList")
	@ApiOperation(value = "查询电源设备故障列表", notes = "查询电源设备故障列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryDeviceAlertPageList(@RequestBody AlertCabinetColumnReq req);

	@PostMapping(value = "/queryBizSystemPageList")
	@ApiOperation(value = "查询影响业务列表", notes = "查询影响业务列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<Map<String, Object>> queryBizSystemPageList(@RequestBody AlertCabinetColumnReq req);
	@PostMapping(value = "/queryBizSystemList")
	@ApiOperation(value = "查询影响业务列表", notes = "查询影响业务列表", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	List<Map<String, Object>> queryBizSystemList(@RequestBody AlertCabinetColumnReq req);
	@PostMapping(value = "/initalConfig")
	@ApiOperation(value = "初始化", notes = "初始化", response = ResponseEntity.class, tags = { "Cabinet_Column_API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> initalConfig();
}
