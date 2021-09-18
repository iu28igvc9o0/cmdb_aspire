package com.aspire.mirror.alert.api.dto.notify;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertSubscribeRulesDetailDto {
    /**
     * 规则名称
     */
    private String subscribeRules;
    /**
     * 是否启用 0-关闭(默认) 1-启用
     */
    private String isOpen;
    /**
     * 通知类型 0-邮件/短信 1-邮件 2-短信
     */
    private String notifyType;
    /**
     * '告警通知类型(0：告警生成，1:告警清除与解除)'
     */
    private String notifyAlertType;

    /**
     * id
     */
    private String id;

    // 是否重发
    private int isRecurrenceInterval;
    private Integer recurrenceCount;
    // 重发间隔时间
    private String recurrenceInterval;
    // 重发间隔单位
    private String recurrenceIntervalUtil;
    // 邮件发送方式
    private int emailType;
    // 发送记录
    private String sendOperation;
    // 创建人
    private String creator;
    // 执行时间段 0-全天 1-区间
    private String period;
    private String startPeriod;
    private String endPeriod;
    private String resendTime;
    //邮件内容
    private String emialContent;
    //邮件主题
    private String emialSubject;
    //短信内容
    private String smsContent;
    /**
     * 创建人
     */
    private String defensetor;
}
