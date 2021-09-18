package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

@Data
public class AlertNotifyConfigReceiverDetail {

    // uuid
    private String uuid;
    // 告警通知配置id
    private String alertNotifyConfigId;
    // 通知对象类型
    private String notifyObjType;
    // 通知对象信息
    private String notifyObjInfo;
    // 通知类型
    private String notifyType;

}
