package com.aspire.ums.cmdb.v2.cache;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.instance.payload.FullInstance;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstanceCache
 * Author:   zhu.juwang
 * Date:     2019/5/21 15:02
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class InstanceCache {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private SchemaService schemaService;

    public void refreshCache() {
        List<Module> moduleList =  moduleService.getModuleTree(null, null);
        log.info("开始计算实例缓存...");
        if (moduleList != null && moduleList.size() > 0) {
            moduleList.stream().forEach((module) -> {
                refreshCache(module.getId());
            });
        }
        //默认添加一个moduleId = ""的节点数据
        CacheConst.CACHE_INSTANCE_MAP.put("", getPrimaryInstance());
        log.info("计算模型缓存结束.");
    }

    /**
     * 刷新指定模型ID的instance
     * @param moduleId
     */
    public void refreshCache(String moduleId) {
        log.info("开始计算实例 -> {}", moduleId);
        Module module = moduleService.getModuleDetail(moduleId);
        CacheConst.CACHE_INSTANCE_MAP.put(module.getId(), getFullInstance(module));
        if (module.getChildModules() != null && module.getChildModules().size() > 0) {
            module.getChildModules().stream().forEach((child) -> {
                refreshCache(child.getId());
            });
        }
    }

    private FullInstance getPrimaryInstance() {
        FullInstance fullInstance = new FullInstance();
        CmdbCode queryCode = new CmdbCode();
//        queryCode.setIsBuiltIn("是");
        List<CmdbCode> cmdbCodeList = codeService.listByEntity(queryCode);
        fullInstance.setCodeList(cmdbCodeList);
        Module module = new Module();
        module.setId("");
        fullInstance.setModule(module);
        List<ColumnInfo> instanceColumns = schemaService.getColumnListByTableName("cmdb_instance");
        fullInstance.setInstanceColumns(instanceColumns);
        fullInstance.setOtherColumns(new ArrayList<>());
        return fullInstance;
    }

    private FullInstance getFullInstance(Module module) {
        FullInstance fullInstance = new FullInstance();
        List<CmdbCode> cmdbCodeList = codeService.getCodeListByModuleId(module.getId());
        fullInstance.setCodeList(cmdbCodeList);
        fullInstance.setModule(module);
        fullInstance.setTableName("cmdb_instance_" + module.getCode());
        List<ColumnInfo> instanceColumns = schemaService.getColumnListByTableName("cmdb_instance");
        List<ColumnInfo> portalColumns = schemaService.getColumnListByTableName(fullInstance.getTableName());
        final List<ColumnInfo> otherColumns = new LinkedList<>();
        portalColumns.forEach((portal) -> {
            ColumnInfo temp = instanceColumns.stream().filter(instance -> portal.getName().equalsIgnoreCase(instance.getName()))
                    .findFirst()
                    .orElse(null);
            if (temp == null) {
                otherColumns.add(portal);
            }
        });
        fullInstance.setInstanceColumns(instanceColumns);
        fullInstance.setOtherColumns(otherColumns);
        return fullInstance;
    }

    public FullInstance getFullInstanceByModuleId(String moduleId) {
        FullInstance fullInstance = CacheConst.CACHE_INSTANCE_MAP.get(moduleId);
        if (fullInstance == null) {
            refreshCache(moduleId);
            fullInstance = CacheConst.CACHE_INSTANCE_MAP.get(moduleId);
        }
        return fullInstance;
    }
}
