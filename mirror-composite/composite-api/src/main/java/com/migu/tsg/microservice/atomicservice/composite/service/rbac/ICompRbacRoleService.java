package com.migu.tsg.microservice.atomicservice.composite.service.rbac;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.composite.ErrorsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompRoleCreatePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RbacRoleListItem;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.ResourceRoleList;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleAddParentRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleDetailResponsePer;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RolePageRequestPayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RolePermission;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleUserPayload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* 角色管理相关Controller
* Project Name:composite-service
* File Name:RbacRolesController.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc
* ClassName: RbacRolesController <br/>
* date: 2017年8月24日 下午2:56:56 <br/>
* 角色管理相关Controller
* @author pengguihua
* @version
* @since JDK 1.6
*/
@RequestMapping(value="${version}/roles", produces = "application/json;charset=UTF-8")
@Api(value = "Composite Rbac role service", description = "Composite Rbac role service")
public interface ICompRbacRoleService {
    /**
     * retrieveRoleByName：获取角色详情
     * 作者： yangshilei
     * @param reqCtx
     * @param namespace
     * @param roleName
     * @return
     */
    @ApiOperation(value = "根据用户名获取角色详情", notes = "根据用户名获取角色详情", response = RoleDetailResponse.class,
            tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the role detail data",
            response = RoleDetailResponse.class)})
    @GetMapping(path = "/{namespace}/getRoleByUserName/{user_name}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<RoleDetailResponsePer> retrieveRoleByUserName(@PathVariable("namespace") String namespace,
                                              @PathVariable("user_name") String userName);
    /**
    * 获取功能角色列表 <br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param search
    * @return
    */
    @ApiOperation(value = "获取功能角色列表", notes = "获取功能角色列表",
            response = RoleListResponse.class, tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List roles for the namespace",
    response = RoleListResponse.class)})
    @GetMapping(path = "/{namespace}")
    @ResponseStatus(HttpStatus.OK)
    RoleListResponse rolesList(@PathVariable("namespace") String namespace,
                                @RequestParam(name = "search", required = false) String search,
                                @RequestParam(name = "project_name", required = false) String projectName,
                                @RequestParam(name = "name", required = false) String Name);


    /**
     * 获取功能角色分页列表 <br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param search
     * @return
     */
     @ApiOperation(value = "获取功能角色分页列表", notes = "获取功能角色分页列表",
             response = PageResponse.class, tags = {"Composite Role service API"})
     @ApiResponses(value = {@ApiResponse(code = 200, message = "List roles for the namespace",
     response = PageResponse.class)})
     @PostMapping(path = "/pageList")
     @ResponseStatus(HttpStatus.OK)
    public PageResult<RbacRoleListItem> pageList(@RequestBody RolePageRequestPayload request);
    /**
    * 获取资源角色列表 <br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param search
    * @return
    */
    @ApiOperation(value = "获取资源角色列表", notes = "获取资源角色列表",
            response = RoleListResponse.class, tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List roles for the namespace",
    response = RoleListResponse.class)})
    @GetMapping(path = "/rolesRerouceList")
    @ResponseStatus(HttpStatus.OK)
    RoleListResponse rolesRerouceList(@RequestParam(name = "id", required = false) String id,
    							@RequestParam(name = "name", required = false) String name);

    /**
    * 获取资源角色列表 <br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param search
    * @return
    */
    @ApiOperation(value = "获取资源角色树", notes = "获取资源角色树",
            response = RoleListResponse.class, tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "List roles for the namespace",
    response = RoleListResponse.class)})
    @GetMapping(path = "/rolesRerouceAllList")
    @ResponseStatus(HttpStatus.OK)
    List<ResourceRoleList> rolesRerouceAllList(@RequestParam(name = "id", required = false) String id,
    							@RequestParam(name = "name", required = false) String name);
    /**
     * retrieveRoleByName：获取角色详情
     * 作者： yangshilei
     * @param reqCtx
     * @param namespace
     * @param roleName
     * @return
     */
    @ApiOperation(value = "根据角色名获取角色详情", notes = "根据角色名获取角色详情", response = RoleDetailResponse.class,
            tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the role detail data",
    response = RoleDetailResponse.class)})
    @GetMapping(path = "/{namespace}/{role_name}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    RoleDetailResponse retrieveRoleByName(@PathVariable("namespace") String namespace,
                                                       @PathVariable("role_name") String roleName);
    /**
     * retrieveRoleByName：获取角色详情
     * 作者： yangshilei
     * @param reqCtx
     * @param namespace
     * @param roleName
     * @return
     */
    @ApiOperation(value = "根据角色ID获取角色详情", notes = "根据角色ID获取角色详情", response = RoleDetailResponse.class,
            tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Return the role detail data",
    response = RoleDetailResponse.class)})
    @GetMapping(path = "/detail/{role_id}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    RoleDetailResponsePer retrieveRoleById(@PathVariable("role_id") String roleId);
    /**
     * updateRoleByName：修改角色信息
     * 作者： yangshilei
     * @param reqCtx
     * @param namespace
     * @param role_name
     * @return
     */
    @ApiOperation(value = "修改角色", notes = "修改角色", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Role was modified successfully"),
                           @ApiResponse(code = 403, message = "User doesn't have permission to change role",
                           response = ErrorsResponse.class)})
    @PutMapping(path = "/{namespace}/{role_id}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse updateRoleById(@PathVariable("namespace") String namespace,
                                 @PathVariable("role_id")String roleId,
                                 @RequestBody CompRoleCreatePayload requestRole);

    /**
    * deleteRoleByName:(删除角色). <br/>
    * 作者： yangshilei
    * @param reqCtx
    * @param namespace
    * @param role_name
    */
    @ApiOperation(value = "根据角色名删除角色", notes = "根据角色名删除角色", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Deleted successfully"),
                           @ApiResponse(code = 403, message = "No permission to remove role",
                           response = ErrorsResponse.class),
                           @ApiResponse(code = 409, message = "Role is inherited by other roles and cannot be removed",
                           response = ErrorsResponse.class)})
    @DeleteMapping(path = "/{namespace}/{role_name}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse deleteRoleByName(@PathVariable("namespace") String namespace,
                                 @PathVariable("role_name") String roleName);

    /**
    * deleteRoleByName:(删除角色). <br/>
    * 作者： yangshilei
    * @param reqCtx
    * @param namespace
    * @param role_name
    */
    @ApiOperation(value = "根据角色ID删除角色", notes = "根据角色ID删除角色", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Deleted successfully"),
                           @ApiResponse(code = 403, message = "No permission to remove role",
                           response = ErrorsResponse.class),
                           @ApiResponse(code = 409, message = "Role is inherited by other roles and cannot be removed",
                           response = ErrorsResponse.class)})
    @DeleteMapping(path = "/delete/{namespace}/{role_id}", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse deleteRoleById(@PathVariable("namespace") String namespace,
                                 @PathVariable("role_id") String roleId);
    /**
    * Create a one or more roles for the namespace.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param roleName
    * @param createRoleList
    * @return
    */
    @ApiOperation(value = "新增功能角色", notes = "Create a one or more roles for the namespace",
                  response = CompRoleCreatePayload.class, tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create role(s) successfully.",
    response = CompRoleCreatePayload.class),
                           @ApiResponse(code = 403, message = "Failed to create the role(s)",
                           response = ErrorsResponse.class)})
    @PostMapping(value = "/{namespace}", 
                 consumes = "application/json",
                    produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<CompRoleCreatePayload> createRole(@PathVariable("namespace") String namespace,
                                           @RequestBody CompRoleCreatePayload createRole);
    /**
     * Create a one or more roles for the namespace.<br/>
     *
     * 作者： 曾祥华
     * @param namespace
     * @param roleName
     * @param createRoleList
     * @return
     */
     @ApiOperation(value = "新增资源角色", notes = "Create a one or more roles for the namespace",
                   response = CompRoleCreatePayload.class, tags = {"Composite Role service API"})
     @ApiResponses(value = {@ApiResponse(code = 200, message = "create role(s) successfully.",
     response = CompRoleCreatePayload.class),
                            @ApiResponse(code = 403, message = "Failed to create the role(s)",
                            response = ErrorsResponse.class)})
     @PostMapping(value = "/resource/{namespace}", 
                  consumes = "application/json",
                     produces = "application/json; charset=UTF-8")
     @ResponseStatus(HttpStatus.OK)
     List<CompRoleCreatePayload> createResourceRole(@PathVariable("namespace") String namespace,
                                            @RequestBody CompRoleCreatePayload createRole);
    /**
    * add parent for the Role. <br/>
    *
    * 作者： pengguihua
    * @param roleName
    */
    @ApiOperation(value = "挂接父角色", notes = "挂接父角色", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Parent role added successfully"),
                           @ApiResponse(code = 403, message = "Failed to add parent role.",
                           response = ErrorsResponse.class),
                           @ApiResponse(code = 404, message = "Parent role not found",
                           response = ErrorsResponse.class)})
    @PostMapping("/{namespace}/{role_name}/parents")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse addParent4Role(@PathVariable("namespace") String namespace,
                               @PathVariable("role_name") String roleName,
                               @RequestBody RoleAddParentRequest reqBody);
    /**
    * Remove one parent instance.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param roleName
    * @param parentUuid
    */
    @ApiOperation(value = "移除父角色", notes = "移除父角色", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Parent role removed successfully"),
                           @ApiResponse(code = 403, message = "Failed to remove parent role.",
                           response = ErrorsResponse.class),
                           @ApiResponse(code = 404, message = "Parent role is not in the role.",
                           response = ErrorsResponse.class)})
    @DeleteMapping("/{namespace}/{role_name}/parents/{parent_uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse removeParent4Role(@PathVariable("namespace") String namespace,
                                  @PathVariable("role_name") String roleName,
                                  @PathVariable("parent_uuid") String parentUuid);
    /**
    * addPermission:(角色新增权限). <br/>
    * 作者： yangshilei
    * @param reqCtx
    * @param namespace
    * @param roleName
    * @return
    */
    @ApiOperation(value = "角色新增权限", notes = "角色新增权限", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Permission added")})
    @PostMapping("/{namespace}/{role_name}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    RolePermission addPermission(@PathVariable("namespace") String namespace,
                              @PathVariable("role_name") String roleName,
                              @RequestBody RolePermission requestPermission);
    /**
    * Remove a permission from a role.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param roleName
    * @param permissionUuid
    * @param requestPermission
    */
    @ApiOperation(value = "从角色中删除权限", notes = "从角色中删除权限", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Permission removed successfully"),
                           @ApiResponse(code = 404, message = "Permission not found.")})
    @DeleteMapping("/{namespace}/{role_name}/permissions/{permission_uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse removePermissionFromRole(@PathVariable("namespace") String namespace,
                                         @PathVariable("role_name") String roleName,
                                         @PathVariable("permission_uuid") String permissionUuid);
    /**
    * List users that belong to a role.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param roleName
    * @return
    */
    @ApiOperation(value = "获取角色用户列表", notes = "获取角色用户列表",
            response = RoleUserPayload.class, tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "", response = RoleUserPayload.class)})
    @GetMapping(path = "/{namespace}/{role_name}/users", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<RoleUserPayload> listRoleUsers(@PathVariable("namespace") String namespace,
                                               @PathVariable("role_name") String roleName);
    /**
    * 为角色添加用户.<br/>
    *
    * 作者： pengguihua
    * @param namespace
    * @param roleName
    * @param roleUserList
    * @return
    */
    @ApiOperation(value = "为角色添加用户", notes = "为角色添加用户", response = RoleUserPayload.class,
            tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Assigned successfully",
            response = RoleUserPayload.class),
    @ApiResponse(code = 400, message = "Validation failed because one of the users already have role",
            response = ErrorsResponse.class)})
    @PostMapping(path = "/{namespace}/{role_name}/users", produces = "application/json; charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    List<RoleUserPayload> assignRoleUsers(@PathVariable("namespace") String namespace,
                                              @PathVariable("role_name") String roleName,
                                              @RequestBody List<RoleUserPayload> roleUserList);
    /**
    * Revoke a role from one or more users.<br/>
    * @param namespace
    * @param roleName
    * @param roleUserList
    * 作者： pengguihua
    */
    @ApiOperation(value = "从角色中移除用户", notes = "从角色中移除用户", tags = {"Composite Role service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "All revoked successfully"),
                           @ApiResponse(code = 404,
                           message = "Validation failed because one of the users dont have the role",
                           response = ErrorsResponse.class)})
    @DeleteMapping(path = "/{namespace}/{role_name}/users", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    BaseResponse revokeRoleUsers(@PathVariable("namespace") String namespace,
                                @PathVariable("role_name") String roleName,
                                @RequestBody List<RoleUserPayload> roleUserList);
    /**
     * update a permission from a role.<br/>
     * 作者： yangshilei
     * @param namespace
     * @param roleName
     * @param permissionUuid
     * @param requestPermission
     */
     @ApiOperation(value = "从角色中修改权限", notes = "从角色中修改权限", tags = {"Composite Role service API"})
     @ApiResponses(value = {@ApiResponse(code = 204, message = "Permission updated successfully"),
                            @ApiResponse(code = 404, message = "Permission not found.")})
     @PutMapping("/{namespace}/{role_name}/permissions/{permission_uuid}")
     @ResponseStatus(HttpStatus.NO_CONTENT)
     BaseResponse updatePermissionFromRole(@PathVariable("namespace") String namespace,
                                          @PathVariable("role_name") String roleName,
                                          @PathVariable("permission_uuid") String permissionUuid,
                                          @RequestBody RolePermission requestPermission);


}
