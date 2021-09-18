package com.migu.tsg.microservice.atomicservice.composite.controller;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.AuthServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyResponse;

/**
* 测试ResourceAuthHelper
* Project Name:composite-service
* File Name:ResourceAuthHelperTest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller
* ClassName: ResourceAuthHelperTest <br/>
* date: 2017年9月5日 下午8:46:24 <br/>
* 测试ResourceAuthHelper
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=ResourceAuthHelper.class)
public class ResourceAuthV2HelperTest {

    @Autowired
    private ResourceAuthHelper authHelper;

    @MockBean
    private AuthServiceClient mockAuthClient;


    @Test(expected=ResourceActionAuthException.class)
    public final void testResourceActionVerifyFail() {
        // mock
        AuthVerifyResponse mockResp = new AuthVerifyResponse();
        mockResp.setOk(false);
        given(mockAuthClient.authVerify(Mockito.any(AuthVerifyRequest.class))).willReturn(mockResp);

        // test
        RequestHeadUser user = new RequestHeadUser();
        CompositeResource resource = new CompositeResource();
        resource.setId(new Random().nextInt(1000));
        resource.setType("role");
        resource.setUuid("1234321431241");
        resource.setName("testRole01");
        resource.setNamespace("aspire");
        authHelper.resourceActionVerify(user, resource, "view", new HashMap<String, String>());
    }

    @Test
    public final void testResourceActionVerifySuccess() {
        // mock
        AuthVerifyResponse mockResp = new AuthVerifyResponse();
        mockResp.setOk(true);
        given(mockAuthClient.authVerify(Mockito.any(AuthVerifyRequest.class))).willReturn(mockResp);

        // test
        RequestHeadUser user = new RequestHeadUser();
        CompositeResource resource = new CompositeResource();
        resource.setId(new Random().nextInt(1000));
        resource.setType("role");
        resource.setUuid("1234321431241");
        resource.setName("testRole01");
        resource.setNamespace("aspire");
        authHelper.resourceActionVerify(user, resource, "view", new HashMap<String, String>());
    }

    @Test
    public final void testFilterRbacResourceList() {
        // mock
        List<AuthFilterResponse> mockResp = new ArrayList<AuthFilterResponse>();
        AuthFilterResponse oneItem = new AuthFilterResponse();
        oneItem.setActions(Arrays.asList("role:view", "role:update", "role:delete"));
        Map<String, String> resourceMap = new HashMap<String, String>();
        resourceMap.put("uuid", UUID.randomUUID().toString());
        resourceMap.put("name", "testRole01");
        resourceMap.put("res:project", "microServices");
        oneItem.setResource(resourceMap);
        mockResp.add(oneItem);
        given(mockAuthClient.authFilter(Mockito.any(AuthFilterRequest.class))).willReturn(mockResp);

        // test
        List<RbacResource> rbacResList = new ArrayList<RbacResource>();
        RbacResource rbacRes = new RbacResource();
        rbacRes.setUuid(resourceMap.get("uuid"));
        rbacRes.setName(resourceMap.get("name"));
        rbacResList.add(rbacRes);

        List<CompAuthFilterResponse> filterList = authHelper.filterRbacResourceList(
                new RequestHeadUser(), "role:update", rbacResList, new HashMap<String, String>());
        Assert.isTrue(filterList.size() == 1, "testFilterRbacResourceList() failed ... ");
        Assert.isTrue(filterList.get(0).getResTypeActionList().size() == 3,
                "filterList.get(0).getResTypeActionList().size() failed ... ");
        Assert.isTrue(filterList.get(0).getResource().getName().equals("testRole01"), "name is not testRole01 ... ");
    }
}
