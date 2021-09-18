/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.util.PropertiesUtil;
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
* 类名称: OrgsControllerMockMvcTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月20日下午3:23:07 <br>
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
public class OrgsControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 根账号(LDAP初始化必须存在此NAMESPACE)
     */
    private static final String NAMESPACE = "migu";
    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#updateSubAccountPassword(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountPasswordRequest, java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testUpdateSubAccountPassword() throws Exception {
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount();
        String password = PropertiesUtil.getProperties().getStringProperty("PASSWORD");
        password = password == null ? "123456":password;
        // Admin updating other users
        String json = "{\"password\": " + password + "}";
        mvc.perform(put("/v1/orgs/{org_name}/accounts/{username}", NAMESPACE,
                createOrgAccount.get(0).getUsername()).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().is(204)).andDo(print());
        // Subaccounts updating own password
        json = "{\"password\": " + password + ",\"old_password\": " + password + "}";
        mvc.perform(put("/v1/orgs/{org_name}/accounts/{username}", NAMESPACE,
                createOrgAccount.get(0).getUsername()).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().is(204)).andDo(print());
        removeOrgAccount(createOrgAccount.get(0).getUsername());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#removeOrgAccount(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testRemoveOrgAccount() throws Exception {
        removeOrgAccount(createOrgAccount().get(0).getUsername());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#updateOrg(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateOrgRequest, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testUpdateOrg() throws Exception {
        String json = "{\"company\": \"Migu, inc\"}";
        mvc.perform(put("/v1/orgs/{org_name}", NAMESPACE).contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)).andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#getOrgDetail(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testGetOrgDetail() throws Exception {
        mvc.perform(get("/v1/orgs/{org_name}", NAMESPACE).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#getOrgUserDetail(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testGetOrgUserDetail() throws Exception {
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount();
        String username = createOrgAccount.get(0).getUsername();
        mvc.perform(get("/v1/orgs/{org_name}/accounts/{username}", NAMESPACE, username)
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(print());
        removeOrgAccount(username);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#createOrgAccount(com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testCreateOrgAccount() throws Exception {
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount();
        removeOrgAccount(createOrgAccount.get(0).getUsername());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#listOrgAccounts(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)}.
     * @throws Exception 
     */
    @Test
    public final void testListOrgAccounts() throws Exception {
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount();
        String username = createOrgAccount.get(0).getUsername();
        mvc.perform(get("/v1/orgs/{org_name}/accounts", NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON_UTF8).param("search", username))
                .andExpect(status().isOk()).andDo(print());
        removeOrgAccount(username);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#listAccountRoles(java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testListAccountRoles() {
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#deleteAccountRoles(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public final void testDeleteAccountRoles() {
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#fileUpload(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testFileUpload() throws Exception {
        String json = "{\"logo_file\": \"image/png;base64,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\"}";
        mvc.perform(post("/v1/user/update-logo/{namespace}", NAMESPACE)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json)).andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 初始化根账号
     * @throws Exception
     */
    //    @Before
    //    public void initCreateOrg() throws Exception {
    //        String json = "{\"userType\":\"admin\",\"mobile\":\"18061683816\",\"mail\":\"18061683816@163.com\",\"password\":\"123456\",\"company\":\"migu company\"}";
    //        mvc.perform(post("/v1/ldap/user/{namespace}", NAMESPACE).contentType(MediaType.APPLICATION_JSON_UTF8)
    //                .content(json)).andExpect(status().isOk()).andDo(print());
    //    }

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
     * 新增成员
     * @return
     * @throws Exception
     */
    private List<CreateOrgAccountResponse> createOrgAccount() throws Exception {
        List<InsertRoleResponse> insertRoles = insertRoles();
        CreateOrgAccountRequest request = new CreateOrgAccountRequest();
//        request.setAccounts(Arrays.asList(new InsertAccountDTO(UUID.randomUUID().toString(),
//                "325056665@qq.com", null, null, Arrays.asList("B1", "B2"))));
        AccountRoleDTO ar = new AccountRoleDTO();
        BeanUtils.copyProperties(insertRoles.get(0), ar);
        request.setRoles(Arrays.asList(ar));
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
