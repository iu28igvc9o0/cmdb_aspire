package com.aspire.mirror.alert.server.dao.mailAlert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class AlertMailFilter {
    private String id;
    private Integer recipientId;
    private String receiver;
    private String sender;
    private String titleInclude;
    private String contentInclude;
    private Integer active;
    private Integer times;
    private Date lastSendTime;
    private String name;
}
