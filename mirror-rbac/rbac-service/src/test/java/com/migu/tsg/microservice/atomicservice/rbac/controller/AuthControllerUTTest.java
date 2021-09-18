/**
 * @Title: AuthControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.controller
 * @Description: AuthController.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月24日 下午4:05:13
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AuthResourceActionDTO;

/**
 * @ClassName: AuthControllerUT
 * @Description:
 * @author botao gao
 * @date 2018年1月24日 下午4:05:13
 *
 */
@RunWith(SpringRunner.class)
public class AuthControllerUTTest {

	@Mock
	private AuthBiz authBiz;

	@InjectMocks
	private AuthController authController;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authVerify(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthVerifyRequest)}.
	 */
	@Test
	public void testAuthVerify() {
		AuthVerifyRequest request = new AuthVerifyRequest();
		when(authBiz.authVerify(anyString(), anyString(), anyBoolean(), anyMap(),
				anyString(), anyMap())).thenReturn(true);
		authController.authVerify(request);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilter(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterRequest)}.
	 */
	@Test
	public void testAuthFilter() {
		AuthFilterRequest request = new AuthFilterRequest();
		when(authBiz.authFilter(anyString(), anyString(), anyBoolean(), anyString(),
				anyList(), anyMap())).thenReturn(buildAuthResourceActionDTOList());
		List<AuthFilterResponse> response =  authController.authFilter(request);
		assertEquals("web", response.get(0).getResource().get("name"));
	}

	/**
	  * buildAuthResourceActionDTOList(这里用一句话描述这个方法的作用)
	  *
	  * @Title: buildAuthResourceActionDTOList
	  * @Description: 
	  * @param: @return    
	  * @return: List<AuthResourceActionDTO>   
	  * @throws:
	  */
	private List<AuthResourceActionDTO> buildAuthResourceActionDTOList() {
		List<AuthResourceActionDTO> authResourceActionDTOList = new ArrayList<>();
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web");
		resource.put("action", "re:13");
		for (int i = 0; i < 5; i++) {
			AuthResourceActionDTO authResourceActionDTO = new AuthResourceActionDTO(
					buildAction(), resource);
			authResourceActionDTOList.add(authResourceActionDTO);
		}
		return authResourceActionDTOList;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActions(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsRequest)}.
	 */
	@Test
	public void testAuthActions() {
		AuthActionsRequest request = new AuthActionsRequest();
		when(authBiz.authActions(anyString(), anyString(), anyBoolean(), anyString(), anyMap(), anyMap())).thenReturn(buildAction());
		List<String> response =  authController.authActions(request);
		assertEquals("application:create", response.get(0));
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authActionsBulk(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthActionsBulkRequest)}.
	 */
	@Test
	public void testAuthActionsBulk() {
		AuthActionsBulkRequest request = new AuthActionsBulkRequest();
		when(authBiz.authActionsBulk(anyString(), anyString(), anyBoolean(), anyString(), anyMap(), anyList())).thenReturn(buildAuthResourceActionDTOList());
		authController.authActionsBulk(request);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.AuthController#authFilterMixed(com.migu.tsg.microservice.atomicservice.rbac.dto.AuthFilterMixedRequest)}.
	 */
	@Test
	public void testAuthFilterMixed() {
		AuthFilterMixedRequest request = new AuthFilterMixedRequest();
		when(authBiz.authFilterMixed(anyString(), anyString(), anyBoolean(),anyMap() , anyList())).thenReturn(buildAuthResourceActionDTOList());
		authController.authFilterMixed(request);
	}

	/**
	 * buildResources(这里用一句话描述这个方法的作用)
	 *
	 * @Title: buildResources
	 * @Description:
	 * @param:
	 * @return:设定文件
	 * @return: List<Map<String,String>> 返回类型 @throws
	 */
	private List<Map<String, String>> buildResources() {
		List<Map<String, String>> resources = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Map<String, String> resource = new HashMap<>();
			resource.put("name", "web" + i);
			resource.put("action", "re:13");
			resources.add(resource);
		}
		return resources;
	}

	private List<String> buildAction() {
		String[] actionStrs = { "application:create", "application:delete",
				"application:start", "application:stop", "application:update",
				"application:view" };
		return Arrays.asList(actionStrs);
	}

	/**
	 * buildContext(创建请求的上下文)
	 *
	 * @Title: buildContext
	 * @Description:
	 * @param:
	 * @return: 设定文件
	 * @return: Map<String,String> 返回类型
	 * @throws:
	 */
	private Map<String, String> buildContext() {
		Map<String, String> context = new HashMap<>();
		context.put("res:cluster", "migu");
		return context;
	}
}
