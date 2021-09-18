package com.migu.tsg.microservice.atomicservice.composite.service.resmgr;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogAppsCreateReponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogAppsCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogDashboardUrl;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogListApp;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogSpringConfigService;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CatalogSpringZuul;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CreateCatalogResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload.CreateCatalogRrquest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;

/**
* 应用市场   接口
* Project Name:composite-api
* File Name:ICompResServicesCatalog.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr
* ClassName: ICompResServicesCatalog <br/>
* date: 2017年10月3日 上午11:11:57 <br/>
* 应用市场    接口
* @author weishuai
* @version 
* @since JDK 1.8
*/
@RequestMapping("${version}")
@Api(value = "Composite Resource management(Catalog)", 
     description = "Composite Resource management(Catalog)")  
public interface ICompResServicesCatalog {

    /**
     * 获取应用市场应用列表.<br/>
     * 作者： weishuai
     * @param project_name
     * @param APP_NAME
     * @param APP_TYPE
     * @return
     */
    @ApiOperation(value = "获取应用市场应用列表", notes = "获取应用市场应用列表",
                  response = CatalogListApp.class, 
                  tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "List supported application/cluster",
                                        response = CatalogListApp.class)})
    @GetMapping(path = "/catalog/{namespace}", 
                produces = "application/json;charset=UTF-8")
    JSONObject getCatalogList(
            @RequestParam(name = "APP_NAME", required = false) String app_name,
            @RequestParam(name = "app_type", required = false) String app_type,
            @RequestParam(name = "project_name") String project_name);
    
    
    @ApiOperation(value = "获取分类应用数量", notes = "获取分类应用数量",
            response = CatalogListApp.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                           message = "List application number by type.",
                           response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/app_num", produces = "application/json;charset=UTF-8")
    JSONObject getCatalogNumByType(@PathVariable("namespace") String namespace);
    
    /**
     * 获取应用市场集合.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取应用市场集合", notes = "获取应用市场集合",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
    message = "Get collection of applications(app_name, status)",
    response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/{app_name}/apps", 
    produces = "application/json;charset=UTF-8")
    JSONObject getCatalogCollection(
            @PathVariable("app_name") String app_name);
    
    /**
     * 创建应用市场.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建应用市场", notes = "创建应用市场",
            response = CreateCatalogResponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
    message = "Create an application/cluster",
    response = CreateCatalogResponse.class)})
    @PostMapping(path = "/catalog/{namespace}/{app_name}/apps", 
            produces = "application/json;charset=UTF-8")
    CreateCatalogResponse  createCatalog(
            @PathVariable(name = "app_name") String app_name,
            @RequestParam(name = "project_name", required = false) String project_name,
            @RequestBody CreateCatalogRrquest createcatalog);
    
    /**
     * 更新SpringConfigService.<br/>
     * 作者： weishuai
     * @param app_name
     * @param uuid
     * @return
     */
    @ApiOperation(value = "更新ConfigService", notes = "更新ConfigService",
            response = CreateCatalogResponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Update the config server and restart",
                                        response = CreateCatalogResponse.class)})
    @PutMapping(path = "/catalog/{namespace}/spring_config_server/apps/{uuid}", 
                produces = "application/json;charset=UTF-8")
    CreateCatalogResponse putCatalogSpringConfigService(
            @PathVariable("uuid") String uuid,
            @RequestBody CatalogSpringConfigService configservice);
    
    /**
     * 更新SpringZuul.<br/>
     * 作者： weishuai
     * @param app_name
     * @param uuid
     * @return
     */
    @ApiOperation(value = "更新SpringZuul", notes = "更新SpringZuul",
            response = CreateCatalogResponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Update the config server and restart",
                                        response = CreateCatalogResponse.class)})
    @PutMapping(path = "/catalog/{namespace}/spring_zuul/apps/{uuid}", 
                produces = "application/json;charset=UTF-8")
    CreateCatalogResponse putSpringZuul(
            @PathVariable("uuid") String uuid,
            @RequestBody CatalogSpringZuul springzuul);
    
    /**
     * 获取面板Url.<br/>
     * 作者： weishuai
     * @param app_name
     * @param uuid
     * @param trubine_uuid
     * @return
     */
    @ApiOperation(value = "获取面板Url", notes = "获取面板Url",
            response = CatalogDashboardUrl.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                message = "Get the new dashboard url based on the input turbine",
                response = CatalogDashboardUrl.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_hystrix_dashboard/apps/{uuid}/dashboard/url", 
                produces = "application/json;charset=UTF-8")
    CatalogDashboardUrl getSpringDashboardUrl(
            @PathVariable("uuid") String uuid,
            @RequestParam("trubine_uuid") String trubine_uuid,
            @RequestParam("dashboard_service_id") String dashboard_service_id);
    
    /**
     * 获取Eureka详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Eureka详情", notes = "获取Eureka详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_eureka", produces = "application/json;charset=UTF-8")
    JSONObject getSpringEurekaDetail();
    
    /**
     * 获取ConfigService详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取ConfigService详情", notes = "获取ConfigService详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_config_server", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringConfigServiceDetail();
    
    /**
     * 获取Zipkin详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Zipkin详情", notes = "获取Zipkin详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_zipkin", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringZipkinDetail();
    
    /**
     * 获取Message详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Meaage详情", notes = "获取Message详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the message.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_message", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringMessage();
    
    /**
     * 获取Turbine详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Turbine详情", notes = "获取Turbine详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_turbine", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringTurbineDetail();
    
    /**
     * 获取Zuul详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Zuul详情", notes = "获取Zuul详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_zuul", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringZuulDetail();
    
    /**
     * 获取Dashboard详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取Dashboard详情", notes = "获取Dashboard详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/spring_hystrix_dashboard", 
                produces = "application/json;charset=UTF-8")
    JSONObject getSpringDashboardDetail();


    @ApiOperation(value = "获取Redis详情", notes = "获取Redis详情",
            response = JSONObject.class,
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Get the config info.",
            response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/redis",
            produces = "application/json;charset=UTF-8")
    JSONObject getRedisDetail();

    @ApiOperation(value = "获取ElasticSearch详情", notes = "获取ElasticSearch详情",
            response = JSONObject.class,
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200,
            message = "Get the ElasticSearch info.",
            response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/elasticsearch",
            produces = "application/json;charset=UTF-8")
    JSONObject getElasticSearchDetail();
    
    /**
     * 创建Eureka.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建Eureka", notes = "创建Eureka",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_eureka/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringEureka(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 创建ConfigService.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建ConfigService", notes = "创建ConfigService",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_config_server/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringConfigService(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 创建Zipkin.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建Zipkin", notes = "创建Zipkin",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_zipkin/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringZipkin(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 创建Turbine.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建Turbine", notes = "创建Turbine",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_turbine/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringTurbine(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 创建Zuul.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建Zuul", notes = "创建Zuul",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_zuul/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringZuul(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 创建Dashboard.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建Dashboard", notes = "创建Dashboard",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the eureka,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/spring_hystrix_dashboard/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createSpringDashboard(
            @RequestBody CatalogAppsCreateRequest appsrequest);
    
    /**
     * 获取mysql详情.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "获取mysql详情", notes = "获取mysql详情",
            response = JSONObject.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, 
                                        message = "Get the config info.",
                                        response = JSONObject.class)})
    @GetMapping(path = "/catalog/{namespace}/mysql", produces = "application/json;charset=UTF-8")
    JSONObject getMysqlDetail();
    
    
    /**
     * 创建MysqlService.<br/>
     * 作者： weishuai
     * @param app_name
     * @return
     */
    @ApiOperation(value = "创建MysqlService", notes = "创建MysqlService",
            response = CatalogAppsCreateReponse.class, 
            tags = {"Composite Resource management(Catalog) service API"})
    @ApiResponses(value = {@ApiResponse(code = 201, 
                                        message = "Create the mysql,",
                                        response = CatalogAppsCreateReponse.class)})
    @PostMapping(path = "/catalog/{namespace}/mysql/apps", 
            produces = "application/json;charset=UTF-8")
    CatalogAppsCreateReponse createMysqlService(
            @RequestBody CatalogAppsCreateRequest appsrequest);
}
