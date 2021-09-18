package com.aspire.mirror.zabbixintegrate.domain;

import lombok.Data;

/**
 * @author baiwp
 * @title: ZabbixTrigger
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2420:19
 */
@Data
public class ZabbixTrigger {
    private String itemId;
    private String triggerId;
    private String proxyName;
    private String idc;
    private String expression;
    private String description;
    private String priority;
    private String prefix;
}
