package com.aspire.mirror.composite.service.cmdb.assessment;


import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutListResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompITDeviceCoutRequest;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("${version}/cmdb/device/count")
public interface IITDeviceCountAPI {

    /**
     * 查询所有设备量信息
     * @return 设备量
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有设备量信息", notes = "查询所有设备量信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbItDeviceCount> list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                   @RequestParam(value = "page", required = false) String page,
                                   @RequestBody CmdbItDeviceCount deviceCount);


    /**
     * 存储设备量信息
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "存储设备量信息", notes = "存储设备量信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存储成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody CompITDeviceCoutRequest deviceCoutRequest);

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
    JSONObject getProduceAndDeviceList(@RequestBody Map<String,String> requestMp, HttpServletResponse response);

}
