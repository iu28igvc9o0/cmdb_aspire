package com.migu.tsg.microservice.atomicservice.composite.service.logs;

import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogDownloadResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogChartListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogTagListResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Get  logs 
* Project Name:composite-api
* File Name:ILogsPermission.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service
* ClassName: ILogs <br/>
* date: 2017年9月20日 下午6:01:19 <br/>
* Get  logs 
* @author lifei
* @version
* @since JDK 1.6
*/
public interface ILogsService {
	/**
	 * 日志分页检索方法（提供业务分页检索日志功能）
	 * @param namespace 分页检索条件
	 * @return 指定页的日志数列
	 */
	@RequestMapping(value = "${version}/logs/{namespace}/search",
	        produces = { "application/json" }, method = RequestMethod.GET)
	@ApiOperation(value = "页面查询日志信息", notes = "页面查询日志信息", response = LogListResponse.class, tags = {
			"LogListResponse", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = LogListResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = LogListResponse.class) })
	String search(@PathVariable("namespace") String namespace, 
			@RequestParam(value="clusters", required=false) String clusters, 
			@RequestParam(value="start_time", required=false) String startTime,
			@RequestParam(value="end_time", required=false) String endTime,
			@RequestParam(value="instances", required=false) String instances,
			@RequestParam(value="nodes", required=false) String nodes,
			@RequestParam(value="pageno", required=false) String pageno,
			@RequestParam(value="paths", required=false) String paths,
			@RequestParam(value="query_string", required=false) String queryString, 
			@RequestParam(value="services", required=false) String services,
			@RequestParam(value="size", required=false) String size,
			@RequestParam(value="project_name", required=false) String projectName,
			@RequestParam(value = "region_id", required = false) String regionId);

	@RequestMapping(value = "${version}/logs/{namespace}/exportSysLogs", produces = {"application/json"},
			method = RequestMethod.GET)
	@ApiOperation(value = "页面下载系统日志", notes = "页面下载系统日志", response = LogDownloadResponse.class, tags = {"LogListResponse"})
	LogDownloadResponse exportLogs(String namespace, @RequestParam(value="clusters", required=false) String clusters,
								   @RequestParam(value="start_time", required=false) String startTime,
								   @RequestParam(value="end_time", required=false) String endTime,
								   @RequestParam(value="instances", required=false) String instances,
								   @RequestParam(value="nodes", required=false) String nodes,
								   @RequestParam(value="pageno", required=false) String pageno,
								   @RequestParam(value="paths", required=false) String paths,
								   @RequestParam(value="query_string", required=false) String queryString,
								   @RequestParam(value="services", required=false) String services,
								   @RequestParam(value="size", required=false) String size,
								   @RequestParam(value="project_name", required=false) String projectName,
								   @RequestParam(value = "region_id", required = false) String regionId,
								   HttpServletRequest request,
								   HttpServletResponse resp);

	@RequestMapping(value = "${version}/logs/{namespace}/exportLogsEntity", produces = {"application/json"},
			method = RequestMethod.GET)
	@ApiOperation(value = "页面下载系统日志", notes = "页面下载系统日志", response = LogDownloadResponse.class, tags = {"LogListResponse"})
	ResponseEntity<FileSystemResource> exportEntity(String namespace, HttpServletRequest req);

	@RequestMapping(value = "${version}/logs/exportLogFile", produces = {"application/json"},
			method = RequestMethod.GET)
	@ApiOperation(value = "页面下载系统日志", notes = "页面下载系统日志", tags = {"LogListResponse"})
	void exportLogFile(HttpServletRequest request, HttpServletResponse resp);

	@RequestMapping(value = "${version}/logs/exportZip", produces = {"application/json"},
			method = RequestMethod.GET)
	@ApiOperation(value = "页面下载系统日志", notes = "页面下载系统日志", tags = {"LogListResponse"})
	void exportZip(HttpServletRequest request, HttpServletResponse resp);

	@RequestMapping(value = "${version}/logs/downloadZip", produces = {"application/json"},
			method = RequestMethod.GET)
	@ApiOperation(value = "页面下载系统日志", notes = "页面下载系统日志", tags = {"LogListResponse"})
	LogDownloadResponse downloadZip(HttpServletRequest request, HttpServletResponse resp);
	/**
	 * 日志筛选标签查询（提供可选日志筛选数列）
	 * @param namespace 标签查询条件
	 * @return 可选日志筛选标签数列
	 */
	@RequestMapping(value = "${version}/logs/{namespace}/types",
	        produces = { "application/json" }, method = RequestMethod.GET)
	@ApiOperation(value = "日志筛选标签查询", notes = "日志筛选标签查询", response = LogTagListResponse.class, tags = {
			"LogTagListResponse", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = LogTagListResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = LogTagListResponse.class) })
	LogTagListResponse types(@PathVariable("namespace") String namespace,
	        @RequestParam(value="project_name", required=false) String projectName,
			@RequestParam(value = "region_id", required = false) String regionId);

	/**
	 * 基于时间的日志分布数列查询（页面日志分布构图数据）
	 * @param namespace 日志分布查询条件
	 * @return 基于时间的日志分布数列
	 */
	@RequestMapping(value = "${version}/logs/{namespace}/aggregations",
	        produces = { "application/json" }, method = RequestMethod.GET)
	@ApiOperation(value = "基于时间的日志分布数列查询（页面日志分布构图数据）", notes = "基于时间的日志分布数列查询（页面日志分布构图数据）",
		response = LogChartListResponse.class, tags = {"LogChartListResponse", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = LogChartListResponse.class),
			@ApiResponse(code = 500, message = "Unexpected error", response = LogChartListResponse.class) })
	LogChartListResponse aggregations(@PathVariable("namespace") String namespace, 
			@RequestParam(value="clusters", required=false) String clusters, 
			@RequestParam(value="start_time", required=false) String startTime,
			@RequestParam(value="end_time", required=false) String endTime,
			@RequestParam(value="instances", required=false) String instances,
			@RequestParam(value="nodes", required=false) String nodes,
			@RequestParam(value="pageno", required=false) String pageno,
			@RequestParam(value="paths", required=false) String paths,
			@RequestParam(value="query_string", required=false) String queryString, 
			@RequestParam(value="services", required=false) String services,
			@RequestParam(value="size", required=false) String size,
			@RequestParam(value="project_name", required=false) String projectName,
			@RequestParam(value = "region_id", required = false) String regionId);
}