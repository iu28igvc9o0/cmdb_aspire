package com.aspire.mirror.composite.service.cmdb.restful.beixiang;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IBeiXiangRestfulAPI
 * Author:   hangfang
 * Date:     2020/1/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/restful/beixiang")
public interface IBeiXiangRestfulAPI {
    /**
     * 资源池数量统计
     * 1、大屏登录后的初始展示界面；
     * 2、展示中移信息当前最大的6个资源池数量统计占比；
     * @param top 展示前几
     */
    @RequestMapping(value = "/countIdcTypePer", method = RequestMethod.GET)
    @ApiOperation(value = "资源池数量统计", notes = "资源池数量统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countIdcTypePer(@RequestParam(value = "top", required = false) Integer top);

    /**
     * 资源池设备数量统计
     * 1、展示上述6个最大的资源池的设备数量统计；
     * 2、每个资源池展示X86、云主机、网络设备、安全设备4种设备的数量统计；
     * 3、并且从左到右，从大到小的方式排序；
     * @param top 展示前几
     */
    @RequestMapping(value = "/countIdcDeviceNumber", method = RequestMethod.GET)
    @ApiOperation(value = "资源池设备数量统计", notes = "资源池设备数量统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countIdcDeviceNumber(@RequestParam(value = "top", required = false) Integer top);


    /**
     * 租户业务系统数量占比
     * 1、展示最大的6个租户业务系统数量占比数据；(一级部门且二级部门不为基础平台)
     * @param top 展示前几
     */
    @RequestMapping(value = "/countDepart1BizPer", method = RequestMethod.GET)
    @ApiOperation(value = "租户业务系统数量占比", notes = "租户业务系统数量占比", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDepart1BizPer(@RequestParam(value = "top",required = false) Integer top);

    /**
     * 业务系统个数统计
     * 1、展示最大的6个租户业务系统个数统计，并排序；
     * @param top 展示前几
     */
    @RequestMapping(value = "/countDepart1BizNumber", method = RequestMethod.GET)
    @ApiOperation(value = "业务系统个数统计", notes = "业务系统个数统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDepart1BizNumber(@RequestParam(value = "top",required = false) Integer top);

    /**
     * 主机资源数量统计
     * 1、展示最大的6个租户，主机资源数量统计；
     * 2、具体指标包括：X86主机、云主机数量；
     */
    @RequestMapping(value = "/countDepart1DeviceNumber", method = RequestMethod.GET)
    @ApiOperation(value = "主机资源数量统计", notes = "主机资源数量统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDepart1DeviceNumber(@RequestParam(value = "top",required = false) Integer top);

    /**
     * 信息港基础资源占比
     * 1、展示信息港资源池5类资源的总数及占比；
     * 2、5类资源包括：X86服务器、云主机、网络设备、安全设备、及存储设备；
     * 3、指标数据包括总数和已分配数；
     * @param idcType 资源池名称
     */
    @RequestMapping(value = "/countDeviceByIdcType", method = RequestMethod.GET)
    @ApiOperation(value = "信息港基础资源占比", notes = "信息港基础资源占比", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDeviceByIdcType(@RequestParam(value = "idcType", required = false) String idcType);

    /**
     * TOP10租户资源数量统计
     * 1、展示TOP10租户的资源统计数量；
     * 2、具体指标包括：X86服务器台数、云主机个数、以及使用的存储设备容量；
     * 3、按照从上到下、从大到小排序；
     * 统计已分配数量(存储设备暂时不统计)
     * @param top 展示前几
     */
    @RequestMapping(value = "/countDepart1Device", method = RequestMethod.GET)
    @ApiOperation(value = "TOP10租户资源数量统计", notes = "TOP10租户资源数量统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDepart1Device(@RequestParam(value = "top",required = false) Integer top);

    /**
     * 机房资产信息统计
     * 1、展示3050机房内部四类设备的数量及占比；
     */
    @RequestMapping(value = "/RoomDevicePer", method = RequestMethod.GET)
    @ApiOperation(value = "机房资产信息统计", notes = "机房资产信息统计", tags = {"Cmdb To beixiang Restful API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> roomDevicePer();
}
