package com.aspire.mirror.alert.server.dao.mailAlert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlertMailSubstance {
    private String uid;
    private String receiver;
    private String sender;
    private String subject;
    private String content;
    private Date sendTime;
}
