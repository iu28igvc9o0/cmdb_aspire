package com.aspire.ums.cmdb.v3.module;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @projectName: ICmdbV3ModuleBusinessQuotaAPI
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-04 15:52
 **/
@RequestMapping("/v1/cmdb/module/businessQuota")
public interface ICmdbV3ModuleBusinessQuotaAPI {

    @GetMapping("/list")
    @ApiOperation(value = "获取业务系统配额信息", notes = "获取业务系统配额信息", tags = {"Cmdb Module BusinessQuota API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getAllBusinessQuotaInfo();


    @GetMapping("/listNeedCharge")
    @ApiOperation(value = "获取需要计费的业务系统配额信息", notes = "获取需要计费的业务系统配额信息", tags = {"Cmdb Module BusinessQuota API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getNeedChargeBusinessQuotaInfo();

}