package com.aspire.ums.cmdb.restful.alert;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IAlertRestfulAPI
 * Author:   zhu.juwang
 * Date:     2019/7/29 16:55
 * Description: 该接口类用来专门提供接口给告警模块使用
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/restful/alert")
public interface IAlertRestfulAPI {
    /**
     * 根据业务系统名称获取部门信息
     * @param bizSystem 业务系统名称
     */
    @RequestMapping(value = "/getDepartmentInfoByBizSystem", method = RequestMethod.GET)
    @ApiOperation(value = "根据业务系统名称获取部门信息", notes = "根据业务系统名称获取部门信息", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> getDepartmentInfoByBizSystem(@RequestParam("bizSystem") String bizSystem);

    /**
     * 根据资源池获取资源池下的所有工程期数
     * @param idcType 资源池名称
     */
    @RequestMapping(value = "/getProjectNameByIdcType", method = RequestMethod.GET)
    @ApiOperation(value = "根据资源池获取资源池下的所有工程期数", notes = "根据资源池获取资源池下的所有工程期数", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, String>> getProjectNameByIdcType(@RequestParam("idcType") String idcType);

    /**
     * 根据资源池统计设备数量
     * @param params 请求参数
     */
    @RequestMapping(value = "/statistics/device/byIdcType", method = RequestMethod.POST)
    @ApiOperation(value = "根据资源池统计设备数量", notes = "根据资源池统计设备数量", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> statisticsDeviceByIdcType(@RequestBody Map<String, Object> params);

    /**
     * 根据一级部门统计设备数量
     * @param params 请求参数
     */
    @RequestMapping(value = "/statistics/device/byDepartment1", method = RequestMethod.POST)
    @ApiOperation(value = "根据一级部门统计设备数量", notes = "根据一级部门统计设备数量", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> statisticsDeviceByDepartment1(@RequestBody Map<String, Object> params);

    /**
     * 根据业务系统统计设备数量
     * @param params 请求参数
     */
    @RequestMapping(value = "/statistics/device/byBizSystem", method = RequestMethod.POST)
    @ApiOperation(value = "根据业务系统统计设备数量", notes = "根据业务系统统计设备数量", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> statisticsDeviceByBizSystem(@RequestBody Map<String, Object> params);

    /**
     * 统计一级部门/二级部门的设备数量, 提供给资源利用率接口
     * @param department1 一级部门名称
     */
    @RequestMapping(value = "/statistics/device/departmentForAvailability", method = RequestMethod.GET)
    @ApiOperation(value = "统计一级部门/二级部门的设备数量", notes = "统计一级部门/二级部门的设备数量", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> statisticsDepartmentForAvailability(@RequestParam(value = "department1", required = false) String department1);

    /**
     * 验证设备删除状态
     * @param instanceList 设备列表
     */
    @RequestMapping(value = "/check/deleteStatus", method = RequestMethod.POST)
    @ApiOperation(value = "验证设备删除状态", notes = "验证设备删除状态", tags = {"Cmdb To Alert Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> checkInstanceDeleteStatus(@RequestBody List<Map<String, String>> instanceList);
}
