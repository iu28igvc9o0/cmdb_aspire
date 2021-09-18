package com.aspire.mirror.alert.api.service.monitorHttp;

import java.util.List;

import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpConfigResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpHisResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpIdcTypeResponse;
import com.aspire.mirror.alert.api.dto.monitorHttp.MonitorHttpReqObj;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.aspire.mirror.common.entity.PageResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警服务
 * 项目名称: mirror平台 包: com.aspire.mirror.alert.api.metrics 类名称:
 * AlertsHisService.java 类描述: 历史告警服务 创建人: JinSu 创建时间: 2018/9/19 11:19 版本: v1.0
 */
@RequestMapping("/v1/alerts/monitorHttp")
public interface MonitorHttpManageService {

	/**
	 * 查询告警过滤列表
	 */
	@PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<MonitorHttpConfigResponse> pageList(@RequestBody MonitorHttpReqObj pageRequset);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/detail/{id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = MonitorHttpConfigResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	MonitorHttpConfigResponse findByPrimaryKey(@PathVariable("id") String id);
	
	

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> create(@RequestBody MonitorHttpConfigResponse createRequest);

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/update")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@RequestBody MonitorHttpConfigResponse updateRequest);
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/delete/{id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("id") String id);

	@GetMapping(value = "/findByName/{name}")
	@ApiOperation(value = "删除", notes = "删除", response = MonitorHttpConfigResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = MonitorHttpConfigResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = MonitorHttpConfigResponse.class) })
	MonitorHttpConfigResponse findByName(@PathVariable("name") String name) ;

	@PutMapping(value = "/updateStatus")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> updateStatus(MonitorHttpConfigResponse updateRequest);

	@PostMapping(value = "/pageListHis")
	@ApiOperation(value = "删除", notes = "删除", response = MonitorHttpHisResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = MonitorHttpHisResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = MonitorHttpHisResponse.class) })
	PageResponse<MonitorHttpHisResponse> pageListHis(@RequestBody MonitorHttpReqObj pageRequset);

	@PostMapping(value = "/batchCreateHis")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> batchCreateHis(@RequestBody List<MonitorHttpHisResponse> createRequest);

	@GetMapping(value = "/findHisByPrimaryKey/{id}")
	@ApiOperation(value = "删除", notes = "删除", response = MonitorHttpHisResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = MonitorHttpHisResponse.class),
	@ApiResponse(code = 500, message = "Unexpected error", response = MonitorHttpHisResponse.class) })
	MonitorHttpHisResponse findHisByPrimaryKey(@PathVariable("id") String id);
	
	@GetMapping(value = "/getIdcTypes")
	@ApiOperation(value = "删除", notes = "删除", response = MonitorHttpIdcTypeResponse.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = MonitorHttpIdcTypeResponse.class),
	@ApiResponse(code = 500, message = "Unexpected error", response = MonitorHttpIdcTypeResponse.class) })
	List<MonitorHttpIdcTypeResponse> getIdcTypes();
	
	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/testHttp")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "MonitorHttp" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	MonitorHttpHisResponse testHttp(@RequestBody MonitorHttpConfigResponse createRequest);


}
