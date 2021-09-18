package com.aspire.ums.cmdb.schema.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SchemaService
 * Author:   zhu.juwang
 * Date:     2019/4/11 16:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * 数据库操作Service
 */
public interface SchemaService {

    /**
     * 获取指定schema中 tableName的数量
     * @param tableName 数据表名称
     * @return 存在的数量
     */
    Integer findTableByName(String tableName);

    /**
     * 获取指定schema中 tableName 下column的数量
     * @param tableName 数据表名称
     * @param columnName 数据表列名称
     * @return 存在的数量
     */
    Integer findTableColumnByColumnName (String tableName, String columnName);

    /**
     * 检测schema中 tableName是否存在
     * @param tableName 数据表名称
     * @return true: 存在  false: 不存在
     */
    boolean hasTable(String tableName);

    /**
     * 检测schema中 tableName是否存在
     * @param tableName 数据表名称
     * @param columnName 数据库列名
     * @return true: 存在  false: 不存在
     */
    boolean hasColumn(String tableName, String columnName);

    /**
     * 创建模型表
     * @param tableName 数据表名称
     * @param module 模型数据
     */
    void createModuleTable(String tableName, Module module);

    /**
     * 新增模型表字段
     * @param module 模型对象
     * @param form 字段属性对象
     */
    //void addTableColumn(Module module, Form form);

    /**
     * 新增模型表字段
     * @param tableName 表名
     * @param alterTableSql 修改表语句
     */
    void addTableColumn(String tableName, String alterTableSql);

    /**
     * 根据字段名删除模型表字段
     * @param moduleCode 模型对象
     * @param columnId 字段属性
     */
    //void deleteTableColumnByColumnId(String moduleCode, String columnId);

    /**
     * 根据字段名删除模型表字段
     * @param moduleCode 模型对象
     * @param columnName 字段属性
     */
    void deleteTableColumn(String moduleCode, String columnName);

    /**
     * 根据表名删除表
     * @param moduleTableName
     */
    void deleteTableByName(String moduleTableName);

    /**
     * 查询所有表名
     * @return
     */
    List<TableInfo> getTableList();

    /**
     * 根据表名获取该表的字段属性详情
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumnListByTableName(String tableName);

    /**
     * 检测SQL是否错误
     * @param params
     * @return
     */
    List<Map<String, String>> checkSql(Map<String, String> params);

    /**
     * 根据数据修改实例表字段属性
     * @param tableName  实例表名
     * @param changeMap  addList  updateList
     */
    //void updateTableAttribute(String tableName, Map<String, Object> changeMap);

    /**
     * 执行SQL语句
     * @param sql
     * @return
     */
    List<Map<String, Object>> executeSql(String sql);

    /**
     * 执行SQL语句
     * @param sql
     * @return
     */
    List<LinkedHashMap<String, Object>> executeSqlBySort(String sql);

    /**
     * 执行SQL语句
     * @param sql
     * @return
     */
    Integer executeCount(String sql);

    /**
     * 导入数据
     * @param moduleList
     */
    void importData(List<Module> moduleList);
    /**
     * 插入Ci数据
     * @param instanceData
     */
    void insertCi(String tableName, Map<String, Object> instanceData);
    /**
     * 更新Ci数据
     * @param instanceData
     */
    void updateCi(String tableName, String instanceId, Map<String, Object> instanceData);
    /**
     * 更新Ci数据
     * @param instanceData
     */
    void deleteCi(String tableName, String instanceId, Map<String, Object> instanceData);

    /**
     * 物理删除.
     * 
     * @param
     * @return
     */
    void delete(String tableName, String instanceId);
}
