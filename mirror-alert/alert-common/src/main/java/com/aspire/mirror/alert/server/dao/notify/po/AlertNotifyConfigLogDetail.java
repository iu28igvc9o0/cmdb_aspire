package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

@Data
public class AlertNotifyConfigLogDetail {

    private String alertNotifyConfigId;
    private String voiceAlertId;
    private String operator;
    private String operationTime;

}
