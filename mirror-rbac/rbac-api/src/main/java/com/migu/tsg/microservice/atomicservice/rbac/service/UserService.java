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
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserBatchCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserCreateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserPageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserUpdateResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 对外暴露接口
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.entity 类名称: User 类描述: 对外暴露接口层 创建人: 曾祥华 创建时间: 2019-03-07 16:05:29
 */

@Api(tags = "user", description = "人员信息管理")
public interface UserService {

    /**
     * 创建信息
     *
     * @param UserCreateRequest
     *            动作创建请求对象
     * @return UserCreateResponse 动作创建响应对象
     */
    @PostMapping(value = "/v1/user/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = UserCreateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = UserCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserCreateResponse.class) })
    UserCreateResponse createdUser(@RequestBody UserCreateRequest userCreateRequest);

    /**
     * 创建信息
     *
     * @param userBatchCreateRequest
     *            用户批量创建请求对象
     * @return UserCreateResponse 用户创建响应对象
     */
    @PostMapping(value = "/v1/user/batchInsert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "创建", notes = "创建", response = UserCreateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = UserCreateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserCreateResponse.class) })
    int batchCreatedUser(@RequestBody UserBatchCreateRequest userBatchCreateRequest);

    /**
     * 根据主键删除单作信息
     *
     * @param actionId
     *            主键
     * @@return Result 返回结果
     */
    @DeleteMapping(value = "/v1/user/{user_id}")
    @ApiOperation(value = "删除单条信息", notes = "删除单条信息")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功") })
    ResponseEntity<String> deleteByPrimaryKey(@PathVariable("user_id") String userId);

    /**
     * 根据主键修改信息
     *
     * @param UserUpdateRequest
     *            修改请求对象
     * @return UserUpdateResponse 修改响应对象
     */
    @PutMapping(value = "/v1/user/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "修改", notes = "修改", response = UserUpdateResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回", response = UserUpdateResponse.class),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserUpdateResponse.class) })
    UserUpdateResponse modifyByPrimaryKey(@PathVariable("user_id") String userId, @RequestBody UserUpdateRequest userUpdateRequest);

    /**
     * 根据主键查作详情信息
     *
     * @param userId
     *            动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/user/{user_id}")
    @ApiOperation(value = "详情", notes = "根据userId获取动作详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = UserVO.class),
            @ApiResponse(code = 500, message = "内部错误") })
    UserVO findByPrimaryKey(@PathVariable("user_id") String userId);

    /**
     * 根据ldapID查询用户详情
     *
     * @param userId
     *            动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/user/findByLdapId")
    @ApiOperation(value = "详情", notes = "根据userId获取动作详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    UserVO findByLdapId(@RequestParam("ldap_id") String ldapId);

    /**
     * 根据主键查询动作集合信息
     *
     * @param userId
     *            动作主键
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/user/list/{user_id}")
    @ApiOperation(value = "查询", notes = "查询")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
    List<UserVO> listByPrimaryKeyArrays(@PathVariable("user_id") String userIds);

    /**
     * 根据page对象查询监控项
     *
     * @param request
     *            分页查询监控对象
     * @return PageResult<UserResponse> page返回对象
     */
    @PostMapping(value = "/v1/user/queryList")
    @ApiOperation(value = "根据条件查询列表", notes = "根据条件查询列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误") })
    public List<UserDTO> queryList(UserCreateRequest userCreateRequest);

    /**
     * 根据page对象查询监控项
     *
     * @param request
     *            分页查询监控对象
     * @return PageResult<UserResponse> page返回对象
     */
    @PostMapping(value = "/v1/user/pageList")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误") })
    PageResult<UserDTO> pageList(@RequestBody UserPageRequest request);

    /**
     * 根据名称查作详情信息
     *
     * @param code
     *            用户名
     * @return UserVO 动作详情响应对象
     */
    @GetMapping(value = "/v1/user/findByCode")
    @ApiOperation(value = "详情", notes = "根据userId获取动作详情")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = UserVO.class),
            @ApiResponse(code = 500, message = "内部错误") })
    UserVO findByCode(@RequestParam("code") String value);

    @PutMapping(value = "/v1/user/batchModifyDeptId", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量修改部门ID", notes = "批量修改部门ID")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error", response = UserUpdateResponse.class) })
    int batchModifyDeptIdByUserIdArrays(@RequestParam("dept_id") String deptId, @RequestParam("user_ids") String userIds);

    /**
     * 根据部门查询列表根据部门查询列表
     * 
     * @param deptId
     * @return
     */
    @GetMapping(value = "/v1/user/getByDefId")
    @ApiOperation(value = "根据部门查询列表", notes = "根据部门查询列表")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = PageResponse.class),
            @ApiResponse(code = 500, message = "内部错误") })
    public List<UserDTO> getByDefId(@RequestParam("deptId") String deptId);

    @PostMapping(value = "/v1/user/batchInsertDepartmentUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量新增部门-人员关系", notes = "批量新增部门-人员关系")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code = 500, message = "Unexpected error", response = ResponseEntity.class) })
    ResponseEntity<String> batchInsertDepartmentUser(@RequestBody UserBatchCreateRequest userBatchCreateRequest);

    @GetMapping(value = "/v1/user/getAll")
    @ApiOperation(value = "查询全部用戶", notes = "查询全部用戶")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 500, message = "内部错误") })
	List<UserDTO> getAll();

    @GetMapping(value = "/v1/user/findSameDeptUserIdByLdapId/{ldapId}")
    @ApiOperation(value = "根据同部门用户", notes = "根据同部门用户")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误") })
    List<String> findSameDeptUserIdByLdapId(@PathVariable("ldapId") String ldapId);
}
