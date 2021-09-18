package com.aspire.mirror.zabbixintegrate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.dao
 * @Author: baiwenping
 * @CreateTime: 2020-04-20 09:44
 * @Description: ${Description}
 */
@Mapper
public interface HistoryMapper {

    /**
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> selectBySql(@Param("sql") String sql);

    /**
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectKpi(Map<String, Object> map);

    /**
     * 通过hosts表关联设备
     * @param map
     * @return
     */
    List<Map<String, Object>> selectKpiByHosts(Map<String, Object> map);

    /**
     * 通过hosts表关联设备
     * @param map
     * @return
     */
    List<Map<String, Object>> selectKpiByHostsName(Map<String, Object> map);

    /**
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> selectKpiByDay(Map<String, Object> map);
}
