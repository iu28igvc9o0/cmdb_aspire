package com.migu.tsg.microservice.atomicservice.composite.service.resmgr;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.AppMetaDataResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.ApplicationDetailsResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CompApplicationCreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.ListResApplicationResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;

/**
* composite层应用资源对外接口
* Project Name:composite-api
* File Name:ICompResServicesApplication.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr
* ClassName: ICompResServicesApplication <br/>
* date: 2017年9月18日 下午8:43:06 <br/>
* composite层应用资源对外接口
* @author weishuai
* @version 
* @since JDK 1.8
*/
@RequestMapping(path = "${version}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Composite Resource management(Application)", description = "Composite Resource management(Application")
public interface ICompResServicesApplication {

    /**
     * 加载应用列表.<br/>
     * 作者： weishuai
     * @param namespace
     * @param region_name
     * @param basic
     * @param detail
     * @param instance_ip
     * @param project_name
     * @param name
     * @param label
     * @return
     */
    @ApiOperation(value = "获取应用列表", notes = "获取应用列表", 
                 response = ListResApplicationResponse.class, 
                 tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                  message = "Get collection of applications of a region", 
                  response = ListResApplicationResponse.class)})
    @GetMapping(path = "/applications/{namespace}", produces = "application/json;charset=UTF-8")
    List<ListResApplicationResponse>  listResApplication(
           @PathVariable("namespace") String namespace,
           @RequestParam(name = "region") String region, 
           @RequestParam(name = "basic", defaultValue ="false", required = false) boolean basic, 
           @RequestParam(name = "detail", required = false) boolean detail, 
           @RequestParam(name = "instance_ip") String instance_ip, 
           @RequestParam(name = "project_name") String project_name, 
           @RequestParam(name = "name", required = false) String name, 
           @RequestParam(name = "label", required = false) String label);
   
    /**
     * 获取应用详情.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取应用详情", notes = "获取应用详情", 
            response = ApplicationDetailsResponse.class, 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                 message = "Get application details", 
                 response = ApplicationDetailsResponse.class)})
    @GetMapping(path = "/applications/{namespace}/{app_name}", 
                produces = "application/json;charset=UTF-8")
    ApplicationDetailsResponse appdetailresponse(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 删除应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "删除应用", notes = "删除应用", 
                 tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=204, message = "Delete application ")})
    @DeleteMapping(path = "/applications/{namespace}/{app_name}")
    BaseResponse deleteApplication(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 获取应用程序元数据.<br/>
     * 作者： weishuai
     * @param namespace
     * @param project_name
     * @param app_name
     * @param region
     * @return
     */
    @ApiOperation(value = "获取应用程序元数据", notes = "获取应用程序元数据", 
            response = AppMetaDataResponse.class, 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                 message = "Get application meta data", 
                 response = AppMetaDataResponse.class)})
    @GetMapping(path = "/applications/{namespace}/{app_name}/metadata", 
                produces = "application/json;charset=UTF-8")
    AppMetaDataResponse getAppMetaDataResponse(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 启动应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "启动应用", notes = "启动应用", 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, message = "Start an application ")})
    @PutMapping(path = "/applications/{namespace}/{app_name}/start")
    void startApplication(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 结束应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "结束应用", notes = "结束应用", 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, message = "Stop an application ")})
    @PutMapping(path = "/applications/{namespace}/{app_name}/stop")
    void stopApplication(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 重试应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "重试应用", notes = "重试应用", response = JSONObject.class,
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                  message = "Retry application, make service to their target state", 
                  response = JSONObject.class)})
    @PutMapping(path = "/applications/{namespace}/{app_name}/retry", 
                produces = "application/json;charset=UTF-8")
    JSONObject retryApplication(
            @PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 创建应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param project_name
     * @param app_name
     * @param region
     * @return
     */
    @ApiOperation(value = "创建应用", notes = "创建应用", 
                  response = CompApplicationCreateResponse.class, 
                  tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=201, 
                  message = "Create an application，file name: compose.yaml", 
                  response = CompApplicationCreateResponse.class)})
    @PostMapping(path = "/applications/{namespace}", consumes = MediaType.ALL_VALUE,
                 produces = "application/json;charset=UTF-8")
    CompApplicationCreateResponse createApplication(
           @PathVariable("namespace") String namespace,
           @RequestParam(name = "project_name") String project_name, 
           @RequestParam("services") MultipartFile services,
           @RequestParam("app_name") String app_name,
           @RequestParam("region") String region,
           @RequestParam("knamespace") String knamespace
           );
    
    /**
     * 更新应用.<br/>
     * 作者： weishuai
     * @param namespace
     * @param project_name
     * @param app_name
     * @param region
     * @return
     */
    @ApiOperation(value = "更新应用", notes = "更新应用", 
            response = String.class,
                  tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, message = "Update application", 
            response = String.class)})
    @PostMapping(path = "/applications/{namespace}/{app_name}",
                 produces = MediaType.TEXT_PLAIN_VALUE)
    String updateApplication(
            @PathVariable("namespace") String namespace,
            @PathVariable(name = "app_name" ) String app_name, 
            @RequestParam(name = "services") MultipartFile services,
            @RequestParam(name = "timeout") String timeout, 
            @RequestParam(name = "strict_mode") boolean strict_mode );
    
    /**
     * 获取Yaml.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Yaml", notes = "获取Yaml", 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                  message = "Get application yaml file content (alauda-compose)")})
    @GetMapping(path = "/applications/{namespace}/{app_name}/yaml", 
                produces = MediaType.TEXT_PLAIN_VALUE)
    String getYaml(@PathVariable("namespace") String namespace,
            @PathVariable("app_name") String app_name);
    
    /**
     * 获取compose-yaml.<br/>
     * 作者： weishuai
     * @param namespace
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取composeyaml", notes = "获取composeyaml", 
            tags = {"Composite Resource management(Application) service API"})
    @ApiResponses(value = {@ApiResponse(code=200, 
                  message = "Get application compose-yaml file content")})
    @GetMapping(path = "/applications/{namespace}/{app_name}/compose-yaml", 
                produces = MediaType.TEXT_PLAIN_VALUE)
    String getComposeYaml(@PathVariable("namespace") String namespace, 
            @PathVariable("app_name") String app_name);
}
