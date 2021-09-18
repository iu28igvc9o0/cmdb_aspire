package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCodeSettingClient
 * Author:   hangfang
 * Date:     2020/2/12
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3ModuleCodeSettingClient {
    /**
     * 保存模型字段显示设置
     * @param
     * @return
     */
    @PostMapping("/v3/cmdb/module/codeSetting/saveModuleCodeSetting")
    @ApiOperation(value = "保存模型字段显示设置", notes = "保存模型字段显示设置", tags = {"Cmdb Module CodeSetting API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody List<CmdbV3ModuleCodeSetting> moduleCodeSettings);

}
