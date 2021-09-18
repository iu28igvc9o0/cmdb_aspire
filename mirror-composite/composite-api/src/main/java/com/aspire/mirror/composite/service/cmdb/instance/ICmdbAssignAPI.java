package com.aspire.mirror.composite.service.cmdb.instance;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbAssign;
import com.aspire.ums.cmdb.instance.payload.CmdbAssignQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbAssignAPI
 * Author:   hangfang
 * Date:     2019/11/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/assign/")
public interface ICmdbAssignAPI {

    /**
     * 获取资源分配分析列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源分配分析列表", notes = "获取资源分配分析列表", tags = {"CMDB assign API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbAssign> list(@RequestBody CmdbAssignQuery query);

    /**
     * 导出资源分配分析数据
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation(value = "导出资源分配分析数据", notes = "导出资源分配分析数据", tags = {"CMDB assign API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "导出成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject export(@RequestBody CmdbAssignQuery query, HttpServletResponse response);

    /**
     * 删除资源分配分析数据
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除资源分配分析数据", notes = "删除资源分配分析数据", tags = {"CMDB assign API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> delete(@RequestParam("id") String id);

}
