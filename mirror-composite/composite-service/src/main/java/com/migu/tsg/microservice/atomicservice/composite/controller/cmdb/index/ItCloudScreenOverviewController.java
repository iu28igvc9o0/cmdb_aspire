package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.index;

import com.aspire.mirror.composite.service.cmdb.index.ItCloudScreenOverviewAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.ItCloudScreenOverviewClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenOverviewController
 * @Description 控制层实现类
 * @Author luowenbo
 * @Date 2020/4/15 10:11
 * @Version 1.0
 */
@RestController
public class ItCloudScreenOverviewController implements ItCloudScreenOverviewAPI {

    @Autowired
    private ItCloudScreenOverviewClient screenOverviewClient;

    @Override
    public Map<String,Object> getDeviceTypeList(@RequestParam("moduleType") String moduleType,@RequestParam(value = "exclude",required = false) String exclude) {
        return screenOverviewClient.getDeviceTypeList(moduleType,exclude);
    }

    @Override
    public Map<String, Object> getTenantAndBzNumByDep(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getTenantAndBzNumByDep(req);
    }

    @Override
    public Map<String,Object> getTenantAndBzNumByRp(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getTenantAndBzNumByRp(req);
    }

    @Override
    public Map<String, Object> getHasAndUseAltNumByDt(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getHasAndUseAltNumByDt(req);
    }

    @Override
    public Map<String,Object> getHasAndUseAltNumByDevtAndDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getHasAndUseAltNumByDevtAndDept(req);
    }

    @Override
    public Map<String, Object> getStoreUtzByDt(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getStoreUtzByDt(req);
    }

    @Override
    public Map<String, Object> getStoreUtzByDtAndDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getStoreUtzByDtAndDept(req);
    }

    @Override
    public Map<String, Object> getCpuAndStoreListWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getCpuAndStoreListWithDept(req);
    }

    @Override
    public Map<String, Object> getCpuAndStore(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getCpuAndStore(req);
    }

    @Override
    public Map<String, Object> getAgentNum(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getAgentNum(req);
    }

    @Override
    public Map<String,Object> getAgentNumWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getAgentNumWithDept(req);
    }

    @Override
    public Map<String, Object> getRecycleNum(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getRecycleNum(req);
    }

    @Override
    public Map<String,Object> getRecycleNumWithDevt(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getRecycleNumWithDevt(req);
    }

    @Override
    public Map<String, Object> getBizSystemTotalCount(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getBizSystemTotalCount(req);
    }

    @Override
    public Map<String, Object> getLowUtlCount(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getLowUtlCount(req);
    }

    @Override
    public Map<String,Object> getLowUltListWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getLowUltListWithDept(req);
    }

    @Override
    public Map<String,Object> getScoreWithDept(@RequestBody ItCloudScreenOverviewRequest req) {
        return screenOverviewClient.getScoreWithDept(req);
    }
}
