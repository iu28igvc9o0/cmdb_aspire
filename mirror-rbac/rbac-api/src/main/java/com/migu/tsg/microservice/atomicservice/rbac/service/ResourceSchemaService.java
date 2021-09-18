package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.migu.tsg.microservice.atomicservice.rbac.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.service <br>
 * 类名称: ResourceSchemaService.java <br>
 * 类描述: 【RBAC原子层】资源模式服务 API接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:16:38 <br>
 * 版本: v1.0
 */
@Api(tags = "schemas", description = "资源、操作和约束的模式定义")
public interface ResourceSchemaService {
	
    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 响应参数
     */
    @ApiOperation("查询资源模式列表")
    @GetMapping(value = "/v1/schemas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList();

    /**
     * 查询获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 响应参数
     */
    @ApiOperation("查询子级资源模式列表")
    @GetMapping(value = "/v1/schemas/child", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FetchResourceSchemaDetailResponse> fetchChildrenRoleSchemaList(@RequestParam(name="id", required=false)String id);
    
    /**
     * 查询获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 响应参数
     */
    @ApiOperation("查询子级资源模式列表--级联查询所有子孙节点")
    @GetMapping(value = "/v1/schemas/fullChild", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FetchResourceSchemaDetailResponse> fetchFullChildrenRoleSchemaList(@RequestParam(name="id", required=false)String id);

    /**
     * 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
     * @param resourceType 资源类型
     * @return 响应参数
     */
    @ApiOperation("查询单个资源模式信息")
    @GetMapping(value = "/v1/schemas/{resource_type}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FetchResourceSchemaDetailResponse fetchRoleSchemaDetail(
            @PathVariable("resource_type") String resourceType);

}
