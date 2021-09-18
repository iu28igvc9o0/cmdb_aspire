package com.migu.tsg.microservice.atomicservice.composite.controller;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.AuthServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.BulkActionsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedResponse;

/**
* 测试CompositePermissionController
* Project Name:composite-service
* File Name:CompositePermissionControllerTest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller
* ClassName: CompositePermissionControllerTest <br/>
* date: 2017年9月5日 下午8:46:24 <br/>
* 测试CompositePermissionController
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes=com.migu.tsg.microservice.atomicservice.composite.CompositeServiceApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CompositePermissionControllerTest {

    @MockBean
    private AuthServiceClient mockClient;

    @Autowired
    private MockMvc mvc; 

    @Value("${version}")
    private String version;

    @Before
    public void init() {
        List<String> mockActionList = new ArrayList<String>();
        mockActionList.add("role:view");
        mockActionList.add("role:update");
        given(mockClient.authActions(Mockito.any(AuthActionsRequest.class))).willReturn(mockActionList);
        
        List<AuthFilterMixedResponse> mixFilterResp = new ArrayList<AuthFilterMixedResponse>();
        AuthFilterMixedResponse resp = new AuthFilterMixedResponse();
        Map<String, String> resource = new HashMap<String, String>();
        resource.put("uuid", "123123123");
        resource.put("name", "testRole01");
        resp.setResource(resource);
        List<String> actions = Arrays.asList("role:view", "role:update");
        resp.setActions(actions);
        mixFilterResp.add(resp);
        given(mockClient.authFilterMixed(Mockito.any(AuthFilterMixedRequest.class))).willReturn(mixFilterResp);
    }

    @Test
    public void testUserAuthResolve() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(version + "/permissions/aspire/role")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("orgAccount", "aspire")
                .header("userName", "pgh")
                .header("isAdmin", false)
                ).andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        // validate request userAuth
                        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
                        Assert.notNull(authCtx, "RequestAuthContext.currentRequestAuthContext() is null.");
                        Assert.notNull(authCtx.getUser(), "RequestAuthContext.getUser() is null.");
                        Assert.isTrue("aspire".equals(authCtx.getUser().getNamespace()), "namespace is null.");
                        Assert.isTrue("pgh".equals(authCtx.getUser().getUsername()), "User.getuserName() is null.");
                    }
           });
    }

    @Test
    public final void testActions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(version + "/permissions/aspire/role")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("orgAccount", "aspire")
                .header("userName", "pgh")
                .header("isAdmin", false)
                ).andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        // validate controller result
                        String respJson = result.getResponse().getContentAsString();
                        List<?> parseRespList = PayloadParseUtil.jacksonBaseParse(List.class, respJson);
                        Assert.isTrue(parseRespList != null, "response is null.");
                        Assert.isTrue("role:view".equals(parseRespList.get(0)), "role:view is not indexed at 0.");
                    }
           });
    }

    @Test
    public final void testBulkActions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(version + "/permissions/aspire/role")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("orgAccount", "aspire")
                .header("userName", "pgh")
                .header("isAdmin", false)
                .content("[{\"uuid\":\"123123123\", \"name\": \"testRole01\"},"
                        + "{\"uuid\":\"345345345\", \"name\": \"testRole02\"}]")
                ).andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult result) throws Exception {
                        // validate controller result
                        String respJson = result.getResponse().getContentAsString();
                        ObjectMapper mapper = new ObjectMapper();
                        List<BulkActionsResponse> bulkRlt 
                                = mapper.readValue(respJson, new TypeReference<List<BulkActionsResponse>>() {});   
                        Assert.isTrue(bulkRlt.size() == 1, "response is null.");
                        Assert.isTrue(bulkRlt.get(0).getResult().size() == 2, "bulkAction size is not 2.");
                    }
           });
    }
}
