package com.aspire.mirror.composite.service.cmdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.AutoDiscoveryRuleInsertVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
@RequestMapping("${version}/cmdb/discovery")
public interface ICompDiscoveryService {

    /**
     * 查询模型列表, 返回模型下配置的新发现规则的列表
     * @return 模型列表
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取新发现规则模型列表", notes = "获取新发现规则模型列表", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONArray getModuleList();

    /**
     * 获取自定义查询条件集合
     * @return 自定义查询条件集合
     */
    @RequestMapping(value = "/rule/list/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "获取新发现规则列表", notes = "获取新发现规则列表", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject getRuleList(@PathVariable("moduleId") String moduleId,
                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestBody(required = false) JSONObject param);


    /**
     * 新增规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/insert/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "新增规则", notes = "新增规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> insertRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                   @RequestBody AutoDiscoveryRuleInsertVO ruleEntity);

    /**
     * 修改规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/update/{moduleId}/{ruleId}", method = RequestMethod.PUT)
    @ApiOperation(value = "修改规则", notes = "修改规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> updateRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                   @PathVariable("ruleId") String ruleId,
                                   @RequestBody AutoDiscoveryRuleInsertVO ruleEntity);

    /**
     * 删除规则
     * @param moduleId 模型ID
     * @param ruleIds 删除实体
     * @return
     */
    @RequestMapping(value = "/rule/delete/{moduleId}/", method = RequestMethod.POST)
    @ApiOperation(value = "删除规则", notes = "删除规则", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> deleteRule(HttpServletResponse response, @PathVariable("moduleId") String moduleId,
                                   @RequestBody List<String> ruleIds);

    /**
     * 导出规则
     * @return
     */
    @RequestMapping(value = "/rule/export/{moduleId}", method = RequestMethod.POST)
    @ApiOperation(value = "获取新发现规则列表", notes = "获取新发现规则列表", tags = {"CMDB Discovery API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportRules(HttpServletResponse response, @PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> sendRequest);
}
