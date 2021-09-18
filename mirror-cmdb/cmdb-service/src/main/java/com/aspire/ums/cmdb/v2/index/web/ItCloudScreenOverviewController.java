package com.aspire.ums.cmdb.v2.index.web;

import com.aspire.ums.cmdb.index.ItCloudScreenOverviewAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenStoreLowUltRequest;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenOverviewService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenStoreLowUtilizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenOverviewController
 * @Description 接口实现类
 * @Author luowenbo
 * @Date 2020/4/15 10:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class ItCloudScreenOverviewController implements ItCloudScreenOverviewAPI {
    @Autowired
    private ItCloudScreenOverviewService service;
    @Autowired
    private ItCloudScreenStoreLowUtilizationService storeLowUtilizationService;

    @Override
    public Map<String,Object> getDeviceTypeList(@RequestParam("moduleType") String moduleType,@RequestParam(value = "exclude",required = false) String exclude) {
        log.info("Request into ItCloudScreenOverviewController.getDeviceTypeList()  params -> {}", moduleType);
        return service.getDeviceTypeList(moduleType,exclude);
    }

    @Override
    public Map<String,Object> getTenantAndBzNumByDep(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getTenantAndBzNumByDep()  params -> {}", req);
        return service.getTenantAndBzNumByDep(req);
    }

    @Override
    public Map<String, Object> getTenantAndBzNumByRp(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getTenantAndBzNumByRp()  params -> {}", req);
        return service.getTenantAndBzNumByRp(req);
    }

    @Override
    public Map<String, Object> getHasAndUseAltNumByDt(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getHasAndUseAltNumByDt()  params -> {}", req);
        return service.getHasAndUseAltNumByDt(req);
    }

    @Override
    public Map<String, Object> getHasAndUseAltNumByDevtAndDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getHasAndUseAltNumByDevtAndDept()  params -> {}", req);
        return service.getHasAndUseAltNumByDevtAndDept(req);
    }

    @Override
    public Map<String, Object> getAgentNum(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getAgentNum()  params -> {}", req);
        return service.getAgentNum(req);
    }

    @Override
    public Map<String, Object> getAgentNumWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getAgentNumWithDept()  params -> {}", req);
        return service.getAgentNumWithDept(req);
    }

    @Override
    public Map<String, Object> getStoreUtzByDt(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getStoreUtzByDt()  params -> {}", req);
        return service.getStoreUtzByDt(req);
    }

    @Override
    public Map<String, Object> getStoreUtzByDtAndDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getStoreUtzByDtAndDept()  params -> {}", req);
        return service.getStoreUtzByDtAndDept(req);
    }

    @Override
    public Map<String,Object> getRecycleNum(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getRecycleNum()  params -> {}", req);
        return service.getRecycleNum(req);
    }

    @Override
    public Map<String, Object> getRecycleNumWithDevt(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getRecycleNumWithDevt()  params -> {}", req);
        return service.getRecycleNumWithDevt(req);
    }

    @Override
    public Map<String, Object> getBizSystemTotalCount(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getBizSystemTotalCount()  params -> {}", req);
        return service.getBizSystemTotalCount(req);
    }

    @Override
    public Map<String, Object> getLowUtlCount(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getLowUtlCount()  params -> {}", req);
        if("云存储".equals(req.getDeviceType())) {
            // 云存储处理
            ItCloudScreenStoreLowUltRequest storeLowUltRequest = new ItCloudScreenStoreLowUltRequest();
            storeLowUltRequest.setMonthlyTime(req.getMonthlyTime());
            return storeLowUtilizationService.getLowStoreUltCount(storeLowUltRequest);
        } else {
            return service.getLowUtlCount(req);
        }
    }

    @Override
    public Map<String, Object> getLowUltListWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getLowUltListWithDept()  params -> {}", req);
        if("云存储".equals(req.getDeviceType())) {
            // 云存储处理
            ItCloudScreenStoreLowUltRequest storeLowUltRequest = new ItCloudScreenStoreLowUltRequest();
            storeLowUltRequest.setMonthlyTime(req.getMonthlyTime());
            storeLowUltRequest.setDepType(req.getDepType());
            return storeLowUtilizationService.getLowStoreUltListWithDept(storeLowUltRequest);
        } else {
            return service.getLowUltListWithDept(req);
        }
    }

    @Override
    public Map<String, Object> getCpuAndStoreListWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getCpuAndStoreListWithDept()  params -> {}", req);
        if("MAX".equals(req.getCalculateType())) {
            return service.getCpuAndStoreMaxWithDept(req);
        } else {
            return service.getCpuAndStoreAvgWithDept(req);
        }
    }

    @Override
    public Map<String, Object> getCpuAndStore(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getCpuAndStore()  params -> {}", req);
        if("MAX".equals(req.getCalculateType())) {
            return service.getCpuAndStoreMax(req);
        } else {
            return service.getCpuAndStoreAvg(req);
        }
    }

    @Override
    public Map<String, Object> getScoreWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        log.info("Request into ItCloudScreenOverviewController.getScoreWithDept()  params -> {}", req);
        if("ALL".equals(req.getCalculateType())) {
            // 累计考核情况
            return service.getTotalMonthCheckScoreList(req);
        } else {
            return service.getScoreWithDept(req);
        }
    }
}