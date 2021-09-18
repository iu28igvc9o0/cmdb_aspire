package com.aspire.mirror.zabbixintegrate.daoCmdb.po;

import lombok.Data;

@Data
public class KpiKeyToMetricName {
    private Integer id;
    private String zabbixKey;
    private String zabbixDesc;
    private String deviceType;
    private String metricName;
    private String metricDesc;
    private String operation;
    private Long multiple;
}
