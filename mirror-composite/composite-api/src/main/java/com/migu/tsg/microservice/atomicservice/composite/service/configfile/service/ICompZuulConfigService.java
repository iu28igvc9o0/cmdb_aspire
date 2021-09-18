package com.migu.tsg.microservice.atomicservice.composite.service.configfile.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.ZuulConfigResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.ZuulConfigUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("${version}/zuulConfig")
@Api(value = "Composite Resource management(zuulConfig)", description = "Composite Resource management(zuulConfig)")
public interface ICompZuulConfigService {
    /**
     * 
     * getTypes:(获取zuul文件类型). <br/>
     * 作者： baiwp
     * 
     * @return
     */
    @ApiOperation(value = "获取zuul文件类型", notes = "获取zuul文件类型", response = ZuulConfigResponse.class, tags = {
            "Composite Resource management(zuulConfig) service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "获取zuul文件类型", response = ZuulConfigResponse.class) })
    @GetMapping(path = "/getTypes", produces = "application/json;charset=UTF-8")
    public ZuulConfigResponse getTypes();

    /**
     * 
     * fetch:(根据类型获取对应的zuul配置). <br/>
     * 作者： baiwp
     * 
     * @param envType
     * @return
     */
    @ApiOperation(value = "根据类型获取对应的zuul配置", notes = "根据类型获取对应的zuul配置", response = ZuulConfigResponse.class, tags = {
            "Composite Resource management(zuulConfig) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "根据类型获取对应的zuul配置", response = ZuulConfigResponse.class) })
    @GetMapping(path = "/config", produces = "application/json;charset=UTF-8")
    public ZuulConfigResponse fetch(@ApiParam(value = "环境类型") @RequestParam("envType") String envType);

    /**
     * 
     * modify:(更新zuul配置). <br/>
     * 作者： baiwp
     * 
     * @param request
     * @return
     */
    @ApiOperation(value = "更新zuul配置", notes = "更新zuul配置", response = ZuulConfigResponse.class, tags = {
            "Composite Resource management(zuulConfig) service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "更新zuul配置", response = ZuulConfigResponse.class) })
    @PutMapping(path = "/config", produces = "application/json;charset=UTF-8")
    public ZuulConfigResponse modify(@RequestBody ZuulConfigUpdateRequest request);

}
