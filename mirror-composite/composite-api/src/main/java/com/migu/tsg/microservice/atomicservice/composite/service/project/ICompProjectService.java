package com.migu.tsg.microservice.atomicservice.composite.service.project;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResourceResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectStatusResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectUpdateRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * endpoint to manage project Project Name:composite-api File
 * Name:IProjectTemplateService.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.service.project
 * ClassName: IProjectTemplateService <br/>
 * date: 2017年9月27日 下午2:27:17 <br/>
 * endpoint to manage project
 * 
 * @author baiwp
 * @version
 * @since JDK 1.6
 */
@RequestMapping("${version}/projects")
@Api(value = "Composite Resource management(projects)", description = "Composite Resource management(projects)")
public interface ICompProjectService {

    /**
     * 
     * listProjects:(获取项目列表). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @return
     */
    @ApiOperation(value = "获取项目列表", notes = "获取项目列表", response = CompProjectResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List projects for the namespace",
                    response = CompProjectResponse.class) })
    @GetMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    public List<CompProjectResponse> listProjects(@PathVariable("namespace") String namespace);
    
    /**
     * 
     * listProjects:(获取项目可绑定的共享资源列表). <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @return
     */
    @ApiOperation(value = "获取项目可绑定的共享资源列表", notes = "获取项目可绑定的共享资源列表", response = CompProjectResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List projects for the namespace",
                    response = CompProjectResponse.class) })
    @GetMapping(path = "/{namespace}/resources", produces = "application/json;charset=UTF-8")
    public CompProjectResourceResponse listProjectResources(@PathVariable("namespace") String namespace);

    /**
     * 
     * createProject:创建项目. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param compReq
     * @return
     */
    @ApiOperation(value = "创建项目", notes = "创建项目", response = CompProjectResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "create a project", response = CompProjectResponse.class) })
    @PostMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    public CompProjectResponse createProject(@PathVariable("namespace") String namespace,
            @RequestBody CompProjectCreateRequest compReq);

    /**
     * 
     * getProjectDetail:获取项目详细信息. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param name
     * @return
     */
    @ApiOperation(value = "获取项目详细信息", notes = "获取项目详细信息", response = CompProjectResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get project detail for the namespace",
                    response = CompProjectResponse.class) })
    @GetMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    public CompProjectResponse getProjectDetail(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name);

    /**
     * 
     * updateProjectResources:更新项目资源. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param name
     * @param compReq
     * @return
     */
    @ApiOperation(value = "更新项目资源", notes = "更新项目资源", response = CompProjectResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "update project resources for the namespace",
                    response = CompProjectResponse.class) })
    @PutMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    public CompProjectResponse updateProjectResources(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name, @RequestBody CompProjectUpdateRequest compReq);

    /**
     * 
     * deleteProject:删除项目. <br/>
     *
     * 作者： baiwp
     * 
     * @param namespace
     * @param name
     */
    @ApiOperation(value = "删除项目", notes = "删除项目", tags = { "Composite Resource management(projects) service API" })
    @ApiResponses(value = { @ApiResponse(code = 200, message = "delete project by name") })
    @DeleteMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    public BaseResponse deleteProject(@PathVariable("namespace") String namespace, @PathVariable("name") String name);

    /**
     * 
     * getProjectStatus:获取项目状态. <br/>
     *
     * 作者： baiwp
     * 
     * @param namespace
     * @param name
     * @return
     */
    @ApiOperation(value = "获取项目状态", notes = "获取项目状态", response = CompProjectStatusResponse.class, tags = {
            "Composite Resource management(projects) service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "get project status by project name",
                    response = CompProjectStatusResponse.class) })
    @GetMapping(path = "/{namespace}/{name}/status", produces = "application/json;charset=UTF-8")
    public CompProjectStatusResponse getProjectStatus(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name);
}
