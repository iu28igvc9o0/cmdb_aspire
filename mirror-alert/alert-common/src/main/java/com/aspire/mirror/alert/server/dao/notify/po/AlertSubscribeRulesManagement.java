package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesManagement {
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
     *告警id
     */
    private String alertId;
    /**
     * 告警值
     */
    private String curMoniValue;
    /**
     * 业务系统
     */
    private String objectType;
    /**
     * 设备类型
     */
    private String deviceClass;
    /**
     * 设备分类
     */
    private String deviceType;
    /**
     * 工程期数
     */
    private String projectName;
    /**
     * 设备厂商
     */
    private String deviceMfrs;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 告警开始时间
     */
    private Date alertStartTime;
    /**
     * 告警名称
     */
    private String keyComment;

    /**
     * 当前维护人
     */
    private String  defensetor;

}
