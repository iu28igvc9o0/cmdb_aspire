/**
 * @Title: OrgsBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.biz
 * @Description: 
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月17日 上午10:54:19
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.biz;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.InsertLdapMemberResponse;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import com.migu.tsg.microservice.atomicservice.ldap.enums.LdapUserTypeEnum;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Role;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UpdateSubAccountResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountRoleDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.CreateOrgAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.InsertAccountDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ListOrgAccountsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.OrgDetailDTO;

/**
 * @ClassName: OrgsBizTest
 * @Description:
 * @author botao gao
 * @date 2018年1月17日 上午10:54:19
 *
 */
@RunWith(SpringRunner.class)
public class OrgsBizTest {

	@Mock
	private RoleUsersDao roleUsersDao;

	@Mock
	private RoleDao roleDao;

	@Mock
	private LdapServiceClient ldapServiceClient;

	@InjectMocks
	private OrgsBiz orgsBiz;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#updateSubAccountPassword(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateSubAccountPassword() {
		orgsBiz.updateSubAccountPassword("121212121", "121212121", "admin", "admin1");
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#removeOrgAccount(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRemoveOrgAccount() {
		orgsBiz.removeOrgAccount("admin", "admin1");
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#updateOrg(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateOrg() {
		buildLdapAdmin();
		OrgDetailDTO dto = orgsBiz.updateOrg("migu", "123@qq.com", "121212121",
				"121212121", "admin");
		Assert.assertEquals("admin", dto.getUsername());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#updateSubAccount(java.lang.String, java.lang.String, java.lang.String, java.util.List)}.
	 */
	@Test
	public void testUpdateSubAccount() {
		List<String> projects = buildLdapMember();
//		UpdateSubAccountResponse user = orgsBiz.updateSubAccountPassword("admin", "admin1",
//				"123@qq.com", projects);
//		Assert.assertEquals("123@qq.com", user.getEmail());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#getOrgDetail(java.lang.String)}.
	 */
	@Test
	public void testGetOrgDetail() {
		buildLdapAdmin();
		OrgDetailDTO orgDetailDTO = orgsBiz.getOrgDetail("admin");
		Assert.assertEquals("admin", orgDetailDTO.getUsername());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#getOrgUserDetail(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetOrgUserDetail() {
		buildLdapMember();
		OrgDetailDTO orgDetailDTO = orgsBiz.getOrgUserDetail("admin", "admin1");
		Assert.assertEquals("admin1", orgDetailDTO.getUsername());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#createOrgAccount(java.lang.String, java.util.List, java.util.List, java.lang.String)}.
	 */
	@Test
	public void testCreateOrgAccount() {
		List<InsertLdapMemberResponse> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			InsertLdapMemberResponse ldapUserResponse = new InsertLdapMemberResponse(
					"admin" + i, LdapUserTypeEnum.user, "admin", "13675139941",
					"1212@qq.com", "121212121", "admin" + i, "migu", getProjects(),
					"2017-08-29T09:00:00.000Z");
			userList.add(ldapUserResponse);
		}
		Mockito.when(ldapServiceClient.insertLdapMembers(anyString(), anyList()))
				.thenReturn(userList);
		Mockito.when(roleUsersDao.createRoleUsersBatch(anyList())).thenReturn(10);
		Role role = new Role();
		Mockito.when(roleDao.getRoleByUUID(anyString())).thenReturn(role);
		List<InsertAccountDTO> accounts = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			InsertAccountDTO accoutDto = new InsertAccountDTO("admin" + i, "12121@qq.com",
//					null, null, getProjects());
//			accounts.add(accoutDto);
		}

		List<AccountRoleDTO> roles = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			AccountRoleDTO accountRoleDto = new AccountRoleDTO(
					"0118cccb-fe52-4cba-95c6-c92337ee63f" + i, "role" + i);
			roles.add(accountRoleDto);
		}
		List<CreateOrgAccountDTO> createOrgAccountDTO = orgsBiz.createOrgAccount("admin",
				accounts, roles, "1212121212121");
		Assert.assertEquals("admin0", createOrgAccountDTO.get(0).getUsername());
	}

