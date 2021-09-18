package com.aspire.ums.cmdb.runner;

import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbTableApplicationRunner
 * Author:   zhu.juwang
 * Date:     2019/4/11 16:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * 项目启动监听方法, 在项目启动后, 执行cmdb数据库表操作
 */
//@Component
@Order(1)
@Slf4j
public class CmdbTableApplicationRunner implements ApplicationRunner {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private SchemaService schemaService;
    //CMDB数据库的实例名称
    @Value("${cmdb.schema.name}")
    private String cmdbSchema;

    @Override
    public void run(ApplicationArguments applicationArguments){
        //创建模型实例表
        List<Module> moduleList = moduleService.getModuleList();
        if (moduleList != null) {
            for (Module module : moduleList) {
                try {
                    List<Module> childModules = module.getChildModules();
                    handlerModule(module);
                    if (childModules != null && childModules.size() >0) {
                        for (Module childModule : childModules) {
                            log.info("Start handler module -> {}[{}]", module.getName(), module.getCode());
                            handlerModule(childModule);
                            log.info("End handler module -> {}[{}]", module.getName(), module.getCode());
                        }
                    }
                } catch (Exception e) {
                    log.error("Create module {} table error.", e.getMessage());
                }
            }
        }
    }

    /**
     * 模型表处理
     * @param module
     */
    private void handlerModule(Module module) {
        String tableName = module.getModuleCatalog().getCatalogCode();
        try {
            // 检查表是否存在
            if (!schemaService.hasTable(tableName)) {
                //如果表不存在, 则根据模型所属字段 创建表格
                schemaService.createModuleTable(tableName, module);
            } else {
                log.info("Table [{}].[{}] is exists, handler skip.", cmdbSchema, tableName);
            }
        } catch (Exception e) {
            log.error("Create table [{}].[{}] error.", cmdbSchema, tableName, e);
        }
    }
}
