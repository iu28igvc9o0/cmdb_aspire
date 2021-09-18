package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;

import java.util.Date;

/**
 * @author baiwp
 * @title: AlertsDetail
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/6/1716:11
 */
@Data
public class AlertsDetail {
    private long id;
    private String alertId;
    private String actionId;
    private String eventId;
    private String moniIndex;
    private String moniObject;
    private String curMoniValue;
    private Date curMoniTime;
    private String alertLevel;
    private String itemId;
}
