/**
 * @Title: ResourceSchemaBizTest.java
 * @Package com.migu.tsg.microservice.atomicservice.rbac.biz
 * @Description: ResourceSchemaBiz 单元测试类
 * Copyright: Copyright (c) 2018 
 * Company:咪咕文化科技有限公司
 * 
 * @author botao gao
 * @date 2018年1月18日 下午4:30:16
 * @version V1.0
 */

package com.migu.tsg.microservice.atomicservice.rbac.biz;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaConstraints;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ResourceSchemaDTO;

/**
 * @ClassName: ResourceSchemaBizTest
 * @Description: ResourceSchemaBiz 单元测试类
 * @author botao gao
 * @date 2018年1月18日 下午4:30:16
 *
 */
@RunWith(SpringRunner.class)
public class ResourceSchemaBizTest {

	@Mock
	private CacheBiz cacheBiz;

	@InjectMocks
	private ResourceSchemaBiz resourceSchemaBiz;

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.ResourceSchemaBiz#fetchRoleSchemaList()}.
	 */
	@Test
	public void testFetchRoleSchemaList() {
		List<ResourceSchema> resourceSchemaList = new ArrayList<>();
		resourceSchemaList.add(buildResourceSchema());
		when(cacheBiz.listResourceSchema()).thenReturn(resourceSchemaList);
		List<ResourceSchemaDTO> resourceSchemaDTOList = resourceSchemaBiz
				.fetchRoleSchemaList();
		assertEquals("application", resourceSchemaDTOList.get(0).getResource());
	}

	/**
	 * buildResourceSchema(构建resourceSchema对象)
	 *
	 * @Title: buildResourceSchema @Description: @param @return 设定文件 @return
	 * ResourceSchema 返回类型 @throws
	 */
	private ResourceSchema buildResourceSchema() {
		List<ResourceSchemaActions> resourceSchemaActions = new ArrayList<>();
		String[] actionStrs = { "application:create", "application:delete",
				"application:start", "application:stop", "application:update",
				"application:view" };
		for (int i = 0; i < actionStrs.length; i++) {
			ResourceSchemaActions resourceSchemaAction = new ResourceSchemaActions(
					"application", actionStrs[i], null, null);
			resourceSchemaActions.add(resourceSchemaAction);
		}
		List<ResourceSchemaConstraints> resourceSchemaConstraints = new ArrayList<>();
		ResourceSchemaConstraints resourceSchemaConstraint = new ResourceSchemaConstraints(
				"application", "res:cluster", "name");
		resourceSchemaConstraints.add(resourceSchemaConstraint);
		ResourceSchema resourceSchema = new ResourceSchema("application", "t",
				null, null, new Timestamp(System.currentTimeMillis()), resourceSchemaActions,
				resourceSchemaConstraints);
		return resourceSchema;
	}

	/**
	 * Test method for
	 * {@link com.migu.tsg.microservice.atomicservice.rbac.biz.ResourceSchemaBiz#fetchRoleSchemaDetail(java.lang.String)}.
	 */
	@Test
	public void testFetchRoleSchemaDetail() {
		String resourceType = "application";
		when(cacheBiz.getResourceSchema(resourceType, resourceType))
				.thenReturn(buildResourceSchema());
		ResourceSchemaDTO resourceSchemaDTO = resourceSchemaBiz
				.fetchRoleSchemaDetail(resourceType, resourceType);
		assertEquals(resourceType, resourceSchemaDTO.getResource());
	}

}
