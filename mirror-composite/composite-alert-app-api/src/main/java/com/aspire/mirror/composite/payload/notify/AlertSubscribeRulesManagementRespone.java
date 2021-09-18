package com.aspire.mirror.composite.payload.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesManagementRespone {
    /**
     * 规则名称
     */
    private String subscribeRules;
    /**
     * 资源池名称
     */
    private String idcType;
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

    private String id;
    /**
     * 告警id
     */
    private String alertId;
    /**
     * 当前维护人
     */
    private String  defensetor;
}
