package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

import java.util.Date;

@Data
public class AlertNotifyConfigLog {
    // 告警通知配置id
    private String alertNotifyConfigId;
    // 发送状态
    private String sendStatus;
    // 接收人信息
    private String receiver;
    // 发送时间
    private Date sendTime;
    // 发送内容
    private String sendContent;
    // 发送告警ID
    private String sendAlertId;
    // 发送类型
    private String sendType;
    // 发送类型
    private String resendTime;
    // 发送类型
    private String isResend;
}
