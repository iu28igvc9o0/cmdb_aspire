package com.aspire.cmdb.agent.cmic.schedule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: IPInfoSyncSchedule
 * Author:   zhu.juwang
 * Date:     2020/6/1 15:24
 * Description: IP地址库的相关信息同步
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@EnableScheduling
@Slf4j
public class IPInfoSyncSchedule {

    @Autowired
    ICmdbConfigService configService;
    @Autowired
    ICmdbV3ModuleCatalogService catalogService;
    @Autowired
    ICmdbInstanceService instanceService;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void startCountSegmentIp() {
        CmdbConfig cmdbConfig = configService.getConfigByCode("need_count_ip_module");
        List<Map> needCounts = JSONArray.parseArray(cmdbConfig.getConfigValue(), Map.class);
        for (Map needCount : needCounts) {
            try {
                count(needCount.get("ip").toString(),needCount.get("ipSegment").toString(), needCount.get("segment").toString(),  needCount.get("segmentAddress").toString());
                log.info("统计模型[" + needCount.get("segment").toString() + "]各状态ip数据结束");
            } catch (Exception e) {
                throw new RuntimeException("统计模型[" + needCount.get("segment").toString() + "]各状态ip数据失败. error" + e.getMessage());
            }
        }
    }

    private void count(String ip, String ipSegment ,String segment,String segmentAddress) {
        CmdbConfig ipConfig = configService.getConfigByCode(ip);
        CmdbV3ModuleCatalog  ipCatalog= catalogService.getByModuleId(ipConfig.getConfigValue());
        CmdbConfig segmentConfig = configService.getConfigByCode(segment);
        CmdbV3ModuleCatalog segmentCatalog = catalogService.getByModuleId(segmentConfig.getConfigValue());
        // 获取模型下所有网段信息
        instanceService.countSegmentIp(segmentCatalog.getCatalogCode(),segmentAddress, ipCatalog.getCatalogCode(), ipSegment);
    }
//    @Scheduled(cron = "0 0/1 * * * ?")
//    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void startSyncBusiness() {
        CmdbConfig cmdbConfig = configService.getConfigByCode("need_sync_ip_business_module");
        List<Map> needCounts = JSONArray.parseArray(cmdbConfig.getConfigValue(), Map.class);
        for (Map needCount : needCounts) {
            try {
                String key = needCount.get("ipCode").toString();
                CmdbConfig ipConfig = configService.getConfigByCode(needCount.get("ip").toString());
                CmdbV3ModuleCatalog ipCatalog = catalogService.getByModuleId(ipConfig.getConfigValue());
                CmdbConfig segmentConfig = configService.getConfigByCode(needCount.get("segment").toString());
                CmdbV3ModuleCatalog segmentCatalog = catalogService.getByModuleId(segmentConfig.getConfigValue());
                instanceService.syncIpBussiness(key, segmentCatalog.getCatalogCode(),needCount.get("segmentAddress").toString(), ipCatalog.getCatalogCode(),needCount.get("ipSegment").toString());
                log.info("统计模型[" + needCount.get("segment").toString() + "]同步业务数据结束");
            } catch (Exception e) {
                throw new RuntimeException("统计模型[" + needCount.get("segment").toString() + "]同步业务数据失败. error", e);
            }
        }
    }
}
