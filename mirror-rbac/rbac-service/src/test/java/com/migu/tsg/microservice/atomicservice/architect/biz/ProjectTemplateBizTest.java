/**
 * @Title: ProjectTemplateBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.architect.biz
 * @Description: ProjectTemplateBiz.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月18日 下午5:07:46
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.architect.biz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateItemDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplate;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplateItem;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateResourceDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleParentDTO;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRolePermissionDTO;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.util.FileUtil;

/**
 * @ClassName: ProjectTemplateBizTest
 * @Description:
 * @author botao gao
 * @date 2018年1月18日 下午5:07:46
 *
 */
@RunWith(SpringRunner.class)
public class ProjectTemplateBizTest {

	@Mock
	private ProjectTemplateDao projectTemplateDao;

	@Mock
	private ProjectTemplateItemDao projectTemplateItemDao;

	@InjectMocks
	private ProjectTemplateBiz projectTemplateBiz;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectTemplateBiz#fetchProjectTemplateList(java.util.List)}.
	 */
	@Test
	public void testFetchProjectTemplateList() {
		String[] uuidStrs = { UUID.randomUUID().toString(), UUID.randomUUID().toString() };
		List<String> uuids = Arrays.asList(uuidStrs);
		List<ProjectTemplate> list = new ArrayList<>();
		List<ProjectTemplateItem> projectTemplateItems = new ArrayList<>();
		String itemData = FileUtil.readFile("json/projectTemplateItem.json");
		for (int i = 0; i < 5; i++) {
			ProjectTemplateItem projectTemplateItem = new ProjectTemplateItem(UUID.randomUUID().toString(), i+1, "item"+i, 1, "", "role", itemData);
			projectTemplateItems.add(projectTemplateItem);
		}
		for (int i = 0; i < uuids.size(); i++) {
			ProjectTemplate projectTemplate = new ProjectTemplate(uuids.get(i), "template" + i,
					new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()),
					projectTemplateItems);
			list.add(projectTemplate);
		}
		when(projectTemplateDao.listProjectTemplateByUuids(uuids)).thenReturn(list);
		projectTemplateBiz.fetchProjectTemplateList(uuids);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectTemplateBiz#deleteProjectTemplate(java.lang.String)}.
	 */
	@Test
	public void testDeleteProjectTemplate() {
		projectTemplateBiz.deleteProjectTemplate(UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectTemplateBiz#createProjectTemplate(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest)}.
	 */
	@Test
	public void testCreateProjectTemplate() {
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		List<Integer> dependsOn = new ArrayList<>();
		List<ProjectTemplateRoleParentDTO> parents = new ArrayList<>();
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
			permissions.add(permission);
		}
		for(int i = 0;i<5;i++){
			ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(i+1, false, "role"+i, dependsOn, parents, permissions);
			roles.add(projectTemplateRoleDTO);
		}
		List<ProjectTemplateResourceDTO> projectTemplateResources = new ArrayList<>();
		ProjectTemplateResourceDTO projectTemplateResource = new ProjectTemplateResourceDTO(111,false,"resource",null,"resource",null);
		projectTemplateResources.add(projectTemplateResource);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, projectTemplateResources);
		CreateProjectTemplateResponse response = projectTemplateBiz.createProjectTemplate(request);
		assertEquals("template", response.getName());
	}
	/**
	  * blankNameException
	  *
	  * @Title: blankNameException
	  * @Description: 模版名称为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankNameException(){
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("", null, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * projectTemplateAlreadyExist
	  *
	  * @Title: projectTemplateAlreadyExist
	  * @Description: 项目模版已经存在
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void projectTemplateAlreadyExist(){
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", null, null);
		thrown.expect(BadRequestFieldException.class);
		when(projectTemplateDao.countProjectTemplateByName(request.getName())).thenReturn(1);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankRoleIdException
	  *
	  * @Title: blankRoleIdException
	  * @Description: 角色id为空抛出异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankRoleIdException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(null, false, "role", null, null, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * blankRoleNameException
	  *
	  * @Title: blankRoleNameException
	  * @Description: 角色名称为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankRoleNameException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "", null, null, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * RoleNameAlreadyExistException
	  *
	  * @Title: RoleNameAlreadyExistException
	  * @Description: 项目模板角色名称重复异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void RoleNameAlreadyExistException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, null, null);
		ProjectTemplateRoleDTO projectTemplateRoleDTO2 = new ProjectTemplateRoleDTO(2, false, "role", null, null, null);
		roles.add(projectTemplateRoleDTO);
		roles.add(projectTemplateRoleDTO2);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankParentRoleAndPermissionException
	  *
	  * @Title: blankParentRoleAndPermissionException
	  * @Description: 父角色和权限都为空
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankParentRoleAndPermissionException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, null, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankParentRoleItemException
	  *
	  * @Title: blankParentRoleItemException
	  * @Description: 父角色item为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankParentRoleItemException(){
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(null,"parentRole");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, parentRoles, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankParentRoleNameException
	  *
	  * @Title: blankParentRoleNameException
	  * @Description: 父角色名称为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankParentRoleNameException(){
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(2,"");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, parentRoles, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * parentRoleItemEqualRoleIdException
	  *
	  * @Title: parentRoleItemEqualRoleIdException
	  * @Description: 父角色的item和角色id相同异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void parentRoleItemEqualRoleIdException(){
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(1,"parentRole");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, parentRoles, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * parentRoleItemNotExistException
	  *
	  * @Title: parentRoleItemNotExistException
	  * @Description: 父角色不存在异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void parentRoleItemNotExistException(){
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(2,"parentRole");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, parentRoles, null);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	 * parentRoleNameNotExistException
	 *
	 * @Title: parentRoleNameNotExistException
	 * @Description: 父角色名称不存在异常
	 * @param:     
	 * @return: void   
	 * @throws:
	 */
	@Test
	public void parentRoleNameNotExistException(){
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
			permissions.add(permission);
		}
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(2,"parentRole");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, parentRoles, null);
		ProjectTemplateRoleDTO projectTemplateRoleDTO2 = new ProjectTemplateRoleDTO(2, false, "role2", null, null, permissions);
		roles.add(projectTemplateRoleDTO);
		roles.add(projectTemplateRoleDTO2);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * dependsOnRoleNotExistException
	  *
	  * @Title: dependsOnRoleNotExistException
	  * @Description: 依赖的角色ID不存在异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void dependsOnRoleNotExistException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		List<Integer> dependsOn = new ArrayList<>();
		dependsOn.add(2);
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
			permissions.add(permission);
		}
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", dependsOn, null, permissions);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * dependsOnRoleEqualSelfException
	  *
	  * @Title: dependsOnRoleEqualSelfException
	  * @Description: 依赖的角色是本身异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void dependsOnRoleEqualSelfException(){
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		List<Integer> dependsOn = new ArrayList<>();
		dependsOn.add(1);
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
			permissions.add(permission);
		}
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", dependsOn, null, permissions);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * parentRolesNeDependsOnException
	  *
	  * @Title: parentRolesNeDependsOnException
	  * @Description: 父角色和依赖都不为空，二者不相等异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void parentRolesNeDependsOnException(){
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		for(int i=0;i<5;i++){
			ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
			permissions.add(permission);
		}
		List<Integer> dependsOn = new ArrayList<>();
		dependsOn.add(2);
		dependsOn.add(3);
		List<ProjectTemplateRoleParentDTO> parentRoles = new ArrayList<>();
		ProjectTemplateRoleParentDTO parentRole = new ProjectTemplateRoleParentDTO(2,"parentRole");
		parentRoles.add(parentRole);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", dependsOn, parentRoles, null);
		ProjectTemplateRoleDTO projectTemplateRoleDTO2 = new ProjectTemplateRoleDTO(2, false, "parentRole", null, null, permissions);
		ProjectTemplateRoleDTO projectTemplateRoleDTO3 = new ProjectTemplateRoleDTO(3, false, "parentRole2", null, null, permissions);
		roles.add(projectTemplateRoleDTO);
		roles.add(projectTemplateRoleDTO2);
		roles.add(projectTemplateRoleDTO3);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankPermissionResourceNameException
	  *
	  * @Title: blankPermissionResourceNameException
	  * @Description: 权限资源名称为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankPermissionResourceNameException(){
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		List<String> resources = new ArrayList<>();
		List<Map<String, String>> constraints = new ArrayList<>();
		ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
		permissions.add(permission);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, null, permissions);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankPermissionActionsException
	  *
	  * @Title: blankPermissionActionsException
	  * @Description: 权限的操作集合为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test
	public void blankPermissionActionsException(){
		List<ProjectTemplateRolePermissionDTO> permissions = new ArrayList<>();
		List<String> actions = new ArrayList<>();
		List<String> resources = new ArrayList<>();
		resources.add("web*");
		List<Map<String, String>> constraints = new ArrayList<>();
		ProjectTemplateRolePermissionDTO permission = new ProjectTemplateRolePermissionDTO(actions, resources, constraints);
		permissions.add(permission);
		List<ProjectTemplateRoleDTO> roles = new ArrayList<>();
		ProjectTemplateRoleDTO projectTemplateRoleDTO = new ProjectTemplateRoleDTO(1, false, "role", null, null, permissions);
		roles.add(projectTemplateRoleDTO);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", roles, null);
		thrown.expect(BadRequestFieldException.class);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * blankResourceIdException
	  *
	  * @Title: blankResourceIdException
	  * @Description: 资源id为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected=BadRequestFieldException.class)
	public void blankResourceIdException(){
		List<ProjectTemplateResourceDTO> resources = new ArrayList<>();
		ProjectTemplateResourceDTO resource = new ProjectTemplateResourceDTO(null,false,"resource",null,"resource",null);
		resources.add(resource);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", null, resources);
		projectTemplateBiz.createProjectTemplate(request);
	}
	
	/**
	  * duplicateResourceIdException
	  *
	  * @Title: duplicateResourceIdException
	  * @Description: 资源id重复异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected=BadRequestFieldException.class)
	public void duplicateResourceIdException(){
		List<ProjectTemplateResourceDTO> resources = new ArrayList<>();
		ProjectTemplateResourceDTO resource = new ProjectTemplateResourceDTO(1,false,"resource",null,"resource",null);
		ProjectTemplateResourceDTO resource2 = new ProjectTemplateResourceDTO(1,false,"resource2",null,"resource",null);
		resources.add(resource);
		resources.add(resource2);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", null, resources);
		projectTemplateBiz.createProjectTemplate(request);
	}
	/**
	  * blankResourceNameException
	  *
	  * @Title: blankResourceNameException
	  * @Description: 资源名称为空异常
	  * @param:     
	  * @return: void   
	  * @throws:
	  */
	@Test(expected=BadRequestFieldException.class)
	public void blankResourceNameException(){
		List<ProjectTemplateResourceDTO> resources = new ArrayList<>();
		ProjectTemplateResourceDTO resource = new ProjectTemplateResourceDTO(1,false,"",null,"resource",null);
		resources.add(resource);
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest("template", null, resources);
		projectTemplateBiz.createProjectTemplate(request);
	}
}
