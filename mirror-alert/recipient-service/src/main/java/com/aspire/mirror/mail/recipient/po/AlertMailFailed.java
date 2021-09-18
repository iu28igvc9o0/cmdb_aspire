package com.aspire.mirror.mail.recipient.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 失败解析实体
 */
@Data
@NoArgsConstructor
public class AlertMailFailed {
    private String receiver;
    private String method;
    private String sender;
    private String uid;
    private String message;
}
