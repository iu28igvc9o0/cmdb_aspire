package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;

/**
 * @ClassName ItCloudScreenOverviewClient
 * @Description CRT调用IT租户利用率大屏总览接口客户端
 * @Author luowenbo
 * @Date 2020/4/15 10:06
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface ItCloudScreenOverviewClient {
    /*
     *  依据资源类型，获取设备类型
     * */
    @RequestMapping(value = "/cmdb/index/overview/list/dept", method = RequestMethod.GET)
    Map<String,Object> getDeviceTypeList(@RequestParam("moduleType") String moduleType,@RequestParam(value = "exclude",required = false) String exclude);

    /*
     *  依据租户，获取租户、业务系统总数
     * */
    @RequestMapping(value = "/cmdb/index/overview/tenantAndBz/dept", method = RequestMethod.POST)
    Map<String,Object> getTenantAndBzNumByDep(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据资源池，获取租户、业务系统总数
     * */
    @RequestMapping(value = "/cmdb/index/overview/tenantAndBz/rp", method = RequestMethod.POST)
    Map<String,Object> getTenantAndBzNumByRp(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取租户资源的分配量和使用量
     * */
    @RequestMapping(value = "/cmdb/index/overview/hasAndUse/devt", method = RequestMethod.POST)
    Map<String, Object> getHasAndUseAltNumByDt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户类型，获取业务系统的资源分配量和使用量
     * */
    @RequestMapping(value = "/cmdb/index/overview/hasAndUse/devtAndDept", method = RequestMethod.POST)
    Map<String,Object> getHasAndUseAltNumByDevtAndDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取月度的存储资源利用率
     * */
    @RequestMapping(value = "/cmdb/index/overview/storeUlt/devt", method = RequestMethod.POST)
    Map<String, Object> getStoreUtzByDt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户部门，获取月度业务系统的存储资源利用率
     * */
    @RequestMapping(value = "/cmdb/index/overview/storeUlt/tenantAndDevt", method = RequestMethod.POST)
    Map<String,Object> getStoreUtzByDtAndDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  各租户设备的CPU和内存的均峰值或者均值
     * */
    @RequestMapping(value = "/cmdb/index/overview/cpuAndStore/list", method = RequestMethod.POST)
    Map<String, Object> getCpuAndStoreListWithDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存的均峰值或者均值
     * */
    @RequestMapping(value = "/cmdb/index/overview/cpuAndStore", method = RequestMethod.POST)
    Map<String,Object> getCpuAndStore(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取月度的Agent部署的数量
     * */
    @RequestMapping(value = "/cmdb/index/overview/agentNum/all", method = RequestMethod.POST)
    Map<String, Object> getAgentNum(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取每个租户下的Agent部署的数量
     * */
    @RequestMapping(value = "/cmdb/index/overview/agentNum/dept", method = RequestMethod.POST)
    Map<String,Object> getAgentNumWithDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取资源回收的数量
     * */
    @RequestMapping(value = "/cmdb/index/overview/recycleCount/all", method = RequestMethod.POST)
    Map<String,Object> getRecycleNum(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统资源回收的数量
     * */
    @RequestMapping(value = "/cmdb/index/overview/recycleCount/devt", method = RequestMethod.POST)
    Map<String,Object> getRecycleNumWithDevt(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统总数
     * */
    @RequestMapping(value = "/cmdb/index/overview/bzCount/total", method = RequestMethod.POST)
    Map<String,Object> getBizSystemTotalCount(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的利用率
     * */
    @RequestMapping(value = "/cmdb/index/overview/bzCount/detail", method = RequestMethod.POST)
    Map<String, Object> getLowUtlCount(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的租户业务系统数量
     * */
    @RequestMapping(value = "/cmdb/index/overview/bzList/detail", method = RequestMethod.POST)
    Map<String,Object> getLowUltListWithDept(@RequestBody ItCloudScreenOverviewRequest req);

    /*
     *  各租户考核项列表
     * */
    @RequestMapping(value = "/cmdb/index/overview/score/list", method = RequestMethod.POST)
    Map<String,Object> getScoreWithDept(@RequestBody ItCloudScreenOverviewRequest req);
}
