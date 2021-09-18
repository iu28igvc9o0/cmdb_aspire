package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author menglinjie
 */
@Data
public class SmsTemplate implements Serializable {
    private String id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}