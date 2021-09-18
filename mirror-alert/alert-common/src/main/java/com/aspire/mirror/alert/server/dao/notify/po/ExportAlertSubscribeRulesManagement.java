package com.aspire.mirror.alert.server.dao.notify.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportAlertSubscribeRulesManagement {
    /**
     * 规则名称
     */

    private String subscribeRules;
    /**
     * 设备IP
     */

    private String deviceIp;


    /**
     * 告警等级
     */

    private String alertLevel;

    /**
     * 资源池名称
     */

    private String idcType;

    /**
     * 监控项
     */

    private String itemKey;
    /**
     * 内容
     */

    private String moniIndex;

}
