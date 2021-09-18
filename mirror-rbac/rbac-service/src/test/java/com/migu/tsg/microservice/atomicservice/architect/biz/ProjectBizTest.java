/**
 * @Title: ProjectBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.architect.biz
 * @Description: ProjectBiz.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月19日 下午2:08:27
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.architect.biz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.ProjectTemplateDao;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.Project;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplate;
import com.migu.tsg.microservice.atomicservice.architect.dao.po.ProjectTemplateItem;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.util.FileUtil;

/**
 * @ClassName: ProjectBizTest
 * @Description: ProjectBiz.java 单元测试类
 * @author botao gao
 * @date 2018年1月19日 下午2:08:27
 *
 */
@RunWith(SpringRunner.class)
public class ProjectBizTest {

	@Mock
	private ProjectTemplateDao projectTemplateDao;
	@Mock
	private ProjectDao projectDao;
	@InjectMocks
	private ProjectBiz projectBiz;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz#fetchProjectList(java.util.List)}.
	 */
	@Test
	public void testFetchProjectList() {
		String[] uuidStrs = { UUID.randomUUID().toString(),
				UUID.randomUUID().toString() };
		List<String> uuids = Arrays.asList(uuidStrs);
		List<Project> projects = new ArrayList<>();
		for (int i = 0; i < uuids.size(); i++) {
			Project project = new Project(uuids.get(i), "project" + i, "admin",
					"template" + i, UUID.randomUUID().toString(), "success",
					new Timestamp(System.currentTimeMillis()), null);
			projects.add(project);
		}
		when(projectDao.listProjectByUuids(uuids)).thenReturn(projects);
		projectBiz.fetchProjectList(uuids);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz#fetchProjectByUuid(java.lang.String)}.
	 */
	@Test
	public void testFetchProjectByUuid() {
		String uuid = UUID.randomUUID().toString();
		Project project = new Project(uuid, "project", "admin", "template",
				UUID.randomUUID().toString(), "success",
				new Timestamp(System.currentTimeMillis()), null);
		when(projectDao.getProjectByUuid(uuid)).thenReturn(project);
		FetchProjectResponse response = projectBiz.fetchProjectByUuid(uuid);
		assertEquals("project", response.getName());
	}

	/**
	 * projectNotExistException
	 *
	 * @Title: projectNotExistException
	 * @Description: 项目不存在异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void projectNotExistException() {
		String uuid = UUID.randomUUID().toString();
		when(projectDao.getProjectByUuid(uuid)).thenReturn(new Project());
		projectBiz.fetchProjectByUuid(uuid);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz#createProject(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest)}.
	 */
	@Test
	public void testCreateProject() {
		CreateProjectRequest request = new CreateProjectRequest("project", "template",
				"admin");
		List<ProjectTemplateItem> projectTemplateItems = new ArrayList<>();
		String itemData = FileUtil.readFile("json/projectTemplateItem.json");
		for (int i = 0; i < 5; i++) {
			ProjectTemplateItem projectTemplateItem = new ProjectTemplateItem(
					UUID.randomUUID().toString(), i + 1, "item" + i, 1, "", "role",
					itemData);
			projectTemplateItems.add(projectTemplateItem);
		}
		ProjectTemplate projectTemplate = new ProjectTemplate(
				UUID.randomUUID().toString(), "template",
				new Timestamp(System.currentTimeMillis()),
				new Timestamp(System.currentTimeMillis()), projectTemplateItems);
		when(projectTemplateDao.getProjectTemplateByName("template"))
				.thenReturn(projectTemplate);
		projectBiz.createProject(request);
	}

	/**
	 * projectTemplateNotExistException
	 *
	 * @Title: projectTemplateNotExistException
	 * @Description: 项目模板不存在异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void projectTemplateNotExistException() {
		CreateProjectRequest request = new CreateProjectRequest("project", "template",
				"admin");
		when(projectTemplateDao.getProjectTemplateByName("template")).thenReturn(new ProjectTemplate());
		projectBiz.createProject(request);
	}

	/**
	 * blankProjectNameException
	 *
	 * @Title: blankProjectNameException
	 * @Description: 项目名称为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankProjectNameException() {
		CreateProjectRequest request = new CreateProjectRequest("", "template", "admin");
		projectBiz.createProject(request);
	}

	/**
	 * blankProjecTemplatetNameException
	 *
	 * @Title: blankProjecTemplatetNameException
	 * @Description: 项目模板名称为空
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankProjecTemplatetNameException() {
		CreateProjectRequest request = new CreateProjectRequest("project", "", "admin");
		projectBiz.createProject(request);
	}

	/**
	 * blankNamespaceException
	 *
	 * @Title: blankNamespaceException
	 * @Description: 根帐号为空异常
	 * @param:
	 * @return: void
	 * @throws:
	 */
	@Test(expected = BadRequestFieldException.class)
	public void blankNamespaceException() {
		CreateProjectRequest request = new CreateProjectRequest("project", "template",
				"");
		projectBiz.createProject(request);
	}

	@Test(expected = BadRequestFieldException.class)
	public void projectAlreadyExistException() {
		CreateProjectRequest request = new CreateProjectRequest("project", "template",
				"admin");
		//在根帐号下项目已经存在
		when(projectDao.countProjectByName("admin", "project")).thenReturn(1);
		projectBiz.createProject(request);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz#deleteProject(java.lang.String)}.
	 */
	@Test
	public void testDeleteProject() {
		projectBiz.deleteProject(UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz#updateProject(java.lang.String)}.
	 */
	@Test
	public void testUpdateProject() {
		String uuid = UUID.randomUUID().toString();
		projectBiz.updateProject(uuid);
	}

}
