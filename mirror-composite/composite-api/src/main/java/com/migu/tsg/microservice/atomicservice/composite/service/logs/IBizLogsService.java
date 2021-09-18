package com.migu.tsg.microservice.atomicservice.composite.service.logs;

import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Get bizlogs Project Name:composite-api File Name:ILogsPermission.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service ClassName: ILogs <br/>
 * date: 2017年12月27日 下午6:01:19 <br/>
 * Get logs
 * 
 * @author jiangfuyi
 * @version 1.0
 * @since JDK 1.8
 */
public interface IBizLogsService {
    /**
     * 日志分页检索方法（提供业务分页检索日志功能）
     * 
     * @param request
     *            分页检索条件
     * @return 指定页的日志数列
     */
    @RequestMapping(value = "${version}/bizlogs/{namespace}/search", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "页面查询业务日志信息", notes = "页面查询业务日志信息", response = BizLogListResponse.class,
            tags = {"LogListResponse",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = BizLogListResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BizLogListResponse.class)})
    String search(@PathVariable("namespace") String namespace, @RequestBody BizLogSearchRequest request);

    /**
     * 导出业务日志
     * @param namespace
     * @param request
     * @param resp
     * @return
     */
    @RequestMapping(value = "${version}/bizlogs/{namespace}/exportBizLogs", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "页面查询系统日志信息", notes = "页面查询系统日志信息", response = LogDownloadResponse.class,
            tags = {"LogListResponse",})
    LogDownloadResponse exportBizLogs(String namespace, @RequestBody BizLogSearchRequest request,
                                      HttpServletRequest req, HttpServletResponse resp);

    /**
     * 日志筛选标签查询（提供可选日志筛选数列）
     * 
     * @param namespace
     *            标签查询条件
     * @return 可选日志筛选标签数列
     */
    @RequestMapping(value = "${version}/bizlogs/{namespace}/types", produces = {"application/json"},
            method = RequestMethod.GET)
    @ApiOperation(value = "业务日志筛选标签查询", notes = "业务日志筛选标签查询", response = BizLogTagListResponse.class,
            tags = {"LogTagListResponse",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = BizLogTagListResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BizLogTagListResponse.class)})
    BizLogTagListResponse types(@PathVariable("namespace") String namespace, @RequestParam(value = "project_name",
            required = false) String projectName, @RequestParam(
            value = "region_id", required = false) String regionId);

    /**
     * 基于时间的日志分布数列查询（页面日志分布构图数据）
     * 
     * @param request
     *            日志分布查询条件
     * @return 基于时间的日志分布数列
     */
    @RequestMapping(value = "${version}/bizlogs/{namespace}/aggregations", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "基于时间的业务日志分布数列查询（页面业务日志分布构图数据）", notes = "基于时间的业务日志分布数列查询（页面业务日志分布构图数据）",
            response = LogChartListResponse.class, tags = {"LogChartListResponse",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = LogChartListResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = LogChartListResponse.class)})
    LogChartListResponse aggregations(@PathVariable("namespace") String namespace, @RequestBody BizLogSearchRequest request);
    
    /**
     * 导出
     * 
     * @param request
     *            日志分布查询条件
     * @param response 返回内容
     */
    @RequestMapping(value = "${version}/bizlogs/{namespace}/exportLogs", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "页面查询业务日志信息", notes = "页面查询业务日志信息", response = BizLogListResponse.class,
            tags = {"LogListResponse",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回", response = BizLogListResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = BizLogListResponse.class)})
    void exportLogs(@PathVariable("namespace") String namespace, @RequestBody BizLogSearchRequest request,
            HttpServletResponse response);
    /**
     * 获取服务资源.<br/>
     * 作者： jiangfuyi
     * @param serviceName
     */
    @ApiOperation(value = "获取业务日志上下文", notes = "获取业务日志上下文",
            tags = {"Composite Resource management(Services) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                           message = "get service log context.")})
    @GetMapping(path = "${version}/bizlogs/{service_name}/context", produces = { "application/json" })
    String getServiceLogContext(
            @PathVariable("service_name") String serviceName,
            @RequestParam(name = "instance_id") String instanceId,
            @RequestParam(name = "log_time") String log_time,
            @RequestParam(name = "application",required=false) String application,
            @RequestParam(name = "biz_id", required = false) String traceId,
            @RequestParam(value = "region_id", required = false) String regionId);
}