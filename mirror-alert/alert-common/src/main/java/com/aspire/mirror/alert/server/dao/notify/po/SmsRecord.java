package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author menglinjie
 */
@Data
@NoArgsConstructor
public class SmsRecord implements Serializable {
    private String id;

    private String receiverMobile;

    private String receiverName;

    private String receiverUuid;

    private String senderUuid;

    private Date createTime;

    private Integer status;

    private Integer isDelete;

    private String content;

    private String errorLog;

    private static final long serialVersionUID = 1L;

}