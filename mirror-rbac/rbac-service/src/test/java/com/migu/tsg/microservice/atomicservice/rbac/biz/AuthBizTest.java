/**
 * @Title: AuthBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.biz
 * @Description: AuthBiz.java 单元测试类 Copyright: Copyright (c) 2018 Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月19日 下午4:19:37
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.biz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaConstraints;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AuthResourceActionDTO;

/**
 * @ClassName: AuthBizTest
 * @Description: AuthBiz.java 单元测试类
 * @author botao gao
 * @date 2018年1月19日 下午4:19:37
 *
 */
@RunWith(SpringRunner.class)
public class AuthBizTest {

	@Mock
	private CacheBiz cacheBiz;

	@InjectMocks
	private AuthBiz authBiz;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz#authVerify(java.lang.String, java.lang.String, java.lang.Boolean, java.util.Map, java.lang.String, java.util.Map)}.
	 */
	@Before
	public void setUp() {
		List<ResourceSchemaActions> resourceSchemaActions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete",
				"application:start", "application:stop", "application:update",
				"application:view" };
		for (int i = 0; i < actionStrs.length; i++) {
			ResourceSchemaActions resourceSchemaAction = new ResourceSchemaActions(
					"application", actionStrs[i], null, null);
			resourceSchemaActions.add(resourceSchemaAction);
		}
		String resourceSchemaStr = "application";
		ResourceSchemaConstraints resourceSchemaConstraint = new ResourceSchemaConstraints(
				"application", "res:cluster", "name");
		List<ResourceSchemaConstraints> resourceSchemaConstraints = new ArrayList<ResourceSchemaConstraints>();
		resourceSchemaConstraints.add(resourceSchemaConstraint);
		ResourceSchema resourceSchema = new ResourceSchema("application", "t",
				resourceSchemaStr, resourceSchemaStr, new Timestamp(System.currentTimeMillis()), resourceSchemaActions,
				resourceSchemaConstraints);
		when(cacheBiz.getResourceSchema(resourceSchemaStr, resourceSchemaStr)).thenReturn(resourceSchema);
		List<ResourceSchema> resourceSchemas = new ArrayList<>();
		resourceSchemas.add(resourceSchema);
//		when(cacheBiz.listOfResourceSchema()).thenReturn(resourceSchemas);
		String resources = "[\"web*\"]";
		String actions = "[ \"application:create\", \"application:update\", \"application:view\"]";
		List<Permission> permissions = new ArrayList<>();
		Permission permission = new Permission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), actions, resources, "[{\"res:cluster\":\"migu\"}]");
		permissions.add(permission);
		when(cacheBiz.listOfPermissionForParentRoleUuid("admin1", "admin"))
				.thenReturn(permissions);
	}

	@Test
	public void testAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		resource.put("res:cluster", "migu");
		String action = "application:create";
		Map<String, String> context = buildContext();
		boolean result = authBiz.authVerify("admin1", "admin", false, resource, action,
				context);
		assertEquals(true, result);
	}

	@Test
	public void adminAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		String action = "application:create";
		Map<String, String> context = buildContext();
		boolean result = authBiz.authVerify("admin1", "admin", true, resource, action,
				context);
		assertEquals(true, result);
	}

	/**
	 * blankUsernameExceptionByAuthVerify
	 *
	 * @Title: blankUsernameExceptionByAuthVerify
	 * @Description: username为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankUsernameExceptionByAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		String action = "application:create";
		Map<String, String> context = buildContext();
		boolean result = authBiz.authVerify("", "admin", false, resource, action,
				context);
	}

	/**
	 * blankNamespaceExceptionByAuthVerify
	 *
	 * @Title: blankNamespaceExceptionByAuthVerify
	 * @Description: namespace为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankNamespaceExceptionByAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		String action = "application:create";
		Map<String, String> context = buildContext();
		boolean result = authBiz.authVerify("admin1", "", false, resource, action,
				context);
	}

	/**
	 * blankActionExceptionByAuthVerify
	 *
	 * @Title: blankActionExceptionByAuthVerify
	 * @Description: 验证时操作集合为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankActionExceptionByAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		Map<String, String> context = buildContext();
		boolean result = authBiz.authVerify("admin1", "admin", false, resource, "",
				context);
		assertEquals(true, result);
	}

	/**
	 * blankPermissionExceptionByAuthVerify
	 *
	 * @Title: blankPermissionExceptionByAuthVerify
	 * @Description: 普通用户没有分配权限异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void blankPermissionExceptionByAuthVerify() {
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		Map<String, String> context = buildContext();
		String action = "application:create";
		when(cacheBiz.listOfPermissionForParentRoleUuid(anyString(), anyString()))
				.thenReturn(new ArrayList<>());
		boolean result = authBiz.authVerify("admin1", "admin", false, resource, action,
				context);
		assertEquals(false, result);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz#authFilter(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.util.List, java.util.Map)}.
	 */
	@Test
	public void testAuthFilter() {
		String action = "application:create";
		List<Map<String, String>> resources = buildResources();
		Map<String, String> context = buildContext();
		List<AuthResourceActionDTO> authResourceActionDTOList = authBiz
				.authFilter("admin1", "admin", false, action, resources, context);
		assertEquals("web0", authResourceActionDTOList.get(0).getResource().get("name"));
	}

	/**
	 * adminAuthFilter
	 *
	 * @Title: adminAuthFilter
	 * @Description: 超级管理员用户
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void adminAuthFilter() {
		String action = "application:create";
		List<Map<String, String>> resources = buildResources();
		Map<String, String> context = buildContext();
		List<AuthResourceActionDTO> authResourceActionDTOList = authBiz
				.authFilter("admin1", "admin", true, action, resources, context);
		assertEquals("web0", authResourceActionDTOList.get(0).getResource().get("name"));
	}

	@Test
	public void blankPermissionByAuthFilter() {
		String action = "application:create";
		List<Map<String, String>> resources = buildResources();
		Map<String, String> context = buildContext();
		when(cacheBiz.listOfPermissionForParentRoleUuid(anyString(), anyString()))
				.thenReturn(new ArrayList<Permission>());
		List<AuthResourceActionDTO> authResourceActionDTOList = authBiz
				.authFilter("admin1", "admin", false, action, resources, context);
		assertEquals(0, authResourceActionDTOList.size());
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

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz#authActions(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.util.Map, java.util.Map)}.
	 */
	@Test
	public void testAuthActions() {
		Map<String, String> context = buildContext();
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		List<String> actions = authBiz.authActions("admin1", "admin", false,
				"application", context, resource);
		assertEquals(true, actions.contains("application:create"));
	}

	/**
	 * adminAuthActions
	 *
	 * @Title: adminAuthActions
	 * @Description: 超级管理员用户
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void adminAuthActions() {
		Map<String, String> context = buildContext();
		Map<String, String> resource = new HashMap<>();
		resource.put("name", "web1");
		List<String> actions = authBiz.authActions("admin1", "admin", true, "application",
				context, resource);
		assertEquals(true, actions.contains("application:create"));
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

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz#authActionsBulk(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.String, java.util.Map, java.util.List)}.
	 */
	@Test
	public void testAuthActionsBulk() {
		List<AuthResourceActionDTO> result = authBiz.authActionsBulk("admin1", "admin",
				false, "application", buildContext(), buildResources());
		assertEquals(true, result.get(0).getActions().contains("application:create"));
	}

	/**
	  * adminAuthActionsBulk
	  *
	  * @Title: adminAuthActionsBulk
	  * @Description: 管理员操作
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void adminAuthActionsBulk() {
		List<AuthResourceActionDTO> result = authBiz.authActionsBulk("admin1", "admin",
				true, "application", buildContext(), buildResources());
		assertEquals(true, result.get(0).getActions().contains("application:create"));
	}
	
	/**
	  * blankPermissionAuthActionsBulk
	  *
	  * @Title: blankPermissionAuthActionsBulk
	  * @Description: 普通用户没有分配权限返回空
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankPermissionAuthActionsBulk() {
		when(cacheBiz.listOfPermissionForParentRoleUuid(anyString(), anyString()))
		.thenReturn(new ArrayList<>());
		List<AuthResourceActionDTO> result = authBiz.authActionsBulk("admin1", "admin",
				false, "application", buildContext(), buildResources());
		assertEquals(0, result.size());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.AuthBiz#authFilterMixed(java.lang.String, java.lang.String, java.lang.Boolean, java.util.Map, java.util.List)}.
	 */
	@Test
	public void testAuthFilterMixed() {
		List<Map<String, String>> resources = buildResources();
		resources.get(0).put("action", "application:create");
		resources.get(1).put("action", "application:view");
		resources.get(2).put("action", "application:update");
		resources.get(3).put("action", "application:update");
		resources.get(4).put("action", "application:update");
		List<AuthResourceActionDTO> result = authBiz.authFilterMixed("admin1", "admin",
				false, buildContext(), resources);
		assertEquals(5, result.size());
	}

	/**
	 * blankPermissionAuthFilterMixed
	 *
	 * @Title: blankPermissionAuthFilterMixed
	 * @Description: 普通用户没有分配权限返回空
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void blankPermissionAuthFilterMixed() {
		List<Map<String, String>> resources = buildResources();
		resources.get(0).put("action", "application:create");
		resources.get(1).put("action", "application:view");
		resources.get(2).put("action", "application:update");
		resources.get(3).put("action", "application:update");
		resources.get(4).put("action", "application:update");
		when(cacheBiz.listOfPermissionForParentRoleUuid(anyString(), anyString()))
				.thenReturn(new ArrayList<Permission>());
		List<AuthResourceActionDTO> result = authBiz.authFilterMixed("admin1", "admin",
				false, buildContext(), resources);
		assertEquals(0, result.size());
	}

	/**
	 * blankActionExceptionByAuthFilterMixed
	 *
	 * @Title: blankActionExceptionByAuthFilterMixed
	 * @Description: 资源对象集合的操作属性为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankActionExceptionByAuthFilterMixed() {
		List<Map<String, String>> resources = buildResources();
		resources.get(0).put("action", "");
		resources.get(1).put("action", "");
		resources.get(2).put("action", "");
		resources.get(3).put("action", "");
		resources.get(4).put("action", "");
		authBiz.authFilterMixed("admin1", "admin", false, buildContext(), resources);
	}

	/**
	 * adminAuthFilterMixed
	 *
	 * @Title: adminAuthFilterMixed
	 * @Description: 管理员用户之间返回资源类型的所有操作。
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void adminAuthFilterMixed() {
		List<Map<String, String>> resources = buildResources();
		resources.get(0).put("action", "application:create");
		resources.get(1).put("action", "application:view");
		resources.get(2).put("action", "application:update");
		resources.get(3).put("action", "application:update");
		resources.get(4).put("action", "application:update");
		List<AuthResourceActionDTO> result = authBiz.authFilterMixed("admin1", "admin",
				true, buildContext(), resources);
		assertEquals(5, result.size());
	}

}
