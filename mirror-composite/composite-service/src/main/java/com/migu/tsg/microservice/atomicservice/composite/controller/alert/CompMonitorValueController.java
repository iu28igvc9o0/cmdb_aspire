package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.AlertValueSearchRequest;
import com.aspire.mirror.composite.service.alert.ICompMonitorValueService;
import com.aspire.mirror.composite.service.alert.payload.CompAlertValueRequest;
import com.aspire.mirror.composite.service.alert.payload.CompMonitorRequest;
import com.aspire.mirror.composite.service.alert.payload.CompMonitorResult;
import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.HistoryServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

/**
 * 监控值控制层（拓扑用）
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.alert
 * 类名称:    CompMonitorValueController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 19:39
 * 版本:      v1.0
 */
@RestController
public class CompMonitorValueController implements ICompMonitorValueService {
    @Autowired
    private HistoryServiceClient historyServiceClient;

    @Autowired
    private AlertsServiceClient alertsServiceClient;

    /**
     * 获取监控值
     *
     * @param monitorRequest
     * @return
     * @throws Exception
     */
    @Override
    public CompMonitorResult getMonitorValue(@RequestBody @Validated CompMonitorRequest monitorRequest) throws
            Exception {
        HistorySearchRequest historySearchRequest = new HistorySearchRequest();
        historySearchRequest.setItemList(monitorRequest.getItemKeyList());
        String deviceString = monitorRequest.getDeviceString();
        Map<String, List<String>> ipMap = Maps.newHashMap();
        getIpMap(deviceString, ipMap);
        historySearchRequest.setIpMap(ipMap);
        historySearchRequest.setCountType(monitorRequest.getCountType());
        Map<String, Object> result = historyServiceClient.getMonitorValue(historySearchRequest);
        CompMonitorResult compMonitorResult = new CompMonitorResult();
        compMonitorResult.setValue(Double.valueOf((String) result.get("value")));
        return compMonitorResult;
    }

    @Override
    public CompMonitorResult getAlertValue(@RequestBody @Validated CompAlertValueRequest monitorRequest) throws
            Exception {
        String deviceString = monitorRequest.getDeviceString();
        Map<String, List<String>> ipMap = Maps.newHashMap();
        getIpMap(deviceString, ipMap);
        AlertValueSearchRequest alertValueSearchRequest = new AlertValueSearchRequest();
        alertValueSearchRequest.setIpMap(ipMap);
        alertValueSearchRequest.setAlertLevel(monitorRequest.getAlertLevelList());
        alertValueSearchRequest.setItemIdList(monitorRequest.getItemIdList());
        int result = alertsServiceClient.getAlertValue(alertValueSearchRequest);

        CompMonitorResult compMonitorResult = new CompMonitorResult();
        compMonitorResult.setValue((double) result);
        return compMonitorResult;
    }

    private void getIpMap(String deviceString, Map<String, List<String>> ipMap) {
        JSONArray deviceArray = JSON.parseArray(deviceString);
        for (int i = 0; i < deviceArray.size(); i++) {
            JSONObject jsonObject = deviceArray.getJSONObject(i);
            String idcType = jsonObject.getString("idcType");
            if (ipMap.get(idcType) == null) {
                List<String> ipList = Lists.newArrayList();
                ipList.add(jsonObject.getString("ip"));
                ipMap.put(idcType, ipList);
            } else {
                List<String> ipList = ipMap.get(idcType);
                ipList.add(jsonObject.getString("ip"));
            }
        }
    }
}
