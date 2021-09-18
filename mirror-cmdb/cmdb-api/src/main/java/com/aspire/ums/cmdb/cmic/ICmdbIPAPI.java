package com.aspire.ums.cmdb.cmic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbIPAPI
 * Author:   zhu.juwang
 * Date:     2020/5/27 10:33
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = " IP地址接口类")
@RequestMapping("/cmdb/cmic/ip")
public interface ICmdbIPAPI {

    /**
     * 获取指定IP类型指定网段的IP地址使用情况
     * @param conditions 查询条件
     * @return
     */
    @RequestMapping(value = "/statisticsIpUseInfo", method = RequestMethod.POST)
    @ApiOperation(value = "获取指定IP类型指定网段的IP地址使用情况", notes = "获取指定IP类型指定网段的IP地址使用情况",
            tags = {"CMDB CMIC IP API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> statisticsIpUseInfo(@RequestBody(required = false) Map<String, Object> conditions);

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "获取指定IP类型指定网段的IP地址使用情况", notes = "获取指定IP类型指定网段的IP地址使用情况",
            tags = {"CMDB CMIC IP API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<String> test();
}
