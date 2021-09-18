/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.architect.controller;

import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.controller <br>
* 类名称: ProjectTemplateControllerTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日下午9:23:27 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class ProjectTemplateControllerTest {

    @Autowired
    private ProjectTemplateController projectTemplateController;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#fetchProjectTemplateList(java.util.List)}.
     */
    @Test
    public final void testFetchProjectTemplateList() {
        CreateProjectTemplateResponse createProjectTemplate = createProjectTemplate();
        projectTemplateController.fetchProjectTemplateList(Arrays.asList(createProjectTemplate.getUuid()));
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#createProjectTemplate(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest)}.
     */
    @Test
    public final void testCreateProjectTemplate() {
        createProjectTemplate();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectTemplateController#deleteProjectTemplate(java.lang.String)}.
     */
    @Test
    public final void testDeleteProjectTemplate() {
        CreateProjectTemplateResponse createProjectTemplate = createProjectTemplate();
        projectTemplateController.deleteProjectTemplate(createProjectTemplate.getUuid());
    }

    /**
     * 新增项目模板
     * @return
     */
    private CreateProjectTemplateResponse createProjectTemplate() {
        String json = "{\"name\":\"simple-project-test\",\"roles\":[{\"id\":1,\"required\":true,\"depends_on\":[],\"parents\":[],\"name\":\"dev-parent-[name]\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\",\"application:*\"],\"constraints\":[{\"res:cluster\":\"dev\",\"res:project\":\"[name]\",\"res:space\":\"dev-test\"}]}]},{\"id\":2,\"required\":false,\"depends_on\":[1],\"parents\":[{\"item\":1,\"name\":\"dev-parent-[name]\"}],\"name\":\"dev-[name]\",\"permissions\":[{\"resource\":[\"*\"],\"actions\":[\"service:*\",\"application:*\"],\"constraints\":[{\"res:cluster\":\"dev\",\"res:project\":\"[name]\",\"res:space\":\"dev-[name]\"}]}]}],\"resources\":[{\"id\":3,\"required\":false,\"depends_on\":[],\"name\":\"dev-[name]\",\"type\":\"REGISTRY_PROJECT\",\"parent_resource\":{\"type\":\"REGISTRY\",\"name\":\"dev-registry\",\"uuid\":\"1234-1234-1234\",\"details\":\"http://dev-registry:5000\"}},{\"id\":4,\"required\":false,\"depends_on\":[],\"name\":\"dev-[name]\",\"type\":\"SPACE\",\"parent_resource\":null}]}";
        CreateProjectTemplateRequest request = new Gson().fromJson(json, CreateProjectTemplateRequest.class);
        return projectTemplateController.createProjectTemplate(request);
    }

}
