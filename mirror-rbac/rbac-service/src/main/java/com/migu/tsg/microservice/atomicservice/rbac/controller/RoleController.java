package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.common.util.FieldUtil;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.GetRoleDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListRolesAssignedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListRolesResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolePageRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesAssignedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesAssignedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesRevokeRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.GetRoleDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRolePermissionsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesAssignedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolesAssignedRevokeDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UpdateRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.RoleService;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: RoleController.java <br>
 * 类描述: 【RBAC原子层】角色控制层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:19:59 <br>
 * 版本: v1.0
 */
@RestController
public class RoleController implements RoleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RoleBiz roleBiz;


    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return 响应对象
     */
    @ResultCode("105010401")
    public List<ListRolesResponse> listRoles(@RequestParam(value = "uuids", required = false) final List<String> uuids,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "namespace", required = false) final String namespace,
            @RequestParam(value = "roleType", required = false) final Integer roleType,
            @RequestParam(value = "username", required = false) final String username) {
        List<ListRolesDTO> listRoles = roleBiz.listRoles(null==uuids?null:uuids.toArray(new String[] {}), name,namespace,roleType,username);
        List<ListRolesResponse> response = listRoles.stream().map(e -> {
            ListRolesResponse lrr = new ListRolesResponse();
            BeanUtils.copyProperties(e, lrr);
            return lrr;
        }).collect(Collectors.toList());
        return response;
    }

    /**
    * 根据查询条件获取分页列表获取分页列表
    * @param request 分页查询监控对象
    * @return
    */
    @Override
    public PageResult<ListRolesResponse> pageList(@RequestBody RolePageRequest request) {
        if (null == request) {
            LOGGER.warn("pageList param templatePageRequest is null");
            return null;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageSize(request.getPageSize());
        pageRequest.setPageNo(request.getPageNo());
        Map<String, Object> map = FieldUtil.getFiledMap(request);
        for (String key : map.keySet()) {
            pageRequest.addFields(key, map.get(key));
        }
        
        //查询条件，可自己增加PageRequest中参数
        /** pageRequest.addFields("precinctId", request.getPrecinctId());
        pageRequest.addFields("precinctName", request.getPrecinctName());
        pageRequest.addFields("precinctKind", request.getPrecinctKind());
        pageRequest.addFields("lscId", request.getLscId());
        pageRequest.addFields("areaCode", request.getAreaCode());*/
        PageResult<ListRolesResponse> RoleDTOPageResponse = roleBiz.pageList(pageRequest);
        return RoleDTOPageResponse;
     }
    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return 响应对象
     */
    @ResultCode("105010401")
    public List<ListRolesResponse> childListRoles(@RequestParam(value = "uuids", required = false) final List<String> uuids,
            @RequestParam(value = "name", required = false) final String name,
            @RequestParam(value = "namespace", required = false) final String namespace,
            @RequestParam(value = "roleType", required = false) final Integer roleType) {
        List<ListRolesDTO> listRoles = roleBiz.childListRoles(null==uuids?null:uuids.toArray(new String[] {}), name,namespace,roleType);
        List<ListRolesResponse> response = listRoles.stream().map(e -> {
            ListRolesResponse lrr = new ListRolesResponse();
            BeanUtils.copyProperties(e, lrr);
            return lrr;
        }).collect(Collectors.toList());
        return response;
    }

    /**
     * 新增多个角色
     * @param request 请求参数
     * @return 响应对象
     */
    @ResultCode("105010402")
    public List<InsertRoleResponse> insertRoles(@RequestBody final List<InsertRoleRequest> request) {
    	List<String> resource=new ArrayList<>();
    	resource.add("#");
        List<InsertRoleDTO> irdtoList = request.stream().map(input -> {
            InsertRoleDTO irdto = new InsertRoleDTO();
            BeanUtils.copyProperties(input, irdto);
            if (CollectionUtils.isNotEmpty(input.getPermissions())) {
                List<PermissionDTO> permissionDTO = input.getPermissions().stream().map(permissions -> {
                    PermissionDTO pdto = new PermissionDTO();
                    BeanUtils.copyProperties(permissions, pdto);
                    if(CollectionUtils.isEmpty(pdto.getResource())) {
                    	pdto.setResource(resource);
                    }
                    return pdto;
                }).collect(Collectors.toList());
                irdto.setPermissions(permissionDTO);
            }
            return irdto;
        }).collect(Collectors.toList());
        List<InsertRoleDTO> result = roleBiz.insertRoles(irdtoList);
        List<InsertRoleResponse> response = result.stream().map(input -> {
            InsertRoleResponse irr = new InsertRoleResponse();
            BeanUtils.copyProperties(input, irr);
            return irr;
        }).collect(Collectors.toList());
        return response;
    }

    /**
     * 查询单个角色详细信息
     * @param roleUuid 角色UUID
     * @return 响应对象
     */
    @ResultCode("105010403")
    public GetRoleDetailResponse getRoleDetail(@PathVariable("role_uuid") final String roleUuid) {
        GetRoleDetailDTO roleDetail = roleBiz.getRoleDetail(roleUuid);
        GetRoleDetailResponse response = new GetRoleDetailResponse();
        BeanUtils.copyProperties(roleDetail, response);
        return response;
    }

    /**
     * 修改单个角色
     * @param roleUuid 角色UUID
     * @param request 请求对象
     */
    @ResultCode("105010404")
    public void modifyRole(@PathVariable("role_uuid") final String roleUuid,
            @RequestBody final ModifyRoleRequest request) {
    	UpdateRoleDTO dto=new UpdateRoleDTO();
    	BeanUtils.copyProperties(request, dto);
    	dto.setUuid(roleUuid);
        roleBiz.modifyRole(dto);
    }

    /**
     * 通过角色UUID删除角色
     * @param roleUuid 角色UUID
     */
    @ResultCode("105010405")
    public void deleteRole(@PathVariable("role_uuid") final String roleUuid) {
        roleBiz.deleteRole(roleUuid);
    }

    /**
     * 新增单个父角色
     * @param request 请求对象
     * @param roleUuid 角色UUID
     */
    @ResultCode("105010406")
    public void insertParentRole(@RequestBody final InsertParentRoleRequest request,
            @PathVariable("role_uuid") final String roleUuid) {
        roleBiz.insertParentRole(roleUuid, request.getUuid(), request.getName());
    }

    /**
     * @param roleUuid 角色UUID
     * @param parentUuid 父角色UUID
     */
    @ResultCode("105010407")
    public void deleteParentRole(@PathVariable("role_uuid") final String roleUuid,
            @PathVariable("parent_uuid") final String parentUuid) {
        roleBiz.deleteParentRole(roleUuid, parentUuid);
    }

    /**
     * 新增角色单个权限
     * @param request 请求对象
     * @param roleUuid 角色UUID
     * @return 响应对象
     */
    @ResultCode("105010408")
    public InsertRolePermissionsResponse insertRolePermission(@RequestBody final InsertRolePermissionsRequest request,
            @PathVariable("role_uuid") final String roleUuid) {
        InsertRolePermissionsResponse response = new InsertRolePermissionsResponse();
        InsertRolePermissionsDTO dto = roleBiz.insertRolePermission(roleUuid, request.getActions(),
                request.getResource(), request.getConstraints());
        BeanUtils.copyProperties(dto, response);
        return response;
    }

    /**
     * 修改角色单个权限
     * @param request 请求对象
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    @ResultCode("105010409")
    public void modifyRolePermission(@RequestBody final InsertRolePermissionsRequest request,
            @PathVariable("role_uuid") final String roleUuid,
            @PathVariable("permission_uuid") final String permissionUuid) {
        roleBiz.modifyRolePermission(roleUuid, permissionUuid, request.getActions(), request.getResource(),
                request.getConstraints());
    }

    /**
     * 删除角色单个权限
     * @param roleUuid 角色UUID
     * @param permissionUuid 权限UUID
     */
    @ResultCode("105010410")
    public void deleteRolePermission(@PathVariable("role_uuid") final String roleUuid,
            @PathVariable("permission_uuid") final String permissionUuid) {
        roleBiz.deleteRolePermission(roleUuid, permissionUuid);
    }

    /**
     * 查询已经分配给用户的角色列表OR已经分配给角色的用户列表
     * @param namespace 空间名称 required (string) filter roles by namespace
     * @param roleUuid 角色UUID (string) role uuid which
     * @param user 用户名称 (string) filter roles by related user
     * @return 响应对象
     */
    @ResultCode("105010411")
    public List<ListRolesAssignedResponse> listRolesAssigned(@RequestParam("namespace") final String namespace,
            @RequestParam(value = "role_uuid", required = false) final List<String> roleUuid,
            @RequestParam(value = "username", required = false) final String user) {
        List<ListRolesAssignedDTO> listRolesAssigned = roleBiz.listRolesAssigned(namespace,
                roleUuid == null ? null : roleUuid.toArray(new String[] {}), user);
        List<ListRolesAssignedResponse> response = listRolesAssigned.stream().map(input -> {
            ListRolesAssignedResponse lrar = new ListRolesAssignedResponse();
            BeanUtils.copyProperties(input, lrar);
            return lrar;
        }).collect(Collectors.toList());
        return response;
    }

    /**
     * 分配一个或多个角色给一个或者多个用户
     * @param request 请求参数对象
     */
    @ResultCode("105010412")
    public List<RolesAssignedResponse> rolesAssigned(@RequestBody final List<RolesAssignedRequest> request) {
        List<RolesAssignedRevokeDTO> dtoList = request.stream().map(input -> {
            RolesAssignedRevokeDTO dto = new RolesAssignedRevokeDTO();
            BeanUtils.copyProperties(input, dto);
            return dto;
        }).collect(Collectors.toList());
        List<RolesAssignedRevokeDTO> rolesAssigned = roleBiz.rolesAssigned(dtoList);
        List<RolesAssignedResponse> response = rolesAssigned.stream().map(input -> {
            RolesAssignedResponse rar = new RolesAssignedResponse();
            BeanUtils.copyProperties(input, rar);
            return rar;
        }).collect(Collectors.toList());
        return response;
    }

    /**
     * 撤销一个或多个用户的一个或多个角色
     * @param request 请求参数对象
     */
    @ResultCode("105010413")
    public void rolesRevoke(@RequestBody final List<RolesRevokeRequest> request) {
        List<RolesAssignedRevokeDTO> dtoList = request.stream().map(input -> {
            RolesAssignedRevokeDTO dto = new RolesAssignedRevokeDTO();
            BeanUtils.copyProperties(input, dto);
            return dto;
        }).collect(Collectors.toList());
        roleBiz.rolesRevoke(dtoList);
    }

    /**
     * 撤销用户组的所有角色
     * @param namespace 空间名称/根账号
     * @param request 请求参数对象
     */
    @ResultCode("105010414")
    public void rolesRevokeAll(@PathVariable("namespace") final String namespace,
            @RequestBody final List<String> request) {
        roleBiz.rolesRevokeAll(namespace, request);
    }

    @Override
    public Map<String, String[]> getOperRoleIdByUserName(@PathVariable("namespace") String namespace, @PathVariable("user_name") String userName) {
        Map<String, String[]> map = Maps.newHashMap();
        map.put("ids", roleBiz.getOperRoleIdByUserName(namespace, userName));
        return map;
    }

}
