package com.aspire.mirror.composite.service.cmdb;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.inspection.payload.InsertDemandEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.cmdb
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/13 14:32
 * 版本: v1.0
 */
@Api(value = "需求收集申请")
@RequestMapping("${version}/cmdb/demandManager")
public interface IDemandService {
    @PostMapping(value = "/list")
    @ApiOperation(value = "需求收集列表", notes = "需求收集列表", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject list(@RequestBody Map<String, Object> queryMap);

    @GetMapping(value = "/get")
    @ApiOperation(value = "获取单个需求详情", notes = "获取单个需求详情", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject get(@RequestParam("demandId") String demandId);

    @GetMapping(value = "/list/header")
    @ApiOperation(value = "查询需求", notes = "查询需求", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getTableHeader();
    
    @GetMapping(value = "/demandTypeList")
    @ApiOperation(value = "查询需求类型和值", notes = "查询需求类型和值", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getDemandTypeList();
    
    
    @PostMapping(value = "/save")
    @ApiOperation(value = "需求申请", notes = "需求申请", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String save(@RequestBody InsertDemandEntity insertDemandEntity);
    
    @PutMapping(value = "/update")
    @ApiOperation(value = "需求修改", notes = "需求修改", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    String update(@RequestBody InsertDemandEntity insertDemandEntity);
    
    @PostMapping(value = "/exportDemand")
    @ApiOperation(value = "导出数据", notes = "导出数据", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> exportDemand (HttpServletResponse response,
                                             @RequestBody Map<String, Object> sendRequest);

    @GetMapping(value = "/importDemandTemplate")
    @ApiOperation(value = "生成导入模板", notes = "生成导入模板", tags = {"Demand API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, String> importDemandTemplate (HttpServletResponse response);
}
