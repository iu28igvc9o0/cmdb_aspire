package com.migu.tsg.microservice.atomicservice.architect.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 项目名称: architect-api <br>
* 包: com.migu.tsg.microservice.atomicservice.architect.service <br>
* 类名称: ProjectsService.java <br>
* 类描述: 项目接口<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年9月26日下午3:12:38 <br>
* 版本: v1.0
*/
@Api(tags = "projects", description = "项目管理端点")
public interface ProjectService {

    /**
     * 获取项目列表
     * @param uuids 项目UUID集合
     * @return 响应对象 
     */
    @ApiOperation("获取项目列表")
    @GetMapping(value = "/v1/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<FetchProjectResponse> fetchProjectList(
            @RequestParam(value = "uuids", required = false) List<String> uuids);

    /**
     * 获取单个项目详情
     * @param uuid 项目UUID
     * @return 响应对象 
     */
    @ApiOperation("获取单个项目详情")
    @GetMapping(value = "/v1/projects/{uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public FetchProjectResponse fetchProjectByUuid(@PathVariable("uuid") String uuid);

    /**
     * 创建单个项目
     * @param request 请求对象
     * @return 响应对象
     */
    @ApiOperation("创建单个项目")
    @PostMapping(value = "/v1/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CreateProjectResponse createProject(@RequestBody CreateProjectRequest request);
    
    /**
     * 删除单个项目
     * @param uuid 项目UUID
     */
    @ApiOperation("删除单个项目")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/v1/projects/{uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteProject(@PathVariable("uuid") String uuid);

    /**
     * 修改单个项目(只更新项目修改时间)
     * @param uuid 项目UUID
     */
    @ApiOperation("修改单个项目")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/v1/projects/{uuid}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void updateProject(@PathVariable("uuid") String uuid);
}
