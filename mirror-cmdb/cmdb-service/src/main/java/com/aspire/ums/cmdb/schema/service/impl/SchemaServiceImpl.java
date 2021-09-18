package com.aspire.ums.cmdb.schema.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.mapper.SchemaMapper;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司 FileName: SchemaService Author: zhu.juwang Date: 2019/4/11 16:44 Description: ${DESCRIPTION}
 * History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述 数据库操作Service
 */
@Service
@Slf4j
public class SchemaServiceImpl implements SchemaService {

    @Autowired
    private SchemaMapper schemaMapper;

    @Autowired
    private ICmdbCodeService codeService;

    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;

    @Autowired
    private JDBCHelper jdbcHelper;

    @Value("${cmdb.schema.name}")
    private String schemaName;

    // CMDB数据库的实例是否需要导入初始数据
    @Value("${cmdb.schema.importData}")
    private Boolean importData;

    @Override
    public Integer findTableByName(String tableName) {
        return schemaMapper.findTableByName(schemaName, tableName);
    }

    @Override
    public Integer findTableColumnByColumnName(String tableName, String columnName) {
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage();
        cmdbSqlManage.setNeedAuth(Constants.UN_NEED_AUTH);
        cmdbSqlManage.setChartSql(
                "select * from information_schema.`columns`\n" + "        where lower(`table_schema`)=lower(#{schemaName})\n"
                        + "        and lower(`table_name`)=lower(#{tableName})\n"
                        + "        and lower(`column_name`)=LOWER(#{columnName})");
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("schemaName", schemaName);
        queryMap.put("tableName", tableName);
        queryMap.put("columnName", columnName);
        return jdbcHelper.getInt(cmdbSqlManage, null, queryMap);
    }

    @Override
    public boolean hasTable(String tableName) {
        CmdbSqlManage cmdbSqlManage = new CmdbSqlManage();
        cmdbSqlManage.setNeedAuth(Constants.UN_NEED_AUTH);
        cmdbSqlManage.setChartSql("select * from information_schema.TABLES where lower(table_schema)=lower(#{schemaName}) "
                + " and lower(table_name)=lower(#{tableName})");
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("schemaName", schemaName);
        queryMap.put("tableName", tableName);
        return jdbcHelper.getInt(cmdbSqlManage, null, queryMap) > 0;
    }

    @Override
    public boolean hasColumn(String tableName, String columnName) {
        return this.findTableColumnByColumnName(tableName, columnName) > 0;
    }

