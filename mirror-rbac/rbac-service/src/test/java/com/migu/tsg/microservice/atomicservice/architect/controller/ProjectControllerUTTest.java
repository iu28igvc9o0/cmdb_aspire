/**
 * @Title: ProjectControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.architect.controller
 * @Description: ProjectController.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月24日 下午3:15:18
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.architect.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * @ClassName: ProjectControllerUT
 * @Description: ProjectController.java 单元测试类
 * @author botao gao
 * @date 2018年1月24日 下午3:15:18
 *
 */
@RunWith(SpringRunner.class)
public class ProjectControllerUTTest {

	@Mock
	private ProjectBiz projectBiz;

	@InjectMocks
	private ProjectController projectController;

	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#fetchProjectList(java.util.List)}.
	 */
	@Test
	public void testFetchProjectList() {
		String[] uuidStrs = {UUID.randomUUID().toString(),UUID.randomUUID().toString()};
		List<String> uuids = Arrays.asList(uuidStrs);
		List<FetchProjectResponse> response = new ArrayList<>();
		when(projectBiz.fetchProjectList(uuids)).thenReturn(response);
		projectController.fetchProjectList(uuids);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#fetchProjectByUuid(java.lang.String)}.
	 */
	@Test
	public void testFetchProjectByUuid() {
		String uuid = UUID.randomUUID().toString();
		FetchProjectResponse response = new FetchProjectResponse();
		when(projectBiz.fetchProjectByUuid(uuid)).thenReturn(response);
		projectController.fetchProjectByUuid(uuid);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#createProject(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest)}.
	 */
	@Test
	public void testCreateProject() {
		CreateProjectRequest request = new CreateProjectRequest("project", "template", "admin");
		CreateProjectResponse response = new CreateProjectResponse();
		when(projectBiz.createProject(request)).thenReturn(response);
		projectController.createProject(request);
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#deleteProject(java.lang.String)}.
	 */
	@Test
	public void testDeleteProject() {
		projectController.deleteProject(UUID.randomUUID().toString());
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#updateProject(java.lang.String)}.
	 */
	@Test
	public void testUpdateProject() {
		projectController.updateProject(UUID.randomUUID().toString());
	}

}
