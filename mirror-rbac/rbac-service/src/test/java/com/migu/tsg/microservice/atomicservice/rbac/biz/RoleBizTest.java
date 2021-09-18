/**
 * @Title: RoleBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.biz
 * @Description: 
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月17日 下午3:28:53
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.biz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.rbac.dao.PermissionDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleParentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Role;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleParent;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.GetRoleDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesAssignedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolesAssignedRevokeDTO;

/**
 * @ClassName: RoleBizTest
 * @Description:
 * @author botao gao
 * @date 2018年1月17日 下午3:28:53
 *
 */
@RunWith(SpringRunner.class)
public class RoleBizTest {
	@Mock
	private RoleDao roleDao;

	@Mock
	private RoleParentDao roleParentDao;

	@Mock
	private PermissionDao permissionDao;

	@Mock
	private RoleUsersDao roleUsersDao;

	@Mock
	private LdapServiceClient ldapServiceClient;

	@InjectMocks
	private RoleBiz roleBiz;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#listRoles(java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testListRoles() {
		List<Role> roles = new ArrayList<>();
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		for (int i = 0; i < 10; i++) {
			Role role = new Role("0118cccb-fe52-4cba-95c6-c92337ee63f" + i, "role" + i,
					"admin", 0, createTime, null, i, null);
			roles.add(role);
		}
		when(roleDao.listRoles(any(String[].class), anyString(), null, null, null)).thenReturn(roles);
		String[] uuids = { "0118cccb-fe52-4cba-95c6-c92337ee63f5" };
		List<ListRolesDTO> rolesDTO = roleBiz.listRoles(uuids, "admin", null, null, null);
		assertEquals("role0", rolesDTO.get(0).getName());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#insertRoles(java.util.List)}.
	 */
	@Test
	public void testInsertRoles() {
		List<RoleParentsDTO> parents = buildRoleParentsDTO();
		List<PermissionDTO> permissions = buildPermissionDTO();

		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			InsertRoleDTO roleDTO = new InsertRoleDTO("", "role" + i, "admin", false,
					"2018-01-03 16:45:23", null, null, i, null, null, parents, permissions);
			insertRoleDTOs.add(roleDTO);
		}

