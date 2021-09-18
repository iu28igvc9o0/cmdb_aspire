package com.aspire.mirror.composite.service.cmdb.v3.condication;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3CondicationSettingAPI
 * Author:   zhu.juwang
 * Date:     2020/1/10 9:39
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/v3/cmdb/condication")
public interface ICmdbV3CondicationSettingAPI {
    /**
     * 获取组织列表
     * @param
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "获取查询条件列表", notes = "获取查询条件列表", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbV3CondicationSetting> list(@RequestBody CmdbV3CondicationSettingQuery settingQuery);

    @PostMapping("/save")
    @ApiOperation(value = "新增查询条件", notes = "新增查询条件", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编辑成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> save(@RequestBody CmdbV3CondicationSetting setting);

    @PutMapping("/update")
    @ApiOperation(value = "修改查询条件", notes = "修改查询条件", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编辑成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> update(@RequestBody CmdbV3CondicationSetting setting);

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除查询条件", notes = "删除查询条件", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编辑成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> delete(@RequestBody CmdbV3CondicationSetting setting);

    @PostMapping("/get")
    @ApiOperation(value = "查询查询条件配置", notes = "查询查询条件配置", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编辑成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbV3CondicationSetting get(@RequestBody CmdbV3CondicationSetting setting);

    @GetMapping("/validate/unique")
    @ApiOperation(value = "验证条件编码或条件编码和条件地址的唯一性", notes = "验证条件编码或条件编码和条件地址的唯一性", tags = {"Cmdb Condication API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "编辑成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject validConditionUnique( @RequestParam(value = "code",required = false) String code,
                                     @RequestParam(value = "name",required = false) String name);
}
