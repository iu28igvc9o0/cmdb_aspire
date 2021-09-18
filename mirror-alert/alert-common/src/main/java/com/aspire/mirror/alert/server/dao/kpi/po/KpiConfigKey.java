package com.aspire.mirror.alert.server.dao.kpi.po;

import lombok.Data;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert.po
 * @Author: baiwenping
 * @CreateTime: 2020-04-15 09:57
 * @Description: ${Description}
 */
@Data
public class KpiConfigKey {
    /**
     *
     */
    private String id;
    /**
     * 配置id
     */
    private String configId;
    /**
     * 指标名称
     */
    private String keyName;
    /**
     *
     */
    private String kpiKey;
    /**
     * 检索模式，1-精确，2-右模糊
     */
    private String kpiKeyModel;
    /**
     * 指标所属字段
     */
    private String kpiKeyField;
    /**
     * 指标描述
     */
    private String description;
}
