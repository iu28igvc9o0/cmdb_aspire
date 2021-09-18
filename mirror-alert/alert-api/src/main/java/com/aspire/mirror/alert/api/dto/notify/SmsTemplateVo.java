package com.aspire.mirror.alert.api.dto.notify;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @author menglinjie
 */
@Data
public class SmsTemplateVo implements Serializable {
    private String id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private String content;
    
    private List<String> idList;

    private static final long serialVersionUID = 1L;

}