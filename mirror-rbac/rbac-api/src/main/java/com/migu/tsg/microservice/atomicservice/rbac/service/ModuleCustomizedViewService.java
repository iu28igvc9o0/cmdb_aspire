package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewDesignRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleCustomizedViewUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedViewDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(tags = "View", description = "配置试图管理")
public interface ModuleCustomizedViewService {
	
	
	
	/**
     * 创建信息
     *
     * @param ModuleCustomizedPayload 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/insert")
    @ApiOperation(value = "创建视图", notes = "创建视图",  tags = {"moduleCustomizedview"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> createdModuleCustomizedView(@RequestBody ModuleCustomizedViewRequest moduleCustomizedViewRequest);
    
    
    /**
     * 创建信息
     *
     * @param ModuleCustomizedPayload 动作创建请求对象
     * @return ModuleCustomizedCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/update")
    @ApiOperation(value = "更新视图", notes = "更新视图", tags = {"moduleCustomizedview"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> modifyByPrimaryKey(@RequestBody ModuleCustomizedViewUpdateRequest moduleCustomizedViewRequest);
    
    
   /**
     * 根据主键删除单作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/moduleCustomizedView/{id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息",
            tags = {"moduleCustomizedview"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("id") String id);
    
	
	
	
    /**
     * 根据主键查询动作集合信息
     *
     * @param moduleCustomized 动作主键
     * @return ModuleCustomizedVO 动作详情响应对象
     */
    @PostMapping(value = "/v1/moduleCustomizedView/select")
    @ApiOperation(value = "查询", notes = "查询", tags = {"/v1/moduleCustomizedView"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<ModuleCustomizedViewDTO> select(@RequestBody ModuleCustomizedViewRequest moduleCustomizedViewRequest);
	
	
    @PostMapping(value = "/v1/moduleCustomizedView/design")
    @ApiOperation(value = "设计视图", notes = "设计视图",  tags = {"moduleCustomizedview"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = ResponseEntity.class),
            @ApiResponse(code = 500, message = "内部错误")})
	ResponseEntity<String> designView(@RequestBody ModuleCustomizedViewDesignRequest m);

}
