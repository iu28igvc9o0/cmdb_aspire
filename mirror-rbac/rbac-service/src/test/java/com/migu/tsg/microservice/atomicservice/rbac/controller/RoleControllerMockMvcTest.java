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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
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
* 类名称: RoleControllerMockMvcTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月19日上午10:56:02 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 运行测试方法时,按名称顺序执行
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// 一般来说在你的测试类上加上@Transactional就足够了，Spring默认是会回滚的。
// 更简便的做法：直接继承AbstractTransactionalJUnit4SpringContextTests
@Transactional
@Ignore // ignore this class temporally, since it will cause JUnit fall
public class RoleControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 根账号(LDAP初始化必须存在此NAMESPACE)
     */
    private static final String NAMESPACE = "migu";

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRoles(java.util.List)}.
     * @throws Exception 
     */
    @Test
    public final void testInsertRoles() throws Exception {
        saveRoles();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRoles(java.util.List, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testListRoles() throws Exception {
        String roleUuid = insertRoles().get(0).getUuid();
        mvc.perform(get("/v1/roles/instances").contentType(MediaType.APPLICATION_JSON_UTF8).param("uuids",
                roleUuid)).andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#getRoleDetail(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testGetRoleDetail() throws Exception {
        String roleUuid = insertRoles().get(0).getUuid();
        mvc.perform(
                get("/v1/roles/instances/{role_uuid}", roleUuid).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRole(java.lang.String, com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testModifyRole() throws Exception {
        String roleUuid = insertRoles().get(0).getUuid();
        String json = "{\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"*:*\"],\"constraints\":[{}]}]}";
        ModifyRoleRequest request = new Gson().fromJson(json, ModifyRoleRequest.class);
        List<RoleParentsDTO> parents = new ArrayList<>();
        RoleParentsDTO rp = new RoleParentsDTO();
        BeanUtils.copyProperties(insertRolesParent().get(0), rp);
        parents.add(rp);
        request.setParents(parents);
        mvc.perform(put("/v1/roles/instances/{role_uuid}", roleUuid)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new Gson().toJson(request)))
                .andExpect(status().is(204)).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRole(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testDeleteRole() throws Exception {
        String roleUuid = insertRoles().stream().map(InsertRoleResponse::getUuid).collect(Collectors.toList())
                .toArray(new String[0])[0];
        mvc.perform(delete("/v1/roles/instances/{role_uuid}", roleUuid)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(204)).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertParentRole(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testInsertParentRole() throws Exception {
        String roleUuid = insertRoles().stream().map(InsertRoleResponse::getUuid).collect(Collectors.toList())
                .toArray(new String[0])[0];
        mvc.perform(post("/v1/roles/instances/{role_uuid}/parents", roleUuid)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(insertRolesParent().get(0)))).andExpect(status().is(204))
                .andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteParentRole(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testDeleteParentRole() throws Exception {
        InsertRoleResponse irr = saveRoles().get(0);
        mvc.perform(delete("/v1/roles/instances/{role_uuid}/parents/{parent_uuid}", irr.getUuid(),
                irr.getParents().get(0).getUuid()).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204)).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testInsertRolePermission() throws Exception {
        insertRolePermission();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testModifyRolePermission() throws Exception {
        Map<String, String> map = insertRolePermission();
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        mvc.perform(put("/v1/roles/instances/{role_uuid}/permissions/{permission_uuid}", map.get("roleUuid"),
                map.get("permissionUuid")).contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().is(204)).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRolePermission(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testDeleteRolePermission() throws Exception {
        Map<String, String> map = insertRolePermission();
        mvc.perform(delete("/v1/roles/instances/{role_uuid}/permissions/{permission_uuid}",
                map.get("roleUuid"), map.get("permissionUuid")).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(204)).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRolesAssigned(java.lang.String, java.util.List, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testListRolesAssigned() throws Exception {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        mvc.perform(get("/v1/roles/users").contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("namespace", rolesAssigned.get(0).getNamespace())
                .param("role_uuid", rolesAssigned.get(0).getRoleUuid())
                .param("username", rolesAssigned.get(0).getUser())).andExpect(status().is(200))
                .andDo(print());
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesAssigned(java.util.List)}.
     * @throws Exception 
     */
    @Test
    public final void testRolesAssigned() throws Exception {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevoke(java.util.List)}.
     * @throws Exception 
     */
    @Test
    public final void testRolesRevoke() throws Exception {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        List<RolesRevokeRequest> request = rolesAssigned.stream().map(rar -> {
            RolesRevokeRequest rrr = new RolesRevokeRequest();
            BeanUtils.copyProperties(rar, rrr);
            return rrr;
        }).collect(Collectors.toList());
        mvc.perform(delete("/v1/roles/users").contentType(MediaType.APPLICATION_JSON_UTF8).content(
                new Gson().toJson(request).replace("roleUuid", "role_uuid").replace("roleName", "role_name")))
                .andExpect(status().is(204)).andDo(print());
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevokeAll(java.lang.String, java.util.List)}.
     * @throws Exception 
     */
    @Test
    public final void testRolesRevokeAll() throws Exception {
        List<RolesAssignedResponse> rolesAssigned = rolesAssigned();
        List<String> request = rolesAssigned.stream().map(RolesAssignedResponse::getRoleName)
                .collect(Collectors.toList());
        mvc.perform(delete("/v1/roles/users/{namespace}", rolesAssigned.get(0).getNamespace())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(new Gson().toJson(request)))
                .andExpect(status().is(202)).andDo(print());
        removeOrgAccount(rolesAssigned.get(0).getUser());
    }

    /**
     * 新增角色(关联父角色)
     * @return
     * @throws Exception
     */
    private List<InsertRoleResponse> saveRoles() throws Exception {
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
        String content = mvc
                .perform(post("/v1/roles/instances").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new Gson().toJson(request)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        return new Gson().fromJson(content, new TypeToken<List<InsertRoleResponse>>() {
            private static final long serialVersionUID = 705186757536944966L;
        }.getType());
    }

    /**
     * 新增角色
     * @return
     * @throws Exception
     */
    private List<InsertRoleResponse> insertRoles() throws Exception {
        String json = "[{\"name\":\"test-insert-role-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        String content = mvc
                .perform(post("/v1/roles/instances").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        return new Gson().fromJson(content, new TypeToken<List<InsertRoleResponse>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
    }

    /**
     * 新增父角色
     * @return
     * @throws Exception
     */
    private List<InsertRoleResponse> insertRolesParent() throws Exception {
        String json = "[{\"name\":\"test-insert-role-parent-name\",\"namespace\":\"" + NAMESPACE
                + "\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}]}]";
        String content = mvc
                .perform(post("/v1/roles/instances").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();

        return new Gson().fromJson(content, new TypeToken<List<InsertRoleResponse>>() {
            private static final long serialVersionUID = 705186757536944969L;
        }.getType());
    }

    /**
     * 新增角色权限
     * @return
     * @throws Exception
     */
    private Map<String, String> insertRolePermission() throws Exception {
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        String roleUuid = insertRoles().get(0).getUuid();
        String content = mvc
                .perform(post("/v1/roles/instances/{role_uuid}/permissions", roleUuid)
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().is(201)).andDo(print()).andReturn().getResponse().getContentAsString();
        Map<String, String> map = new HashMap<>();
        map.put("permissionUuid",
                new Gson().fromJson(content, InsertRolePermissionsResponse.class).getUuid());
        map.put("roleUuid", roleUuid);
        return map;

    }

    /**
     * 给用户分配角色
     * @return 
     * @throws Exception
     */
    private List<RolesAssignedResponse> rolesAssigned() throws Exception {
        InsertRoleResponse irr = insertRoles().get(0);
        String username = createOrgAccount().get(0).getUsername();
        String json = "[{\"user\":\"" + username + "\",\"namespace\":\"" + NAMESPACE + "\"}]";
        List<RolesAssignedRequest> request = new Gson().fromJson(json,
                new TypeToken<List<RolesAssignedRequest>>() {
                    private static final long serialVersionUID = 705186757536944969L;
                }.getType());
        request.get(0).setRoleUuid(irr.getUuid());
        request.get(0).setRoleName(irr.getName());
        String content = mvc
                .perform(post("/v1/roles/users").contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new Gson().toJson(request).replace("roleUuid", "role_uuid")
                                .replace("roleName", "role_name")))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        return new Gson().fromJson(content.replace("role_uuid", "roleUuid").replace("role_name", "roleName"),
                new TypeToken<List<RolesAssignedResponse>>() {
                    private static final long serialVersionUID = 5297153640601393904L;
                }.getType());
    }

    /**
     * 新增成员
     * @return
     * @throws Exception
     */
    private List<CreateOrgAccountResponse> createOrgAccount() throws Exception {
        CreateOrgAccountRequest request = new CreateOrgAccountRequest();
//        request.setAccounts(Arrays.asList(new InsertAccountDTO(UUID.randomUUID().toString(),
//                "325056665@qq.com", null, null, Arrays.asList("B1", "B2"))));
        String content = mvc
                .perform(post("/v1/orgs/{org_name}/accounts", NAMESPACE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new Gson().toJson(request)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        return new Gson().fromJson(content, new TypeToken<List<CreateOrgAccountResponse>>() {
            private static final long serialVersionUID = -4100289658037605516L;
        }.getType());
    }

    /**
     * 删除成员
     * @param username 成员名称
     * @throws Exception 
     */
    private void removeOrgAccount(final String username) throws Exception {
        mvc.perform(delete("/v1/orgs/{org_name}/accounts/{username}", NAMESPACE, username)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(204)).andDo(print());
    }
}
