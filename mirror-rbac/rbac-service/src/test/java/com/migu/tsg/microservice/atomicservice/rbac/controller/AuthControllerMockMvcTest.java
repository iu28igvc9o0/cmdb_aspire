/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
* 类名称: AuthControllerMockMvcTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月20日下午2:06:36 <br>
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
public class AuthControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 根账号(LDAP初始化必须存在此NAMESPACE)
     */
    private static final String NAMESPACE = "migu";

    /**
     * 成员名称
     */
    private String username;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authVerify(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testAuthVerify() throws Exception {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resource\":{\"name\":\"my-service\"},\"action\":\"service:create\",\"context\":{\"res:cluster\":\"dev\"}}";
        mvc.perform(post("/v1/auth/verify").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resource\":{\"name\":\"my-service\"},\"action\":\"service:create\",\"context\":{\"res:cluster\":\"dev\"}}";
        mvc.perform(post("/v1/auth/verify").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilter(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testAuthFilter() throws Exception {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"action\":\"service:view\",\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"}],\"context\":{\"res:project\":\"project\"}}";
        mvc.perform(post("/v1/auth/filter").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"action\":\"service:view\",\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"}],\"context\":{\"res:project\":\"project\"}}";
        mvc.perform(post("/v1/auth/filter").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActions(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testAuthActions() throws Exception {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resource_type\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resource\":{\"uuid\":\"123-321-123\",\"name\":\"web\"}}";
        mvc.perform(post("/v1/auth/actions").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resource_type\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resource\":{\"uuid\":\"123-321-123\",\"name\":\"web\"}}";
        mvc.perform(post("/v1/auth/actions").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActionsBulk(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testAuthActionsBulk() throws Exception {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resource_type\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resources\":[{\"uuid\":\"1234-123-123-123\",\"name\":\"test\",\"res:cluster\":\"dev\",\"res:space\":\"dev-space\"}]}";
        mvc.perform(post("/v1/auth/actions/bulk").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resource_type\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resources\":[{\"uuid\":\"1234-123-123-123\",\"name\":\"test\",\"res:cluster\":\"dev\",\"res:space\":\"dev-space\"}]}";
        mvc.perform(post("/v1/auth/actions/bulk").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilterMixed(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest)}.
     * @throws Exception 
     */
    @Test
    public final void testAuthFilterMixed() throws Exception {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"service:create\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"application:update\"}],\"context\":{\"res:project\":\"project\"}}";
        mvc.perform(post("/v1/auth/filter/mixed").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"service:create\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"application:update\"}],\"context\":{\"res:project\":\"project\"}}";
        mvc.perform(post("/v1/auth/filter/mixed").contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * 初始化用户角色权限
     * @throws Exception
     */
    @Before
    public void initUserRolePermission() throws Exception {
        List<InsertRoleResponse> insertRoles = insertRoles();
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount(insertRoles.get(0));
        username = createOrgAccount.get(0).getUsername();
        insertRolePermission(insertRoles.get(0).getUuid());
    }

    /**
     * 删除成员
     * @throws Exception 
     */
    @After
    public void removeOrgAccount() throws Exception {
        mvc.perform(delete("/v1/orgs/{org_name}/accounts/{username}", NAMESPACE, username)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is(204)).andDo(print());
    }

    /**
     * 新增角色
     * @return 新增角色对象
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
     * 新增角色权限
     * @param roleUuid 角色UUID
     * @throws Exception
     */
    private void insertRolePermission(String roleUuid) throws Exception {
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        mvc.perform(post("/v1/roles/instances/{role_uuid}/permissions", roleUuid)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andExpect(status().is(201))
                .andDo(print());

    }

    /**
     * 新增成员
     * @param irr 新增角色对象
     * @return
     * @throws Exception
     */
    private List<CreateOrgAccountResponse> createOrgAccount(InsertRoleResponse irr) throws Exception {
        CreateOrgAccountRequest request = new CreateOrgAccountRequest();
//        request.setAccounts(Arrays.asList(new InsertAccountDTO(UUID.randomUUID().toString(),
//                "325056665@qq.com", username, username, Arrays.asList("B1", "B2"))));
        AccountRoleDTO ar = new AccountRoleDTO();
        BeanUtils.copyProperties(irr, ar);
        request.setRoles(Arrays.asList(ar));
        String content = mvc
                .perform(post("/v1/orgs/{org_name}/accounts", NAMESPACE)
                        .contentType(MediaType.APPLICATION_JSON_UTF8).content(new Gson().toJson(request)))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        return new Gson().fromJson(content, new TypeToken<List<CreateOrgAccountResponse>>() {
            private static final long serialVersionUID = -4100289658037605516L;
        }.getType());
    }

}
