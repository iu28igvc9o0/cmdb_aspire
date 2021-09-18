package com.aspire.mirror.composite.service.dashboard;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.composite.payload.alert.ComAlertDeviceItemInfo;
import com.aspire.mirror.composite.payload.dashboard.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警服务
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RequestMapping("/${version}/alerts/monitor")
public interface IComAlertRepPanelService {

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/detail/{id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertRepPanelDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertRepPanelDTO findByPrimaryKey(@PathVariable("id") String id);
	
	@GetMapping(value = "/check/{name}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertRepPanelDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertRepPanelDTO findByName(@PathVariable("name") String name);

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ComAlertRepPanelDTO create(@RequestBody ComAlertRepPanelDTO panel) throws Exception;

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/update")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@RequestBody ComAlertRepPanelDTO panel) throws Exception;
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("id") String id) throws Exception;

	@GetMapping(value = "/selectAll")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = ComAlertRepPanelDTO.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComAlertRepPanelDTO.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ComAlertRepPanelDTO.class) })
	List<ComAlertRepPanelDTO> getAllPanel();

	
	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/getEsData",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ComAlertDashboardResponse getEsData(@RequestBody ComAlertDataSetsDto dataSetsDto) throws Exception;

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/getMoniterItems",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	PageResponse<ComAlertMoniterItemsResponse> getMoniterItems(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst);

	/**
	 * 获取监控项分类
	 */
	@PostMapping(value = "/getMoniObjects",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "获取监控项分类", notes = "获取监控项分类", response = List.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = List.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = List.class) })
	List<String> getMoniObject(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst);

	@GetMapping(value = "/getDeviceClass")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = String.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = String.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = String.class) })
	List<String> getDeviceClass();

	@GetMapping(value = "/getDeviceType")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = String.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = String.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = String.class) })
	List<String> getDeviceType(@RequestParam("deviceClass")String deviceClass);

	@GetMapping(value = "/getSubclass")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = String.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = String.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = String.class) })
	List<String> getSubclass(@RequestParam("deviceClass")String deviceClass,@RequestParam("deviceType") String deviceType);

	@GetMapping(value = "/getMonitor")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = String.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = String.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = String.class) })
	List<ComAlertDeviceItemInfo> getMonitor(@RequestParam("deviceClass")String deviceClass, @RequestParam("deviceType")String deviceType
			, @RequestParam("subclass")String subclass);

	
	@GetMapping(value = "/getSubMonitorList")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = Map.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = Map.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = Map.class) })
	Map<String, List<ComAlertDeviceItemInfo>> getSubMonitors(@RequestParam("deviceClass")String deviceClass, @RequestParam("deviceType")String deviceType);

	@PostMapping(value = "/getMoniObjectList")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = ComMonitorKeyDto.class, tags = { "dashboard" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComMonitorKeyDto.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ComMonitorKeyDto.class) })
	ComMonitorKeyDto getMoniObjectList(@RequestBody ComAlertDeviceIpRequst comAlertDeviceIpRequst);
}
