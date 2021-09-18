package com.aspire.mirror.alert.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsExportDTO {
    // 警报ID
    private String alertId;
    // 级别
    private String alertLevel;
    // 所属设备
    private String deviceId;
    // 设备IP
    private String deviceIp;
    // 监控对象
    private String moniObject;
    // 监控值
    private String curMoniValue;
    // 告警内容
    private String moniIndex;
    // 开始时间
    private String alertStartTime;
    // 当前时间
    private String curMoniTime;
    // 所属位置
    private String source;
    // 机房位置
    private String sourceRoom;
    // 业务线
    private String idcType;
    // 告警类型
    private String objectType;
    // 状态
    private String orderStatus;
    // 通知方式
    private String reportType;
    // 通知状态
    private String reportStatus;
    // 通知时间
    private String reportTime;
    // 转派人
    private String transUser;
    // 转派状态
    private String transStatus;
    // 转派时间
    private String transTime;
    // 待确认人
    private String toConfirmUser;
    // 确认人
    private String confirmedUser;
    // 确认时间
    private String confirmedTime;
    // 确认内容
    private String confirmedContent;
    // 派单状态
    private String deliverStatus;
    // 派单时间
    private String deliverTime;
    // 派单类型
    private String orderType;
    //告警次数
    private Integer alertCount;
    /**
     * 设备分类
     */
    private String deviceClass;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备提供商
     */
    private String deviceMfrs;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 主机名
     */
    private String hostName;
}
