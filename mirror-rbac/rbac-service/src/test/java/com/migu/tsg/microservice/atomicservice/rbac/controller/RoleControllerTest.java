/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesAssignedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesAssignedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesRevokeRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentsDTO;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
* 类名称: RoleControllerTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日下午7:14:38 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class RoleControllerTest {

    @Autowired
    private RoleController roleController;

    @Autowired
    private OrgsController orgsController;

    /**
     * 根账号(LDAP初始化必须存在此NAMESPACE)
     * 提示：目前LDAP初始化必须存在此NAMESPACE,后期可以实现ldap对于NAMESPACE的新增和删除接口,
     *      单元测试前(@Before)新增NAMESPACE,单元测试后(@After)删除NAMESPACE
     */
    private static final String NAMESPACE = "migu";

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRoles(java.util.List, java.lang.String)}.
     */
    @Test
    public final void testListRoles() {
        String roleUuid = insertRoles().get(0).getUuid();
        roleController.listRoles(Arrays.asList(roleUuid), null, roleUuid, null, roleUuid);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRoles(java.util.List)}.
     */
    @Test
    public final void testInsertRoles() {
        String json = "[{\"name\":\"test-insert-role-parent-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        List<InsertRoleRequest> request = new Gson().fromJson(json, new TypeToken<List<InsertRoleRequest>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
        roleController.insertRoles(request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#getRoleDetail(java.lang.String)}.
     */
    @Test
    public final void testGetRoleDetail() {
        String roleUuid = insertRoles().get(0).getUuid();
        roleController.getRoleDetail(roleUuid);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRole(java.lang.String, com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest)}.
     */
    @Test
    public final void testModifyRole() {
        String roleUuid = insertRoles().get(0).getUuid();
        String json = "{\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"*:*\"],\"constraints\":[{}]}]}";
        ModifyRoleRequest request = new Gson().fromJson(json, ModifyRoleRequest.class);
        List<RoleParentsDTO> parents = new ArrayList<>();
        RoleParentsDTO rp = new RoleParentsDTO();
        BeanUtils.copyProperties(insertRolesParent().get(0), rp);
        parents.add(rp);
        request.setParents(parents);
        roleController.modifyRole(roleUuid, request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRole(java.lang.String)}.
     */
    @Test
    public final void testDeleteRole() {
        String roleUuid = insertRoles().get(0).getUuid();
        roleController.deleteRole(roleUuid);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertParentRole(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest, java.lang.String)}.
     */
    @Test
    public final void testInsertParentRole() {
        String roleUuid = insertRoles().get(0).getUuid();
        InsertRoleResponse insertRoleResponse = insertRolesParent().get(0);
        InsertParentRoleRequest request = new InsertParentRoleRequest();
        BeanUtils.copyProperties(insertRoleResponse, request);
        roleController.insertParentRole(request, roleUuid);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteParentRole(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testDeleteParentRole() {
        InsertRoleResponse irr = saveRoles().get(0);
        roleController.deleteParentRole(irr.getUuid(), irr.getParents().get(0).getUuid());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String)}.
     */
    @Test
    public final void testInsertRolePermission() {
        insertRolePermission();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testModifyRolePermission() {
        Map<String, String> map = insertRolePermission();
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        InsertRolePermissionsRequest request = new Gson().fromJson(json, InsertRolePermissionsRequest.class);
        roleController.modifyRolePermission(request, map.get("roleUuid"), map.get("permissionUuid"));
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRolePermission(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testDeleteRolePermission() {
        Map<String, String> map = insertRolePermission();
        roleController.deleteRolePermission(map.get("roleUuid"), map.get("permissionUuid"));
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRolesAssigned(java.lang.String, java.util.List, java.lang.String)}.
     */
    @Test
    public final void testListRolesAssigned() {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        roleController.listRolesAssigned(rolesAssigned.get(0).getNamespace(),
                Arrays.asList(rolesAssigned.get(0).getRoleUuid()), rolesAssigned.get(0).getUser());
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesAssigned(java.util.List)}.
     */
    @Test
    public final void testRolesAssigned() {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevoke(java.util.List)}.
     */
    @Test
    public final void testRolesRevoke() {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        List<RolesRevokeRequest> request = rolesAssigned.stream().map(rar -> {
            RolesRevokeRequest rrr = new RolesRevokeRequest();
            BeanUtils.copyProperties(rar, rrr);
            return rrr;
        }).collect(Collectors.toList());
        roleController.rolesRevoke(request);
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevokeAll(java.lang.String, java.util.List)}.
     */
    @Test
    public final void testRolesRevokeAll() {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        List<String> request = rolesAssigned.stream().map(RolesAssignedResponse::getRoleName)
                .collect(Collectors.toList());
        roleController.rolesRevokeAll(rolesAssigned.get(0).getNamespace(), request);
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * 新增角色(关联父角色)
     * @return
     */
    private List<InsertRoleResponse> saveRoles() {
        String json = "[{\"name\":\"test-insert-role-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        List<InsertRoleRequest> request = new Gson().fromJson(json, new TypeToken<List<InsertRoleRequest>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
        List<RoleParentsDTO> parents = new ArrayList<>();
        RoleParentsDTO rp = new RoleParentsDTO();
        BeanUtils.copyProperties(insertRolesParent().get(0), rp);
        parents.add(rp);
        request.get(0).setParents(parents);
        return roleController.insertRoles(request);
    }

    /**
     * 新增角色
     * @return
     */
    private List<InsertRoleResponse> insertRoles() {
        String json = "[{\"name\":\"test-insert-role-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        List<InsertRoleRequest> request = new Gson().fromJson(json, new TypeToken<List<InsertRoleRequest>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
        return roleController.insertRoles(request);
    }

    /**
     * 新增父角色
     * @return
     */
    private List<InsertRoleResponse> insertRolesParent() {
        String json = "[{\"name\":\"test-insert-role-parent-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        List<InsertRoleRequest> request = new Gson().fromJson(json, new TypeToken<List<InsertRoleRequest>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
        return roleController.insertRoles(request);
    }

    /**
     * 新增角色权限
     * @return
     */
    private Map<String, String> insertRolePermission() {
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        String roleUuid = insertRoles().get(0).getUuid();
        InsertRolePermissionsRequest request = new Gson().fromJson(json, InsertRolePermissionsRequest.class);
        InsertRolePermissionsResponse insertRolePermission = roleController.insertRolePermission(request,
                roleUuid);
        Map<String, String> map = new HashMap<>();
        map.put("permissionUuid", insertRolePermission.getUuid());
        map.put("roleUuid", roleUuid);
        return map;

    }

    /**
     * 给用户分配角色
     * @return 
     */
    private List<RolesAssignedResponse> rolesAssigned() {
        InsertRoleResponse irr = insertRoles().get(0);
        String username = createOrgAccount().get(0).getUsername();
        String json = "[{\"user\":\"" + username + "\",\"namespace\":\"" + NAMESPACE + "\"}]";
        List<RolesAssignedRequest> request = new Gson().fromJson(json,
                new TypeToken<List<RolesAssignedRequest>>() {
                    private static final long serialVersionUID = 705186757536944969L;
                }.getType());
        request.get(0).setRoleUuid(irr.getUuid());
        request.get(0).setRoleName(irr.getName());
        return roleController.rolesAssigned(request);
    }

    /**
     * 新增成员
     * @return
     */
    private List<CreateOrgAccountResponse> createOrgAccount() {
        CreateOrgAccountRequest request = new CreateOrgAccountRequest();
//        request.setAccounts(Arrays.asList(new InsertAccountDTO(UUID.randomUUID().toString(),
//                "325056665@qq.com", null, null, Arrays.asList("B1", "B2"))));
        return orgsController.createOrgAccount(request, NAMESPACE);
    }

    /**
     * 删除成员
     * @param username 成员名称
     */
    private void removeOrgAccount(final String username) {
        orgsController.removeOrgAccount(NAMESPACE, username);
    }

}
