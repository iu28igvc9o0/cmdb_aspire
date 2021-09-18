package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentPayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentQueryPagePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentTreePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.DepartmentUserTreePayload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 对外暴露接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.entity   
 * 类名称:     Department
 * 类描述:     对外暴露接口层
 * 创建人:     曾祥华
 * 创建时间:     2019-03-04 16:04:48
 */
 
@RequestMapping(value="${version}/department", produces = "application/json;charset=UTF-8")
@Api(value = "Composite department service", description = "Composite department service")
public interface IDepartmentService {
	/**
     * 创建信息
     *
     * @param DepartmentCreateRequest 动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = DepartmentPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentPayload.class)})
    DepartmentResponse createdDepartment(@RequestBody DepartmentPayload departmentCreateRequest);
    
   /**
     * 根据主键删除单作信息
     *
     * @param actionId 主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/{department_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功")})
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("department_id") String departmentId);
    
    /**
     * 根据主键修改信息
     *
     * @param DepartmentUpdateRequest 修改请求对象
     * @return DepartmentUpdateResponse 修改响应对象
     */
    @PutMapping(value = "/{department_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改", notes = "修改", response = DepartmentPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentPayload.class)})
    DepartmentResponse modifyByPrimaryKey(@PathVariable("department_id") String departmentId, @RequestBody
    		DepartmentPayload departmentUpdateRequest);
            
    /**
     * 根据主键查作详情信息
     *
     * @param departmentId 动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @GetMapping(value = "/{department_id}")
    @ApiOperation(value = "详情", notes = "根据departmentId获取动作详情")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "内部错误")})
    DepartmentResponse findByPrimaryKey(@PathVariable("department_id") String departmentId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param departmentId 动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @GetMapping(value = "/list/{department_id}")
    @ApiOperation(value = "查询", notes = "查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 500, message = "内部错误")})
    List<DepartmentResponse> listByPrimaryKeyArrays(@PathVariable("department_id") String departmentIds);

	/**
     * 创建信息
     *
     * @param DepartmentCreateRequest 动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/queryList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询列表", notes = "查询列表", response = DepartmentPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentPayload.class)})
    List<DepartmentResponse> queryList(@RequestBody DepartmentPayload departmentCreateRequest);

	/**
     * 创建信息
     *
     * @param DepartmentCreateRequest 动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/queryTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "查询部门树", notes = "查询部门树", response = DepartmentPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentPayload.class)})
    List<DepartmentTreePayload> queryTree(@RequestBody DepartmentPayload departmentCreateRequest);

    /**
    * 根据page对象查询监控项
    *
    * @param request 分页查询监控对象
    * @return PageResult<DepartmentResponse> page返回对象
    */
    @PostMapping(value = "/pageList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "成功", response = PageResponse.class),
    @ApiResponse(code = 500, message = "内部错误")})
    PageResult<DepartmentResponse> pageList(@RequestBody DepartmentQueryPagePayload request);

   /**
    * 通讯录部门树
    * @return
    */
    @GetMapping(value = "/deptTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "通讯录部门树", notes = "通讯录部门树", response = DepartmentPayload.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentPayload.class)})
    Map<String, Object> deptTree(@ApiParam(name="deptId",required = false) @RequestParam(value = "deptId", required = false)String deptId);

    /**
     * 部门-人员树形
     * 
     * @return
     */
    @GetMapping(value = "/deptUserTree", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "部门-人员树形", notes = "部门-人员树形", response = DepartmentPayload.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = DepartmentPayload.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentUserTreePayload.class) })
    Map<String, Object> deptUserTree(
            @ApiParam(name = "deptId", required = false) @RequestParam(value = "deptId", required = false) String deptId);

}
