/**
 * @Title: ResourceSchemaControllerUT.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.controller
 * @Description: ResourceSchemaController.java 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月24日 上午11:10:59
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.rbac.biz.ResourceSchemaBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.FetchResourceSchemaDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ResourceSchemaDTO;

/**
  * @ClassName: ResourceSchemaControllerUT
  * @Description: 
  * @author botao gao
  * @date 2018年1月24日 上午11:10:59
  *
  */
@RunWith(SpringRunner.class)
public class ResourceSchemaControllerUTTest {
	@Mock
	private ResourceSchemaBiz resourceSchemaBiz;
	@InjectMocks
	private ResourceSchemaController resourceSchemaController;
	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaList()}.
	 */
	@Test
	public void testFetchRoleSchemaList() {
		List<ResourceSchemaDTO> fetchRoleSchemaList = new ArrayList<>();
		ResourceSchemaDTO resourceSchemaDTO = buildResourceSchemaDTO();
		for(int i =0;i<5;i++){
			fetchRoleSchemaList.add(resourceSchemaDTO);
		}
		when(resourceSchemaBiz.fetchRoleSchemaList()).thenReturn(fetchRoleSchemaList);
		resourceSchemaController.fetchRoleSchemaList();
	}

	/**
	  * buildResourceSchemaDTO(这里用一句话描述这个方法的作用)
	  *
	  * @Title: buildResourceSchemaDTO
	  * @Description: 
	  * @param: @return    
	  * @return: ResourceSchemaDTO   
	  * @throws:
	  */
	private ResourceSchemaDTO buildResourceSchemaDTO() {
		String[] actionStrs = { "application:create", "application:delete", "application:start", "application:stop",
				"application:update", "application:view" };
		List<String> actions = Arrays.asList(actionStrs);
		ResourceSchemaDTO resourceSchemaDTO = new ResourceSchemaDTO();
		return resourceSchemaDTO;
	}

	/**
	 * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaDetail(java.lang.String)}.
	 */
	@Test
	public void testFetchRoleSchemaDetail() {
		String resourceType = "application";
		when(resourceSchemaBiz.fetchRoleSchemaDetail(resourceType, resourceType)).thenReturn(buildResourceSchemaDTO());
		FetchResourceSchemaDetailResponse response = resourceSchemaController.fetchRoleSchemaDetail(resourceType);
		assertEquals(resourceType,response.getResource());
	}

}
