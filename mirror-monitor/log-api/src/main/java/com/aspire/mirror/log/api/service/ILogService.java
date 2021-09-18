package com.aspire.mirror.log.api.service;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.mirror.log.api.dto.LogChartListResponse;
import com.aspire.mirror.log.api.dto.LogListResponse;
import com.aspire.mirror.log.api.dto.LogTagListRequest;
import com.aspire.mirror.log.api.dto.LogTagListResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 日志接口
 * 项目名称: 微服务运维平台（monitor-api模块）
 * 包: com.migu.tsg.microservice.atomicservice.monitor.log.service
 * 类名称: ILogService.java
 * 类描述: 日志接口
 * 创建人: sunke
 * 创建时间: 2017年7月26日 下午8:20:39
 */
@Api(value = "日志接口")
public interface ILogService {

	/**
	 * 日志分页检索方法（提供业务分页检索日志功能）
	 * @param json 分页检索条件
	 * @return 指定页的日志数列
	 */
	@RequestMapping(value = "/ajax/logs/search", produces = { "application/json" }, method = RequestMethod.POST)
	@ApiOperation(value = "页面查询日志信息", notes = "页面查询日志信息", response = LogListResponse.class, tags = {
			"LogListResponse", })
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = LogListResponse.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
			response = LogListResponse.class) })
	String list(@RequestBody String json);

	/**
	 * 日志筛选标签查询（提供可选日志筛选数列）
	 * @param request 标签查询条件
	 * @return 可选日志筛选标签数列
	 */
	@RequestMapping(value = "/ajax/logs/types", produces = { "application/json" }, method = RequestMethod.POST)
	@ApiOperation(value = "日志筛选标签查询", notes = "日志筛选标签查询", response = LogTagListResponse.class, tags = {
			"LogTagListResponse", })
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = LogTagListResponse.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
			response = LogTagListResponse.class) })
	LogTagListResponse listTags(@RequestBody LogTagListRequest request);

	/**
	 * 基于时间的日志分布数列查询（页面日志分布构图数据）
	 * @param json 日志分布查询条件
	 * @return 基于时间的日志分布数列
	 */
	@RequestMapping(value = "/ajax/logs/aggregations", produces = { "application/json" }, method = RequestMethod.POST)
	@ApiOperation(value = "基于时间的日志分布数列查询（页面日志分布构图数据）", notes = "基于时间的日志分布数列查询（页面日志分布构图数据）",
		response = LogChartListResponse.class, tags = {"LogChartListResponse", })
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回",
	    response = LogChartListResponse.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
			response = LogChartListResponse.class) })
	LogChartListResponse listChart(@RequestBody String json);

}
