package com.migu.tsg.microservice.atomicservice.composite.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.RoleServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.KeyValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompRoleCreatePayload;

/**
* 测试RbacRolesController
* Project Name:composite-service
* File Name:RbacRolesControllerTest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller
* ClassName: RbacRolesControllerTest <br/>
* date: 2017年9月5日 下午8:46:24 <br/>
* 测试RbacRolesController
* @author pengguihua
* @version
* @since JDK 1.6
*/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=com.migu.tsg.microservice.atomicservice.composite.CompositeServiceApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RbacRolesControllerTest {

    @MockBean
    private ResourceAuthHelper resAuthHelper;
    
    @MockBean
    private RoleServiceClient rbacClient; 

    @Autowired
    private MockMvc mvc;

    @Value("${version}")
    private String version;
    
//    private final List<CompRoleCreatePayload> createRoleList = buildCreateRoleList();
    
    // 构造新增角色数据
    private List<CompRoleCreatePayload> buildCreateRoleList() {
        List<CompRoleCreatePayload> roleCreateList = new ArrayList<CompRoleCreatePayload>();
        CompRoleCreatePayload oneItem = new CompRoleCreatePayload();
        oneItem.setAdminRole(false);
        oneItem.setName("testRole01");
        oneItem.setNamespace("aspire");
        CompRoleCreatePayload secItem = new CompRoleCreatePayload();
        secItem.setAdminRole(false);
        secItem.setName("testRole02");
        secItem.setNamespace("aspire");
        roleCreateList.add(oneItem);
        roleCreateList.add(secItem);
        return roleCreateList;
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testCreateRoleFail() throws Exception {
        // mock data
        KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>> mockResp
            = new KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>>();
        mockResp.setKey(new ArrayList<CompositeResource>());
        mockResp.setValue(new ArrayList<CompAuthFilterResponse>());

//        given(resAuthHelper.filterResourceList(
//                any(RequestHeadUser.class), any(String.class), any(List.class), any(Map.class)))
//                .willReturn(mockResp);
        // test
        String createRolejson = new ObjectMapper().writeValueAsString(buildCreateRoleList());
        mvc.perform(MockMvcRequestBuilders.post(version + "/roles/aspire")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("orgAccount", "aspire")
                .header("userName", "pgh")
                .header("isAdmin", false)
                .content(createRolejson)
                ).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
