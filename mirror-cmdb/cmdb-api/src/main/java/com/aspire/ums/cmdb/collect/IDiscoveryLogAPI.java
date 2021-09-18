package com.aspire.ums.cmdb.collect;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.payload.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.payload.AutoDiscoveryLogListResp;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IDiscoveryLogAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 13:25
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/discovery/log")
public interface IDiscoveryLogAPI {
    
    /**
     * 获取指定moduleId的规则列表
     * @param moduleId 模型ID
     * @param pageNumber 当前页数
     * @param pageSize 每页数量5
     * @return 新发现数据列表
     * queryData {segment: 192.168.0,startIp: 1,endIp: 255}
     */
    @RequestMapping(value = "/list/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "获取模型列表", notes = "获取模型列表", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getLogList(@PathVariable("moduleId") String moduleId,
                                 @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @RequestBody(required = false) JSONObject queryData);

    /**
     * 屏蔽新发现实例
     * @param discoveryLogs 新发现实例集合
     * @return
     */
    @RequestMapping(value = "/shield", method = RequestMethod.POST)
    @ApiOperation(value = "查询屏蔽IP列表", notes = "查询屏蔽IP列表", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> shieldIp(@RequestBody List<AutoDiscoveryLogEntity> discoveryLogs);

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "查看IP详情", notes = "查看IP详情", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = AutoDiscoveryLogListResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
    AutoDiscoveryLogListResp getLogDetailById(@PathVariable("id") String logId);

    /**
     * 维护新发现是咧
     * @param id 实例ID
     * @param status 维护状态
     * @return
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "更新IP", notes = "更新IP", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "更新成功", response = AutoDiscoveryLogListResp.class),
            @ApiResponse(code = 500, message = "内部错误")})
     Map<String, Object> update(@PathVariable("id") String id,@RequestParam(value = "status") String status);

    @RequestMapping(value = "/bind/{instanceId}", method = RequestMethod.POST)
    @ApiOperation(value = "绑定实例", notes = "绑定实例", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> bind(@PathVariable("instanceId") String instanceId,
                                    @RequestBody AutoDiscoveryLogEntity discoveryLog);

//    @RequestMapping(value = "/maintain", method = RequestMethod.POST)
//    Map<String, Object> maintain(@RequestParam(value = "id") String id,
//                                        @RequestBody InstanceModel instanceModel);

    @RequestMapping(value = "/listInstance/{moduleId}", method = RequestMethod.GET)
    @ApiOperation(value = "查询实例列表", notes = "根据模型ID查询实例列表", tags = {"CMDB Discovery Log API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject listInstanceByModulId(@PathVariable(value = "moduleId") String moduleId);
}
