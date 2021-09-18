package com.aspire.ums.cmdb.v3.es;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbESAPI
 * Author:   zhu.juwang
 * Date:     2020/2/17 9:29
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/v3/cmdb/es")
public interface ICmdbESAPI {
    /**
     * 获取根模型分组列表
     * @param
     * @return
     */
    @GetMapping("/refreshModuleData")
    @ApiOperation(value = "刷新ES库中模型实例数据", notes = "刷新ES库中模型实例数据", tags = {"Cmdb ES API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> refreshModuleData(@RequestParam(value = "module_id", required = false) String moduleId);
}
