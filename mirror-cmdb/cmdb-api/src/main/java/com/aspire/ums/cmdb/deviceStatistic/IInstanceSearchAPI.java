package com.aspire.ums.cmdb.deviceStatistic;

import com.aspire.ums.cmdb.allocate.payload.BizSysRequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IInstanceAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "资源查询接口类")
@RequestMapping("/v1/cmdb/instanceSearch")
public interface IInstanceSearchAPI {


    @PostMapping(value = "/selectInstanceByPage")
    @ApiOperation(value = "资源查询接口类", notes = "资源查询接口类", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public PageBean<InstanceSearchResp> selectInstanceByPage(@RequestBody InstanceSearchRequest instanceRequest);


    /**
     * 查询特殊权限数据实例数据
     */
    @RequestMapping(value = "/getAuthDeviceData", method = RequestMethod.GET)
    @ApiOperation(value = "获取特殊权限实例数据", notes = "获取特殊权限实例数据", tags = {"CMDB Instance API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getAuthDeviceData(@RequestParam(value = "idcs", required = false) String idcs,
                                                @RequestParam(value = "rooms", required = false) String rooms,
                                                @RequestParam(value = "bisSyss", required = false) String bizSys,
                                                @RequestParam(value = "deviceTypes", required = false) String
                                                        deviceType);

    @GetMapping(value = "/selectDepartBizSystem")
    @ApiOperation(value = "部门业务系统查询接口", notes = "部门业务系统查询接口", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    public Map<String, Object> selectDepartBizSystem(@RequestParam("ip") String ip ,
    		                                         @RequestParam("bizSystem") String bizSystem);

    @PostMapping(value = "/selectDepartBizSystemInfo")
    @ApiOperation(value = "部门业务系统查询接口", notes = "部门业务系统查询接口", tags = {"CMDB DeviceStatistic API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> selectDepartBizSystemInfo(@RequestBody BizSysRequestBody requestBody);
}
