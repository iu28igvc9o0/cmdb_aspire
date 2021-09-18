package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEvent;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbModuleEventAPI
 * Author:   zhu.juwang
 * Date:     2020/5/19 10:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbModuleEventClient {

    @RequestMapping(value = "/cmdb/module/event/getModuleEventList", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型后置事件列表", notes = "获取模型后置事件列表", tags = {"CMDB Module Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbModuleEvent> getModuleEventList(@RequestParam("moduleId") String moduleId,
                                             @RequestParam(value = "eventClass", required = false) String eventClass);

    @RequestMapping(value = "/cmdb/module/event/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增模型后置事件列表", notes = "新增模型后置事件列表", tags = {"CMDB Module Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestParam("userName") String userName,
                             @RequestParam("moduleId") String moduleId,
                             @RequestBody List<CmdbModuleEvent> eventList);


    @RequestMapping(value = "/cmdb/module/event/getCodeEventData", method = RequestMethod.GET)
    @ApiOperation(value = "获取配置项事件返回数据", notes = "获取配置项事件返回数据", tags = {"CMDB Module Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getCodeEventData(@RequestParam("moduleId") String moduleId,
                                         @RequestParam("codeId") String codeId,
                                         @RequestBody Map<String, Object> selectItem,
                                         @RequestParam("eventType") String eventType);


    @RequestMapping(value = "/cmdb/module/event/getHaveEventCodeList", method = RequestMethod.GET)
    @ApiOperation(value = "获取有配置项事件的字段", notes = "获取有配置项事件的字段", tags = {"CMDB Module Event API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, List<Map<String, Object>>> getHaveEventCodeList(@RequestParam("moduleId") String moduleId);


}
