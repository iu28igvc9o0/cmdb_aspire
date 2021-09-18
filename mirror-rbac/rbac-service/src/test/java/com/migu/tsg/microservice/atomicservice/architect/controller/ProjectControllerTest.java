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
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.controller <br>
* 类名称: ProjectControllerTest.java <br>
* 类描述: <br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月23日下午9:23:06 <br>
* 版本: v1.0
*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Ignore
public class ProjectControllerTest {

    @Autowired
    private ProjectTemplateController projectTemplateController;

    @Autowired
    private ProjectController projectController;

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#fetchProjectList(java.util.List)}.
     */
    @Test
    public final void testFetchProjectList() {
        CreateProjectResponse createProject = createProject();
        projectController.fetchProjectList(Arrays.asList(createProject.getUuid()));
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#fetchProjectByUuid(java.lang.String)}.
     */
    @Test
    public final void testFetchProjectByUuid() {
        CreateProjectResponse createProject = createProject();
        projectController.fetchProjectByUuid(createProject.getUuid());
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#createProject(com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest)}.
     */
    @Test
    public final void testCreateProject() {
        createProject();
    }

    /**
     * Test method for {@link com.migu.tsg.microservice.atomicservice.architect.controller.ProjectController#deleteProject(java.lang.String)}.
     */
    @Test
    public final void testDeleteProject() {
        CreateProjectResponse createProject = createProject();
        projectController.deleteProject(createProject.getUuid());
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

    /**
     * 新增项目
     * @return
     */
    private CreateProjectResponse createProject() {
        String json = "{\"name\":\"project-test\",\"template\":\"simple-project-test\",\"namespace\":\"admin\"}";
        CreateProjectRequest request = new Gson().fromJson(json, CreateProjectRequest.class);
        request.setTemplate(createProjectTemplate().getName());
        return projectController.createProject(request);
    }

}
