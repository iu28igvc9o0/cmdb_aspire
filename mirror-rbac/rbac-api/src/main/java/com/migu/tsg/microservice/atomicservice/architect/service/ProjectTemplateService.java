package com.migu.tsg.microservice.atomicservice.architect.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectTemplateResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 项目名称: architect-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.service <br>
* 类名称: ProjectTemplateService.java <br>
* 类描述: 项目模板接口<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午3:12:38 <br>
* 版本: v1.0
*/
@Api(tags = "templates", description = "模板管理端点")
public interface ProjectTemplateService {

    /**
     * 获取可用模板的列表
     * @param uuids 项目模板UUID集合
     * @return 响应对象 
     */
    @ApiOperation("获取可用模板的列表")
    @GetMapping(value = "/v1/templates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FetchProjectTemplateResponse> fetchProjectTemplateList(
            @RequestParam(value = "uuids", required = false) List<String> uuids);

    /**
     * 创建单个项目模板
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation("创建单个模板")
    @PostMapping(value = "/v1/templates", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CreateProjectTemplateResponse createProjectTemplate(@RequestBody CreateProjectTemplateRequest request);

    /**
     * 删除单个项目模板
     * @param uuid 项目模板UUID
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("删除单个模板")
    @DeleteMapping(value = "/v1/templates/{uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteProjectTemplate(@PathVariable("uuid") String uuid);
}
