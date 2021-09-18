package com.aspire.mirror.mail.recipient.po;

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
    private String alertName;
    private String moniIndex;
    private String alertDesc;
    private String alertLevel;
    private String alertId;
}
