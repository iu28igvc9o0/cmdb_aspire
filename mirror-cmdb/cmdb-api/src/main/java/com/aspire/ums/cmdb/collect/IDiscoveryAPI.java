package com.aspire.ums.cmdb.collect;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.payload.InsertDiscoveryRuleEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IDiscoveryAPI
 * Author:   zhu.juwang
 * Date:     2019/5/6 12:28
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Api(value = "新发现采集接口类")
@RequestMapping("/cmdb/discovery")
public interface IDiscoveryAPI {
    /**
     * 查询模型列表, 返回模型下配置的新发现规则的列表
     *
     * @return 模型列表
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    @ApiOperation(value = "查询模型列表", notes = "查询模型列表, 返回模型下配置的新发现规则的列表", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = JSONArray.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getModuleList();

    /**
     * 获取指定moduleId的规则列表
     *
     * @param moduleId   模型ID
     * @param pageNumber 当前页数
     * @param pageSize   每页数量
     * @return 规则列表
     */
    @RequestMapping(value = "/rule/list/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "查询规则列表", notes = "获取指定moduleId的规则列表", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getRuleList(@PathVariable("moduleId") String moduleId,
                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestBody(required = false) JSONObject param);

    /**
     * 新增规则
     *
     * @param moduleId   模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/insert/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "新增规则", notes = "新增规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "新增成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> insertRule(@PathVariable("moduleId") String moduleId,
                                   @RequestBody InsertDiscoveryRuleEntity ruleEntity);

    /**
     * 修改规则
     *
     * @param moduleId   模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/update/{moduleId}/{ruleId}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改规则", notes = "修改规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "修改成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> updateRule(@PathVariable("moduleId") String moduleId,
                                   @PathVariable("ruleId") String ruleId,
                                   @RequestBody InsertDiscoveryRuleEntity ruleEntity);

    /**
     * 删除规则
     *
     * @param moduleId 模型ID
     * @param ruleIds  删除实体
     * @return
     */
    @RequestMapping(value = "/rule/delete/{moduleId}/", method = RequestMethod.POST)
    @ApiOperation(value = "删除规则", notes = "删除规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "删除成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> deleteRule(@PathVariable("moduleId") String moduleId,
                                   @RequestBody List<String> ruleIds);
}
