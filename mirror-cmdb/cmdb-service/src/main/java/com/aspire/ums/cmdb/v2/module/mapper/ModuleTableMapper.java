package com.aspire.ums.cmdb.v2.module.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleTableMapper {

    /**
     * 获取指定schema中 tableName的数量
     * @param schemaName 数据库实例
     * @param tableName 数据表名称
     * @return 存在的数量
     */
    Integer findTableByName(@Param("schemaName") String schemaName, @Param("tableName") String tableName);


    /**
     *  创建表
     */
    void createTable(@Param("tableName") String tableName);

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
     *  添加列
     */
    void alterAddTable(@Param("tableName") String tableName, @Param("columns") List<String> columns);

    /**
     *   修改列
     */
    void alterModifyTable(@Param("tableName") String tableName, @Param("columns") List<String> columns);


    /**
     *  删除列
     */
    void alterDropTable(@Param("tableName") String tableName, @Param("columns") List<String> columns);

    /**
     * 根据表名删除表
     * @param tableName
     */
    void deleteTableByName(@Param("tableName")String tableName);

    /**
     * 复制新增列的数据
     * @param tableName
     */
    void copyColumnData(@Param("tableName")String tableName, @Param("tableHisName")String tableHisName, @Param("columns") List<String> columns);


    /**
     * 复制已存在列的数据
     * @param tableName
     */
    void copyOtherColumnData(@Param("tableName")String tableName, @Param("tableHisName")String tableHisName, @Param("columns") List<String> columns);


    /**
     * 创建复制表数据
     * @param tableName
     */
    void createAndCopyTable(@Param("tableName")String tableName, @Param("tableHisName")String tableHisName);

    /**
     * 获取表中所有列
     * @param tableName
     */
    List<String> getColumnListByTableName(@Param("tableName")String tableName, @Param("schemaName")String schemaName);
    /**
     * 获取表中所有列
     * @param sql
     */
    List<Map<String, Object>> getDataBySql(@Param("sql") String sql);


    /**
     * 获取表中数据量
     * @param tableName
     */
    Integer getTableDataCount(@Param("tableName") String tableName);
}
