package com.migu.tsg.microservice.atomicservice.architect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.architect.biz.ProjectBiz;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.service.ProjectService;
import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.controller <br>
* 类名称: ProjectController.java <br>
* 类描述: 项目控制层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午5:14:08 <br>
* 版本: v1.0
*/
@RestController
public class ProjectController implements ProjectService {

    @Autowired
    private ProjectBiz projectBiz;

    /**
     * 获取项目列表
     * @param uuids 项目UUID集合
     * @return 响应对象
     */
    @ResultCode("105020101")
    public List<FetchProjectResponse> fetchProjectList(
            @RequestParam(value = "uuids", required = false) final List<String> uuids) {
        return projectBiz.fetchProjectList(uuids);
    }

    /**
     * 获取单个项目详情
     * @param uuid 项目UUID
     * @return 响应对象
     */
    @ResultCode("105020102")
    public FetchProjectResponse fetchProjectByUuid(@PathVariable("uuid") String uuid) {
        return projectBiz.fetchProjectByUuid(uuid);
    }

    /**
     * 创建单个项目
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105020103")
    public CreateProjectResponse createProject(@RequestBody final CreateProjectRequest request) {
        return projectBiz.createProject(request);
    }

    /**
     * 删除单个项目
     * @param uuid 项目UUID
     */
    @ResultCode("105020104")
    public void deleteProject(@PathVariable("uuid") final String uuid) {
        projectBiz.deleteProject(uuid);
    }

    /**
     * 修改单个项目(只更新项目修改时间)
     * @param uuid 项目UUID
     */
    @ResultCode("105020105")
    public void updateProject(@PathVariable("uuid") final String uuid) {
        projectBiz.updateProject(uuid);
    }

}
