package com.aspire.mirror.mail.recipient.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertMailRecipientTaskRule {
    private Integer id;
    private String receiver;
    private String password;
    private String mailServer;
    private Integer receiveProtocal;
    private Integer receivePort;
    private Integer active;
    private Integer strategyNum;
    private String description;
    private Integer period;
    private Integer unit;
}
