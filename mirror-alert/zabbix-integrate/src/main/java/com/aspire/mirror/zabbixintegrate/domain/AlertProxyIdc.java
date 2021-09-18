package com.aspire.mirror.zabbixintegrate.domain;

import lombok.Data;

@Data
public class AlertProxyIdc {
    private Integer id;
    private String proxyName;
    private String idc;
    private String remark;
}
