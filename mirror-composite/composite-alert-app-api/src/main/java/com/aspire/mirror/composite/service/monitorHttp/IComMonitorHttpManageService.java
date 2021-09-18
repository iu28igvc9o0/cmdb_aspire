package com.aspire.mirror.composite.service.monitorHttp;

import java.util.List;

import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpConfigResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpHisResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpIdcTypeResponse;
import com.aspire.mirror.composite.payload.monitorHttp.ComMonitorHttpReq;
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
@RequestMapping("/${version}/alerts/monitorHttp")
public interface IComMonitorHttpManageService {

	/**
	 * 查询告警过滤列表
	 */
	@PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<ComMonitorHttpConfigResponse> pageList(@RequestBody ComMonitorHttpReq pageRequset);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/detail/{id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComMonitorHttpConfigResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComMonitorHttpConfigResponse findByPrimaryKey(@PathVariable("id") String id);
	
	

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> create(@RequestBody ComMonitorHttpConfigResponse createRequest);

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/update")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@PathVariable@RequestBody ComMonitorHttpConfigResponse updateRequest,
			@RequestParam(value = "oldName", required = false) String oldName);
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("id") String id);

	/*
	 * @GetMapping(value = "/findByName/{name}")
	 * 
	 * @ApiOperation(value = "删除", notes = "删除", response =
	 * ComMonitorHttpConfigResponse.class, tags = { "MonitorHttp" })
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response =
	 * ComMonitorHttpConfigResponse.class),
	 * 
	 * @ApiResponse(code = 500, message = "Unexpected error", response =
	 * ComMonitorHttpConfigResponse.class) }) ComMonitorHttpConfigResponse
	 * findByName(@PathVariable("name") String name) ;
	 */

	@PutMapping(value = "/updateStatus")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> updateStatus(ComMonitorHttpConfigResponse updateRequest);

	@PostMapping(value = "/pageListHis")
	@ApiOperation(value = "删除", notes = "删除", response = ComMonitorHttpHisResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComMonitorHttpHisResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ComMonitorHttpHisResponse.class) })
	PageResponse<ComMonitorHttpHisResponse> pageListHis(@RequestBody ComMonitorHttpReq pageRequset);


	@GetMapping(value = "/findHisByPrimaryKey/{id}")
	@ApiOperation(value = "删除", notes = "删除", response = ComMonitorHttpHisResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComMonitorHttpHisResponse.class),
	@ApiResponse(code = 500, message = "Unexpected error", response = ComMonitorHttpHisResponse.class) })
	ComMonitorHttpHisResponse findHisByPrimaryKey(@PathVariable("id") String id);
	
	@GetMapping(value = "/getIdcTypes")
	@ApiOperation(value = "删除", notes = "删除", response = ComMonitorHttpIdcTypeResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComMonitorHttpIdcTypeResponse.class),
	@ApiResponse(code = 500, message = "Unexpected error", response = ComMonitorHttpIdcTypeResponse.class) })
	List<ComMonitorHttpIdcTypeResponse> getIdcTypes();
	
	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/testHttp")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ComMonitorHttpHisResponse testHttp(@RequestBody ComMonitorHttpConfigResponse createRequest);

}
