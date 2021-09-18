package com.aspire.mirror.alert.server.dao.kpi;

import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigLogs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-21 20:26
 * @Description: ${Description}
 */
@Mapper
public interface KpiConfigLogsMapper {
    /**
     *
     * @param kpiConfigLogs
     * @return
     */
    int insert(KpiConfigLogs kpiConfigLogs);
    /**
     * 更新
     * @param kpiConfigLogs
     * @return
     */
    int update (KpiConfigLogs kpiConfigLogs);

    /**
     *
     * @param map
     * @return
     */
    List<KpiConfigLogs> selectNewestByConfigId(Map<String, Object> map);
}
