package com.aspire.mirror.composite.service.cmdb.restful.cloud;

import com.aspire.ums.cmdb.common.CloudResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICloudRestfulAPI
 * Author:   HANGFANG
 * Date:     2021/1/12 10:55
 * Description: 全网监控中心系统-云资源数据集成
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/RESTForQW")
public interface ICloudRestfulAPI {
    /**
     * 根据查询条件,获取资源池数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCfgZYC", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源池数据", notes = "获取资源池数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCfgZYC(@RequestBody Map<String, Object> params);

    /**
     * 根据查询条件,获取服务器数据 设备类型为X86服务器
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCfgFWQ", method = RequestMethod.POST)
    @ApiOperation(value = "获取服务器数据", notes = "获取服务器数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCfgFWQ(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取云主机数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCfgXNJ", method = RequestMethod.POST)
    @ApiOperation(value = "获取云主机数据", notes = "获取云主机数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCfgXNJ(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取分布式块存储数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCfgKCC", method = RequestMethod.POST)
    @ApiOperation(value = "获取分布式块存储数据", notes = "获取分布式块存储数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCfgKCC(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取裸金属服务器数据，设备类型为X86服务器并节点类型为计算节点
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCfgLJS", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源池数据", notes = "获取资源池数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCfgLJS(@RequestBody Map<String, Object> params);
    /**
     * 根据查询条件,获取业务系统数据
     * @param params 查询参数
     *   {"begintime": "", "endtime": ""}
     */
    @RequestMapping(value = "/getCRCBIZ", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统数据", notes = "获取业务系统数据", tags = {"Cmdb Cloud Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CloudResult<Map<String, Object>> getCRCBIZ(@RequestBody Map<String, Object> params);
}
