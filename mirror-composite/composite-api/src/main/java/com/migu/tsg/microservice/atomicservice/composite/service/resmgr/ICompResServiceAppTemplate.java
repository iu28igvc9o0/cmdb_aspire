package com.migu.tsg.microservice.atomicservice.composite.service.resmgr;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.AppTemplateListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.AppTemplateServiceList;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* 资源管理  应用模块 API接口
* Project Name:composite-api
* File Name:ICompResServiceAppTemplate.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr
* ClassName: ICompResServiceAppTemplate <br/>
* date: 2017年9月27日 下午4:38:12 <br/>
* 资源管理  应用模块 API接口
* @author weishuai
* @version 
* @since JDK 1.8
*/
@RequestMapping(path="${version}/application-templates",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Composite Resource management(Application Template)", 
     description = "Composite Resource management(Application Template)")    
public interface ICompResServiceAppTemplate {
    
    
    /**
     * 获取应用模板列表.<br/>
     * 作者： weishuai
     * @param project_name
     * @return
     */
    @ApiOperation(value = "获取应用模板列表", notes = "获取应用模板列表",
                  response = AppTemplateListResponse.class, 
                  tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get list of application-templates",
                                        response = AppTemplateListResponse.class)})
    @GetMapping(path = "/{namespace}", 
                produces = "application/json;charset=UTF-8")
    public List<AppTemplateListResponse> getAppTemplateList(@PathVariable("namespace") String namespace,
                        @RequestParam(name="project_name", required=false) String project_name);
    
    /**
     * 创建应用模板.<br/>
     * 作者： weishuai
     * @param namespace
     * @param name
     * @param project_name
     * @param space_name
     * @param template
     * @return
     */
    @ApiOperation(value = "创建应用模板", notes = "创建应用模板", 
                  response = AppTemplateListResponse.class, 
                  tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code=201, 
                                        message = "Create a new application-template", 
                                        response = AppTemplateListResponse.class)})
    @PostMapping(path = "/{namespace}", consumes=MediaType.ALL_VALUE,
                 produces = "application/json;charset=UTF-8")
    public AppTemplateListResponse createAppTemplate(@PathVariable("namespace") String namespace,
                        @RequestParam(name="project_name",required=false) String project_name,
                        @RequestParam("name") String name,
                        @RequestParam(name="description",required=false) String description,
                        @RequestParam(name="space_name",required=false) String space_name,
                        @RequestParam(name="knamespace",required=false) String knamespace,
                        @RequestParam("template") MultipartFile template);
    
    /**
     * 获取应用模板详情.<br/>
     * 作者： weishuai
     * @param namespace
     * @param name
     * @return
     */
    @ApiOperation(value = "获取应用模板详情", notes = "获取应用模板详情", 
                 response = AppTemplateListResponse.class, 
                 tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                 message = "Get application template details", 
                 response = AppTemplateListResponse.class)})
    @GetMapping(path = "/{namespace}/{name}", 
                produces = "application/json;charset=UTF-8")
    public AppTemplateListResponse getAppTemplateDetail(@PathVariable("namespace") String namespace,
                 @PathVariable("name") String name);
    
    /**
     * 更新应用模板.<br/>
     * 作者： weishuai
     * @param namespace
     * @param project_name
     * @param name
     * @param description
     * @param template
     * @return
     */
    @ApiOperation(value = "更新应用模板", notes = "更新应用模板", 
            response = AppTemplateListResponse.class, 
            tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                                      message = "Update one application-template", 
                                      response = AppTemplateListResponse.class)})
    @PostMapping(path = "/{namespace}/{name}",
                consumes = MediaType.ALL_VALUE, 
                produces = "application/json;charset=UTF-8")
    public AppTemplateListResponse updateAppTemplate(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name,
            @RequestParam(name="description",required=false) String description,
            @RequestParam("template") MultipartFile template);
    
    /**
     * 删除应用模板.<br/>
     * 作者： weishuai
     * @param namespace
     * @param name
     * @return
     */
    @ApiOperation(value = "删除应用模板", notes = "删除应用模板", 
                  tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, message = "Delete one application-template")})
    @DeleteMapping(path = "/{namespace}/{name}")
    public BaseResponse deleteAppTemplate(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name);
    
    /**
     * 获取应用模板下的服务列表.<br/>
     * 作者： weishuai
     * @param namespace
     * @param name
     * @return
     */
    @ApiOperation(value = "获取应用模板下的服务列表", notes = "获取应用模板下的服务列表", 
            response = AppTemplateServiceList.class, 
            tags = {"Composite Resource management(Application Template) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                message = "Get list of services described in the application-template", 
                response = AppTemplateServiceList.class)})
    @GetMapping(path = "/{namespace}/{name}/services", 
               produces = "application/json;charset=UTF-8")
    public List<AppTemplateServiceList>  getAppTemplateServiceList(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name);
}
