package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCatalogClient
 * Author:   zhu.juwang
 * Date:     2020/1/15 10:51
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3ModuleGroupClient {

    @RequestMapping(value = "/v3/cmdb/moduleGroup/header", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型表头", notes = "获取模型表头", tags = { "CMDB Module Group API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, Object>> getModuleHeader(@RequestParam(value = "moduleId") String moduleId,
                                              @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode);

    @RequestMapping(value = "/v3/cmdb/moduleGroup/getAllCodeByGroupCode", method = RequestMethod.GET)
    @ApiOperation(value = "根据模型、分组编码获取分组下所有的码表配置信息",
            notes = "根据模型、分组编码获取分组下所有的码表配置信息", tags = { "CMDB Module Group API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<CmdbSimpleCode> getAllCodeByGroupCode(@RequestParam(value = "moduleId") String moduleId,
                                               @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode);
}
