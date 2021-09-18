package com.aspire.mirror.log.api.dto;

import lombok.Data;

@Data
public class LogAlertRuleDetail {

    /**
     * uuid
     */
    private String uuid;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 运行状态
     */
    private String runStatus;
    /**
     * 规则描述
     */
    private String description;
    /**
     * 资源池
     */
    private String idcType;
    /**
     * 设备ip
     */
    private String ip;
    /**
     * 告警名称
     */
    private String keyComment;
    /**
     * 包含
     */
    private String include;
    /**
     * 包含关键字
     */
    private String includeKey;
    /**
     * 不包含
     */
    private String noInclude;
    /**
     * 不包含关键字
     */
    private String noIncludeKey;
    /**
     * 过滤时间
     */
    private Integer filterTime;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 用户名
     */
    private String userName;

    /**
     *  告警等级
     */
    private String alertLevel;
    /**
     * 告警数量
     */
    private int alertCount;
}
