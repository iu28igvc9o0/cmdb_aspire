package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.service <br>
 * 类名称: RoleService.java <br>
 * 类描述: 【RBAC原子层】角色服务 API接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:16:38 <br>
 * 版本: v1.0
 */
@Api(tags = "roles", description = "角色管理和分配端点")
public interface RoleService {

    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称(空间名称),通过相关的用户名过滤角色列表
     * @return 响应对象
     */
    @ApiOperation("查询角色列表")
    @GetMapping(value = "/v1/roles/instances", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ListRolesResponse> listRoles(@RequestParam("uuids") List<String> uuids,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "namespace", required = false) final String namespace,
            @RequestParam(value = "roleType", required = false) final Integer roleType,
            @RequestParam(value = "username", required = false) final String username);
    

    @ApiOperation("查询分页角色列表")
    @PostMapping(value = "/v1/roles/pageList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public PageResult<ListRolesResponse> pageList(@RequestBody RolePageRequest request);
    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称(空间名称),通过相关的用户名过滤角色列表
     * @return 响应对象
     */
    @ApiOperation("查询子级角色列表")
    @GetMapping(value = "/v1/roles/childListRoles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ListRolesResponse> childListRoles(@RequestParam(name = "uuids", required = false) List<String> uuids,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "namespace", required = false) final String namespace,
            @RequestParam(value = "roleType", required = false) final Integer roleType);

    /**
     * 新增多个角色
     * @param request 请求参数
     * @return 角色UUIDs
     */
    @ApiOperation("新增多个角色")
    @PostMapping(value = "/v1/roles/instances", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<InsertRoleResponse> insertRoles(@RequestBody List<InsertRoleRequest> request);

    /**
     * 查询单个角色详细信息
     * @param roleUuid 角色UUID
     * @return 响应对象
     */
    @ApiOperation("查询角色详细信息")
    @GetMapping(value = "/v1/roles/instances/{role_uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GetRoleDetailResponse getRoleDetail(@PathVariable("role_uuid") String roleUuid);

    /**
     * 修改单个角色
     * @param roleUuid 请求参数
     * @param request 修改角色请求
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("修改单个角色")
    @PutMapping(value = "/v1/roles/instances/{role_uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void modifyRole(@PathVariable("role_uuid") String roleUuid,
            @RequestBody ModifyRoleRequest request);

    /**
     * 通过角色UUID删除角色
     * @param roleUuid 角色UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除单个角色")
    @DeleteMapping(value = "/v1/roles/instances/{role_uuid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRole(@PathVariable("role_uuid") String roleUuid);

    /**
     * 新增单个父角色
     * @param request 请求参数
     * @param roleUuid 角色UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("新增单个父角色")
    @PostMapping(value = "/v1/roles/instances/{role_uuid}/parents",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void insertParentRole(@RequestBody InsertParentRoleRequest request,
            @PathVariable("role_uuid") String roleUuid);

    /**
     * 删除单个父角色
     * @param roleUuid 空间名
     * @param parentUuid 父角色UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除单个父角色")
    @DeleteMapping(value = "/v1/roles/instances/{role_uuid}/parents/{parent_uuid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteParentRole(@PathVariable("role_uuid") String roleUuid,
            @PathVariable("parent_uuid") String parentUuid);

    /**
     * 新增角色单个权限
     * @param request 请求对象
     * @param roleUuid 角色UUID
     * @return 响应对象
     */
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("新增角色单个权限")
    @PostMapping(value = "/v1/roles/instances/{role_uuid}/permissions",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public InsertRolePermissionsResponse insertRolePermission(
            @RequestBody InsertRolePermissionsRequest request, @PathVariable("role_uuid") String roleUuid);

    /**
     * 修改角色单个权限
     * @param request 请求对象
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("修改角色单个权限")
    @PutMapping(value = "/v1/roles/instances/{role_uuid}/permissions/{permission_uuid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void modifyRolePermission(@RequestBody InsertRolePermissionsRequest request,
            @PathVariable("role_uuid") String roleUuid,
            @PathVariable("permission_uuid") String permissionUuid);

    /**
     * 删除角色单个权限
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除角色权限")
    @DeleteMapping(value = "/v1/roles/instances/{role_uuid}/permissions/{permission_uuid}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteRolePermission(@PathVariable("role_uuid") String roleUuid,
            @PathVariable("permission_uuid") String permissionUuid);

    /**
     * 查询已经分配给用户的角色列表OR已经分配给角色的用户列表
     * @param namespace 空间名称 required (string) filter roles by namespace
     * @param roleUuid 角色UUID (string) role uuid which
     * @param user 用户名称 (string) filter roles by related user
     * @return 响应对象
     */
    @ApiOperation("查询已经分配给用户的角色列表OR已经分配给角色的用户列表")
    @GetMapping(value = "/v1/roles/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ListRolesAssignedResponse> listRolesAssigned(@RequestParam("namespace") String namespace,
            @RequestParam(value = "role_uuid", required = false) List<String> roleUuid,
            @RequestParam(value = "username", required = false) String user);

    /**
     * 分配一个或多个角色给一个或者多个用户
     * @param request 请求参数对象
     */
    @ApiOperation("分配一个或多个角色给一个或者多个用户")
    @PostMapping(value = "/v1/roles/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RolesAssignedResponse> rolesAssigned(@RequestBody List<RolesAssignedRequest> request);

    /**
     * 撤销一个或多个用户的一个或多个角色
     * @param request 请求参数对象
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("撤销一个或多个用户的一个或多个角色")
    @DeleteMapping(value = "/v1/roles/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void rolesRevoke(@RequestBody List<RolesRevokeRequest> request);

    /**
     * 撤销用户组的所有角色
     * @param namespace namespace
     * @param request 请求参数对象
     */
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation("撤销用户组的所有角色")
    @DeleteMapping(value = "/v1/roles/users/{namespace}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void rolesRevokeAll(@PathVariable("namespace") String namespace,
            @RequestBody List<String> request);

    @ApiOperation("查询角色详细信息")
    @GetMapping(value = "/v1/roles/{namespace}/{user_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, String[]> getOperRoleIdByUserName(@PathVariable("namespace") String namespace, @PathVariable("user_name") String userName);
}
