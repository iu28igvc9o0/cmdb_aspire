package com.aspire.mirror.zabbixintegrate.domain;

import lombok.Data;

/**
 * @author baiwp
 * @title: ZabbixTrends
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2420:19
 */
@Data
public class ZabbixTrends {
    private String itemId;
    private String proxyName;
    private String idc;
    private Long clock;
    private String num;
    private String valueMin;
    private String valueAvg;
    private String valueMax;
    private String prefix;
}
