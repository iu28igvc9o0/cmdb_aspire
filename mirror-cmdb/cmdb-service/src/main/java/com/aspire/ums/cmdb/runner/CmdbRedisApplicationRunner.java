package com.aspire.ums.cmdb.runner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3CondicationSettingService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.aspire.ums.cmdb.view.payload.CmdbView;
import com.aspire.ums.cmdb.view.payload.CmdbViewQuery;
import com.aspire.ums.cmdb.view.service.ICmdbViewService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbRedisApplicationRunner
 * Author:   zhu.juwang
 * Date:     2019/12/17 19:48
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@ConditionalOnProperty(prefix = "cmdb", name = "cache", havingValue = "redis")
@Component
@Order(2)
@Slf4j
public class CmdbRedisApplicationRunner implements ApplicationRunner {

    @Autowired
    private ICmdbCodeService codeService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private ICmdbV3ModuleCatalogService moduleCatalogService;

    @Autowired
    private ConfigDictService configDictService;

    @Autowired
    private ICmdbViewService cmdbViewService;

    @Autowired
    private IRedisService redisService;

    @Autowired
    private ICmdbV3CondicationSettingService condicationSettingService;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        new Thread(new Runnable() {

            @Override
            public void run() {
                writeCode();
                writeModule();
                writeView();
                writeModuleAllTree();
                writeModuleCatalog();
            }
        }).start();

        new Thread(new Runnable() {

            @Override
            public void run() {
                writeCodeGroup();
                writeDict();
                writeCondicationSetting();
            }
        }).start();
    }

    /**
     * 加载模型分组 将模型分组信息缓存到redis中
     */
    private void writeView() {
        // 先清空原型的redis缓存
        List<CmdbView> viewList = cmdbViewService.listByQuery(new CmdbViewQuery());
        // List<CmdbV3ModuleCatalog> catalogs = moduleCatalogService.getFirstLevel();
        log.info("开始将树信息信息缓存到redis...");
        for (CmdbView c : viewList) {
            try {
                redisService.syncRefresh(Constants.REDIS_TYPE_VIEW, c.getViewCode());
                log.info("自定义树[{}]缓存成功.", c.getViewName());
            } catch (Exception e) {
                log.info("自定义树[{}]缓存失败.", c.getViewName(), e);
            }
        }
        log.info("模型分组信息缓存到redis结束.");
    }

    /**
     * 加载模型分组 将模型分组信息缓存到redis中
     */
    private void writeModuleCatalog() {
        // 先清空原型的redis缓存
        List<CmdbV3ModuleCatalog> catalogs = moduleCatalogService.getFirstLevel();
        log.info("开始将模型分组信息缓存到redis...");
        for (CmdbV3ModuleCatalog c : catalogs) {
            try {
                redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, c.getId());
                log.info("分组[{}]缓存成功.", c.getCatalogName());
            } catch (Exception e) {
                log.info("分组[{}]缓存失败.", c.getCatalogName(), e);
            }
        }
        log.info("模型分组信息缓存到redis结束.");
    }

    /**
     * 加载模型 将模型信息缓存到redis中
     */
    private void writeModule() {
        log.info("开始将模型信息缓存到redis...");
        List<Module> moduleList = moduleService.getModuleList();
        if (moduleList != null && moduleList.size() > 0) {
            for (Module module : moduleList) {
                try {
                    redisService.syncRefresh(Constants.REDIS_TYPE_MODULE, module.getId());
                    log.info("模型[{}]缓存成功.", module.getCode());
                } catch (Exception e) {
                    log.error("模型[{}]缓存失败.", module.getCode(), e);
                }
            }
        }
        log.info("模型信息缓存到redis结束.");
    }

    /**
     * 加载模型 将模型信息缓存到redis中
     */
    private void writeModuleAllTree() {
        log.info("开始将模型信息缓存到redis...");
        try {
            List<Module> moduleTree = moduleService.getModuleTree(null, null);
            redisService.set(String.format(Constants.REDIS_MODULE_CATALOG_TREE, null), moduleTree);
            log.info("模型总树缓存成功.");
        } catch (Exception e) {
            log.error("模型总树缓存失败 {}.", e);
        }
        log.info("模型信息缓存到redis结束.");
    }

    /**
     * 加载分组码表 将分组码表信息缓存到redis中
     */
    private void writeCodeGroup() {
        log.info("开始将码表分组信息缓存到redis...");
        try {
            // 先加载所有的码表分组
            redisService.syncRefresh(Constants.REDIS_TYPE_CODE_GROUP, null);
            log.info("所有码表分组被写入redis.");
        } catch (Exception e) {
            log.error("所有码表分组写入redis失败.");
        }
        // 再加载单个分组
        List<CmdbV3ModuleCatalog> catalogs = moduleCatalogService.getFirstLevel();
        if (catalogs != null) {
            for (CmdbV3ModuleCatalog catalog : catalogs) {
                try {
                    redisService.syncRefresh(Constants.REDIS_TYPE_CODE_GROUP, catalog.getId());
                    log.info("码表分组编号 {} 被写入redis.", catalog.getId());
                } catch (Exception e) {
                    log.error("码表分组编号 {} 写入redis失败.", catalog.getId());
                }
            }
        }
        log.info("码表分组信息缓存到redis结束.");
    }

    /**
     * 加载码表 将码表信息缓存到redis中
     */
    private void writeCode() {
        log.info("开始将码表信息缓存到redis...");
        List<CmdbCode> codeList = codeService.listByEntity(null);
        if (codeList != null && codeList.size() > 0) {
            codeList.forEach((code) -> {
                try {
                    redisService.syncRefresh(Constants.REDIS_TYPE_CODE, code.getCodeId());
                    log.info("码表编号 {} 被写入redis.", code.getCodeId());
                } catch (Exception e) {
                    log.error("码表编号 {} 写入redis失败.", code.getCodeId());
                }
            });
        }
        log.info("码表信息缓存到redis结束.");
    }

    /**
     * 将数据字典缓存到redis中
     */
    private void writeDict() {
        log.info("开始清空原先的redis缓存...");
        redisService.syncRemovePattern(Constants.REDIS_DICT_PREFIX);
        log.info("开始将数据字典信息缓存到redis...");
        List<Dict> dictList = configDictService.getDictAll(null);
        if (dictList != null) {
            if (this.redisService.set(Constants.REDIS_DICT_PREFIX, dictList)) {
                log.info("数据字典写入redis缓存成功.");
            } else {
                log.error("数据字典写入redis缓存失败.");
            }
        }
        log.info("开始将数据字典信息缓存到redis结束.");
    }

    /**
     * 加载查询条件配置到redis
     */
    private void writeCondicationSetting() {
        log.info("开始将查询条件配置信息缓存到redis...");
        List<CmdbV3CondicationSetting> condicationList = condicationSettingService.list();
        if (condicationList != null && condicationList.size() > 0) {
            condicationList.forEach((setting) -> {
                try {
                    redisService.syncRefresh(Constants.REDIS_TYPE_CONDITION_SETTING, setting);
                    log.info("查询条件配置 {} 被写入redis.", setting.getId());
                } catch (Exception e) {
                    log.error("查询条件配置 {} 写入redis失败.", setting.getId());
                }
            });
        }
        log.info("开始将查询条件配置信息缓存到redis结束.");
    }
}
