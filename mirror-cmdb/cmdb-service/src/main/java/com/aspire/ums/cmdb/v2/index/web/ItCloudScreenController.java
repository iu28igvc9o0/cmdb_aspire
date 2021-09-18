package com.aspire.ums.cmdb.v2.index.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.IItCloudScreenAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenController
 * @Description IT云租户大屏——控制层类
 * @Author luowenbo
 * @Date 2020/2/27 13:59
 * @Version 1.0
 */
@RestController
@Slf4j
public class ItCloudScreenController implements IItCloudScreenAPI {

    @Autowired
    private ItCloudScreenService itCloudScreenService;

    @Override
    public Map<String, Object> getResourceAllocateList(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getResourceAllocateList()  params -> {}", req);
        return itCloudScreenService.getResourceAllocateList(req);
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByMonth(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getMaxUtilizationByMonth()  params -> {}", req);
        return itCloudScreenService.getMaxUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getMaxUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getMaxUtilizationByBizSystem()  params -> {}", req);
        return itCloudScreenService.getMaxUtilizationByBizSystem(req);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByMonth(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getAvgUtilizationByMonth()  params -> {}", req);
        return itCloudScreenService.getAvgUtilizationByMonth(req);
    }

    @Override
    public List<Map<String, Object>> getAvgUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getAvgUtilizationByBizSystem()  params -> {}", req);
        return itCloudScreenService.getAvgUtilizationByBizSystem(req);
    }

    @Override
    public Map<String, Object> getMonthMaxUtilization(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getMonthMaxUtilization()  params -> {}", req);
        return itCloudScreenService.getMonthMaxUtilization(req);
    }

    @Override
    public Map<String, Object> getMonthAvgUtilization(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getMonthAvgUtilization()  params -> {}", req);
        return itCloudScreenService.getMonthAvgUtilization(req);
    }

    @Override
    public Map<String,String> getBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getBizSystemCount()  params -> {}", req);
        return itCloudScreenService.getBizSystemCount(req);
    }

    @Override
    public Map<String, String> getPmBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getPmBizSystemCount()  params -> {}", req);
        return itCloudScreenService.getPmBizSystemCount(req);
    }

    @Override
    public List<Map<String, Object>> getBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getBizSystemListWithIdcType()  params -> {}", req);
        return itCloudScreenService.getBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String, String> getVmBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getVmBizSystemCount()  params -> {}", req);
        return itCloudScreenService.getVmBizSystemCount(req);
    }

    @Override
    public Map<String, String> getStoreBizSystemCount(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getStoreBizSystemCount()  params -> {}", req);
        return itCloudScreenService.getStoreBizSystemCount(req);
    }

    @Override
    public List<Map<String, Object>> getStoreBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getStoreBizSystemCount()  params -> {}", req);
        return itCloudScreenService.getStoreBizSystemListWithIdcType(req);
    }

    @Override
    public Map<String, Object> judgeStatus(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getMaxBizSystemCount()  params -> {}", req);
        return itCloudScreenService.judgeStatus(req);
    }

    @Override
    public JSONObject udpateCpuMaxList(@RequestParam("monthlyTime") String monthlyTime) {
        JSONObject rs = new JSONObject();
        try {
            itCloudScreenService.updateCpuMaxList(monthlyTime);
            rs.put("flag",true);
            rs.put("msg","success");
        } catch (Exception e) {
            e.printStackTrace();
            rs.put("flag",false);
            rs.put("msg",e.getMessage());
        }
        return rs;
    }

    @Override
    public LinkedHashMap<String, Object> getSpecificDeviceByBz(@RequestBody ItCloudScreenRequest req) {
        log.info("Request into ItCloudScreenController.getSpecificDeviceByBz()  params -> {}", req);
        return itCloudScreenService.getSpecificDeviceByBz(req);
    }
}
