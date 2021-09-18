package com.aspire.mirror.composite.service.cmdb.v3.module;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 说明: 模型表头分组配置类
 * 工程: UMS
 * 作者: zhujuwang
 * 时间: 2021/2/24 14:41
 */
@RequestMapping("${version}/v3/cmdb/moduleGroup")
public interface ICmdbV3ModuleGroupAPI {

    @RequestMapping(value = "/header", method = RequestMethod.GET)
    @ApiOperation(value = "获取模型表头", notes = "获取模型表头", tags = { "CMDB Module Group API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<Map<String, Object>> getModuleHeader(@RequestParam(value = "moduleId") String moduleId,
                                              @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode);

}
