package com.aspire.mirror.alert.server.dao.mailAlert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlertMailResolveRecord {
    private Integer id;
    private String filterId;
    private String mailTitle;
    private String mailContent;
    private String mailSender;
    private String mailReceiver;
    private Date mailSendTime;
    private Date resolveTime;
    private String moniIndex;
    private String moniObject;
    private String deviceIp;
    private String alertLevel;
    private String alertId;
}
