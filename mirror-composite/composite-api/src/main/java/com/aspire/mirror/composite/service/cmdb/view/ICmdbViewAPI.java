package com.aspire.mirror.composite.service.cmdb.view;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewData;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbViewAPI
 * Author:   hangfang
 * Date:     2020/5/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("${version}/cmdb/view")
public interface ICmdbViewAPI {

    /**
     * 根据模型分组获取视图列表
     */
    @RequestMapping(value = "/listByQuery", method = RequestMethod.POST)
    @ApiOperation(value = "根据模型分组获取视图列表", notes = "根据模型分组获取视图列表", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Result<CmdbView> listByQuery(@RequestBody CmdbViewQuery query);

    /**
     * 根据id获取数据结构(全部)
     */
    @RequestMapping(value = "/getDataStructure", method = RequestMethod.GET)
    @ApiOperation(value = "根据id获取数据结构", notes = "根据id获取数据结构", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbViewData getDataStructure(@RequestParam("id") String id);

    /**
     * 根据id获取一级节点数据（静态节点则获取2层）
     */
    @RequestMapping(value = "/getNextNodeData", method = RequestMethod.POST)
    @ApiOperation(value = "根据id获取一级节点数据", notes = "根据id获取一级节点数据", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbViewData getNextNodeData(@RequestBody CmdbViewData viewData);

    /**
     * 根据id获取视图结构
     */
    @RequestMapping(value = "/getStructure", method = RequestMethod.GET)
    @ApiOperation(value = "根据id获取视图结构", notes = "根据id获取视图结构", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    CmdbView getStructure(@RequestParam("id") String id);

    /**
     * 新增自定义视图
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "新增自定义视图", notes = "新增自定义视图", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> save(@RequestBody CmdbView cmdbView);

    /**
     * 删除自定义视图
     */
    @RequestMapping(value = "/deleteView", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除自定义视图", notes = "删除自定义视图", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteView(@RequestParam("id") String id);
    /**
     * 删除自定义视图节点
     */
    @RequestMapping(value = "/deleteNode", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除自定义视图节点", notes = "删除自定义视图节点", tags = {"CMDB VIEW API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "删除成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> deleteNode(@RequestParam("id") String id);
}
