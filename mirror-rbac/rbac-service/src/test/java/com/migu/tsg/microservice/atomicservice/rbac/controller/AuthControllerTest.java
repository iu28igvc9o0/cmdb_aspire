/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
* 类名称: AuthControllerTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日下午8:12:24 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

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
     * 成员名称
     */
    private String username;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authVerify(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest)}.
     */
    @Test
    public final void testAuthVerify() {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resource\":{\"name\":\"my-service\"},\"action\":\"service:create\",\"context\":{\"res:cluster\":\"dev\"}}";
        AuthVerifyRequest request = new Gson().fromJson(json, AuthVerifyRequest.class);
        authController.authVerify(request);
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resource\":{\"name\":\"my-service\"},\"action\":\"service:create\",\"context\":{\"res:cluster\":\"dev\"}}";
        request = new Gson().fromJson(json, AuthVerifyRequest.class);
        authController.authVerify(request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilter(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest)}.
     */
    @Test
    public final void testAuthFilter() {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"action\":\"service:view\",\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"}],\"context\":{\"res:project\":\"project\"}}";
        AuthFilterRequest request = new Gson().fromJson(json, AuthFilterRequest.class);
        authController.authFilter(request);
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"action\":\"service:view\",\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\"}],\"context\":{\"res:project\":\"project\"}}";
        request = new Gson().fromJson(json, AuthFilterRequest.class);
        authController.authFilter(request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActions(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest)}.
     */
    @Test
    public final void testAuthActions() {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resourceType\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resource\":{\"uuid\":\"123-321-123\",\"name\":\"web\"}}";
        AuthActionsRequest request = new Gson().fromJson(json, AuthActionsRequest.class);
        authController.authActions(request);
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resourceType\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resource\":{\"uuid\":\"123-321-123\",\"name\":\"web\"}}";
        request = new Gson().fromJson(json, AuthActionsRequest.class);
        authController.authActions(request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActionsBulk(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest)}.
     */
    @Test
    public final void testAuthActionsBulk() {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resourceType\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resources\":[{\"uuid\":\"1234-123-123-123\",\"name\":\"test\",\"res:cluster\":\"dev\",\"res:space\":\"dev-space\"}]}";
        AuthActionsBulkRequest request = new Gson().fromJson(json, AuthActionsBulkRequest.class);
        authController.authActionsBulk(request);
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resourceType\":\"service\",\"context\":{\"res:cluster\":\"dev\",\"res:project\":\"my-project\"},\"resources\":[{\"uuid\":\"1234-123-123-123\",\"name\":\"test\",\"res:cluster\":\"dev\",\"res:space\":\"dev-space\"}]}";
        request = new Gson().fromJson(json, AuthActionsBulkRequest.class);
        authController.authActionsBulk(request);
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilterMixed(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest)}.
     */
    @Test
    public final void testAuthFilterMixed() {
        // isAdmin=false
        String json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":false,\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"service:create\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"application:update\"}],\"context\":{\"res:project\":\"project\"}}";
        AuthFilterMixedRequest request = new Gson().fromJson(json, AuthFilterMixedRequest.class);
        authController.authFilterMixed(request);
        // isAdmin=true
        json = "{\"username\":\"" + username + "\",\"namespace\":\"" + NAMESPACE
                + "\",\"is_admin\":true,\"resources\":[{\"uuid\":\"123-321-123\",\"name\":\"my-web\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"service:create\"},{\"uuid\":\"123-321-123\",\"name\":\"my-web-another\",\"res:cluster\":\"dev-cluster\",\"res:space\":\"dev-space\",\"action\":\"application:update\"}],\"context\":{\"res:project\":\"project\"}}";
        request = new Gson().fromJson(json, AuthFilterMixedRequest.class);
        authController.authFilterMixed(request);
    }

    /**
     * 初始化用户角色权限
     */
    @Before
    public void initUserRolePermission() {
        List<InsertRoleResponse> insertRoles = insertRoles();
        List<CreateOrgAccountResponse> createOrgAccount = createOrgAccount(insertRoles.get(0));
        username = createOrgAccount.get(0).getUsername();
        insertRolePermission(insertRoles.get(0).getUuid());
    }

    /**
     * 删除成员
     */
    @After
    public void removeOrgAccount() {
        orgsController.removeOrgAccount(NAMESPACE, username);
    }

    /**
     * 新增角色
     * @return 新增角色对象
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
     * 新增角色权限
     * @param roleUuid 角色UUID
     */
    private void insertRolePermission(String roleUuid) {
        String json = "{\"resource\":[\"*\"],\"actions\":[\"service:*\"],\"constraints\":[{\"res:cluster\":\"dev\"}]}";
        InsertRolePermissionsRequest request = new Gson().fromJson(json, InsertRolePermissionsRequest.class);
        roleController.insertRolePermission(request, roleUuid);
    }

    /**
     * 新增成员
     * @param irr 新增角色对象
     * @return
     */
    private List<CreateOrgAccountResponse> createOrgAccount(InsertRoleResponse irr) {
        CreateOrgAccountRequest request = new CreateOrgAccountRequest();
//        request.setAccounts(Arrays.asList(new InsertAccountDTO(UUID.randomUUID().toString(),
//                "325056665@qq.com", username, username, Arrays.asList("B1", "B2"))));
        AccountRoleDTO ar = new AccountRoleDTO();
        BeanUtils.copyProperties(irr, ar);
        request.setRoles(Arrays.asList(ar));
        return orgsController.createOrgAccount(request, NAMESPACE);
    }

}
