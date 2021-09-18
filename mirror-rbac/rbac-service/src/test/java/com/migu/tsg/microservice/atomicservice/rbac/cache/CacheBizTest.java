/**
 * @Title: CacheBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.cache
 * @Description: CacheBiz.jav  单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月22日 下午1:55:11
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.common.config.RedisProperties;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.helper.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.PermissionDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleParentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ParentRole;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaConstraints;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;

/**
 * @ClassName: CacheBizTest
 * @Description: CacheBiz.jav 单元测试类
 * @author botao gao
 * @date 2018年1月22日 下午1:55:11
 *
 */
@RunWith(SpringRunner.class)
public class CacheBizTest {

	/**
	  * @Fields APPLICATIONTYPE : （用一句话描述这个变量表示什么）
	  */
	
	private static final String APPLICATIONTYPE = "application";

	private static final String NAMESPACE = "admin";

	private static final String USERNAME = "admin1";

	private static final String REDISKEYSPLIT = "~";

	private static final String REDISKEYPREFIXPERMISSION = "rbac-service-permission";

	private static final String REDISKEYPREFIXRESOURCESCHEMA = "rbac-service-resource-schema";
	private static final String REDISKEY = "rbac-service-permission~admin~admin1";
	@Mock
	private RedisProperties redisProperties;

	@Mock
	private RoleParentDao roleParentDao;

	@Mock
	private RoleUsersDao roleUsersDao;

	@Mock
	private PermissionDao permissionDao;

	@Mock
	private ResourceSchemaDao resourceSchemaDao;

	@Mock
	private RedisCacheHelper redisCacheHelper;

	@InjectMocks
	private CacheBiz cacheBiz;

	@Before
	public void setUp() {
		String resourceType = APPLICATIONTYPE;
//		when(redisProperties.getRedisKeyPrefixResourceSchema())
//				.thenReturn(REDISKEYPREFIXRESOURCESCHEMA);
		when(redisCacheHelper.hasKey(REDISKEYPREFIXRESOURCESCHEMA)).thenReturn(true);
		when(redisCacheHelper.hasKey(REDISKEYPREFIXRESOURCESCHEMA, resourceType))
				.thenReturn(true);
		when(redisProperties.getRedisKeyPrefixPermission())
				.thenReturn(REDISKEYPREFIXPERMISSION);
		List<ResourceSchemaActions> resourceSchemaActions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete",
				"application:start", "application:stop", "application:update",
				"application:view" };
		for (int i = 0; i < actionStrs.length; i++) {
			ResourceSchemaActions resourceSchemaAction = new ResourceSchemaActions(
					APPLICATIONTYPE, actionStrs[i], resourceType, resourceType);
			resourceSchemaActions.add(resourceSchemaAction);
		}
		ResourceSchema resourceSchema = new ResourceSchema(APPLICATIONTYPE, "t",
				resourceType, resourceType, new Timestamp(System.currentTimeMillis()), resourceSchemaActions,
				new ArrayList<ResourceSchemaConstraints>());
		when(redisCacheHelper.get(REDISKEYPREFIXRESOURCESCHEMA, resourceType))
				.thenReturn(resourceSchema);
		// 设置从缓存中获取权限信息
		when(redisProperties.getUsable()).thenReturn(true);
		when(resourceSchemaDao.fetchResourceSchemaDetail(resourceType, resourceType))
				.thenReturn(resourceSchema);
		List<Object> resourceSchemas = new ArrayList<>();
		resourceSchemas.add(resourceSchema);
		when(redisCacheHelper.values(REDISKEYPREFIXRESOURCESCHEMA))
				.thenReturn(resourceSchemas);
		List<ResourceSchema> resourceSchemaList = new ArrayList<>();
		resourceSchemaList.add(resourceSchema);
		when(resourceSchemaDao.fetchResourceSchemaList()).thenReturn(resourceSchemaList);
		when(redisProperties.getRedisKeySplit()).thenReturn(REDISKEYSPLIT);
//		String key = getKey();
		when(redisCacheHelper.hasKey(REDISKEY)).thenReturn(true);
		String resources = "[\"web*\"]";
		String actions = "[ \"application:create\", \"application:update\", \"application:view\"]";
		List<Object> permissions = new ArrayList<>();
		Permission permission = new Permission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), actions, resources, null);
		permissions.add(permission);
		List<Permission> permissionList = new ArrayList<>();
		permissionList.add(permission);
		when(redisCacheHelper.values(REDISKEY)).thenReturn(permissions);
		List<RoleUsers> listOfRoleUsers = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			RoleUsers roleUsers = new RoleUsers(UUID.randomUUID().toString(), "role" + i,
					NAMESPACE, USERNAME, new Timestamp(System.currentTimeMillis()));
			listOfRoleUsers.add(roleUsers);
		}
		when(roleUsersDao.listOfRoleUsers(USERNAME, NAMESPACE))
				.thenReturn(listOfRoleUsers);
		List<ParentRole> parentRoles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ParentRole parentRole = new ParentRole(UUID.randomUUID().toString(), null,
					null, null);
			parentRoles.add(parentRole);
		}
		when(roleParentDao.listOfParentRoleUuid(anyList())).thenReturn(parentRoles);
		when(permissionDao.listOfPermission(anyList())).thenReturn(permissionList);
	}

	/**
	 * getKey(拼装key)
	 *
	 * @Title: getKey
	 * @Description:
	 * @param: @return
	 * @return: String
	 * @throws:
	 */
