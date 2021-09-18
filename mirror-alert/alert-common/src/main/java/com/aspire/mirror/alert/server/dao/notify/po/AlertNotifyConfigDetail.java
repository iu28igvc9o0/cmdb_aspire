package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AlertNotifyConfigDetail {

    // uuid
    private String uuid;
    // 告警配置名称
    private String name;
    // 是否启用
    private int isOpen;
    // 告警过滤器id
    private int alertFilterId;
    // 告警过滤器id
    private String alertFilterName;
    // 告警场景id
    private int alertFilterSceneId;
    // 告警场景详情
    private Map<String, Object> alertFilterSceneInfo;
    // 通知告警类型  -1:告警产生；2-告警派单；3-告警清除
    private String notifyAlertType;
    // 通知类型
//    private String notifyType;
    // 通知对象信息
    private boolean personalNotifyObjFlag;
    private String personalNotifyObjLists;
    private List<AlertNotifyConfigReceiverDetail> notifyObjInfo;
    // 是否重发
    private int isRecurrenceInterval;
    // 重发间隔时间
    private String recurrenceInterval;
    // 重发间隔单位
    private String recurrenceIntervalUtil;
    // 重发次数
    private Integer recurrenceCount;
//    // 重发时间
//    private String resendTime;
    // 邮件发送方式
    private int emailType;
    // 发送记录
    private String sendOperation;
    // 最近发送时间
    private String curSendTime;
    // 创建人
    private String creator;
    // 执行时间段 0-全天 1-区间
    private String period;
    private String startPeriod;
    private String endPeriod;
    private String resendTime;
}
