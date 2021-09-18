/**
 * @Title: OrgsControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.controller
 * @Description: OrgsController.java 单元测试
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月22日 下午3:35:23
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;
import com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateOrgRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountPasswordRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.CreateOrgAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.FileUpload;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListOrgAccountsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.OrgDetailDTO;

/**
 * @ClassName: OrgsControllerUT
 * @Description: OrgsController.java 单元测试
 * @author botao gao
 * @date 2018年1月22日 下午3:35:23
 *
 */
@RunWith(SpringRunner.class)
public class OrgsControllerUTTest {

	private static final String NAMESPACE = "admin";
	private static final String USERNAME = "admin1";
	@Mock
	private OrgsBiz orgsBiz;

	@InjectMocks
	private OrgsController orgsController;

	@Before
	public void setUp() {

	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#updateSubAccountPassword(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountPasswordRequest, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateSubAccountPassword() {
		UpdateSubAccountPasswordRequest request = new UpdateSubAccountPasswordRequest(
				"12121212", "12121212");
		orgsController.updateSubAccountPassword(request, NAMESPACE, USERNAME);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#removeOrgAccount(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRemoveOrgAccount() {
		orgsController.removeOrgAccount(NAMESPACE, USERNAME);

	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#updateOrg(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateOrgRequest, java.lang.String)}.
	 */
	@Test
	public void testUpdateOrg() {
		UpdateOrgRequest request = new UpdateOrgRequest("migu", "1324@qq.com",
				"12121212121", "12121212121");
		OrgDetailDTO dto = buildOrgDetailDTO();
		when(orgsBiz.updateOrg(anyString(), anyString(), anyString(), anyString(),
				anyString())).thenReturn(dto);
		orgsController.updateOrg(request, NAMESPACE);
	}

	/**
	 * buildOrgDetailDTO(这里用一句话描述这个方法的作用)
	 *
	 * @Title: buildOrgDetailDTO
	 * @Description:
	 * @param: @return
	 * @return: OrgDetailDTO
	 * @throws:
	 */
	private OrgDetailDTO buildOrgDetailDTO() {
		OrgDetailDTO dto = new OrgDetailDTO(USERNAME, "2017-08-29T09:00:00.000Z", "",null, "",
				USERNAME, null, null, 1, "", "migu", "", 1, getProjects(), "1324@qq.com");
		return dto;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#getOrgDetail(java.lang.String)}.
	 */
	@Test
	public void testGetOrgDetail() {
		OrgDetailDTO dto = buildOrgDetailDTO();
		when(orgsBiz.getOrgDetail(NAMESPACE)).thenReturn(dto);
		orgsController.getOrgDetail(NAMESPACE);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#getOrgUserDetail(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetOrgUserDetail() {
		when(orgsBiz.getOrgUserDetail(NAMESPACE, USERNAME))
				.thenReturn(buildOrgDetailDTO());
		orgsController.getOrgUserDetail(NAMESPACE, USERNAME);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#createOrgAccount(com.migu.tsg.microservice.atomicservice.rbac.dto.CreateOrgAccountRequest, java.lang.String)}.
	 */
//	@Test
//	public void testCreateOrgAccount() {
//		List<InsertAccountDTO> accounts = new ArrayList<>();
//		for (int i = 0; i < 10; i++) {
//			InsertAccountDTO accoutDto = new InsertAccountDTO("admin" + i, "12121@qq.com",
//					getProjects());
//			accounts.add(accoutDto);
//		}
//
//		List<AccountRoleDTO> roles = new ArrayList<>();
//		for (int i = 0; i < 2; i++) {
//			AccountRoleDTO accountRoleDto = new AccountRoleDTO(
//					UUID.randomUUID().toString(), "role" + i);
//			roles.add(accountRoleDto);
//		}
//		CreateOrgAccountRequest request = new CreateOrgAccountRequest(null, accounts, roles,
//				"12121212121");
//		List<CreateOrgAccountDTO> createOrgAccountDTOList = new ArrayList<>();
//		for (int i = 0; i < 5; i++) {
//			CreateOrgAccountDTO createOrgAccountDTO = new CreateOrgAccountDTO("admin" + i,
//					"12121212121", "12121@qq.com", getProjects());
//			createOrgAccountDTOList.add(createOrgAccountDTO);
//		}
//		when(orgsBiz.createOrgAccount(NAMESPACE, accounts, roles, "12121212121"))
//				.thenReturn(createOrgAccountDTOList);
//		List<CreateOrgAccountResponse> result = orgsController.createOrgAccount(request,
//				NAMESPACE);
//		assertEquals(5, result.size());
//	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#listOrgAccounts(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)}.
	 */
	@Test
	public void testListOrgAccounts() {
		List<AccountDTO> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			AccountDTO user = new AccountDTO("admin" + i, LdapUserTypeEnum.user, "", "",
//					null, getProjects());
//			userList.add(user);
		}
		ListOrgAccountsDTO listOrgAccountsDTO = new ListOrgAccountsDTO();
		listOrgAccountsDTO.setCount(10);
		listOrgAccountsDTO.setNext(1);
		listOrgAccountsDTO.setNumPages(1);
		listOrgAccountsDTO.setPrevious(1);
		listOrgAccountsDTO.setResults(userList);
		when(orgsBiz.listOrgAccounts(anyString(), anyString(), anyString(), anyString(), anyString(),
				anyInt(), anyInt())).thenReturn(listOrgAccountsDTO);
		orgsController.listOrgAccounts(NAMESPACE, "", "","", "", 5, 1);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#fileUpload(com.migu.tsg.microservice.atomicservice.rbac.dto.model.FileUpload, java.lang.String)}.
	 */
	@Test
	public void testFileUpload() {
		FileUpload file = new FileUpload("12121");
		orgsController.fileUpload(file, NAMESPACE);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.controller.OrgsController#updateSubAccount(com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountRequest, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateSubAccount() {
		UpdateSubAccountRequest  request = new UpdateSubAccountRequest(null,null,"1212@qq.com",  null, null, null, null, null, null, null, getProjects(), false);
		UpdateSubAccountResponse response = new UpdateSubAccountResponse(request.getMail(), getProjects());
//		when(orgsBiz.updateSubAccount(anyString(),anyString(),anyString(),anyList())).thenReturn(response);
		orgsController.updateSubAccount(request, NAMESPACE, USERNAME);
	}

	private List<String> getProjects() {
		String[] projectStrs = { "porject1" };
		List<String> projects = Arrays.asList(projectStrs);
		return projects;
	}
}
