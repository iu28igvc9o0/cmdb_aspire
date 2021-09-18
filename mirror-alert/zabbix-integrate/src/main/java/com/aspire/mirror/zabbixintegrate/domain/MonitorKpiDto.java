package com.aspire.mirror.zabbixintegrate.domain;

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
public class MonitorKpiDto {
    private String dataType;
    private String dataTable;
    private List<String> fieldList;
    private List<Map<String, Object>> dataList;
}
