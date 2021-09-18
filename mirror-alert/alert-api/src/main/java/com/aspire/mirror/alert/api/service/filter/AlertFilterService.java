package com.aspire.mirror.alert.api.service.filter;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.alert.api.dto.filter.AlertFilterDataRequest;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterDataResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterResponse;
import com.aspire.mirror.alert.api.dto.alert.AlertsDetailResponse;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterDTO;
import com.aspire.mirror.alert.api.dto.filter.AlertFilterSceneDTO;
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
public interface AlertFilterService {

	/**
	 * 查询告警过滤列表
	 */
	@PostMapping(value = "/v1/alerts/alerts_filter/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<AlertFilterDTO> pageList(@RequestBody AlertFilterResponse pageRequset);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/v1/alerts/alerts_filter/detail/{filter_id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertFilterDTO findByPrimaryKey(@PathVariable("filter_id") String filterId);
	
	@GetMapping(value = "/v1/alerts/alerts_filter/check/{filter_name}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	AlertFilterDTO findByName(@PathVariable("filter_name") String filterName);

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/v1/alerts/alerts_filter/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> create(@RequestBody AlertFilterDTO createRequest);

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/v1/alerts/alerts_filter/update/{filter_id}")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@PathVariable("filter_id") String filterId,
                                  @RequestBody AlertFilterDTO updateRequest);
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/v1/alerts/alerts_filter/delete/{filter_id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("filter_id") String filterId);

	@GetMapping(value = "/v1/alerts/alerts_filter/selectAll")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = AlertFilterDTO.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = AlertFilterDTO.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = AlertFilterDTO.class) })
	List<AlertFilterDTO> getAllFilter(@RequestParam(value = "filterFlag", required = false ) boolean filterFlag,
			@RequestParam(value = "operateUser", required = false ) String operateUser);

	@PostMapping(value = "/v1/alerts/alerts_filter/filterData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "告警看板筛选查询", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<AlertsDetailResponse> queryAlertData(@RequestBody AlertFilterDataRequest pageRequset);

	@GetMapping(value = "/v1/alerts/alerts_filter/filter/{filter_id}")
	@ApiOperation(value = "详情", notes = "告警看板筛选场景", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<AlertFilterDataResponse> queryAlertCount(@PathVariable("filter_id") String filterId, 
			@RequestParam(value = "userName", required = false) String userName);
	
	/**
	 * 复制告警过滤
	 */
	@PostMapping(value = "/v1/alerts/alerts_filte/copy")
	@ApiOperation(value = "复制", notes = "复制", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> copy(@RequestBody AlertFilterDTO createRequest);
	
	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/querySceneByFilterId/{filter_id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = AlertFilterSceneDTO.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<AlertFilterSceneDTO>  querySceneByFilterId(@PathVariable("filter_id") String filterId);

}
