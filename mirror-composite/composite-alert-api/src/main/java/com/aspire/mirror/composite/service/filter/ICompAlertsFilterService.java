package com.aspire.mirror.composite.service.filter;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.composite.payload.alert.CompAlertsDetailResp;
import com.aspire.mirror.composite.payload.filter.*;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 历史告警暴露服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert
 * 类名称:    ICompAlertsHisService.java
 * 类描述:    历史告警暴露服务
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 20:03
 * 版本:      v1.0
 */
@Api(value = "历史告警信息管理")
@RequestMapping("/${version}/alerts/alerts_filter")
public interface ICompAlertsFilterService {
	/**
	 * 查询告警过滤列表
	 */
	@PostMapping(value = "/pageList", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "获取历史告警列表", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<ComAlertFilter> pageList(@RequestBody ComAlertFilterRequest pageRequset);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/detail/{filter_id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertFilter.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertFilter findByPrimaryKey(@PathVariable("filter_id") String filterId);
	
	@GetMapping(value = "/check/{filter_name}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertFilter.class),
			@ApiResponse(code = 500, message = "内部错误") })
	ComAlertFilter findByName(@PathVariable("filter_name") String filterName);

	/**
	 * 新增告警过滤
	 */
	@PostMapping(value = "/create")
	@ApiOperation(value = "批量创建历史告警", notes = "批量创建历史告警", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> create(@RequestBody ComAlertFilter createRequest);

	/**
	 * 修改告警过滤
	 * 
	 */

	@PutMapping(value = "/update/{filter_id}")
	@ApiOperation(value = "修改", notes = "修改", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> update(@PathVariable("filter_id") String filterId,
			@RequestBody ComAlertFilter updateRequest);
	
	/**
	 * 删除告警过滤
	 * 
	 */

	@DeleteMapping(value = "/delete/{filter_id}")
	@ApiOperation(value = "删除", notes = "删除", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> delete(@PathVariable("filter_id") String filterId);
	
	@GetMapping(value = "/selectAll")
	@ApiOperation(value = "查询所有", notes = "查询所有", response = ComAlertFilter.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ComAlertFilter.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ComAlertFilter.class) })
	List<ComAlertFilter> getAllFilter(@RequestParam(value="filterFlag" ,required =false ) boolean filterFlag);
	

	@PostMapping(value = "/filterData", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "列表", notes = "告警看板筛选查询", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
			@ApiResponse(code = 500, message = "内部错误") })
	PageResponse<CompAlertsDetailResp> queryAlertData(@RequestBody ComAlertFilterDataRequest pageRequset);

	@GetMapping(value = "/filter/{filter_id}")
	@ApiOperation(value = "详情", notes = "告警看板筛选场景", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = CompAlertsDetailResp.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<ComAlertFilterDataResponse> queryAlertCount(@PathVariable("filter_id")  String filterId);
	
	/**
	 * 复制告警过滤
	 */
	@PostMapping(value = "/copy")
	@ApiOperation(value = "复制", notes = "复制", response = ResponseEntity.class, tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = ResponseEntity.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
	ResponseEntity<String> copy(@RequestBody ComAlertFilter createRequest);
	
	/**
	 * 查询告警过滤详情
	 *
	 */
	@GetMapping(value = "/querySceneByFilterId/{filter_id}")
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = ComAlertFilterScene.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<ComAlertFilterScene>  querySceneByFilterId(@PathVariable("filter_id") String filterId);

	/**
	 * 查询告警过滤详情
	 *
	 */
	@PostMapping(value = "/exportFilterAlerts", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "详情", notes = "根据alert_id获取历史告警详情", tags = { "AlertFilter API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = void.class),
			@ApiResponse(code = 500, message = "内部错误") })
	void exportFilterAlerts(@RequestBody ComAlertFilterDataRequest  pageRequset, HttpServletResponse response) throws Exception;

}
