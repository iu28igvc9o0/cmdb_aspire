package com.aspire.mirror.alert.server.biz.kgEnv.impl;

import com.aspire.mirror.alert.server.biz.kgEnv.KGMonitorIndexBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbInstanceClient;
import com.aspire.mirror.alert.server.dao.alert.AlertsV2Dao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KGMonitorIndexBizImpl implements KGMonitorIndexBiz {

    @Autowired
    private CmdbInstanceClient cmdbInstanceClient;
    @Autowired
    private AlertsV2Dao alertsV2Dao;

    @Override
    public List<Map<String, Object>> getAlertView(Map<String, Object> param) {
        String deviceClass = "服务器,网络设备,存储设备,安全设备";
        // 获取设备数量
        List<Map<String, Object>> maps = cmdbInstanceClient.deviceCountByDeviceClass(deviceClass);
        Map<String, Object> deviceCount = listToMap(maps);
        // 获取告警数据
        param.put("deviceClass",deviceClass);
        List<Map<String, Object>> alert = alertsV2Dao.deviceCountByDeviceClass(param);
        Map<String, Object> alertMap = Maps.newHashMap();
        for (Map<String, Object> map : alert) {
            alertMap.put(String.valueOf(map.get("device_class")),map);
        }
        List<Map<String, Object>> response = Lists.newArrayList();
        Arrays.asList(deviceClass.split(",")).forEach(dc -> {
            Map<String, Object> obj = (Map<String, Object>)alertMap.get(dc);
            if (null == obj) {
                obj = Maps.newHashMap();
                obj.put("device_class",dc);
                obj.put("s_count",0);
                obj.put("h_count",0);
                obj.put("m_count",0);
                obj.put("l_count",0);
            }
            obj.put("device_count", null != deviceCount && null != deviceCount.get(dc) ? deviceCount.get(dc) : 0);
            response.add(obj);
        });

        return response;
    }

    private Map<String, Object> listToMap(List<Map<String, Object>> param) {
        Map<String, Object> res = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(param)) {
            param.forEach(item -> {
                res.put(String.valueOf(item.get("device_class")),item.get("d_count"));
            });
        }
        return res;
    }
}
