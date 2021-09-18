package com.aspire.mirror.composite.service.cmdb.v3.module;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCodeSettingAPI
 * Author:   hangfang
 * Date:     2020/2/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/v3/cmdb/module/codeSetting")
public interface ICmdbV3ModuleCodeSettingAPI {

    /**
     * 保存模型字段显示设置
     * @param
     * @return
     */
    @PostMapping("/saveModuleCodeSetting")
    @ApiOperation(value = "保存模型字段显示设置", notes = "保存模型字段显示设置", tags = {"Cmdb Module CodeSetting API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody List<CmdbV3ModuleCodeSetting> moduleCodeSettings);
}
