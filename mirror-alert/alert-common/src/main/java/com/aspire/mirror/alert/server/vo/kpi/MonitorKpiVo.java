package com.aspire.mirror.alert.server.vo.kpi;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.domain
 * @Author: baiwenping
 * @CreateTime: 2020-04-20 14:00
 * @Description: ${Description}
 */
@Data
public class MonitorKpiVo {
    private String dataType;
    private String dataTable;
    private List<String> fieldList;
    private List<Map<String, Object>> dataList;
}
