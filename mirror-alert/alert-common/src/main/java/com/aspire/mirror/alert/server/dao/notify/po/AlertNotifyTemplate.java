package com.aspire.mirror.alert.server.dao.notify.po;

import lombok.Data;

import java.util.Date;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao.po
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 14:28
 * @Description: ${Description}
 */
@Data
public class AlertNotifyTemplate {
    private String id;
    private String templateName;
    private String smsTemplate;
    private String emailTemplate;
    private String isEmailMerge;
    private String emailMergeTemplate;
    private String isDelete;
    private String creater;
    private Date createTime;
    private String updater;
    private Date updateTime;
    private String subject;
}
