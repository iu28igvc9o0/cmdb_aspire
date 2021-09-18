package com.aspire.mirror.composite.service.cmdb.assessment;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceProduce;
import io.swagger.annotations.Api;
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
 * FileName: IDeviceProduceAPI
 * Author:   hangfang
 * Date:     2019/6/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/device/")
public interface IDeviceProduceAPI {

    /**
     * 查询所有厂家信息
     * @return 所有厂家信息
     */
    @RequestMapping(value = "/produce/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有厂家信息", notes = "查询所有厂家信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbDeviceProduce> list();


    /**
     * 存储厂家信息
     */
    @RequestMapping(value = "/produce/save", method = RequestMethod.POST)
    @ApiOperation(value = "存储厂家信息", notes = "存储厂家信息", tags = {"CMDB device API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "存储成功", response = void.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody List<CmdbDeviceProduce> deviceProduces);
}
