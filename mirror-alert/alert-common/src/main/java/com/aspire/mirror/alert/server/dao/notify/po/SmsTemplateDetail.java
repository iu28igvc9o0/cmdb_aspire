package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author menglinjie
 */
@Data
public class SmsTemplateDetail implements Serializable {
    private String id;

    private String content;

    private String smsTemplateId;

    private Date createTime;

    private static final long serialVersionUID = 1L;

}