package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesManagementDto {
    /**
     * 规则名称
     */
    private String subscribeRules;
    /**
     * 资源池名称
     */
    private String idcType;
    /**
     * 设备IP
     */
    private String deviceIp;
    /**
     * 告警等级
     */
    private String alertLevel;
    /**
     * 启用状态
     */
    private String isOpen;

    /**
     * 规则id
     */
    private String alertSubscribeRulesId;
    /**
     * uuid
     */
    private String id;
    /**
     * 监控项
     */
    private String itemKey;
    /**
     * 内容
     */
    private String moniIndex;
    /**
     * 告警id
     */
    private String alertId;

    /**
     * 当前维护人
     */
    private String  defensetor;
}