//	public String getKey() {
//		String key = REDISKEYPREFIXPERMISSION + REDISKEYSPLIT + NAMESPACE + REDISKEYSPLIT
//				+ USERNAME;
//		return key;
//	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz#getResourceSchema(java.lang.String)}.
	 */
	@Test
	public void testGetResourceSchema() {
		ResourceSchema resourceSchema = cacheBiz.getResourceSchema(APPLICATIONTYPE, null);
		assertEquals(APPLICATIONTYPE, resourceSchema.getResource());
		when(redisCacheHelper.hasKey(REDISKEYPREFIXRESOURCESCHEMA)).thenReturn(false);
		when(redisCacheHelper.put(anyString(),anyString(),anyObject())).thenReturn(true);
		resourceSchema = cacheBiz.getResourceSchema(APPLICATIONTYPE, null);
		assertEquals(APPLICATIONTYPE, resourceSchema.getResource());
	}
	/**
	  * blankResourceTypeByGetResourceSchema
	  *
	  * @Title: blankResourceTypeByGetResourceSchema
	  * @Description: 空的资源类型
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankResourceTypeByGetResourceSchema() {
		cacheBiz.getResourceSchema(null, null);
	}
	/**
	  * blankResourceSchemaException
	  *
	  * @Title: blankResourceSchemaException
	  * @Description: 数据库中查找不到记录
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankResourceSchemaException() {
		when(redisCacheHelper.hasKey(REDISKEYPREFIXRESOURCESCHEMA)).thenReturn(false);
		when(resourceSchemaDao.fetchResourceSchemaDetail(APPLICATIONTYPE, null)).thenReturn(new ResourceSchema());
		cacheBiz.getResourceSchema(APPLICATIONTYPE, null);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz#listOfPermissionForParentRoleUuid(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testListOfPermissionForParentRoleUuid() {
		List<Permission> permissionList = cacheBiz
				.listOfPermissionForParentRoleUuid("admin1", NAMESPACE);
		assertEquals(1, permissionList.size());
		assertEquals("[\"web*\"]", permissionList.get(0).getResources());
		when(redisCacheHelper.hasKey(REDISKEY)).thenReturn(false);
		permissionList = cacheBiz.listOfPermissionForParentRoleUuid("admin1", NAMESPACE);
		assertEquals(1, permissionList.size());
		assertEquals("[\"web*\"]", permissionList.get(0).getResources());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz#run(java.lang.String[])}.
	 */
	@Test
	public void testRun() {
		try {
			cacheBiz.run("1");
		}
		catch (Exception e) {

		}
	}

}
