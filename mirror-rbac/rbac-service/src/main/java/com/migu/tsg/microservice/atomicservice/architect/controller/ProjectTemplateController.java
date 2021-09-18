package com.migu.tsg.microservice.atomicservice.architect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.architect.biz.ProjectTemplateBiz;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.service.ProjectTemplateService;
import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.controller <br>
* 类名称: ProjectTemplateController.java <br>
* 类描述: 项目模板控制层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午5:14:08 <br>
* 版本: v1.0
*/
@RestController
public class ProjectTemplateController implements ProjectTemplateService {

    @Autowired
    private ProjectTemplateBiz projectTemplateBiz;

    /**
     * 获取可用模板的列表
     * @param uuids 项目模板UUID集合
     * @return 响应对象
     */
    @ResultCode("105020201")
    public List<FetchProjectTemplateResponse> fetchProjectTemplateList(
            @RequestParam(value = "uuids", required = false) final List<String> uuids) {
        return projectTemplateBiz.fetchProjectTemplateList(uuids);
    }

    /**
     * 创建单个项目模板
     * @param request 请求对象
     * @return 响应对象
     */
    @ResultCode("105020202")
    public CreateProjectTemplateResponse createProjectTemplate(
            @RequestBody final CreateProjectTemplateRequest request) {
        return projectTemplateBiz.createProjectTemplate(request);
    }

    /**
     * 删除单个项目模板
     * @param uuid 项目模板UUID
     */
    @ResultCode("105020202")
    public void deleteProjectTemplate(@PathVariable("uuid") final String uuid) {
        projectTemplateBiz.deleteProjectTemplate(uuid);
    }

}
