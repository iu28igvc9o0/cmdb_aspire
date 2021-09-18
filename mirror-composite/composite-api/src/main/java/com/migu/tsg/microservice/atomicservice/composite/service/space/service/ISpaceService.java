package com.migu.tsg.microservice.atomicservice.composite.service.space.service;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.space.dto.CreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.space.dto.Dto;
import com.migu.tsg.microservice.atomicservice.composite.service.space.dto.ListResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
* 资源空间管理
* Project Name:composite-api
* File Name:ISpaceService.java
* Context:/spaces/{namespace}
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.space.service
* ClassName: ISpaceService <br/>
* date: 2017年11月24日 下午19:22:02 <br/>
* version v1
* @author zhangqing
* @version 
* @since JDK 1.6
 */
public interface ISpaceService {

    /**
     * ok
     */
    int RESPONSE_SUCCESS200 = 200;
    /**
     * No Content
     */
    int RESPONSE_SUCCESS204 = 204;
    /**
     * created
     */
    int RESPONSE_SUCCESS201 = 201;
    
    /**
     * 
    * list:查询所有资源空间概要信息. <br/>
    *
    * 作者： zhangqing
    * @param namespace
    * @param projectName
    * @return
     */
 
    @RequestMapping(value = "/v1/spaces/{namespace}", produces = { "application/json" }, 
                    method = RequestMethod.GET)
    @ApiOperation(value = "获取所有资源空间概要信息", notes = "获取所有资源空间概要信息", response = ListResponse.class, 
                  tags = {"SPACE SERVICE MANAGEMENT API"})
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS204, message = "返回一组资源空间概要信息", 
                            response = ListResponse.class) })          
    public ListResponse list(@PathVariable("namespace") String namespace,
                             @RequestParam(value = "project_name",required = false) String projectName);  
    
   /**
    * 
   * create:创建一个资源空间. <br/>
   *
   * 作者： zhangqing
   * @param namespace
   * @param projectName
   * @param request
   * @return
    */
    @PostMapping(path = "/v1/spaces/{namespace}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建一个资源空间", notes = "创建一个资源空间", response = CreateResponse.class, 
                  tags = { "SPACE SERVICE MANAGEMENT API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "资源空间创建成功", 
                            response = CreateResponse.class)})
    public CreateResponse create(@PathVariable("namespace") String namespace,
                         @RequestParam(value = "project_name",required = false) String projectName, 
                         @RequestBody Dto request); 
    
    /**
     * 
    * retrieve:根据名称查资源空间详细信息. <br/>
    *
    * 作者： zhangqing
    * @param namespace
    * @param spaceName
    * @return
     */
    @GetMapping(path = "/v1/spaces/{namespace}/space/{space_name}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据名称查资源空间详细信息", notes = "根据名称查资源空间详细信息，并返回json格式", 
                  response = Dto.class,tags = { "SPACE SERVICE MANAGEMENT API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "成功获取{name}资源的详细信息",
                            response = Dto.class)})             
    public Dto retrieve(@PathVariable("namespace") String namespace,
                        @PathVariable("space_name") String spaceName);
    
    /**
     * 
    * update:更新space_name对应的资源空间的详细信息. <br/>
    *
    * 作者： zhangqing
    * @param namespace
    * @param name
    * @param request
    * @return
     */
   @PutMapping(path = "/v1/spaces/{namespace}/space/{space_name}", produces = "application/json;charset=UTF-8")
   @ApiOperation(value = "更新space_name对应的资源空间的详细信息", notes = "更新space_name对应的资源空间的详细信息", response = String.class, 
   tags = {"SPACE SERVICE MANAGEMENT API"})
   @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "更新成功", response = void.class)})
   public void update(@PathVariable("namespace") String namespace,
                        @PathVariable("space_name") String spaceName,
                        @RequestBody Dto request);
   

    /**
     * 
    * delete:删除space_name对应的资源空间. <br/>
    *
    * 作者： zhangqing
    * @param namespace
    * @param spaceName
    * @return
     */
   @DeleteMapping(path = "/v1/spaces/{namespace}/space/{space_name}", produces = "application/json;charset=UTF-8")
   @ApiOperation(value = "删除space_name对应的资源空间", notes = "删除space_name对应的资源空间", 
                 response = String.class, 
   tags = { "SPACE SERVICE MANAGEMENT API" })
   @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "删除成功",
                           response = String.class) })            
   public void delete(@PathVariable("namespace") String namespace,@PathVariable("space_name") String spaceName);
}
