package com.aspire.cmdb.agent.v3.schedule;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: SyncESDataSchedule
 * Author:   zhu.juwang
 * Date:     2020/2/18 13:49
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@EnableScheduling
@Slf4j
public class SyncESDataSchedule {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbESService cmdbESService;
    @Autowired
    private ICmdbV3ModuleCatalogService moduleCatalogService;

//    @Scheduled(cron = "0 1 * * * ?")
    public void startSync() {
        List<CmdbV3ModuleCatalog> catalogs = moduleCatalogService.getFirstLevel();
        log.info("开始将模型数据同步到ES库...");
        for (CmdbV3ModuleCatalog c : catalogs) {
            List<Module> moduleList = moduleService.getModuleList();
            if (moduleList != null && moduleList.size() > 0) {
                for (Module module : moduleList) {
                    Map<String, String> result = cmdbESService.writeModuleData(module.getId());
                    if (result.containsKey("flag") && ("error").equalsIgnoreCase(result.get("flag"))) {
                        log.error("模型[{}]数据入库失败. 原因: {}", module.getCode(), result.get("msg"));
                    }
                    if (module.getChildModules() != null && module.getChildModules().size() > 0) {
                        for (Module child : module.getChildModules()) {
                            Map<String, String> childResult = cmdbESService.writeModuleData(child.getId());
                            if (childResult.containsKey("flag") && ("error").equalsIgnoreCase(childResult.get("flag"))) {
                                log.error("模型[{}]数据入库失败. 原因: {}", module.getCode(), childResult.get("msg"));
                            }
                        }
                    }
                }
            }
        }
        log.info("模型数据同步到ES库结束.");
    }
}
