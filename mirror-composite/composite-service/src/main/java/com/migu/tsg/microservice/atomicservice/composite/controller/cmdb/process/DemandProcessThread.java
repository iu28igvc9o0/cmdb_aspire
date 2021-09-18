package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.DemandClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DemandProcessThread
 * Author:   zhu.juwang
 * Date:     2019/8/9 10:36
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
public class DemandProcessThread extends ProcessThread {

    private DemandClient demandClient;

    private Map<String, String> demandHeaderMap = new LinkedHashMap<>();

    @Override
    protected void setSpecialData(Map<String, Object> importData) {}

    @Override
    protected void init() {
        List<Map<String, Object>> headerList = demandClient.getTableHeader();
        if (headerList != null && headerList.size() > 0) {
            for (Map<String, Object> headerMap : headerList) {
                if (headerMap.get("data") != null) {
                    List<Map<String, String>> resourceList = (List<Map<String, String>>) headerMap.get("data");
                    if (resourceList != null) {
                        for (Map<String, String> source : resourceList) {
                            demandHeaderMap.put(source.get("label"), source.get("key"));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void valid() {}

    /**
     * 设置数据Map的值
     * @param keyValueMap
     * @param key
     * @param value
     */
    protected void setDataMap(Map<String, String> keyValueMap, String key, String value) {
        String newKey = key;
        if (key.contains("[必填]")) {
            newKey = key.replace("[必填]", "");
        }
        if (demandHeaderMap.containsKey(newKey)) {
            keyValueMap.put(key, value);
        }
    }
}
