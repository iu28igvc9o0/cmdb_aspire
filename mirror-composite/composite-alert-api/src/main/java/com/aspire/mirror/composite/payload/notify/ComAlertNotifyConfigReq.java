package com.aspire.mirror.composite.payload.notify;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ComAlertNotifyConfigReq {

    // uuid
    private String uuid;
    // 告警配置名称
    private String name;
    // 是否启用
    private int isOpen;
    // 告警过滤器id
    private int alertFilterId;
    // 告警过滤场景id
    private int alertFilterSceneId;
    // 告警通知类型
    private String notifyAlertType;
    // 通知类型
    private String notifyType;
    // 通知对象信息
    private List<Map<String, String>> notifyObjInfo;
    // 重发间隔时间
    private String recurrenceInterval;
    // 重发间隔单位
    private String recurrenceIntervalUtil;
    // 是否重发
    private int isRecurrenceInterval;
    // 重发次数
    private Integer recurrenceCount;
    // 邮件发送方式
    private int emailType;
    // 发送记录
    private String sendOperation;
    // 最近发送时间
    private String curSendTime;
    // 最近发送时间
    private String resendTime;
    // 创建人
    private String creator;
    // 执行时间段 0-全天 1-区间
    private String period;
    private String startPeriod;
    private String endPeriod;

}
