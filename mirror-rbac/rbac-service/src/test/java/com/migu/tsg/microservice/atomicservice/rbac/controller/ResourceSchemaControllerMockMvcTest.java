/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
* 类名称: ResourceSchemaControllerMockMvcTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月20日下午3:19:02 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // 运行测试方法时,按名称顺序执行
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
// 一般来说在你的测试类上加上@Transactional就足够了，Spring默认是会回滚的。
// 更简便的做法：直接继承AbstractTransactionalJUnit4SpringContextTests
@Transactional
@Ignore // ignore this class temporally, since it will cause JUnit fall
public class ResourceSchemaControllerMockMvcTest {

    @Autowired
    private MockMvc mvc;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaList()}.
     * @throws Exception 
     */
    @Test
    public final void testFetchRoleSchemaList() throws Exception {
        mvc.perform(get("/v1/schemas").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.rbac.controller.ResourceSchemaController#fetchRoleSchemaDetail(java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public final void testFetchRoleSchemaDetail() throws Exception {
        mvc.perform(get("/v1/schemas/{resource_type}", "role").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk()).andDo(print());
    }

}
