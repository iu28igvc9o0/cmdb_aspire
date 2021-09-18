package com.aspire.ums.cmdb.index;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenOverviewAPI
 * @Description 总租户利用率大屏——对外暴露的接口层
 * @Author luowenbo
 * @Date 2020/4/15 10:04
 * @Version 1.0
 */
@RequestMapping("/cmdb/index/overview")
public interface ItCloudScreenOverviewAPI {

    /*
     *  依据资源类型，获取设备类型
     * */
    @RequestMapping(value = "/list/dept", method = RequestMethod.GET)
    @ApiOperation(value = "依据资源类型，获取设备类型", notes = "依据资源类型，获取设备类型", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getDeviceTypeList(@RequestParam("moduleType") String moduleType,@RequestParam(value = "exclude",required = false) String exclude);

    /*
     *  依据租户，获取租户、业务系统总数
     * */
    @RequestMapping(value = "/tenantAndBz/dept", method = RequestMethod.POST)
    @ApiOperation(value = "依据租户，获取租户、业务系统总数", notes = "依据租户，获取租户、业务系统总数", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getTenantAndBzNumByDep(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据资源池，获取租户、业务系统总数
     * */
    @RequestMapping(value = "/tenantAndBz/rp", method = RequestMethod.POST)
    @ApiOperation(value = "依据资源池，获取租户、业务系统总数", notes = "依据资源池，获取租户、业务系统总数", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getTenantAndBzNumByRp(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取租户资源的分配量和使用量
     * */
    @RequestMapping(value = "/hasAndUse/devt", method = RequestMethod.POST)
    @ApiOperation(value = "依据设备类型，获取租户资源的分配量和使用量", notes = "依据设备类型，获取租户资源的分配量和使用量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getHasAndUseAltNumByDt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户类型，获取业务系统的资源分配量和使用量
     * */
    @RequestMapping(value = "/hasAndUse/devtAndDept", method = RequestMethod.POST)
    @ApiOperation(value = "依据设备类型和租户类型，获取业务系统的资源分配量和使用量", notes = "依据设备类型和业务系统，获取业务系统的资源分配量和使用量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getHasAndUseAltNumByDevtAndDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取月度的Agent部署的数量
     * */
    @RequestMapping(value = "/agentNum/all", method = RequestMethod.POST)
    @ApiOperation(value = "获取月度的Agent部署的数量", notes = "获取月度的Agent部署的数量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getAgentNum(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取每个租户下的Agent部署的数量
     * */
    @RequestMapping(value = "/agentNum/dept", method = RequestMethod.POST)
    @ApiOperation(value = "获取每个租户下的Agent部署的数量", notes = "获取每个租户下的Agent部署的数量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getAgentNumWithDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取月度的存储资源利用率
     * */
    @RequestMapping(value = "/storeUlt/devt", method = RequestMethod.POST)
    @ApiOperation(value = "依据设备类型，获取月度的存储资源利用率", notes = "依据设备类型，获取月度的存储资源利用率", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getStoreUtzByDt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户部门，获取月度业务系统的存储资源利用率
     * */
    @RequestMapping(value = "/storeUlt/tenantAndDevt", method = RequestMethod.POST)
    @ApiOperation(value = "依据设备类型和租户部门，获取月度业务系统的存储资源利用率", notes = "依据设备类型和租户部门，获取月度业务系统的存储资源利用率", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getStoreUtzByDtAndDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取资源回收的数量
     * */
    @RequestMapping(value = "/recycleCount/all", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源回收的数量", notes = "获取资源回收的数量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getRecycleNum(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统资源回收的数量
     * */
    @RequestMapping(value = "/recycleCount/devt", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统资源回收的数量", notes = "获取业务系统资源回收的数量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getRecycleNumWithDevt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统总数
     * */
    @RequestMapping(value = "/bzCount/total", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统总数", notes = "获取业务系统总数", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getBizSystemTotalCount(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的利用率
     * */
    @RequestMapping(value = "/bzCount/detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统总数", notes = "获取业务系统总数", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getLowUtlCount(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的租户业务系统数量
     * */
    @RequestMapping(value = "/bzList/detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取均峰值利用率低于30%的租户业务系统数量", notes = "获取均峰值利用率低于30%的租户业务系统数量", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getLowUltListWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  各租户设备的CPU和内存的均峰值或者均值
     * */
    @RequestMapping(value = "/cpuAndStore/list", method = RequestMethod.POST)
    @ApiOperation(value = "各租户设备的CPU和内存的均峰值或者均值", notes = "各租户设备的CPU和内存的均峰值或者均值", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getCpuAndStoreListWithDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存的均峰值或者均值
     * */
    @RequestMapping(value = "/cpuAndStore", method = RequestMethod.POST)
    @ApiOperation(value = "租户设备的CPU和内存的均峰值或者均值", notes = "租户设备的CPU和内存的均峰值或者均值", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getCpuAndStore(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  各租户考核项列表
     * */
    @RequestMapping(value = "/score/list", method = RequestMethod.POST)
    @ApiOperation(value = "各租户考核项列表", notes = "各租户考核项列表", tags = {"CMDB Index Overview API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> getScoreWithDept(@RequestBody ItCloudScreenOverviewRequest req);
}
