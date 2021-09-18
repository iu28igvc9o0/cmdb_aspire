package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 敏感命令实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveConfig.java
 * 类描述:    敏感命令实体
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 13:38
 * 版本:      v1.0
 */
@Data
public class SensitiveConfig {
    @JsonProperty("sensitive_config_id")
    private Long  sensitiveConfigId;

    private String command;

    private String params;

    private String label;

    private String path;

    @JsonProperty("level_id")
    private Long levelId;

    @JsonProperty("level_name")
    private String levelName;

    //暂无用
    @JsonProperty("os_type")
    private String osType;
    //暂无用
    @JsonProperty("ops_user")
    private String opsUser;
    //暂无用
    @JsonProperty("ops_target_hosts")
    private String opsTargetHosts;

    @JsonProperty("sensitive_range")
    private String sensitiveRange;

    @JsonProperty("creater")
    protected String	creater;

    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;

    @JsonProperty("updater")
    protected String	updater;

    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date		updateTime;

    @JsonProperty("content_type")
    private Integer	contentType;				// 脚本内容类型    1 sh 2 bat 3 python  参考ScriptContentTypeEnum

    @JsonProperty("deal_type")
    private Integer dealType; //响应方式  1：审核2：阻断

    @JsonProperty("match_type")
    private Integer matchType; //匹配方式 1：完全匹配 2：正则匹配

    @JsonProperty("rule_num")
    private Integer ruleNum;

    @JsonProperty("review_num")
    private Integer reviewNum;

    @JsonProperty("pending_review_num")
    private Integer pendingReviewNum;

    @JsonProperty("rule_list")
    private List<SensitiveRule> ruleList;

    @JsonProperty("exec_role_list")
    private String execRoleList;

    @JsonProperty("audit_role_list")
    private String auditRoleList;
}
