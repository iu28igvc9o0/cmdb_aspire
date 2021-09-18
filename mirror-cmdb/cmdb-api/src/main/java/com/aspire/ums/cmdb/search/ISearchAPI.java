package com.aspire.ums.cmdb.search;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.search.payload.ColumnFilter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ISearchAPI
 * Author:   zhu.juwang
 * Date:     2019/5/21 20:07
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/search")
public interface ISearchAPI {

    /**
     * 获取自定义过滤列
     */
    @RequestMapping(value = "/getOrInsertColumnFilter/{menuType}/{moduleId}/{loginName}", method = RequestMethod.GET)
    @ApiOperation(value = "获取自定义过滤列", notes = "获取自定义过滤列", tags = {"CMDB Search API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getOrInsertColumnFilter(@PathVariable("menuType") String menuType,
                                       @PathVariable("moduleId") String moduleId,
                                       @PathVariable("loginName")String loginName);

    /**
     * 获取自定义过滤列
     */
    @RequestMapping(value = "/updateColumnFilter", method = RequestMethod.PUT)
    @ApiOperation(value = "获取自定义过滤列", notes = "获取自定义过滤列", tags = {"CMDB Search API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> updateColumnFilter(@RequestBody ColumnFilter columnFilter);
}