	/**
	  * invalidRoleUuidExceptionByCreateOrgAccount
	  *
	  * @Title: invalidRoleUuidExceptionByCreateOrgAccount
	  * @Description: 角色集合中角色UUID不合法异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void invalidRoleUuidExceptionByCreateOrgAccount() {
		List<InsertAccountDTO> accounts = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			InsertAccountDTO accoutDto = new InsertAccountDTO("admin" + i, "12121@qq.com",
//					null, null, getProjects());
//			accounts.add(accoutDto);
		}
		List<AccountRoleDTO> roles = new ArrayList<>();
		AccountRoleDTO accountRoleDto = new AccountRoleDTO("1212", "role");
		roles.add(accountRoleDto);
		orgsBiz.createOrgAccount("admin", accounts, roles, "1212121212121");
	}
	/**
	  * duplicateRoleUuidExceptionByCreateOrgAccount
	  *
	  * @Title: duplicateRoleUuidExceptionByCreateOrgAccount
	  * @Description: 重复的角色uuid
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected = BadRequestFieldException.class)
	public void duplicateRoleUuidExceptionByCreateOrgAccount() {
		List<InsertAccountDTO> accounts = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			InsertAccountDTO accoutDto = new InsertAccountDTO("admin" + i, "12121@qq.com",
//					null, null, getProjects());
//			accounts.add(accoutDto);
		}
		List<AccountRoleDTO> roles = new ArrayList<>();
		String roleUuid = UUID.randomUUID().toString();
		AccountRoleDTO accountRoleDto = new AccountRoleDTO(roleUuid, "role");
		AccountRoleDTO accountRoleDto2 = new AccountRoleDTO(roleUuid, "role2");
		roles.add(accountRoleDto);
		roles.add(accountRoleDto2);
		orgsBiz.createOrgAccount("admin", accounts, roles, "1212121212121");
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#listOrgAccounts(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)}.
	 */
	@Test
	public void testListOrgAccounts() {
		List<GetLdapUserResponse> userList = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
//			GetLdapUserResponse ldapUserResponse = new GetLdapUserResponse("admin" + i,
//					"admin" + i, "13675139941", "14141@qq.com", "migu", "migu", "",
//					getProjects(), getProjects(), LdapUserTypeEnum.user, "admin",
//					"2017-08-29T09:00:00.000Z", "2017-08-29T09:00:00.000Z", null);
//			userList.add(ldapUserResponse);
		}
		ListPagenationResponse listPagenationResponse = new ListPagenationResponse();
		listPagenationResponse.setCurrentPage(1);
		listPagenationResponse.setFirst(1);
		listPagenationResponse.setLast(1);
		listPagenationResponse.setPrev(1);
		listPagenationResponse.setRowCount(10);
		listPagenationResponse.setPageCount(1);
		listPagenationResponse.setStartRow(1);
		listPagenationResponse.setNext(1);
		listPagenationResponse.setResults(userList);
		Mockito.when(ldapServiceClient.listLdapMember(anyString(), anyList(), anyList(), anyList(), anyList(),
				anyList(), anyInt(), anyInt())).thenReturn(listPagenationResponse);

		ListOrgAccountsDTO orgAccountsDTO = orgsBiz.listOrgAccounts("admin", "", "", "", "",
				10, 1);
		Assert.assertEquals("admin0", orgAccountsDTO.getResults().get(0).getUsername());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.OrgsBiz#fileUpload(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testFileUpload() {
		orgsBiz.fileUpload("1212", "admin");
	}

	/**
	 * blankExceptionFileUpload
	 *
	 * @Title: blankExceptionFileUpload
	 * @Description: 文件内容为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankExceptionFileUpload() {
		orgsBiz.fileUpload("", "admin");
	}

	/**
	 * getProjects(获取project列表)
	 *
	 * @Title: getProjects @Description: @param @return 设定文件 @return List<String>
	 * 返回类型 @throws
	 */
	private List<String> getProjects() {
		String[] projectStrs = { "porject1" };
		List<String> projects = Arrays.asList(projectStrs);
		return projects;
	}

	/**
	 * buildLdapAdmin(构造ldapAdmin对象) @Title: buildLdapAdmin @Description: @param
	 * 设定文件 @return void 返回类型 @throws
	 */
	private void buildLdapAdmin() {
//		List<String> projects = getProjects();
//		GetLdapUserResponse respone = new GetLdapUserResponse("admin", "admin",
//				"13675139941", "123@qq.com", "migu", "migu", "", projects, projects,
//				LdapUserTypeEnum.admin, "admin", "2017-08-29T09:00:00.000Z", "", null);
//		Mockito.when(ldapServiceClient.getLdapAdmin("admin")).thenReturn(respone);
	}

	/**
	 * buildLdapMember(构建子帐号用户)
	 *
	 * @Title: buildLdapMember @Description: @param @return 设定文件 @return List<String>
	 * 返回类型 @throws
	 */
	private List<String> buildLdapMember() {
		List<String> projects = getProjects();
//		GetLdapUserResponse respone = new GetLdapUserResponse("admin1", "admin1",
//				"13675139941", "123@qq.com", "migu", "migu", "", projects, projects,
//				LdapUserTypeEnum.user, "admin", "2017-08-29T09:00:00.000Z", "", null);
//		Mockito.when(ldapServiceClient.getLdapMember("admin", "admin1"))
//				.thenReturn(respone);
		return projects;
	}
}
