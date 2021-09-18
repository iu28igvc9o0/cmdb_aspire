package com.aspire.ums.cmdb.automate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fanwenhui
 * @date 2020-08-24 10:04
 * @description 自动化模型日志入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomateInstanceLog{
    private String id;
    private String instanceId;
    private String eventType;
    private String eventId;
    private String system;
    private String objectId;
    private String objectName;
    private Integer objectVersion;
    private Integer version;
    private Long optTimestamp;
    private String operator;
    private String optDesc;
    private String targetCategory;
    private String targetId;
    private String targetName;
    private Date createTime;
    private Date updateTime;
    private String optContent;
    private String synLogId;
}
