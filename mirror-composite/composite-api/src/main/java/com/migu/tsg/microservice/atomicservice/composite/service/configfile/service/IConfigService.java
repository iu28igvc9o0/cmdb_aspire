package com.migu.tsg.microservice.atomicservice.composite.service.configfile.service;
 

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.create.ConfigCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.create.ConfigCreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.list.ConfigListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.specified.ConfigSpecifiedResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.update.ConfigUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.configfile.dto.update.ConfigUpdateResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONArray;

/**
 * 
* 配置管理
* Project Name:composite-api
* File Name:IConfigService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.configs.service
* ClassName: IConfigService <br/>
* date: 2017年9月24日 下午9:02:02 <br/>
* version v1
* @author zhangqing
* @version 
* @since JDK 1.6
 */
@RequestMapping("${version}/configs")
@Api(value = "配置管理服务", description = "配置管理服务 API")
public interface IConfigService {

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
    * getConfigs:获取config数据在网页列表显示 <br/>
    *
    * 作者： zhangqing
    * @param currentPage
    * @param pageSize
    * @return
     */
    @GetMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取所有配置", notes = "获取所有配置", response = ConfigListResponse.class, 
                  tags = {"composite configuration_file management service API"})
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS204, message = "返回一组配置信息", 
                  response = ConfigListResponse.class) })          
    public ConfigListResponse getConfigs(@PathVariable("namespace") String namespace,
                             @RequestParam(value = "project_name",required = false) String projectName,  
                             @RequestParam(value = "detail", required = false,defaultValue = "false") String detail);  
                             
 
    /**
     * 
    * createConfig:创建一个新的配置. <br/>
    *
    * 作者： zhangqing
    * @param request
    * @return
     */
    @PostMapping(path = "/{namespace}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "创建配置", notes = "创建配置", response = ConfigCreateResponse.class, 
                  tags = { "composite configuration_file management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "创建成功", 
                            response = ConfigCreateResponse.class)})
    public ConfigCreateResponse createConfig(@PathVariable("namespace") String namespace,
                               @RequestParam(value = "project_name",required = false) String projectName, 
                               @RequestBody ConfigCreateRequest createRequest); 
                                   
           
    /**
     * getConfigByName:根据config主键获取config. <br/>
     *
     * 作者： zhangqing
     * @param name config的主键
     * @return 返回config的基本参数
     */
    @GetMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据名称查询对应配置", notes = "根据名称查询对应配置，并返回json格式", 
                  response = ConfigSpecifiedResponse.class,
                  tags = { "composite configuration_file management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "成功获取{name}配置的详细信息",
                  response = ConfigSpecifiedResponse.class)})             
    public ConfigSpecifiedResponse getConfigByName(@PathVariable("namespace") String namespace,
                                  @PathVariable("name") String name,
                                  @RequestParam(value="detail",required=false,defaultValue="true")boolean detail);
                                      

    /**
     *
    * updateConfig:根据配置名字来更新相应的配置数据 <br/>
    *
    * 作者： zhangqing
    * @param request
    * @param configName
    * @return
     */
    @PutMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据名称更新某个配置", notes = "根据名称更新某个配置", response = ConfigUpdateResponse.class, 
                  tags = {"composite configuration_file management service API"})
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "更新成功", 
                            response = ConfigUpdateResponse.class)})
    public ConfigUpdateResponse updateConfig(@PathVariable("namespace") String namespace,
                               @PathVariable("name") String name,
                               @RequestBody ConfigUpdateRequest request);

    /**
     * 
    * delConfig:根据配置名字来删除相应的配置 <br/>
    *
    * 作者： zhangqing
    * @param name
     */
    @DeleteMapping(path = "/{namespace}/{name}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据名称删除某个配置", notes = "根据名称删除某个配置", response = void.class, 
                  tags = { "composite configuration_file management service API" })
    @ApiResponses(value = { @ApiResponse(code = RESPONSE_SUCCESS200, message = "删除成功",
                            response = void.class) })            
    public void delConfig(@PathVariable("namespace") String namespace,@PathVariable("name") String name);



    /**
     * getConfigVersions:根据config名称获取config的所有版本. <br/>
     *
     * 作者： zhangqing
     * @param name config主键
     * @return 返回config的所有版本信息
     */
    @GetMapping(path = "/{namespace}/{name}/versions", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据名称获取配置所有版本", notes = "根据名称获取配置所有版本", response = JSONArray.class, 
                  tags = {"composite configuration_file management service API"})
    @ApiResponses(value = {@ApiResponse(code = RESPONSE_SUCCESS200, message = "返回配置所有版本信息的JSON格式",
                  response = JSONArray.class) })          
    public JSONArray getConfigVersions(@PathVariable("namespace") String namespace,@PathVariable("name") String name);  
}
