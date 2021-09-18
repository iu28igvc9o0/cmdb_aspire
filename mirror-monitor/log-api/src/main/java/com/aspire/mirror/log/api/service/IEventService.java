package com.aspire.mirror.log.api.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.mirror.log.api.dto.EventInfoResponse;
import com.aspire.mirror.log.api.dto.EventLogListResponse;

/**
* 类说明: 事件提供http接口定义
* 项目名称:  微服务
* 包:        com.migu.tsg.microservice.monitor.log.service
* 类名称:    IEventService.java
* 类描述:    本文件为事件接口的定义类
* 创建人:    jiangfuyi
* 创建时间:  2017年7月27日
 */
@Api(value = "日志", description = "日志 API")
public interface IEventService {
    /**
    *保存构建日志信息
    * @param  requestJson   [事件信息]
    * @return EventInfoResponse
     */
    @RequestMapping(value = "/ajax/logs/build", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "保存构建日志信息", notes = "保存构建日志信息", response = EventInfoResponse.class,
            tags = {"EventInfoResponse",})
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = EventInfoResponse.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
                    response = EventInfoResponse.class)})
    EventInfoResponse saveBuildsLogInfo(@RequestBody String requestJson);

    /**
    *保存服务日志信息
    * @param  requestJson   [事件信息]
    * @return EventInfoResponse
     */
    @RequestMapping(value = "/ajax/logs/services", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "保存服务日志信息", notes = "保存服务日志信息", response = EventInfoResponse.class,
            tags = {"EventInfoResponse",})
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = EventInfoResponse.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
                    response = EventInfoResponse.class)})
    EventInfoResponse saveServicesLogInfo(@RequestBody String requestJson);

    /**
    *保存事件日志信息
    * @param  requestJson   [事件信息]
    * @return EventInfoResponse
     */
    @RequestMapping(value = "/ajax/logs/events", produces = {"application/json"}, method = RequestMethod.POST)
    @ApiOperation(value = "保存事件日志信息", notes = "保存事件日志信息", response = EventInfoResponse.class,
            tags = {"EventInfoResponse",})
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = EventInfoResponse.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
                    response = EventInfoResponse.class)})
    EventInfoResponse saveEventsLogInfo(@RequestBody String requestJson);

    /**
     * 事件日志分页查询
     * @param json 事件日志分页查询条件
     * @return 事件日志分页查询数列
     */
    @RequestMapping(value = "/ajax/logs/events/list", produces = {"application/json"},
            method = RequestMethod.POST)
    @ApiOperation(value = "事件日志分页查询", notes = "事件日志分页查询", response = EventLogListResponse.class,
            tags = {"EventLogListResponse",})
    @ApiResponses(value = {
            @ApiResponse(code = HttpStatus.SC_OK, message = "返回", response = EventLogListResponse.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
                    response = EventLogListResponse.class)})
    String listEventLogs(@RequestBody String json);
}
