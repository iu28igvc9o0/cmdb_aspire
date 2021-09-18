/**
 * @Title: RoleControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.controller
 * @Description: RoleController.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月23日 上午10:29:35
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.rbac.biz.RoleBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.GetRoleDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesAssignedRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.RolesRevokeRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.GetRoleDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertRolePermissionsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesAssignedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListRolesDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolePermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolesAssignedRevokeDTO;

/**
 * @ClassName: RoleControllerUT
 * @Description: RoleController.java 单元测试类
 * @author botao gao
 * @date 2018年1月23日 上午10:29:35
 *
 */
@RunWith(SpringRunner.class)
public class RoleControllerUTTest {
	/**
	 * @Fields NAMESPACE : （用一句话描述这个变量表示什么）
	 */

	private static final String NAMESPACE = "admin";

	/**
	 * @Fields USER : （用一句话描述这个变量表示什么）
	 */

	private static final String USER = "admin1";

	@Mock
	private RoleBiz roleBiz;

	@InjectMocks
	private RoleController roleController;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRoles(java.util.List, java.lang.String)}.
	 */
	@Test
	public void testListRoles() {
		List<String> uuids = buildRoleUuids();
		List<ListRolesDTO> roles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ListRolesDTO role = new ListRolesDTO(UUID.randomUUID().toString(), "role" + i,
					false, NAMESPACE, "", "", i, null);
			roles.add(role);
		}
		when(roleBiz.listRoles(uuids.toArray(new String[] {}), USER, null, null, null)).thenReturn(roles);
		roleController.listRoles(uuids, USER, null, null, null);
	}

	/**
	 * buildRoleUuids(这里用一句话描述这个方法的作用)
	 *
	 * @Title: buildRoleUuids
	 * @Description:
	 * @param: @return
	 * @return: List<String>
	 * @throws:
	 */
	private List<String> buildRoleUuids() {
		List<String> uuids = new ArrayList<>();
		uuids.add(UUID.randomUUID().toString());
		uuids.add(UUID.randomUUID().toString());
		return uuids;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRoles(java.util.List)}.
	 */
	@Test
	public void testInsertRoles() {
		List<InsertRoleRequest> requests = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			InsertRoleRequest request = new InsertRoleRequest("role" + i, null, false,
					i, NAMESPACE, null, null, buildPermissionDTO());
			requests.add(request);
		}
		List<InsertRoleDTO> roles = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			InsertRoleDTO role = new InsertRoleDTO(UUID.randomUUID().toString(),
					"role" + i, NAMESPACE, false, "", "", "", null, null, null, null, buildPermission());
			roles.add(role);
		}
		when(roleBiz.insertRoles(roles)).thenReturn(roles);
		roleController.insertRoles(requests);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#getRoleDetail(java.lang.String)}.
	 */
	@Test
	public void testGetRoleDetail() {
		String roleUuid = UUID.randomUUID().toString();
		GetRoleDetailDTO roleDetailDTO = new GetRoleDetailDTO(roleUuid, "role", NAMESPACE,
				false, "", "", null, roleUuid, null, buildPermission());
		when(roleBiz.getRoleDetail(roleUuid)).thenReturn(roleDetailDTO);
		GetRoleDetailResponse response = roleController.getRoleDetail(roleUuid);
		assertEquals("role", response.getName());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRole(java.lang.String, com.migu.tsg.microservice.atomicservice.rbac.dto.ModifyRoleRequest)}.
	 */
	@Test
	public void testModifyRole() {
		String roleUuid = UUID.randomUUID().toString();
		ModifyRoleRequest request = new ModifyRoleRequest();
		roleController.modifyRole(roleUuid, request);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRole(java.lang.String)}.
	 */
	@Test
	public void testDeleteRole() {
		roleController.deleteRole(UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertParentRole(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertParentRoleRequest, java.lang.String)}.
	 */
	@Test
	public void testInsertParentRole() {
		InsertParentRoleRequest request = new InsertParentRoleRequest(
				UUID.randomUUID().toString(), "role");
		roleController.insertParentRole(request, UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteParentRole(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteParentRole() {
		roleController.deleteParentRole(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#insertRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String)}.
	 */
	@Test
	public void testInsertRolePermission() {
		String roleUuid = UUID.randomUUID().toString();
		InsertRolePermissionsRequest request = new InsertRolePermissionsRequest(
				getActions(), getResources(), null);
		InsertRolePermissionsDTO insertRolePermissionsDTO = new InsertRolePermissionsDTO(
				UUID.randomUUID().toString(), getActions(), getResources(), null);
		when(roleBiz.insertRolePermission(anyString(), anyList(), anyList(), anyList()))
				.thenReturn(insertRolePermissionsDTO);
		InsertRolePermissionsResponse response = roleController
				.insertRolePermission(request, roleUuid);
		response.getActions().contains("service:create");
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#modifyRolePermission(com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRolePermissionsRequest, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testModifyRolePermission() {
		roleController.modifyRolePermission(new InsertRolePermissionsRequest(),
				UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#deleteRolePermission(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeleteRolePermission() {
		roleController.deleteRolePermission(UUID.randomUUID().toString(),
				UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#listRolesAssigned(java.lang.String, java.util.List, java.lang.String)}.
	 */
	@Test
	public void testListRolesAssigned() {
		List<ListRolesAssignedDTO> listRolesAssignedDTO = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ListRolesAssignedDTO rolesAssignedDTO = new ListRolesAssignedDTO(
					UUID.randomUUID().toString(), "role" + i, NAMESPACE, USER, "");
			listRolesAssignedDTO.add(rolesAssignedDTO);
		}
		when(roleBiz.listRolesAssigned(anyString(), any(String[].class), anyString()))
				.thenReturn(listRolesAssignedDTO);
		roleController.listRolesAssigned(NAMESPACE, buildRoleUuids(), USER);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesAssigned(java.util.List)}.
	 */
	@Test
	public void testRolesAssigned() {
		List<RolesAssignedRequest> requests = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			RolesAssignedRequest request = new RolesAssignedRequest(
					UUID.randomUUID().toString(), "role" + i, USER, NAMESPACE);
			requests.add(request);
		}
		List<RolesAssignedRevokeDTO> dtoList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			RolesAssignedRevokeDTO rolesAssignedRevokeDTO = new RolesAssignedRevokeDTO(
					UUID.randomUUID().toString(), "role" + i, USER, NAMESPACE);
			dtoList.add(rolesAssignedRevokeDTO);
		}
		when(roleBiz.rolesAssigned(anyList())).thenReturn(dtoList);
		roleController.rolesAssigned(requests);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevoke(java.util.List)}.
	 */
	@Test
	public void testRolesRevoke() {
		List<RolesRevokeRequest> requests = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			RolesRevokeRequest request = new RolesRevokeRequest(
					UUID.randomUUID().toString(), "role" + i, USER, NAMESPACE);
			requests.add(request);
		}
		List<RolesAssignedRevokeDTO> dtoList = requests.stream().map(input -> {
			RolesAssignedRevokeDTO dto = new RolesAssignedRevokeDTO();
			BeanUtils.copyProperties(input, dto);
			return dto;
		}).collect(Collectors.toList());
		roleBiz.rolesRevoke(dtoList);
		roleController.rolesRevoke(requests);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.RoleController#rolesRevokeAll(java.lang.String, java.util.List)}.
	 */
	@Test
	public void testRolesRevokeAll() {
		String[] uuidStrs = {UUID.randomUUID().toString(),UUID.randomUUID().toString()}; 
		roleController.rolesRevokeAll(NAMESPACE, Arrays.asList(uuidStrs));
	}

	private List<RolePermissionDTO> buildPermissionDTO() {
		List<RolePermissionDTO> permissions = new ArrayList<>();
		List<String> actions = getActions();
		List<String> resources = getResources();
		for (int i = 0; i < 10; i++) {
			RolePermissionDTO permissionDTO = new RolePermissionDTO(actions, resources,
					null);
			permissions.add(permissionDTO);
		}
		return permissions;
	}

	private List<PermissionDTO> buildPermission() {
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

}
