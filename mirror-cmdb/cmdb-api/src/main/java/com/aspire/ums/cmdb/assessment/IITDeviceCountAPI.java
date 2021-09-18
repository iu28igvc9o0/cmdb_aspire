package com.aspire.ums.cmdb.assessment;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IITDeviceCount
 * Author:   HANGFANG
 * Date:     2019/6/25 11:19
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "IT设备量信息接口")
@RequestMapping("/cmdb/device/count")
public interface IITDeviceCountAPI {

    /**
     * 查询所有设备量信息
     * @return 设备量
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有设备量信息", notes = "查询所有设备量信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbItDeviceCount> list(@RequestBody CmdbItDeviceCount deviceCount);


    /**
     * 存储设备量信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "存储设备量信息", notes = "存储设备量信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存储成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody List<CmdbItDeviceCount> deviceCounts);

    /**
     * 删除存储设备量信息
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除存储设备量信息", notes = "删除存储设备量信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@RequestBody List<String> produces);

    /**
     * 获取分支机构状态
     * @param quarter 实例数据
     * @return
     */
    @RequestMapping(value = "/getProvinceStatus", method = RequestMethod.GET)
    @ApiOperation(value = "获取分支机构状态", notes = "获取分支机构状态", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getProvinceStatus(@RequestParam("quarter") String quarter);

    /**
     * 加载某季度的机构下的厂商和设备类型列表
     * @param requestMp 包括province和quarter
     * @return
     */
    @RequestMapping(value = "/getProduceAndDeviceList", method = RequestMethod.POST)
    @ApiOperation(value = "加载机构下的厂商和设备类型列表", notes = "加载机构下的厂商和设备类型列表", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getProduceAndDeviceList(@RequestBody Map<String,String> requestMp);
}
