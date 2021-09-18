package com.aspire.mirror.alert.server.dao.notify.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;


@Data
@NoArgsConstructor
public class AlertSubscribeRulesDetail {
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
     * 最新发送时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date curSendTime;
    /**
     * 点击订阅告警的数量
     */
    private Integer count;
    /**
     * 告警内容
     */
    private String moniIndex;
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
