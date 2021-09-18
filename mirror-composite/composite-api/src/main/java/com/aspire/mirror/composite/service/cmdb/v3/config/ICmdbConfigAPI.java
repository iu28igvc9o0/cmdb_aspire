package com.aspire.mirror.composite.service.cmdb.v3.config;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbConfigAPI
 * Author:   zhu.juwang
 * Date:     2020/5/6 11:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/v3/cmdb/module/config")
public interface ICmdbConfigAPI {

    /**
     * 获取根模型分组列表
     * @param
     * @return
     */
    @GetMapping("/getConfigByCode")
    @ApiOperation(value = "根据配置编码获取配置明细", notes = "根据配置编码获取配置明细", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbConfig getConfigByCode(@RequestParam("configCode") String configCode);

    /*
     *  新增CMDB配置编码
     * */
    @PostMapping("/save")
    @ApiOperation(value = "新增CMDB配置编码", notes = "新增CMDB配置编码", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  编辑CMDB配置编码
     * */
    @PutMapping("/update")
    @ApiOperation(value = "编辑CMDB配置编码", notes = "编辑CMDB配置编码", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> update(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  删除CMDB配置编码
     * */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除CMDB配置编码", notes = "删除CMDB配置编码", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@RequestBody CmdbConfig cmdbConfig);

    /*
     *  分页查询列表数据
     * */
    @PostMapping("/list")
    @ApiOperation(value = "分页查询列表数据", notes = "分页查询列表数据", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbConfig> list(@RequestBody CmdbConfigRequest req);

    /*
     *  依据编码查询唯一
     * */
    @GetMapping("/getOne")
    @ApiOperation(value = "依据编码查询唯一", notes = "依据编码查询唯一", tags = {"Cmdb Config API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Result.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getOne(@RequestParam("configCode") String configCode);

}
