package com.aspire.mirror.zabbixintegrate.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.zabbixintegrate.daoCmdb.KpiKeyToMetricNameMapper;
import com.aspire.mirror.zabbixintegrate.daoCmdb.po.KpiKeyToMetricName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.zabbixintegrate.bean.SuyanMonitorConfig;
import com.aspire.mirror.zabbixintegrate.daoCmdb.CmdbInstanceDao;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * cmdb帮助类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    CmdbHelper.java
 * 类描述:    cmdb帮助类
 * 创建人:    JinSu
 * 创建时间:  2018/9/25 10:25
 * 版本:      v1.0
 */
@Component
@Slf4j
public class SuyanMonitorHelper {
    @Autowired
    private CmdbInstanceDao cmdbInstanceDao;
    @Autowired
    private KpiKeyToMetricNameMapper kpiKeyToMetricNameMapper;

    private Map<String, SuyanMonitorConfig> map;

    private final ConcurrentHashMap<String, List<KpiKeyToMetricName>> keyCache = new ConcurrentHashMap<>();


    @PostConstruct
    private void formMap() {
        try {
            if (null != map) {
                map.clear();
            } else {
                map = Maps.newHashMap();
            }
            List<SuyanMonitorConfig> data = cmdbInstanceDao.selectSuyanMonitor();
            for (SuyanMonitorConfig s : data) {
                String key = s.getSy_key_();
                String[] ss = key.split(",");
                for (String val : ss) {
                    if (!map.containsKey(val)) {
                        map.put(val, s);
                    }
                }

            }
        } catch (Exception e) {
            log.error("", e);
        }


    }

    private SuyanMonitorConfig getConfigBySuyanKey(String suyanKey) {
        SuyanMonitorConfig data = cmdbInstanceDao.selectSuyanMonitorBySuyanKey(suyanKey);
        if (null != data) {
            map.put(suyanKey, data);
        }
        return data;
    }

    public Map<String, SuyanMonitorConfig> getSuyanMonitorConfig() {
        if (null == map) {
            this.formMap();
        }
        return this.map;
    }


    public SuyanMonitorConfig getConfigData(String suyanKey) {
        if (map.containsKey(suyanKey)) {
            return map.get(suyanKey);
        } else {
            return getConfigBySuyanKey(suyanKey);
        }
    }

    public Map<String, Object> getmetricNameByZabbixKey(String key, JSONObject obj) {
        String deviceType = obj.getString("deviceType");

        Map<String, Object> returnMap = Maps.newHashMap();
        if (StringUtils.isEmpty(key)) {
            return returnMap;
        }

        if (keyCache.isEmpty()) {
            List<KpiKeyToMetricName> toMetricNameList = kpiKeyToMetricNameMapper.selectAll();

            if (CollectionUtils.isEmpty(toMetricNameList)) {
                keyCache.put("test", new ArrayList<>(1));
                return returnMap;
            }
            for (KpiKeyToMetricName s : toMetricNameList) {
                String zabbixKey = s.getZabbixKey();
                List<KpiKeyToMetricName> keyList = keyCache.get(zabbixKey);
                if (keyList == null) {
                    keyList = new ArrayList<>();
                }
                keyList.add(s);
                keyCache.put(zabbixKey, keyList);
            }
        }
        KpiKeyToMetricName kpiKeyToMetricName = null;
        int index = key.indexOf("[");
        if (keyCache.containsKey(key)) {
            kpiKeyToMetricName = getMetricName(key, deviceType);
		} else if (index > 0 && key.lastIndexOf("]") > index) {
            String[] split = key.split("\\[");
            key = split[0];
            String metricNodeName = split[1].split("]")[0];
            kpiKeyToMetricName = getMetricName(key, deviceType);
			if (kpiKeyToMetricName != null) {
				returnMap.put("metricNodeName", metricNodeName);
			}
        }

        if (kpiKeyToMetricName != null) {
            returnMap.put("metricName", kpiKeyToMetricName.getMetricName());
            returnMap.put("resourceId", obj.get("resourceId"));
            returnMap.put("time", obj.get("clock"));
            returnMap.put("value", obj.get("value"));
            String operation = kpiKeyToMetricName.getOperation();
            Long multiple = kpiKeyToMetricName.getMultiple();
            if (!StringUtils.isEmpty(operation) && multiple != null) {
                Double value = obj.getDouble("value");
                if ("/".equalsIgnoreCase(operation) && value != null) {
                    returnMap.put("value", value / multiple);
                } else if ("*".equalsIgnoreCase(operation) && value != null) {
                    returnMap.put("value", value * multiple);
                }
            }
        }
        return returnMap;

    }

	/**
	 *
	 * @param key
	 * @param deviceType
	 * @return
	 */
    private KpiKeyToMetricName getMetricName(String key, String deviceType) {
        List<KpiKeyToMetricName> nameList = keyCache.get(key);
        if (CollectionUtils.isEmpty(nameList)) {
            return null;
        }
        if (nameList.size() == 1) {
            return nameList.get(0);
        }

        KpiKeyToMetricName nullMetric = null;
        KpiKeyToMetricName firstMetric = null;
        for (int i = 0; i < nameList.size(); i++) {
            KpiKeyToMetricName kpiKeyToMetricName = nameList.get(i);
            if (i == 0) {
                firstMetric = kpiKeyToMetricName;
            }
            if (StringUtils.isEmpty(kpiKeyToMetricName.getDeviceType()) && nullMetric == null) {
                nullMetric = kpiKeyToMetricName;
            }
            if (!StringUtils.isEmpty(deviceType) && deviceType.equalsIgnoreCase(kpiKeyToMetricName.getDeviceType())) {
                return kpiKeyToMetricName;
            }
        }
        return nullMetric == null ? firstMetric : nullMetric;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void init() {
        keyCache.clear();
    }
}
