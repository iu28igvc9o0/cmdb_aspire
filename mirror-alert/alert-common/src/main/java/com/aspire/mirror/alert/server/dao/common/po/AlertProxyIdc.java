package com.aspire.mirror.alert.server.dao.common.po;

import lombok.Data;

@Data
public class AlertProxyIdc {
    private Integer id;
    private String proxyName;
    private String idc;
    private String remark;
}
