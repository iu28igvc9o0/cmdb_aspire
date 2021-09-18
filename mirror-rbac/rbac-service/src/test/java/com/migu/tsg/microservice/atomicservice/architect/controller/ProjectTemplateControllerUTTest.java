/**
 * @Title: ProjectTemplateControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.architect.controller
 * @Description: ProjectTemplateController.java 单元测试
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月24日 下午2:48:41
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.architect.controller;

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

import com.migu.tsg.microservice.atomicservice.architect.biz.ProjectTemplateBiz;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectTemplateResponse;

/**
  * @ClassName: ProjectTemplateControllerUT
  * @Description: ProjectTemplateController.java 单元测试
  * @author botao gao
  * @date 2018年1月24日 下午2:48:41
  *
  */
@RunWith(SpringRunner.class)
public class ProjectTemplateControllerUTTest {
	@Mock
	private ProjectTemplateBiz projectTemplateBiz;
	@InjectMocks
	private ProjectTemplateController projectTemplateController;
	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#fetchProjectTemplateList(java.util.List)}.
	 */
	@Test
	public void testFetchProjectTemplateList() {
		String[] uuidStrs = {UUID.randomUUID().toString(),UUID.randomUUID().toString()};
		List<String>  uuids = Arrays.asList(uuidStrs);
		List<FetchProjectTemplateResponse> list = new ArrayList<>();
		when(projectTemplateBiz.fetchProjectTemplateList(uuids)).thenReturn(list);
		projectTemplateController.fetchProjectTemplateList(uuids);
	}

	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#createProjectTemplate(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest)}.
	 */
	@Test
	public void testCreateProjectTemplate() {
		CreateProjectTemplateRequest request = new CreateProjectTemplateRequest();
		projectTemplateController.createProjectTemplate(request);
	}

	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#deleteProjectTemplate(java.lang.String)}.
	 */
	@Test
	public void testDeleteProjectTemplate() {
		projectTemplateController.deleteProjectTemplate(UUID.randomUUID().toString());
	}

}
