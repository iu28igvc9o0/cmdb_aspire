package com.aspire.mirror.alert.server.dao.kpi.po;

import lombok.Data;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert.po
 * @Author: baiwenping
 * @CreateTime: 2020-04-20 18:59
 * @Description: ${Description}
 */
@Data
public class KpiConfigDict {
    private Integer id;
    private String dictType;
    private String dictName;
    private String dictValue;
    private String description;
}
