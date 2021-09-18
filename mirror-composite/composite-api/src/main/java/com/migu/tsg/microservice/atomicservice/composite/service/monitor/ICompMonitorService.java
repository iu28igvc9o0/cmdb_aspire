package com.migu.tsg.microservice.atomicservice.composite.service.monitor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.DashChartCreateUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.DashChartDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.DashboardCreateUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.DashboardCreateUpdateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.DashboardDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload.ListDashboardsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONArray;


/**
* Composite Monitor service api
* Project Name:composite-api
* File Name:ICompDashboardService.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard
* ClassName: ICompDashboardService <br/>
* date: 2017年10月9日 下午3:09:42 <br/>
* Composite Dashboard service api
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@RequestMapping(path = "/v2/monitor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Composite Monitor service API", description = "Composite Monitor service API")
public interface ICompMonitorService {
    
    /**
     * 获取Dashboard列表.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    // TODO 添加跨域 region_name
    @ApiOperation(value = "获取Dashboard列表", notes = "获取Dashboard列表",
           response = ListDashboardsResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get dashboard list",
                                        response = ListDashboardsResponse.class)})
    @GetMapping(path = "/{namespace}/dashboards", consumes = MediaType.ALL_VALUE)
    ListDashboardsResponse listDashboards(@PathVariable("namespace") String namespace,
             @RequestParam(name = "region_name", required = false) String regionName);
    
    
    /**
     * 创建Dashboard.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "创建Dashboard", notes = "创建Dashboard",
           response = DashboardCreateUpdateResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create new dashboard",
                                        response = DashboardCreateUpdateResponse.class)})
    @PostMapping(path = "/{namespace}/dashboards", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DashboardCreateUpdateResponse createDashboard(@PathVariable("namespace") String namespace, 
                                                         @RequestBody DashboardCreateUpdateRequest createData);
    
    /**
     * 获取Dashboard详情.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取Dashboard详情", notes = "获取Dashboard详情",
           response = DashboardDetailResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get dashboard detail data",
                                        response = DashboardDetailResponse.class)})
    @GetMapping(path = "/{namespace}/dashboards/{dashboard_uuid}", consumes = MediaType.ALL_VALUE)
    DashboardDetailResponse getDashboardDetail(@PathVariable("namespace") String namespace,
                                                       @RequestParam("region_name") String regionName,
                                                       @PathVariable("dashboard_uuid") String dashboardUuid);
    
    
    /**
     * 获取Dashboard详情.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取Dashboard详情", notes = "获取Dashboard详情",
           response = DashboardCreateUpdateResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get dashboard detail data",
                                        response = DashboardCreateUpdateResponse.class)})
    @PutMapping(path = "/{namespace}/dashboards/{dashboard_uuid}", 
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DashboardCreateUpdateResponse updateDashboard(@PathVariable("namespace") String namespace,
                                                  @RequestParam("region_name") String regionName,
                                                         @PathVariable("dashboard_uuid") String dashboardUuid,
                                                         @RequestBody DashboardCreateUpdateRequest updateData);
    
    /**
     * 获取Dashboard详情.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "获取Dashboard详情", notes = "获取Dashboard详情",
                  tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get dashboard detail data")})
    @DeleteMapping(path = "/{namespace}/dashboards/{dashboard_uuid}", consumes = MediaType.ALL_VALUE)
    BaseResponse removeDashboard(@PathVariable("namespace") String namespace,
                                 @RequestParam("region_name") String regionName,
                                @PathVariable("dashboard_uuid") String dashboardUuid);
    
    
    /**
     * 创建Dashboard chart.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "创建Dashboard chart", notes = "创建Dashboard chart",
           response = DashChartDetailResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "create new dashboard chart",
                                        response = DashChartDetailResponse.class)})
    @PostMapping(path = "/{namespace}/dashboards/{dashboard_uuid}/charts", 
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DashChartDetailResponse createDashboardChart(@PathVariable("namespace") String namespace,
                                                 @RequestParam("region_name") String regionName,
                                                        @PathVariable("dashboard_uuid") String dashboardUuid,
                                                        @RequestBody DashChartCreateUpdateRequest createData);
    
    /**
     * 更新Dashboard下Chart<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "更新Dashboard下Chart", notes = "更新Dashboard下Chart",
           response = DashboardCreateUpdateResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "update a dashboard chart",
                                        response = DashboardCreateUpdateResponse.class)})
    @PutMapping(path = "/{namespace}/dashboards/{dashboard_uuid}/charts/{charts_uuid}", 
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    DashChartDetailResponse updateDashChart(@PathVariable("namespace") String namespace,
                                            @RequestParam("region_name") String regionName,
                                                         @PathVariable("dashboard_uuid") String dashboardUuid, 
                                                         @PathVariable("charts_uuid") String chartUuid,
                                                         @RequestBody DashChartCreateUpdateRequest updateData);
    
    /**
     * 删除Dashboard下Chart.<br/>
     *
     * 作者： pengguihua
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "删除Dashboard下Chart", notes = "删除Dashboard下Chart",
           response = DashboardDetailResponse.class, tags = {"Composite Monitor Dashboard service API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "delete a dashboard chart")})
    @DeleteMapping(path = "/{namespace}/dashboards/{dashboard_uuid}/charts/{charts_uuid}", 
                   consumes = MediaType.ALL_VALUE)
    BaseResponse removeDashChart(@PathVariable("namespace") String namespace,
                                 @RequestParam("region_name") String regionName,
                                @PathVariable("dashboard_uuid") String dashboardUuid, 
                                @PathVariable("charts_uuid") String chartUuid);
    
	/**
	 * 获取metrics.<br/>
	 *
	 * 作者： pengguihua
	 * 
	 * @param namespace
	 * @param req
	 * @return
	 */
	@ApiOperation(value = "获取metrics", notes = "获取metrics", 
				  response = JSONArray.class, tags = {"Composite Monitor Metric service API" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "get metric data", response = JSONArray.class) })
	@GetMapping(path = "/{namespace}/metrics", consumes = MediaType.ALL_VALUE)
	Object listMetrics(@PathVariable("namespace") String namespace, 
	                             @RequestParam(name = "window", required = false) String window,
	                             @RequestParam(name = "simple", required = false) String simple,
	                             @RequestParam(name = "metrics", required = false) String metrics,
                                 @RequestParam(value = "region_name", required = false) String regionName);
	
	
	/**
     * query metrics.<br/>
     *
     * 作者： pengguihua
     * 
     * @param namespace
     * @param req
     * @return
     */
    @ApiOperation(value = "query metrics", notes = "query metrics", 
                  response = JSONArray.class, tags = {"Composite Monitor Metric service API" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "query metrics", response = JSONArray.class) })
    @GetMapping(path = "/{namespace}/metrics/query", consumes = MediaType.ALL_VALUE)
    JSONArray queryMetrics(@PathVariable("namespace") String namespace, 
                                  @RequestParam("q") String qs, 
                                  @RequestParam(name = "start", required = false) String start, 
                                  @RequestParam(name = "end", required = false) String end,
                                  @RequestParam(value = "region_name", required = false) String regionName);
    
}
