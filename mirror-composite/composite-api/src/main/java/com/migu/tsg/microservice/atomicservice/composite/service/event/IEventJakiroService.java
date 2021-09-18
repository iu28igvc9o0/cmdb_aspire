package com.migu.tsg.microservice.atomicservice.composite.service.event;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.event.dto.RequestEventInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * 事件显示 Project Name:composite-api File Name:IEventJakiroService.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service.events
 * ClassName: IEventJakiroService <br/>
 * date: 2017年10月8日 下午4:54:18 <br/>
 * 列出所有成员的操作事件
 * 
 * @author zhangqing
 * @version
 * @since JDK 1.6
 */
@Api(value = "事件列表服务", description = "事件列表服务 API")
public interface IEventJakiroService {
    
    /**
     * response成功值
     */
    int RESPONSE_SUCCESS = 200;
    /**
     * 
     * listEvents:列出指定数量的事件 <br/>
     *
     * 作者： zhangqing
     * 
     * @param projectName
     * @param startTime
     * @param endTime
     * @param size
     * @return
     */
    @RequestMapping(value = "/v1/events/{namespace}", produces = { "application/json" },
            method = RequestMethod.GET)
    @ApiOperation(value = "列表指定数量的事件", notes = "列表指定数量的事件", response = String.class,
            tags = { "composite event management service API" })
    @ApiResponses(value = {@ApiResponse(code = RESPONSE_SUCCESS, message = "返回事件信息的JSON格式",
                  response = String.class) })           
    public String listEvents(@PathVariable("namespace") String namespace,@RequestParam(value = "project_name", 
                             required = false)String projectName,@RequestParam(value = "start_time")String startTime, 
                             @RequestParam(value = "end_time")String endTime, 
                             @RequestParam(value = "size")String size,
                             @RequestParam(value = "pageno")String pageno,
                             @RequestParam(value = "resource_type", required = false) String resourceType,
                             @RequestParam(value = "resource_id", required = false) String resourceId,
                             @RequestParam(value = "query_string", required = false)String queryString);
/////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * 
    * listEventsByResourceType:列表对指定类型资源的操作事件 <br/>
    *
    * 作者： zhangqing
    * @param projectName
    * @param resourceType
    * @param startTime
    * @param endTime
    * @param size
    * @return
     */
    @RequestMapping(value = "/v1/events/{namespace}/{resource_type}", produces = { "application/json" },
            method = RequestMethod.GET)
    @ApiOperation(value = "列表对指定类型资源的操作事件", notes = "列表对指定类型资源的操作事件", 
                response = String.class,tags = { "composite event management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS, message = "返回某个资源的事件信息", 
                response = String.class) })        
    public String listEventsByResourceType(@PathVariable("namespace") String namespace,
                                           @PathVariable("resource_type") String resourceType,
                                           @RequestParam(value = "project_name", required = false)String projectName,   
                                           @RequestParam(value = "start_time")String startTime, 
                                           @RequestParam(value = "end_time")String endTime,
                                           @RequestParam(value = "size", required = false)String size,
                                           @RequestParam(value = "pageno")String pageno,
                                           @RequestParam(value = "query_string", required = false)String queryString);
////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    /**
     * 
    * listEventsByResourceTypeAndUuid:根据资源类型和资源编号列表指定数量的操作事件. <br/>
    *
    * 作者： zhangqing
    * @param resourceType
    * @param resourceUuid
    * @param startTime
    * @param endTime
    * @param size
    * @return
     */
    @RequestMapping(value = "/v1/events/{namespace}/{resource_type}/{resource_uuid}", produces = { "application/json" },
            method = RequestMethod.GET)
    @ApiOperation(value = "根据资源类型和资源编号列表指定数量的操作事件", notes = "根据资源类型和资源编号列表指定数量的操作事件", 
                response = String.class,tags = { "composite event management service API" })
    @ApiResponses(value = {@ApiResponse(code = RESPONSE_SUCCESS, message = "返回事件信息的JSON格式",
                response = String.class) })           
    public String listEventsByResourceTypeAndUuid(@PathVariable("namespace") String namespace,
                                 @PathVariable("resource_type") String resourceType,
                                 @PathVariable("resource_uuid") String resourceUuid,
                                 @RequestParam(value = "start_time")String startTime, 
                                 @RequestParam(value = "end_time")String endTime,
                                 @RequestParam(value = "size")String size,
                                 @RequestParam(value = "pageno")String pageno,
                                 @RequestParam(value = "query_string", required = false)String queryString);

    @RequestMapping(value = "/v1/events/{namespace}/{resource_name}", 
                    produces = { "application/json" }, method = RequestMethod.POST)
    @ApiOperation(value = "资源名称和资源类型的日志信息", notes = "资源名称和资源类型的日志信息", response = BaseResponse.class,
                 tags = { "composite event management service API" })
    @ApiResponses(value = {@ApiResponse(code = RESPONSE_SUCCESS, message = "资源名称和资源类型的日志信息",
                                        response = BaseResponse.class)})
    public BaseResponse envetInfo(@PathVariable("resource_name") String childAccount,
            @PathVariable("namespace") String namespace,
                                  @RequestBody RequestEventInfo request);
}
