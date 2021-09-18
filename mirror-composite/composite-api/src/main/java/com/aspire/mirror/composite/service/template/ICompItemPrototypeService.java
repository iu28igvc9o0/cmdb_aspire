package com.aspire.mirror.composite.service.template;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.ops.api.domain.vulnerability.OpsVulnerabilityInstanceQueryModel;
import com.aspire.mirror.template.api.dto.GeneralResponse;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype.MonitorItemPrototypeQueryModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "监控项原型")
@RequestMapping("${version}/items/monitorItemPrototype")
public interface ICompItemPrototypeService {

    /**
     * 保存监控项原型
     */
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存监控项原型", notes = "保存监控项原型", 
    				response = MonitorItemPrototype.class, tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = GeneralResponse.class)})
    GeneralResponse saveMonitorItemPrototype(@RequestBody MonitorItemPrototype itemPrototype);
    
    /**
     * 删除监控项原型
     */
    @DeleteMapping(value = "/remove/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "删除监控项原型", notes = "删除监控项原型", 
    		response = GeneralResponse.class, tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = GeneralResponse.class)})
    GeneralResponse removeMonitorItemPrototype(@PathVariable("id") Long id);
    
    /**
     * 删除监控项原型
     */
    @DeleteMapping(value = "/batchRemove/{joinIds}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量删除监控项原型", notes = "批量删除监控项原型", 
    		response = GeneralResponse.class, tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = GeneralResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = GeneralResponse.class)})
    GeneralResponse bactchRemoveMonitorItemPrototype(@PathVariable("joinIds") String joinIds);
    
    /**
     * 查询监控项原型列表
     */
    @PostMapping(value = "/queryList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询监控项原型列表", notes = "查询监控项原型列表", 
    	response = MonitorItemPrototype.class, tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = MonitorItemPrototype.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = MonitorItemPrototype.class)})
    PageResponse<MonitorItemPrototype> queryMonitorItemPrototypeList(@RequestBody MonitorItemPrototypeQueryModel queryParams);
    
    /** 
     * 功能描述: 同步监控项原型的配置到引用该原型的指标项  
     */
    @PutMapping(value = "/syncRefreshItemsConfigByItemPrototype/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "同步监控项原型的配置到引用该原型的指标项", notes = "同步监控项原型的配置到引用该原型的指标项", 
    				response = MonitorItemPrototype.class, tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = MonitorItemPrototype.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = MonitorItemPrototype.class)})
    GeneralResponse syncRefreshItemsConfigByItemPrototype(@PathVariable("id") Long id);
    
    @PostMapping(value = "/exportMonitorItemPrototypeList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "导出监控项原型列表", notes = "导出监控项原型列表", tags = {"Monitor Item Prototype API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
    void exportMonitorItemPrototypeList(@RequestBody MonitorItemPrototypeQueryModel queryParams, HttpServletResponse response);
}
