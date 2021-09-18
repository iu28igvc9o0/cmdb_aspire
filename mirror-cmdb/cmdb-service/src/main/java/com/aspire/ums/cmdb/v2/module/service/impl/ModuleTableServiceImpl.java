package com.aspire.ums.cmdb.v2.module.service.impl;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.CmdbConst;
import com.aspire.ums.cmdb.v2.module.mapper.ModuleTableMapper;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v2.module.service.ModuleTabelService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description:
 *
 * @author: hangfang
 *
 * @Date: 2019-5-21
 */
@Service
@Transactional
public class ModuleTableServiceImpl implements ModuleTabelService {

    @Autowired
    private ModuleTableMapper moduleTableMapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbCodeService cmdbCodeService;
    @Autowired
    private IRedisService redisService;

    //CMDB数据库的实例名称
    @Value("${cmdb.schema.name}")
    private String schemaName;

    @Override
    public boolean hasTable(String tableName) {
        return moduleTableMapper.findTableByName(schemaName, tableName) > 0;
    }

    @Override
    public boolean hasColumn(String tableName, String columnName) {
        return moduleTableMapper.findTableColumnByColumnName(schemaName, tableName, columnName) > 0;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void createModuleTable(String tableName) {
        moduleTableMapper.createTable(tableName);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void addTableColumn(String tableName, List<CmdbCode> cmdbCodes) {
        List<String> columns = new ArrayList<>();
        for (CmdbCode code : cmdbCodes) {
            String columnLength;
            if (null == code.getCodeLength()) {
                columnLength = " VARCHAR(40)";
            } else {
                columnLength = String.format(" VARCHAR(%s)", code.getCodeLength());
            }
            String comment = " COMMENT '" + code.getFiledName() + "'";
            columns.add("`" + code.getFiledCode() + "`" + columnLength + comment);
        }
        if (columns.size() > 0) {
            moduleTableMapper.alterAddTable(tableName, columns);
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void deleteTableColumn(Module module, List<CmdbCode> cmdbCodes) {
        String tableName = module.getModuleCatalog().getCatalogCode();
        String tableHisName = module.getModuleCatalog().getCatalogCode() + CmdbConst.CMDB_MODULE_TABLE_SUFFIX;
        List<CmdbCode> addCodes = new ArrayList<>();
        boolean tableExist = hasTable(tableHisName);
        // 如果历史表不存在则先新建表再添加column
        if (!tableExist) {
            createModuleTable(tableHisName);
            if (cmdbCodes.size() > 0) {
                addTableColumn(tableHisName, cmdbCodes);
            }
        } else {
            // 如果存在的话需要先查询历史表中需要加入哪些字段
            List<String> hisColumns = moduleTableMapper.getColumnListByTableName(tableHisName, schemaName);
            if (hisColumns.size() > 0) {
                for(CmdbCode code : cmdbCodes) {
                    if (!hisColumns.contains(code.getFiledCode())) {
                        addCodes.add(code);
                    }
                }
            }
            if (addCodes.size() > 0) {
                addTableColumn(tableHisName, addCodes);
            }
        }
        if (cmdbCodes.size() > 0) {
            List<String> deleteColumns = new ArrayList<>();
            for (CmdbCode code : cmdbCodes) {
                deleteColumns.add(code.getFiledCode());
            }
            //  复制要被删除的列数据到历史表中
            moduleTableMapper.copyColumnData(tableName, tableHisName, deleteColumns);
            //  复制其他列数据到历史表中
            moduleTableMapper.copyOtherColumnData(tableName, tableHisName, deleteColumns);
            //  复制完数据在原表中删除列
            moduleTableMapper.alterDropTable(tableName, deleteColumns);
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void deleteTableByName(Module module) {
        String tableName  = module.getModuleCatalog().getCatalogCode();
        String tableHisName = tableName + CmdbConst.CMDB_MODULE_TABLE_SUFFIX;
        boolean tableExist = hasTable(tableHisName);
        if (!tableExist) {
            moduleTableMapper.createAndCopyTable(tableName, tableHisName);
        } else {
//            module.setCode(tableName.replace(CmdbConst.getModuleTable(module.getModuleCatalog().getCatalogCode()), ""));
            List<CmdbCode> codes = cmdbCodeService.getSelfCodeListByModuleId(module.getId());
            deleteTableColumn(module, codes);
        }
        moduleTableMapper.deleteTableByName(tableName);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    public void deleteEmptyTable(String tableName) {
        moduleTableMapper.deleteTableByName(tableName);
    }


    @Override
    public List<String> getColumnListByTableName(String tableName) {
        return moduleTableMapper.getColumnListByTableName(tableName, schemaName);
    }

    @Override
    public List<Map<String, Object>> getDataBySql(String sql) {
        return moduleTableMapper.getDataBySql(sql);
    }

    @Override
    public Integer getTableDataCount(String tableName) {
        return moduleTableMapper.getTableDataCount(tableName);
    }

    //    @Override
    public String getModuleSql(String moduleId) {
//        Object object = redisService.get(Constants.REDIS_MODULE_TABLE_PREFIX + moduleId);
//        if (object == null) {
//            // 组装String语句
//            Module module = moduleService.getModuleDetail(moduleId);
//            if (module == null) {
//                throw new RuntimeException("未获取到模型配置SQL, 请联系管理员进行排查.");
//            }
//            String primaryAlias = module.getCode();
//            StringBuffer primaryBuf = new StringBuffer();
//            primaryBuf.append("select ");
//            primaryBuf.append(primaryAlias).append(".").append("id, ");
//            primaryBuf.append(primaryAlias).append(".").append("module_id, ");
//            Map<String, Map<String, String>> tableMap = new HashMap<>();
//
//            List<CmdbV3ModuleCodeSetting> codeSettingList = module.getCodeSettingList();
//            if (codeSettingList.size() > 0) {
//                for (CmdbV3ModuleCodeSetting codeSetting : codeSettingList) {
//                    CmdbCode queryCode = new CmdbCode();
//                    queryCode.setCodeId(codeSetting.getCodeId());
//                    CmdbCode cmdbCode = cmdbCodeService.get(queryCode);
//                    if (cmdbCode == null) {
//                        continue;
//                    }
//                    // 模型自己的码表信息
//                    if (moduleId.equals(codeSetting.getOwnerModuleId())) {
//                        primaryBuf.append(primaryAlias).append(".").append(cmdbCode.getFiledCode()).append(", ");
//                    } else {
//                        // 引入的模型码表信息
//                        if (!tableMap.containsKey(codeSetting.getOwnerModuleId())) {
//                            Module codeModule = moduleService.getModuleDetail(codeSetting.getOwnerModuleId());
//                            Map<String, String> refMap = new HashMap<>();
//                            refMap.put("alias", codeModule.getCode());
//                            refMap.put("table", CmdbConst.getModuleTable(codeModule.getModuleCatalog().getCatalogCode()));
//                            tableMap.put(codeSetting.getOwnerModuleId(), refMap);
//                        }
//                        String alias = tableMap.get(codeSetting.getOwnerModuleId()).get("alias");
//                        primaryBuf.append(alias).append(".").append(cmdbCode.getFiledCode()).append(", ");
//                    }
//                }
//            }
//            primaryBuf = new StringBuffer(primaryBuf.substring(0, primaryBuf.length() - 1));
//            primaryBuf.append(" from ").append(CmdbConst.getModuleTable(module.getModuleCatalog().getCatalogCode())).append(" ").append(primaryAlias);
//            for (String key : tableMap.keySet()) {
//                if (!key.equals(moduleId)) {
//                    String alias = tableMap.get(key).get("alias");
//                    String table = tableMap.get(key).get("table");
//                    primaryBuf.append("left join ").append(table).append(" ").append(alias).append(" on ").append(alias).append(".id = ").append(primaryAlias).append(".id ");
//                }
//            }
//            primaryBuf.append("where ").append(primaryAlias).append(".is_delete='0'");
//            redisService.set(Constants.REDIS_MODULE_TABLE_PREFIX + moduleId, primaryBuf.toString());
//        }
//        return (new ObjectMapper()).convertValue(object, new TypeReference<String>(){});
        return "";
    }
}
