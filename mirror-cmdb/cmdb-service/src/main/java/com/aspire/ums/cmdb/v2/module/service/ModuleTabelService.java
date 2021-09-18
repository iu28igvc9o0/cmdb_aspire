package com.aspire.ums.cmdb.v2.module.service;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.module.payload.Module;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SchemaService
 * Author:   HANGFAGN
 * Date:     2019/5/20 16:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * 数据库操作Service
 */
public interface ModuleTabelService {

    /**
     * 检测数据库中 tableName是否存在
     * @param tableName 数据表名称
     * @return true: 存在  false: 不存在
     */
    boolean hasTable(String tableName);

    /**
     * 检测数据库中 tableName是否存在
     * @param tableName 数据表名称
     * @param columnName 数据库列名
     * @return true: 存在  false: 不存在
     */
    boolean hasColumn(String tableName, String columnName);

    /**
     * 创建模型表
     * @param tableName 数据表名称
     */
    void createModuleTable(String tableName);

    /**
     * 新增模型表字段
     * @param tableName 表名
     * @param codes 相关码表字段名
     */
    void addTableColumn(String tableName, List<CmdbCode> codes);

    /**
     * 根据字段名删除模型表字段
     * @param module 模型对象
     * @param removeCodes 字段
     */
    void deleteTableColumn(Module module, List<CmdbCode> removeCodes);

    /**
     * 根据表名删除表
     * @param
     */
    void deleteTableByName(Module moduleId);

    /**
     * 根据表名删除表
     * @param moduleTableName
     */
    void deleteEmptyTable(String moduleTableName);

    /**
     * 获取表中所有列
     * @param tableName
     */
    List<String> getColumnListByTableName(String tableName);

    /**
     * 获取表中所有列
     * @param sql
     */
    List<Map<String, Object>> getDataBySql(String sql);

    /**
     * 获取模型表结构
     * @param moduleId
     * @return
     */
//    String getModuleSql(String moduleId);

    /**
     * 获取表中数据量
     * @param tableName
     */
    Integer getTableDataCount(String tableName);
}
