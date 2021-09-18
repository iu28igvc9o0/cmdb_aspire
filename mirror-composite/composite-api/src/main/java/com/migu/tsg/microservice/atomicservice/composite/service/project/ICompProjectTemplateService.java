package com.migu.tsg.microservice.atomicservice.composite.service.project;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectTemplateCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectTemplateDetailResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * endpoint to manage project templates Project Name:composite-api File
 * Name:IProjectTemplateService.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service.project
 * ClassName: IProjectTemplateService <br/>
 * date: 2017年9月27日 下午2:27:17 <br/>
 * endpoint to manage project templates
 * 
 * @author baiwp
 * @version
 * @since JDK 1.6
 */
@RequestMapping("${version}/project-templates")
@Api(value = "Composite Resource management(project-templates)",
description = "Composite Resource management(project-templates)")
public interface ICompProjectTemplateService {

    /**
     * 
     * listProjectTemplates:(获取项目模板列表). <br/>
     * 作者： baiwp
     * @param namespace
     * @return
     */
    @ApiOperation(value = "获取项目模板列表", notes = "获取项目模板列表", response = CompProjectTemplateDetailResponse.class, tags = {
            "Composite Resource management(project templates) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message =
                    "List project templates for the namespace", response = CompProjectTemplateDetailResponse.class) })
    @GetMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    public List<CompProjectTemplateDetailResponse> listProjectTemplates(@PathVariable("namespace") String namespace);
    
    /**
     * 
     * saveProject:保存为项目模板. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param compReq
     * @return
     */
    @ApiOperation(value = "保存为项目模板", notes = "保存为项目模板", response = CompProjectTemplateCreateRequest.class, tags = {
            "Composite Resource management(project templates) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "create a project template",
                    response = CompProjectTemplateCreateRequest.class) })
    @PostMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    public CompProjectTemplateDetailResponse saveProjectTemplate(@PathVariable("namespace") String namespace,
            @RequestBody CompProjectTemplateCreateRequest compReq);
}
