package com.aspire.mirror.composite.service.cmdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICompQueryService
 * Author:   zhu.juwang
 * Date:     2019/3/27 9:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "自定义查询")
@RequestMapping("${version}/cmdb")
public interface ICompQueryService {
    /**
     * 获取自定义查询条件集合
     * @return 自定义查询条件集合
     */
    @RequestMapping(value = "/query/condition/", method = RequestMethod.GET)
    @ApiOperation(value = "获取自定义查询条件集合", notes = "获取自定义查询条件集合", tags = {"CMDB Query API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getQueryList(@RequestParam(value = "moduleId", required = false) String moduleId,
                                   @RequestParam(value = "menuType", required = false) String menuType);


    /**
     * 新增自定义条件
     */
    @RequestMapping(value = "/query/condition/update", method = RequestMethod.PUT)
    @ApiOperation(value = "新增或修改自定义条件", notes = "新增或修改自定义条件", tags = {"CMDB Query API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> insertOrUpdateQuery(HttpServletResponse response,  @RequestBody JSONObject entity);


    /**
     * 删除自定义条件
     */
    @RequestMapping(value = "/query/condition/delete/{queryId}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除自定义条件", notes = "删除自定义条件", tags = {"CMDB Query API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> deleteQuery(HttpServletResponse response, @PathVariable("queryId") String queryId);
}