    // @Override
    @Override
    // @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void createModuleTable(String tableName, Module module) {
        try {
            /**
             * 先获取模型下有哪些码表字段, 其中内置的字段 都创建在cmdb_instance主表中 这里只将非内置字段 创建到表中
             */
            CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
            codeSetting.setModuleId(module.getId());
            codeSetting.setOwnerModuleId(module.getId());
            // 拿到自己的码表信息
            List<CmdbV3ModuleCodeSetting> settingList = codeSettingService.listByEntity(codeSetting);
            StringBuilder columnBuffer = new StringBuilder();
            if (settingList != null && settingList.size() > 0) {
                for (CmdbV3ModuleCodeSetting setting : settingList) {
                    CmdbCode queryCode = new CmdbCode();
                    queryCode.setCodeId(setting.getCodeId());
                    CmdbCode cmdbCode = codeService.get(queryCode);
                    if (cmdbCode == null) {
                        continue;
                    }
                    if (cmdbCode.getFiledCode().toLowerCase(Locale.ENGLISH).equals("id")) {
                        continue;
                    }
                    columnBuffer.append("`").append(cmdbCode.getFiledCode()).append("`").append(" VARCHAR(")
                            .append(cmdbCode.getCodeLength() == null ? 200 : cmdbCode.getCodeLength()).append(") ")
                            .append(" NULL COMMENT '").append(cmdbCode.getFiledName()).append("',\n");
                }
            }
            StringBuilder tableBuffer = new StringBuilder();
            tableBuffer.append("CREATE TABLE `").append(schemaName).append("`").append(".`").append(tableName).append("` (")
                    .append("\n");
            tableBuffer.append("`id` VARCHAR(40) NOT NULL COMMENT ").append("'资产标识'").append(",").append("\n");
            tableBuffer.append("`is_delete` int(11) NULL COMMENT ").append("'删除标识'").append(",").append("\n");
            tableBuffer.append(columnBuffer);
            tableBuffer.append("PRIMARY KEY (`id`), ").append("\n");
            tableBuffer.append("INDEX `IDX_").append(tableName).append("_id").append("` (`id`) USING BTREE").append("\n");
            tableBuffer.append(") ENGINE=InnoDB ").append("\n");
            tableBuffer.append(" DEFAULT CHARACTER SET = utf8 ").append("\n");
            tableBuffer.append(" COMMENT='").append(module.getName()).append("资产表' ").append("\n");
            log.info("Execute create table [{}].[{}] SQL -> {}", schemaName, tableName, tableBuffer.toString());
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage();
            cmdbSqlManage.setNeedAuth(Constants.UN_NEED_AUTH);
            cmdbSqlManage.setChartSql(tableBuffer.toString().replace("\n", " "));
            jdbcHelper.executeUpdate(cmdbSqlManage, null);
            // 第一次初始化时, 导入新表数据
            // if(importData) {
            // this.importData(tableName, module, codeList);
            // }
        } catch (Exception e) {
            log.error("Create table [{}].[{}] error.", schemaName, tableName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTableColumn(String tableName, String alterTableSql) {
        try {
            schemaMapper.alterTable(alterTableSql);
        } catch (Exception e) {
            log.error("Import table [{}].[{}] data error.", schemaName, tableName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    // @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void deleteTableColumn(String moduleCode, String columnName) {
        String tableName = null; // todo需要改造 CmdbConst.CMDB_MODULE_TABLE_PREFIX + moduleCode;
        try {
            // 判断表中是否已经存在属性
            Integer columnCount = schemaMapper.findTableColumnByColumnName(schemaName, tableName, columnName);
            if (columnCount < 1) {
                log.warn("Table [{}].[{}] don't has column [{}], skip delete table column.", schemaName, tableName, columnName);
                return;
            }
            StringBuilder alterBuilder = new StringBuilder();
            alterBuilder.append(" alter table `").append(schemaName).append("`.").append("`").append(tableName).append("` ");
            alterBuilder.append(" drop column ").append(columnName).append(" ");
            log.info("Execute alter table [{}].[{}] drop column SQL -> {}", schemaName, tableName, alterBuilder.toString());
            schemaMapper.alterTable(alterBuilder.toString());
        } catch (Exception e) {
            log.error("Alter table [{}].[{}] drop column [{}] error.", schemaName, tableName, columnName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importData(List<Module> moduleList) {
        for (Module module : moduleList) {
            // 处理服务器模型数据
            if (module.getCode().startsWith("server")) {
                List<CmdbCode> codeList = codeService.getCodeListByModuleId(module.getId());
                if (codeList != null && codeList.size() > 0) {
                    // 同步每一个CI项
                    importCode(module, codeList);
                } else {

                }
            }
            if (module.getCode().startsWith("network")) {

            }
            if (module.getCode().startsWith("network")) {

            }
            if (module.getCode().startsWith("network")) {

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertCi(String tableName, Map<String, Object> instanceData) {
        schemaMapper.insertCi(tableName, instanceData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCi(String tableName, String instanceId, Map<String, Object> instanceData) {
        schemaMapper.updateCi(tableName, instanceId, instanceData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCi(String tableName, String instanceId, Map<String, Object> instanceData) {
        schemaMapper.deleteCi(tableName, instanceId, instanceData);
    }

    private void importCode(Module module, List<CmdbCode> codeList) {
        // String
    }

    /**
     * 为新建的模型导入数据.
     * 
     * @param module
     *            模型实例
     * @param codeList
     *            列名集合
     */
    private void importData(String tableName, Module module, List<CmdbCode> codeList) {
        try {
            StringBuilder tableColumnBuilder = new StringBuilder();
            tableColumnBuilder.append("`id`").append("\n");
            for (CmdbCode cmdbCode : codeList) {
                // todo 需要改造
                // if (cmdbCode.getIsBuiltIn().equals("是")) {
                // continue;
                // }
                if (StringUtils.isNotEmpty(tableColumnBuilder.toString())) {
                    tableColumnBuilder.append(",").append("\n");
                }
                tableColumnBuilder.append("`").append(cmdbCode.getFiledCode()).append("`");
            }
            StringBuilder importBuilder = new StringBuilder();
            importBuilder.append("insert into `").append(schemaName).append("`").append(".`").append(tableName).append("` (")
                    .append("\n");
            importBuilder.append(tableColumnBuilder.toString()).append(") ").append("\n");
            importBuilder.append("select ").append("\n");
            importBuilder.append("cifv.instanceId ");
            StringBuilder selectColumnBuilder = new StringBuilder();
            for (CmdbCode cmdbCode : codeList) {
                // todo 需要改造
                // if (cmdbCode.getIsBuiltIn().equals("是")) {
                // continue;
                // }
                if (StringUtils.isNotEmpty(selectColumnBuilder.toString())) {
                    selectColumnBuilder.append(",").append("\n");
                }
                selectColumnBuilder.append("max(case cifv.formCode when '").append(cmdbCode.getFiledCode()).append("'")
                        .append(" then cifv.formValue else null end) ").append("`").append(cmdbCode.getFiledCode()).append("`");
            }
            if (StringUtils.isNotEmpty(selectColumnBuilder.toString())) {
                importBuilder.append(",").append("\n");
                importBuilder.append(selectColumnBuilder.toString());
            }
            importBuilder.append(" from (select * from `").append(schemaName).append("`.").append("`cmdb_instance_form_value` cf ")
                    .append("\n");
            importBuilder.append("    where exists (select 1 from `").append(schemaName).append("`.")
                    .append("`cmdb_instance` c where cf.instanceId = c.id and c.module_id='").append(module.getId()).append("')")
                    .append("\n");
            importBuilder.append("    and not exists (select 1 from `").append(schemaName).append("`.").append("`")
                    .append(tableName).append("` tp where tp.instance_id = cf.instanceId)").append("\n");
            importBuilder.append(" ) cifv group by cifv.instanceId ");
            log.info("Execute import table [{}].[{}] SQL -> {}", schemaName, tableName, importBuilder.toString());
            schemaMapper.batchInsert(importBuilder.toString().replace("\n", " "));
        } catch (Exception e) {
            log.error("Import table [{}].[{}] data error.", schemaName, tableName, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据表名删除表
     * 
     * @param moduleTableName
     */
    @Override
    public void deleteTableByName(String moduleTableName) {
        try {
            if (hasTable(moduleTableName)) {
                schemaMapper.deleteTableByName(moduleTableName);
            }
        } catch (Exception e) {
            log.error("DELETE table [{}].[{}] error.", moduleTableName, e);
        }
    }

    /**
     * 查询所有表名
     * 
     * @return
     */
    @Override
    public List<TableInfo> getTableList() {
        return schemaMapper.getTableList(schemaName);
    }

    /**
     * 根据表名获取该表的字段属性详情
     * 
     * @param tableName
     * @return
     */
    @Override
    public List<ColumnInfo> getColumnListByTableName(String tableName) {
        List<ColumnInfo> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(tableName)) {
            list = schemaMapper.getColumnListByTableName(tableName, schemaName);
        }
        return list;
    }

    @Override
    public List<Map<String, String>> checkSql(Map<String, String> params) {
        String sql = params.getOrDefault("sql", "");
        sql = sql.replaceAll("\\?", "1");
        return schemaMapper.checkSql(sql);
    }

    @Override
    public List<Map<String, Object>> executeSql(String sql) {
        return schemaMapper.executeSql(sql);
    }

    @Override
    public List<LinkedHashMap<String, Object>> executeSqlBySort(String sql) {
        return schemaMapper.executeSqlBySort(sql);
    }

    @Override
    public Integer executeCount(String sql) {
        return schemaMapper.executeCount(sql);
    }

    /**
     * 根据数据修改实例表字段属性
     * 
     * @param tableName
     *            实例表名
     * @param changeMap
     *            addList 新增字段列表 updateList 修改字段列表
     */
    // @Override
    // @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    // public void updateTableAttribute(String tableName, Map<String, Object> changeMap) {
    // List<Form> addList = (List<Form>) changeMap.get("addList");
    // List<Form> updateList = (List<Form>) changeMap.get("updateList");
    // if(StringUtils.isEmpty(tableName)||(addList.isEmpty()&&updateList.isEmpty())){
    // return;
    // }
    // if(!addList.isEmpty()){
    // StringBuilder columnBuffer = new StringBuilder();
    // columnBuffer.append(" alter table `").append(schemaName).append("`.").append("`").append(tableName).append("` ");
    // for (int i=0;i<addList.size();i++){
    // Form form = addList.get(i);
    // //判断form 类型
    // String formType = form.getType();
    // if (("groupLine").equals(formType)) {
    // continue;
    // }
    // Map<String, String> column = getColumnType(form);
    // //新增了必填及默认值属性 by pengfeng
    // columnBuffer.append(" add column ").append("`").append(form.getCode()).append("`").append(" "+column.get("type"))
    // .append(" "+column.get("required"));
    // //字段默认值
    // if(StringUtils.isNotEmpty(column.get("defaultvalue"))){
    // columnBuffer.append(" DEFAULT ").append("'").append(form.getDefaultvalue()).append("'");
    // }
    // columnBuffer.append(" COMMENT ").append("'").append(form.getName()).append("'");
    // if(!(i == addList.size()-1)){
    // columnBuffer.append(",").append("\n");
    // }
    // }
    // log.info("Execute alter table [{}].[{}] add column SQL -> {}", schemaName, tableName, columnBuffer.toString());
    // schemaMapper.alterTable(columnBuffer.toString());
    // }
    // if(!updateList.isEmpty()){
    // StringBuilder columnBuffer = new StringBuilder();
    // columnBuffer.append(" alter table `").append(schemaName).append("`.").append("`").append(tableName).append("` ");
    // for(int i=0;i<updateList.size();i++){
    // Form form = updateList.get(i);
    // //判断form 类型
    // String formType = form.getType();
    // if (("groupLine").equals(formType)) {
    // continue;
    // }
    // Map<String, String> column = getColumnType(form);
    // //新增了必填及默认值属性 by pengfeng
    // columnBuffer.append(" modify column ").append("`").append(form.getCode()).append("`").append(" "+column.get("type"))
    // .append(" "+column.get("required"));
    // //字段默认值
    // if(StringUtils.isNotEmpty(column.get("defaultvalue"))){
    // columnBuffer.append(" DEFAULT ").append("'").append(form.getDefaultvalue()).append("'");
    // }
    // columnBuffer.append(" COMMENT ").append("'").append(form.getName()).append("'");
    // if(!(i == updateList.size()-1)){
    // columnBuffer.append(",").append("\n");
    // }
    // }
    // log.info("Execute alter table [{}].[{}] modify column SQL -> {}", schemaName, tableName, columnBuffer.toString());
    // schemaMapper.alterTable(columnBuffer.toString());
    // }
    // }

    @Override
    public void delete(String tableName, String instanceId) {
        schemaMapper.delete(tableName, instanceId);
    }
}