		mockGetRoleByUUID();
		roleBiz.insertRoles(insertRoleDTOs);
		blankRoleNameException(parents, permissions);
	}

	@Test
	public void blankNamespaceException() {
		blankNamespaceException(buildRoleParentsDTO(), buildPermissionDTO());
	}

	@Test
	public void duplicateRoleNameException() {
		duplicateRoleNameException(buildRoleParentsDTO(), buildPermissionDTO());
	}

	@Test
	public void duplicateRoleNameInDBException() {
		duplicateRoleNameInDBException(buildRoleParentsDTO(), buildPermissionDTO());
	}

	@Test
	public void blankParentRolesAndPermissionsException() {
		blankParentRolesAndPermissionsException(null, null);
	}

	@Test
	public void blankParentRoleException() {
		blankParentRoleException(buildPermissionDTO());
	}

	@Test
	public void matchesRegexUuidsParentRoleException() {
		matchesRegexUuidsParentRoleException(buildPermissionDTO());
	}

	@Test
	public void duplicateParentRoleNameException() {
		duplicateParentRoleNameException(buildPermissionDTO());
	}

	/**
	 * blankPermissionResourceException
	 *
	 * @Title: blankPermissionResourceException
	 * @Description: 权限的资源名称为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankPermissionResourceException() {
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, null, null);
		permissions.add(permissionDTO);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, actions, null, permissions);
		insertRoleDTOs.add(roleDTO);
		mockGetRoleByUUID();
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * blankPermissionActionException
	 *
	 * @Title: blankPermissionActionException
	 * @Description: 权限操作集合为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankPermissionActionException() {
		List<PermissionDTO> permissions = new ArrayList<>();
		PermissionDTO permissionDTO = new PermissionDTO("", "", null, getResources(),
				null);
		permissions.add(permissionDTO);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, null, permissions);
		insertRoleDTOs.add(roleDTO);
		mockGetRoleByUUID();
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * blankParentRoleIdException
	 *
	 * @Title: blankParentRoleIdException
	 * @Description: 父角色id为null异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void blankParentRoleIdException() {
		List<RoleParentsDTO> parents = new ArrayList<>();
		// 父角色id为null
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(null, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		insertRoles(buildPermissionDTO(), parents);
	}

	/**
	 * roleNamespaceNeParentRoleNamespaceException
	 *
	 * @Title: roleNamespaceNeParentRoleNamespaceException
	 * @Description: 角色根帐号和父角色根帐号不一致异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void roleNamespaceNeParentRoleNamespaceException() {
		List<RoleParentsDTO> parents = new ArrayList<>();
		String parentRoleId = UUID.randomUUID().toString();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		Role parentRole = new Role();
		parentRole.setNamespace("admin1");
		parentRole.setName("role");
		when(roleDao.getRoleByUUID(parentRoleId)).thenReturn(parentRole);
		insertRoles(buildPermissionDTO(), parents);
	}

	/**
	 * recursionRoleOutMaxException
	 *
	 * @Title: recursionRoleOutMaxException
	 * @Description: 角色集成层级大于4异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void recursionRoleOutMaxException() {
		List<RoleParentsDTO> parents = new ArrayList<>();
		String parentRoleId = UUID.randomUUID().toString();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		mockGetRoleByUUID();
		String parentRoleId2 = UUID.randomUUID().toString();
		List<RoleParent> roleParents = new ArrayList<>();
		RoleParent roleParent = new RoleParent(parentRoleId, parentRoleId2, null, null);
		roleParents.add(roleParent);
		when(roleParentDao.fetchRoleParentList(parentRoleId)).thenReturn(roleParents);

		String parentRoleId3 = UUID.randomUUID().toString();
		List<RoleParent> roleParents2 = new ArrayList<>();
		RoleParent roleParent2 = new RoleParent(parentRoleId2, parentRoleId3, null, null);
		roleParents2.add(roleParent2);
		when(roleParentDao.fetchRoleParentList(parentRoleId2)).thenReturn(roleParents2);

		String parentRoleId4 = UUID.randomUUID().toString();
		List<RoleParent> roleParents3 = new ArrayList<>();
		RoleParent roleParent3 = new RoleParent(parentRoleId3, parentRoleId4, null, null);
		roleParents3.add(roleParent3);
		when(roleParentDao.fetchRoleParentList(parentRoleId3)).thenReturn(roleParents3);

		insertRoles(buildPermissionDTO(), parents);
	}

	/**
	 * blankParentRoleException(父角色不存在抛出异常)
	 *
	 * @Title: blankParentRoleException
	 * @Description:父角色不存在抛出异常
	 * @param: @param permissions
	 * @return: void
	 * @throws:
	 */
	private void blankParentRoleException(List<PermissionDTO> permissions) {
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(UUID.randomUUID().toString(),
				"parentRole", "2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		insertRoles(permissions, parents);
	}

	/**
	 * blankParentRoleName
	 *
	 * @Title: blankParentRoleName
	 * @Description:父角色ID不合法
	 * @param: @param permissions
	 * @return: void
	 * @throws:
	 */
	private void matchesRegexUuidsParentRoleException(List<PermissionDTO> permissions) {
		List<RoleParentsDTO> parents = new ArrayList<>();
		// 父角色ID不合法
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO("121f", "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		insertRoles(permissions, parents);
	}

	/**
	 * duplicateParentRoleNameException(父角色名称为空抛出异常)
	 *
	 * @Title: duplicateParentRoleNameException
	 * @Description:
	 * @param: @param permissions
	 * @return: void
	 * @throws:
	 */
	private void duplicateParentRoleNameException(List<PermissionDTO> permissions) {
		List<RoleParentsDTO> parents = new ArrayList<>();
		// 父角色名重复
		String uuid = UUID.randomUUID().toString();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(uuid, "parentRole2",
				"2018-01-03 16:45:23");
		RoleParentsDTO roleParentsDTO2 = new RoleParentsDTO(uuid, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		parents.add(roleParentsDTO2);
		Role role = new Role();
		role.setUuid(uuid);
		role.setNamespace("admin");
		role.setName("parentRole");
		when(roleDao.getRoleByUUID(uuid)).thenReturn(role);
		insertRoles(permissions, parents);
	}

	/**
	 * insertRoles(插入角色)
	 *
	 * @Title: insertRoles
	 * @Description:
	 * @param: @param permissions
	 * @param: @param parents
	 * @return: void
	 * @throws:
	 */
	public void insertRoles(List<PermissionDTO> permissions,
			List<RoleParentsDTO> parents) {
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * blankRoleNameException(角色名称为空抛出异常)
	 *
	 * @Title: blankRoleNameException
	 * @Description:
	 * @param: @param parents
	 * @param: @param permissions
	 * @param: @param insertRoleDTOs
	 * @return: void
	 * @throws:
	 */
	public void blankRoleNameException(List<RoleParentsDTO> parents,
			List<PermissionDTO> permissions) {
		// 角色名称为空
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * blankNamespaceException(命名空间为空抛出异常)
	 *
	 * @Title: blankNamespaceException
	 * @Description:
	 * @param: @param parents
	 * @param: @param permissions
	 * @param: @param insertRoleDTOs
	 * @return: void
	 * @throws:
	 */
	public void blankNamespaceException(List<RoleParentsDTO> parents,
			List<PermissionDTO> permissions) {
		// 角色名称为空
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * duplicateRoleNameException(角色名重复抛出异常)
	 *
	 * @Title: duplicateRoleNameException
	 * @Description:
	 * @param: @param parents
	 * @param: @param permissions
	 * @param: @param insertRoleDTOs
	 * @return: void
	 * @throws:
	 */
	public void duplicateRoleNameException(List<RoleParentsDTO> parents,
			List<PermissionDTO> permissions) {
		// 角色名称为空
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		InsertRoleDTO roleDTO2 = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		insertRoleDTOs.add(roleDTO2);
		mockGetRoleByUUID();
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * duplicateRoleNameInDBException(数据库中角色名重复抛出异常)
	 *
	 * @Title: duplicateRoleNameInDBException
	 * @Description:
	 * @param: @param parents
	 * @param: @param permissions
	 * @param: @param insertRoleDTOs
	 * @return: void
	 * @throws:
	 */
	public void duplicateRoleNameInDBException(List<RoleParentsDTO> parents,
			List<PermissionDTO> permissions) {
		// 角色名称为空
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		when(roleDao.hasFetchRole(anyString(), anyString())).thenReturn(1);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * blankParentRolesAndPermissionsException(父角色和权限都为空抛出异常)
	 *
	 * @Title: blankParentRolesAndPermissionsException
	 * @Description:
	 * @param: @param parents
	 * @param: @param permissions
	 * @param: @param insertRoleDTOs
	 * @return: void
	 * @throws:
	 */
	public void blankParentRolesAndPermissionsException(List<RoleParentsDTO> parents,
			List<PermissionDTO> permissions) {
		// 角色名称为空
		InsertRoleDTO roleDTO = new InsertRoleDTO("", "role", "admin", false,
				"2018-01-03 16:45:23", null, null, null, null, null, parents, permissions);
		List<InsertRoleDTO> insertRoleDTOs = new ArrayList<>();
		insertRoleDTOs.add(roleDTO);
		thrown.expect(BadRequestFieldException.class);
		when(roleParentDao.hasFetchRoleParent(anyString(), anyString())).thenReturn(0);
		roleBiz.insertRoles(insertRoleDTOs);
	}

	/**
	 * buildPermissionDTO(这里用一句话描述这个方法的作用)
	 *
	 * @Title: buildPermissionDTO @Description: @param @return 设定文件 @return
	 * List<PermissionDTO> 返回类型 @throws
	 */
	private List<PermissionDTO> buildPermissionDTO() {
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		for (int i = 0; i < 10; i++) {
			PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources,
					null);
			permissions.add(permissionDTO);
		}
		return permissions;
	}

	/**
	 * buildRoleParentsDTO(这里用一句话描述这个方法的作用)
	 *
	 * @Title: buildRoleParentsDTO @Description: @param @return 设定文件 @return
	 * List<RoleParentsDTO> 返回类型 @throws
	 */
	private List<RoleParentsDTO> buildRoleParentsDTO() {
		List<RoleParentsDTO> parents = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			RoleParentsDTO roleParentsDTO = new RoleParentsDTO(
					"0118cccb-fe52-4cba-95c6-c92337ee63f" + i, "parentRole" + i,
					"2018-01-03 16:45:23");
			parents.add(roleParentsDTO);
		}
		return parents;
	}

	/**
	 * buildRole(构建角色)
	 *
	 * @Title: mockGetRoleByUUID
	 * @Description:
	 * @param 设定文件
	 * @return void
	 */
	private void mockGetRoleByUUID() {
		Role role = new Role();
		role.setNamespace("admin");
		role.setName("role");
		when(roleDao.getRoleByUUID(anyString())).thenReturn(role);
	}

	/**
	 * getResources(构建资源集合)
	 *
	 * @Title: getResources
	 * @Description:
	 * @param @return 设定文件
	 * @return List<String>
	 */
	private List<String> getResources() {
		String[] resourceStrs = { "web*" };
		List<String> resources = Arrays.asList(resourceStrs);
		return resources;
	}

	/**
	 * getActions(构建资源操作集合)
	 *
	 * @Title: getActions @Description: @param @return 设定文件 @return List<String>
	 * 返回类型 @throws
	 */
	private List<String> getActions() {
		String[] actionStrs = { "service:*", "service:create", "service:update",
				"service:view" };
		List<String> actions = Arrays.asList(actionStrs);
		return actions;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#getRoleDetail(java.lang.String)}.
	 */
	@Test
	public void testGetRoleDetail() {
		String actions = "[\"service:*\", \"service:create\", \"service:update\", \"service:view\"]";
		String resources = "[\"web*\"]";
		List<Permission> permissions = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Permission permission = new Permission(UUID.randomUUID().toString(),
					UUID.randomUUID().toString(), actions, resources, null);
			permissions.add(permission);
		}
		when(permissionDao.fetchPermissionList(anyString())).thenReturn(permissions);
		mockFetchRoleParentList();
		mockGetRoleByUUID();
		GetRoleDetailDTO roleDetailDTO = roleBiz
				.getRoleDetail(UUID.randomUUID().toString());
		assertEquals("role", roleDetailDTO.getName());
	}

	/**
	 * mockFetchRoleParentList(这里用一句话描述这个方法的作用)
	 *
	 * @Title: mockFetchRoleParentList
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void mockFetchRoleParentList() {
		List<RoleParent> parents = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			RoleParent roleParent = new RoleParent(UUID.randomUUID().toString(), null,
					"parnet" + i, null);
			parents.add(roleParent);
		}
		when(roleParentDao.fetchRoleParentList(anyString())).thenReturn(parents);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#modifyRole(java.lang.String, java.lang.Boolean, java.util.List, java.util.List)}.
	 */
	@Test
	public void testModifyRole() {
		mockGetRoleByUUID();
//		roleBiz.modifyRole(UUID.randomUUID().toString(), false, buildRoleParentsDTO(),
//				buildPermissionDTO());
	}

	/**
	 * blankPermissionsException
	 *
	 * @Title: blankPermissionsException
	 * @Description: 修改角色时资源为空
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankPermissionsException() {
		mockGetRoleByUUID();
//		roleBiz.modifyRole(UUID.randomUUID().toString(), false, buildRoleParentsDTO(),
//				null);
	}

	/**
	 * blankPermissionResourceExceptionByModify
	 *
	 * @Title: blankPermissionResourceExceptionByModify
	 * @Description: 修改角色时权限的资源为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankPermissionResourceExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, null, null);
		permissions.add(permissionDTO);
//		roleBiz.modifyRole(UUID.randomUUID().toString(), false, buildRoleParentsDTO(),
//				permissions);
	}

	/**
	 * blankPermissionActionExceptionByModify
	 *
	 * @Title: blankPermissionActionExceptionByModify
	 * @Description: 修改角色时权限的操作为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankPermissionActionExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", null, resources, null);
		permissions.add(permissionDTO);
//		roleBiz.modifyRole(UUID.randomUUID().toString(), false, buildRoleParentsDTO(),
//				permissions);
	}

	/**
	 * roleIdEqualsParnetRoleIdExceptionByModify
	 *
	 * @Title: roleIdEqualsParnetRoleIdExceptionByModify
	 * @Description: 父角色的id等于角色本身异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void roleIdEqualsParnetRoleIdExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources, null);
		permissions.add(permissionDTO);
		String roleId = UUID.randomUUID().toString();
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(roleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
//		roleBiz.modifyRole(roleId, false, parents, permissions);
	}

	/**
	 * roleNamespaceNeParnetRoleNamespaceExceptionByModify
	 *
	 * @Title: roleNamespaceNeParnetRoleNamespaceExceptionByModify
	 * @Description: 角色namespace和父角色namespace不一致异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void roleNamespaceNeParnetRoleNamespaceExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources, null);
		permissions.add(permissionDTO);
		String roleId = UUID.randomUUID().toString();
		String parentRoleId = UUID.randomUUID().toString();
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		Role parentRole = new Role();
		parentRole.setNamespace("parentAdmin");
		parentRole.setName("parentRole");
		when(roleDao.getRoleByUUID(parentRoleId)).thenReturn(parentRole);
//		roleBiz.modifyRole(roleId, false, parents, permissions);
	}

	/**
	 * recursionParentRoleExceptionByModify
	 *
	 * @Title: recursionParentRoleExceptionByModify
	 * @Description: 角色之间不能相互继承
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void recursionParentRoleExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources, null);
		permissions.add(permissionDTO);
		String roleId = UUID.randomUUID().toString();
		String parentRoleId = UUID.randomUUID().toString();
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		Role parentRole = new Role();
		parentRole.setUuid(parentRoleId);
		parentRole.setNamespace("admin");
		parentRole.setName("parentRole");
		when(roleDao.getRoleByUUID(parentRoleId)).thenReturn(parentRole);
		List<RoleParent> roleParents = new ArrayList<>();
		RoleParent roleParent = new RoleParent(parentRoleId, roleId, null, null);
		roleParents.add(roleParent);
		when(roleParentDao.fetchRoleParentList(parentRoleId)).thenReturn(roleParents);
//		roleBiz.modifyRole(roleId, false, parents, permissions);
	}

	/**
	 * recursionRoleOutMaxExceptionByModify
	 *
	 * @Title: recursionRoleOutMaxExceptionByModify
	 * @Description: 角色继承超过4级异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void recursionRoleOutMaxExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources, null);
		permissions.add(permissionDTO);
		String roleId = UUID.randomUUID().toString();
		String parentRoleId = UUID.randomUUID().toString();
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		Role parentRole = new Role();
		parentRole.setUuid(parentRoleId);
		parentRole.setNamespace("admin");
		parentRole.setName("parentRole");
		when(roleDao.getRoleByUUID(parentRoleId)).thenReturn(parentRole);
		String parentRoleId2 = UUID.randomUUID().toString();
		List<RoleParent> roleParents = new ArrayList<>();
		RoleParent roleParent = new RoleParent(parentRoleId, parentRoleId2, null, null);
		roleParents.add(roleParent);
		when(roleParentDao.fetchRoleParentList(parentRoleId)).thenReturn(roleParents);

		String parentRoleId3 = UUID.randomUUID().toString();
		List<RoleParent> roleParents2 = new ArrayList<>();
		RoleParent roleParent2 = new RoleParent(parentRoleId2, parentRoleId3, null, null);
		roleParents2.add(roleParent2);
		when(roleParentDao.fetchRoleParentList(parentRoleId2)).thenReturn(roleParents2);

		String parentRoleId4 = UUID.randomUUID().toString();
		List<RoleParent> roleParents3 = new ArrayList<>();
		RoleParent roleParent3 = new RoleParent(parentRoleId3, parentRoleId4, null, null);
		roleParents3.add(roleParent3);
		when(roleParentDao.fetchRoleParentList(parentRoleId3)).thenReturn(roleParents3);

		/*
		 * String parentRoleId5 = UUID.randomUUID().toString(); List<RoleParent>
		 * roleParents4 = new ArrayList<>(); RoleParent roleParent4 = new
		 * RoleParent(parentRoleId4, parentRoleId5, null, null);
		 * roleParents4.add(roleParent4);
		 * when(roleParentDao.fetchRoleParentList(parentRoleId4)).thenReturn(roleParents4)
		 * ;
		 */
		String childRoleId1 = UUID.randomUUID().toString();

		List<RoleParent> roleChilds1 = new ArrayList<>();
		RoleParent roleChild1 = new RoleParent(childRoleId1, roleId, null, null);
		roleChilds1.add(roleChild1);
		when(roleParentDao.fetchRoleChildrenList(roleId)).thenReturn(roleChilds1);

//		roleBiz.modifyRole(roleId, false, parents, permissions);
	}

	/**
	 * duplicationParentRoleExceptionByModify
	 *
	 * @Title: duplicationParentRoleExceptionByModify
	 * @Description: 父角色重复异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void duplicationParentRoleExceptionByModify() {
		mockGetRoleByUUID();
		List<PermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		PermissionDTO permissionDTO = new PermissionDTO("", "", actions, resources, null);
		permissions.add(permissionDTO);
		String roleId = UUID.randomUUID().toString();
		String parentRoleId = UUID.randomUUID().toString();
		List<RoleParentsDTO> parents = new ArrayList<>();
		RoleParentsDTO roleParentsDTO = new RoleParentsDTO(parentRoleId, "parentRole",
				"2018-01-03 16:45:23");
		parents.add(roleParentsDTO);
		parents.add(roleParentsDTO);
		Role parentRole = new Role();
		parentRole.setUuid(parentRoleId);
		parentRole.setNamespace("admin");
		parentRole.setName("parentRole");
		when(roleDao.getRoleByUUID(parentRoleId)).thenReturn(parentRole);
		List<RoleParent> roleParents = new ArrayList<>();
		RoleParent roleParent = new RoleParent(parentRoleId, UUID.randomUUID().toString(),
				null, null);
		roleParents.add(roleParent);
		when(roleParentDao.fetchRoleParentList(parentRoleId)).thenReturn(roleParents);
//		roleBiz.modifyRole(roleId, false, parents, permissions);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#deleteRole(java.lang.String)}.
	 */
	@Test
	public void testDeleteRole() {
		mockGetRoleByUUID();
		roleBiz.deleteRole(UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#insertParentRole(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testInsertParentRole() {
		mockGetRoleByUUID();
		roleBiz.insertParentRole(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), "parent");
		roleIdEqualParentIdException();
	}

	/**
	 * roleNamespaceNotParanetNamespaceException
	 *
	 * @Title: roleNamespaceNotParanetNamespaceException
	 * @Description: 角色命名空间和父角色命名空间不同异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void roleNamespaceNotParanetNamespaceException() {
		String uuid = UUID.randomUUID().toString();
		String parentUuid = UUID.randomUUID().toString();
		Role role = new Role();
		role.setUuid(uuid);
		role.setNamespace("admin");
		role.setName("role");
		Role parentRole = new Role();
		parentRole.setUuid(parentUuid);
		parentRole.setNamespace("admin1");
		parentRole.setName("role");
		when(roleDao.getRoleByUUID(uuid)).thenReturn(role);
		when(roleDao.getRoleByUUID(parentUuid)).thenReturn(parentRole);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertParentRole(uuid, parentUuid, "parent");
	}

	/**
	 * parentRoleAlreadyExistException
	 *
	 * @Title: parentRoleAlreadyExistException
	 * @Description: 父角色已经存在异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test
	public void parentRoleAlreadyExistException() {
		String uuid = UUID.randomUUID().toString();
		String parentUuid = UUID.randomUUID().toString();
		Role role = new Role();
		role.setUuid(uuid);
		role.setNamespace("admin");
		role.setName("role");
		Role parentRole = new Role();
		parentRole.setUuid(parentUuid);
		parentRole.setNamespace("admin1");
		parentRole.setName("role");
		when(roleDao.getRoleByUUID(uuid)).thenReturn(role);
		when(roleDao.getRoleByUUID(parentUuid)).thenReturn(role);
		when(roleParentDao.hasFetchRoleParent(uuid, parentUuid)).thenReturn(1);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertParentRole(uuid, parentUuid, "parent");
	}

	/**
	 * roleIdEqualParentIdException(角色ID等于父角色ID异常)
	 *
	 * @Title: roleIdEqualParentIdException
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void roleIdEqualParentIdException() {
		List<RoleParent> parents = new ArrayList<>();
		String uuid = UUID.randomUUID().toString();
		RoleParent roleParent = new RoleParent(uuid, null, "parnet", null);
		parents.add(roleParent);
		String parentId = uuid;
		when(roleParentDao.fetchRoleParentList(parentId)).thenReturn(parents);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.insertParentRole(parentId, parentId, "parent");
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#deleteParentRole(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteParentRole() {
		mockGetRoleByUUID();
		when(roleParentDao.hasFetchRoleParent(anyString(), anyString())).thenReturn(1);
		roleBiz.deleteParentRole(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}
	/**
	 * parentRoleNotExistByDelete
	 *
	 * @Title: parentRoleNotExistByDelete
	 * @Description: 删除时父角色不存在异常
	 * @param:     
	 * @return: void   
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void parentRoleNotExistExceptionByDelete() {
		mockGetRoleByUUID();
		roleBiz.deleteParentRole(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}
	
	/**
	  * roleNotExistByDelete
	  *
	  * @Title: roleNotExistByDelete
	  * @Description: 删除时角色不存在异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void roleNotExistExceptionByDelete() {
		when(roleDao.getRoleByUUID(anyString())).thenReturn(new Role());
		roleBiz.deleteParentRole(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#insertRolePermission(java.lang.String, java.util.List, java.util.List, java.util.List)}.
	 */
	@Test
	public void testInsertRolePermission() {
		mockGetRoleByUUID();
		roleBiz.insertRolePermission(UUID.randomUUID().toString(), getActions(),
				getResources(), null);
	}
	/**
	  * roleNotExistExceptionByInsertRolePermission
	  *
	  * @Title: roleNotExistExceptionByInsertRolePermission
	  * @Description: 添加权限时角色不存在。
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void roleNotExistExceptionByInsertRolePermission() {
		when(roleDao.getRoleByUUID(anyString())).thenReturn(new Role());
		roleBiz.insertRolePermission(UUID.randomUUID().toString(), getActions(),
				getResources(), null);
	}
	/**
	  * blankResourceExceptionByInsertRolePermission
	  *
	  * @Title: blankResourceExceptionByInsertRolePermission
	  * @Description: 资源为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankResourceExceptionByInsertRolePermission() {
		when(roleDao.getRoleByUUID(anyString())).thenReturn(new Role());
		roleBiz.insertRolePermission(UUID.randomUUID().toString(), getActions(),
				null, null);
	}
	/**
	  * blankActionExceptionByInsertRolePermission
	  *
	  * @Title: blankActionExceptionByInsertRolePermission
	  * @Description: 操作集合为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankActionExceptionByInsertRolePermission() {
		when(roleDao.getRoleByUUID(anyString())).thenReturn(new Role());
		roleBiz.insertRolePermission(UUID.randomUUID().toString(),null,
				getResources(), null);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#modifyRolePermission(java.lang.String, java.lang.String, java.util.List, java.util.List, java.util.List)}.
	 */
	@Test
	public void testModifyRolePermission() {
		mockGetRoleByUUID();
		when(roleDao.hasGetRoleByUUID(anyString())).thenReturn(1);
		when(permissionDao.hasFetchPermissionByUuid(anyString())).thenReturn(1);
		roleBiz.modifyRolePermission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), getActions(), getResources(), null);
	}
	/**
	  * roleNotExistExceptionByModifyRolePermission
	  *
	  * @Title: roleNotExistExceptionByModifyRolePermission
	  * @Description: 修改权限时，角色不存在
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void roleNotExistExceptionByModifyRolePermission() {
		mockGetRoleByUUID();
		when(roleDao.hasGetRoleByUUID(anyString())).thenReturn(0);
		when(permissionDao.hasFetchPermissionByUuid(anyString())).thenReturn(1);
		roleBiz.modifyRolePermission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), getActions(), getResources(), null);
	}
	/**
	  * permissionNotExistExceptionByModifyRolePermission
	  *
	  * @Title: permissionNotExistExceptionByModifyRolePermission
	  * @Description: 修改权限时，原来的权限不存在
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void permissionNotExistExceptionByModifyRolePermission() {
		mockGetRoleByUUID();
		when(roleDao.hasGetRoleByUUID(anyString())).thenReturn(1);
		when(permissionDao.hasFetchPermissionByUuid(anyString())).thenReturn(0);
		roleBiz.modifyRolePermission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString(), getActions(), getResources(), null);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#deleteRolePermission(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteRolePermission() {
		when(roleDao.hasGetRoleByUUID(anyString())).thenReturn(1);
		when(permissionDao.hasFetchPermissionByUuid(anyString())).thenReturn(1);
		roleBiz.deleteRolePermission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#listRolesAssigned(java.lang.String, java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testListRolesAssigned() {
		String[] roleIds = { UUID.randomUUID().toString() };
		List<RoleUsers> roleUsersList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			RoleUsers roleUsers = new RoleUsers(UUID.randomUUID().toString(), "role" + i,
					"admin", "admin1", new Timestamp(System.currentTimeMillis()));
			roleUsersList.add(roleUsers);
		}
		when(roleUsersDao.fetchRolesAssignedList(anyString(), anyString(),
				any(String[].class))).thenReturn(roleUsersList);
		List<ListRolesAssignedDTO> listRolesAssignedDTO = roleBiz
				.listRolesAssigned("admin", roleIds, "admin1");
		assertEquals(10, listRolesAssignedDTO.size());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#rolesAssigned(java.util.List)}.
	 */
	@Test
	public void testRolesAssigned() {
		List<RolesAssignedRevokeDTO> dtoList = getRolesAssignedRevokeDTO();
		mockdFetchRoleDetail();
		roleBiz.rolesAssigned(dtoList);
		blankRoleNameRolesAssignedException();
	}
	
	/**
	  * blankRoleNamespaceRolesAssignedException
	  *
	  * @Title: blankRoleNamespaceRolesAssignedException
	  * @Description: 分配角色是，命名空间为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankRoleNamespaceRolesAssignedException() {
		List<RolesAssignedRevokeDTO> dtoList = new ArrayList<>();
		RolesAssignedRevokeDTO rolesAssignedRevokeDTO = new RolesAssignedRevokeDTO(
				UUID.randomUUID().toString(), "role1", "admin1", "");
		dtoList.add(rolesAssignedRevokeDTO);
		roleBiz.rolesAssigned(dtoList);
	}
	
	/**
	  * blankUserRolesAssignedException
	  *
	  * @Title: blankUserRolesAssignedException
	  * @Description: 分配角色时，用户名为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankUserRolesAssignedException() {
		List<RolesAssignedRevokeDTO> dtoList = new ArrayList<>();
		RolesAssignedRevokeDTO rolesAssignedRevokeDTO = new RolesAssignedRevokeDTO(
				UUID.randomUUID().toString(), "role1", "", "admin");
		dtoList.add(rolesAssignedRevokeDTO);
		roleBiz.rolesAssigned(dtoList);
	}
	/**
	  * blankRolesExceptionByRolesAssigned
	  *
	  * @Title: blankRolesExceptionByRolesAssigned
	  * @Description: 角色为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankRolesExceptionByRolesAssigned() {
		roleBiz.rolesAssigned(null);
	}
	/**
	 * blankRoleNameRolesAssignedException(角色名称为空抛出异常)
	 *
	 * @Title: blankRoleNameRolesAssignedException
	 * @Description:
	 * @param:
	 * @return: void
	 * @throws:
	 */
	private void blankRoleNameRolesAssignedException() {
		List<RolesAssignedRevokeDTO> dtoList = new ArrayList<>();
		RolesAssignedRevokeDTO rolesAssignedRevokeDTO = new RolesAssignedRevokeDTO(
				UUID.randomUUID().toString(), "", "admin1", "admin");
		dtoList.add(rolesAssignedRevokeDTO);
		thrown.expect(BadRequestFieldException.class);
		roleBiz.rolesAssigned(dtoList);
	}

	/**
	 * buildFetchRoleDetail(mock FetchRoleDetail方法)
	 *
	 * @Title: buildFetchRoleDetail @Description: @param 设定文件 @return void 返回类型 @throws
	 */
	private void mockdFetchRoleDetail() {
		Role role = new Role();
		when(roleDao.fetchRoleDetail(anyString(), anyString(), null)).thenReturn(role);
	}

	/**
	 * getRolesAssignedRevokeDTO(构建RolesAssignedRevokeDTO集合)
	 *
	 * @Title: getRolesAssignedRevokeDTO @Description: @param @return 设定文件 @return
	 * List<RolesAssignedRevokeDTO> 返回类型 @throws
	 */
	private List<RolesAssignedRevokeDTO> getRolesAssignedRevokeDTO() {
		List<RolesAssignedRevokeDTO> dtoList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			RolesAssignedRevokeDTO rolesAssignedRevokeDTO = new RolesAssignedRevokeDTO(
					UUID.randomUUID().toString(), "role" + i, "admin1", "admin");
			dtoList.add(rolesAssignedRevokeDTO);
		}
		return dtoList;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#rolesRevoke(java.util.List)}.
	 */
	@Test
	public void testRolesRevoke() {
		mockdFetchRoleDetail();
		mockRoleRevoke();
		roleBiz.rolesRevoke(getRolesAssignedRevokeDTO());
	}
	/**
	  * blankRoleExceptionByRevoke
	  *
	  * @Title: blankRoleExceptionByRevoke
	  * @Description: 角色为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankRoleExceptionByRevoke() {
		when(roleDao.fetchRoleDetail(anyString(), anyString(), null)).thenReturn(new Role());
		mockRoleRevoke();
		roleBiz.rolesRevoke(getRolesAssignedRevokeDTO());
	}
	
	/**
	  * roleNotAssignExceptionByRevoke
	  *
	  * @Title: roleNotAssignExceptionByRevoke
	  * @Description: 撤销时，角色没有分配异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void roleNotAssignExceptionByRevoke() {
		mockdFetchRoleDetail();
		when(roleUsersDao.hasFetchRoleUsersList(anyString(), anyString(), anyString()))
		.thenReturn(0);
		roleBiz.rolesRevoke(getRolesAssignedRevokeDTO());
	}
	/**
	  * blankRolesDtoExceptionByRevoke
	  *
	  * @Title: blankRolesDtoExceptionByRevoke
	  * @Description: 空角色异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankRolesDtoExceptionByRevoke() {
		roleBiz.rolesRevoke(null);
	}

	/**
	 * mockRoleRevoke(mock RoleRevoke方法)
	 *
	 * @Title: mockRoleRevoke @Description: @param 设定文件 @return void 返回类型 @throws
	 */
	private void mockRoleRevoke() {
		when(roleUsersDao.hasFetchRoleUsersList(anyString(), anyString(), anyString()))
				.thenReturn(1);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz#rolesRevokeAll(java.lang.String, java.util.List)}.
	 */
	@Test
	public void testRolesRevokeAll() {
		List<String> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			userList.add("admin" + i);
		}
		mockRoleRevoke();
		mockdFetchRoleDetail();
		roleBiz.rolesRevokeAll("admin", userList);
	}
	
	/**
	  * blankExceptionRolesRevokeAll
	  *
	  * @Title: blankExceptionRolesRevokeAll
	  * @Description: 撤销角色时用户集合参数为空
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void blankExceptionRolesRevokeAll() {
		roleBiz.rolesRevokeAll("admin", null);
	}

}
