/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
* 类名称: ResourceSchemaControllerTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日下午7:00:35 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class ResourceSchemaControllerTest {

    @Autowired
    private ResourceSchemaController resourceSchemaController;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaList()}.
     */
    @Test
    public final void testFetchRoleSchemaList() {
        resourceSchemaController.fetchRoleSchemaList();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaDetail(java.lang.String)}.
     */
    @Test
    public final void testFetchRoleSchemaDetail() {
        resourceSchemaController.fetchRoleSchemaDetail("role");
    }

}
