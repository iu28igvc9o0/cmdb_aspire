package com.aspire.mirror.alert.server.dao.kpi;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.dao
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 19:37
 * @Description: ${Description}
 */
@Mapper
public interface KpiCommonMapper {
    int insert (@Param("table") String table, @Param("fieldList") List<String> fieldList, @Param("dataList") List<Map<String, Object>> dataList);
}
