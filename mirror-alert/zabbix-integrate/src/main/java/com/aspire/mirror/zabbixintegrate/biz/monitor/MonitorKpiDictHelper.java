package com.aspire.mirror.zabbixintegrate.biz.monitor;

import com.aspire.mirror.zabbixintegrate.daoAlert.KpiConfigDictMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigDict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.biz.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-04-20 18:49
 * @Description: ${Description}
 */
@Slf4j
@Component
public class MonitorKpiDictHelper {

    @Autowired
    private KpiConfigDictMapper kpiConfigDictMapper;

    private final ConcurrentHashMap<String, Map<String, String>> dictCache = new ConcurrentHashMap<>();

    /**
     * 根据kpi字典类型和名称获取转换值
     * @param type
     * @param name
     * @return
     */
    public String getValueByName (String type, String name) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(name)) {
            return name;
        }

        Map<String, String> map = dictCache.get(type);
        if (map == null) {
            List<KpiConfigDict> kpiConfigDictList = kpiConfigDictMapper.selectByType(type);
            if (kpiConfigDictList.isEmpty()) {
                map = new HashMap<>();
                map.put("1", "1");
            } else {
                map = new HashMap<>();
                for (KpiConfigDict kpiConfigDict: kpiConfigDictList) {
                    map.put(kpiConfigDict.getDictName(), kpiConfigDict.getDictValue());
                }
            }
            dictCache.put(type, map);
        }

        return getValue(map, name);
    }

    /**
     *
     * @param map
     * @param name
     * @return
     */
    private String getValue(Map<String, String> map, String name) {
        String s = map.get(name);
        if (!StringUtils.isEmpty(s)) {
            return s;
        }
        for (Map.Entry<String, String> entry: map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                if (name.indexOf(key) > -1 || name.matches(key)) {
                    return value;
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    @Scheduled(cron = "0 0 */1 * * ?")
    public void flushDictCache () {
        dictCache.clear();
    }
}
