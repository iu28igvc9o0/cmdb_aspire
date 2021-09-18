package com.aspire.cmdb.agent.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.client.ICmdbESClient;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyncCiToESByIdc
 * Author:   hangfang
 * Date:     2020/1/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@EnableScheduling
@Slf4j
public class SyncCiToESByIdc {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ConfigDictMapper dictMapper;
    @Autowired
    private ICmdbESClient esClient;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void syncCiToEs() {
        List<ConfigDict> idcTypes = dictMapper.getIdcTypeList();
        log.info("资源池列表：{}", idcTypes);
        if (idcTypes != null && idcTypes.size() > 0) {
            List<Map<String, Object>> result = new ArrayList<>();
            for (ConfigDict idcType : idcTypes) {
                Map<String, Object> ciList = new HashMap<>();
                CmdbInstance instance = new CmdbInstance();
                instance.setIdcType(idcType.getValue());
                List<CmdbInstance> instances = instanceService.listByEntity(instance);
                List<Map<String, String>> data = new ArrayList<>();
                if (instances != null && instances.size() > 0) {
                    for (CmdbInstance i : instances) {
                        Map<String, String> ciData = new HashMap<>();
                        ciData.put("id", i.getIp());
                        ciData.put("ip", i.getIp());
                        ciData.put("device_type", i.getDeviceType());
                        ciData.put("pod_name", i.getPodName());
                        data.add(ciData);
                    }
                }
                ciList.put("idc_type", idcType.getValue());
                ciList.put("data", data);
                result.add(ciList);
            }
            try {
                if ((Boolean) esClient.deleteIndex("cmdb_log").get("success")) {
                    esClient.insert(result, "cmdb_log", "ci_idcType");
                }
            } catch (Exception e) {
                log.error("es 数据存储出错", e);
            }
        }

    }
}
