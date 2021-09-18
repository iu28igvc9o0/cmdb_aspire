package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.DepartmentUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentUserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.DepartmentVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 对外暴露接口
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.entity 类名称: Department 类描述: 对外暴露接口层 创建人: 曾祥华 创建时间: 2019-03-04
 * 16:04:48
 */

@Api(tags = "department", description = "部门管理")
public interface DepartmentService {

    /**
     * 创建信息
     *
     * @param DepartmentCreateRequest
     *            动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/department/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = DepartmentCreateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = DepartmentCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentCreateResponse.class) })
    DepartmentCreateResponse createdDepartment(@RequestBody DepartmentCreateRequest departmentCreateRequest);

    /**
     * 批量创建信息
     *
     * @param departmentCreateRequestList
     *            动作创建请求对象
     * @return DepartmentCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/department/batchInsert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量创建", notes = "批量创建", response = DepartmentCreateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = DepartmentCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentCreateResponse.class) })
    ResponseEntity<String> batchCreatedDepartment(@RequestBody List<DepartmentCreateRequest> departmentCreateRequestList);

    /**
     * 根据主键删除单作信息
     *
     * @param actionId
     *            主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/department/{department_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功") })
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("department_id") String departmentId);

    /**
     * 根据主键批量删除信息
     *
     * @param departmentIdList
     *            主键
     * @@return Result 返回结果
     */
    @PostMapping(value = "/v1/department/batchDelete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量删除信息", notes = "批量删除信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功") })
    ResponseEntity<String> batchDeleteByPrimaryKey(@RequestBody List<String> departmentIdList);

    /**
     * 根据主键修改信息
     *
     * @param DepartmentUpdateRequest
     *            修改请求对象
     * @return DepartmentUpdateResponse 修改响应对象
     */
    @PutMapping(value = "/v1/department/{department_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改", notes = "修改", response = DepartmentUpdateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = DepartmentUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = DepartmentUpdateResponse.class) })
    DepartmentUpdateResponse modifyByPrimaryKey(@PathVariable("department_id") String departmentId,
            @RequestBody DepartmentUpdateRequest departmentUpdateRequest);

    /**
     * 根据主键查作详情信息
     *
     * @param departmentId
     *            动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/department/{department_id}")
    @ApiOperation(value = "详情", notes = "根据departmentId获取动作详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = DepartmentVO.class),
            @ApiResponse(code = 500, message = "内部错误") })
    DepartmentVO findByPrimaryKey(@PathVariable("department_id") String departmentId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param departmentId
     *            动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/department/list/{department_id}")
    @ApiOperation(value = "查询", notes = "查询")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<DepartmentVO> listByPrimaryKeyArrays(@PathVariable("department_id") String departmentIds);

    /**
     * 根据主键查询动作集合信息
     *
     * @param departmentId
     *            动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @PostMapping(value = "/v1/department/queryList")
    @ApiOperation(value = "根据条件查询列表", notes = "根据条件查询")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<DepartmentDTO> queryList(DepartmentCreateRequest departmentCreateRequest);

    /**
     * 根据page对象查询监控项
     *
     * @param request
     *            分页查询监控对象
     * @return PageResult<DepartmentResponse> page返回对象
     */
    @PostMapping(value = "/v1/department/pageDepartmentList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误") })
    PageResult<DepartmentDTO> pageList(@RequestBody DepartmentPageRequest request);

    /**
     * 根据部门名称查作详情信息
     *
     * @param name
     *            动作主键
     * @return DepartmentVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/department/findByname/{name}")
    @ApiOperation(value = "详情", notes = "根据名称获取部门详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = DepartmentVO.class),
            @ApiResponse(code = 500, message = "内部错误") })
    DepartmentVO findByName(@PathVariable("name") String name);

    @GetMapping(value = "/v1/department/queryByDeptId")
    @ApiOperation(value = "根据deptId查询下级", notes = "根据deptId查询下级")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<DepartmentDTO> queryByDeptId(
            @ApiParam(name = "deptId", required = false) @RequestParam(value = "deptId", required = false) String deptId);
    
    @GetMapping(value = "/v1/department/getAll")
    @ApiOperation(value = "查询所有部门", notes = "查询所有部门")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<DepartmentDTO> getAll();

    @GetMapping(value = "/v1/department/queryDepartAndUser")
    @ApiOperation(value = "根据deptId查询下级部门和人员", notes = "根据deptId查询下级部门和人员")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<DepartmentUserDTO> queryDepartAndUser(
            @ApiParam(name = "deptId", required = false) @RequestParam(value = "deptId", required = false) String deptId);
}
