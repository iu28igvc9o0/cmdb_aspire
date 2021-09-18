package com.aspire.mirror.zabbixintegrate.domain;

import lombok.Data;

/**
 * @author baiwp
 * @title: ZabbixItem
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2420:19
 */
@Data
public class ZabbixItem {
    private String itemId;
    private String ip;
    private String proxyName;
    private String idc;
    private String moniObject;
    private String name;
    private String key;
    private String delay;
    private String value;
    private String units;
    private String description;
    private String prefix;
    private String valueType;
    private String status;
}
