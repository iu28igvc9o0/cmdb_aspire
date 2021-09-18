package com.aspire.ums.cmdb.schema.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aspire.ums.cmdb.schema.payload.ColumnInfo;
import com.aspire.ums.cmdb.schema.payload.TableInfo;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SchemaMapper
 * Author:   zhu.juwang
 * Date:     2019/4/11 16:45
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface SchemaMapper {

    /**
     * 获取指定schema中 tableName的数量
     * @param schemaName 数据库实例
     * @param tableName 数据表名称
     * @return 存在的数量
     */
    Integer findTableByName(@Param("schemaName") String schemaName, @Param("tableName") String tableName);

    /**
     *  创建表
     * @param createTabelSql 创建表SQL
     */
    void createTable(@Param("createTabelSql") String createTabelSql);

    /**
     * 批量新增数据
     * @param insertSql 批量创建SQL
     */
    void batchInsert(@Param("insertSql") String insertSql);

    /**
     * 获取指定schema中 tableName 下column的数量
     * @param schemaName 数据库实例
     * @param tableName 数据表名称
     * @param columnName 数据表列名称
     * @return 存在的数量
     */
    Integer findTableColumnByColumnName(@Param("schemaName") String schemaName,
                                        @Param("tableName") String tableName,
                                        @Param("columnName") String columnName);

    /**
     *  修改表
     * @param alterTableSql 修改表SQL
     */
    void alterTable(@Param("alterTableSql") String alterTableSql);

    /**
     * 根据表名删除表
     * @param tableName
     */
    void deleteTableByName(@Param("tableName")String tableName);

    /**
     * 查询所有表名
     * @return
     */
    List<TableInfo> getTableList(@Param("schemaName") String schemaName);

    /**
     * 根据表名获取该表的字段属性详情
     * @param tableName 表名
     * @param schemaName 数据库名称
     * @return
     */
    List<ColumnInfo> getColumnListByTableName(@Param("tableName") String tableName, @Param("schemaName") String schemaName);

    /**
     * 检测SQL是否错误
     * @param sql
     * @return
     */
    List<Map<String, String>> checkSql(@Param("sql") String sql);

    /**
     * 执行SQL语句
     * @param sql
     * @return
     */
    List<Map<String, Object>> executeSql(@Param("sql") String sql);

    /**
     * 执行SQL语句并按列顺序返回数据
     * @param sql
     * @return
     */
    List<LinkedHashMap<String, Object>> executeSqlBySort(@Param("sql") String sql);

    Integer executeCount(@Param("sql") String sql);

    void insertCi(@Param("tableName") String tableName, @Param("instanceData") Map<String, Object> instanceData);
    void updateCi(@Param("tableName") String tableName,
                  @Param("instanceId") String instanceId,
                  @Param("instanceData") Map<String, Object> instanceData);

    void deleteCi(@Param("tableName") String tableName,
                  @Param("instanceId") String instanceId,
                  @Param("instanceData") Map<String, Object> instanceData);

    void delete(@Param("tableName") String tableName, @Param("instanceId") String instanceId);
}
