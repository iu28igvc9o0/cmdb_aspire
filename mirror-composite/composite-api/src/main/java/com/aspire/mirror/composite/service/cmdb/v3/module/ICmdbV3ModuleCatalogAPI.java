package com.aspire.mirror.composite.service.cmdb.v3.module;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCatalogAPI
 * Author:   zhu.juwang
 * Date:     2020/1/15 10:51
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/v3/cmdb/module/catalog")
public interface ICmdbV3ModuleCatalogAPI {
    /**
     * 获取根模型分组列表
     * @param
     * @return
     */
    @GetMapping("/getRootLevel")
    @ApiOperation(value = "获取根模型分组列表", notes = "获取根模型分组列表", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<CmdbV3ModuleCatalog> getFirstLevel();

    /**
     * 获取模型分组树
     * @param
     * @return
     */
    @GetMapping("/getAllLevelTree")
    @ApiOperation(value = "获取模型分组树", notes = "获取模型分组树", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getAllLevelTree(@RequestParam(value = "catalogId", required = false) String catalogId);

    /**
     * 新增模型分组
     * @param
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增模型分组", notes = "新增模型分组", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject create(@RequestBody CmdbV3ModuleCatalog catalog);

    /**
     * 修改模型分组
     * @param
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改模型分组", notes = "修改模型分组", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject update(@RequestBody CmdbV3ModuleCatalog catalog);

    /**
     * 修改模型分组排序
     * @param
     * @return
     */
    @PutMapping("/update/sort")
    @ApiOperation(value = "修改模型分组排序", notes = "修改模型分组排序", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject updateSort(@RequestParam("catalogId") String catalogId, @RequestParam("sortType") String sortType);

    /**
     * 修改模型分组排序
     * @param
     * @return
     */
    @GetMapping("/valid")
    @ApiOperation(value = "验证相同分组下是否有相同的分组", notes = "验证相同分组下是否有相同的分组", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject validCatalog(@RequestParam("parentCatalogId") String parentCatalogId,
                            @RequestParam("catalogCode") String catalogCode,
                            @RequestParam("catalogName") String catalogName);

    /**
     * 删除模型分组
     * @param
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除模型分组", notes = "删除模型分组", tags = {"Cmdb Module Catalog API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject delete(@RequestBody CmdbV3ModuleCatalog catalog);
}
