package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveReview.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/1/20 16:18
 * 版本:      v1.0
 */
@Data
public class SensitiveReview {

    public static final Integer REVIEW_STATUS_PENDING = 1;
    public static final String WHITE_LIST_USER_NAME = "whitelist";

    @JsonProperty("review_id")
    private Long reviewId;

    @JsonProperty("pipeline_instance_id")
    private Long pipelineInstanceId;

    @JsonProperty("sensitive_env_hash")
    private String sensitiveEnvHash;

    @JsonProperty("sensitive_command_hash")
    private String sensitiveCommandHash;

    @JsonProperty("review_status")
    private Integer reviewStatus;

    protected String	applicant;

    @JsonProperty("apply_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date applyTime;

    @JsonProperty("reviewer")
    protected String	reviewer;

    @JsonProperty("review_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date		reviewTime;

    private String command;

    @JsonProperty("rule_name")
    private String ruleName;

    @JsonProperty("sensitive_config_id")
    private Long sensitiveConfigId;

    @JsonProperty("sensitive_rule_id")
    private Long sensitiveRuleId;

    @JsonIgnore
    private String reviewIdsString;

    @JsonProperty("review_content")
    private String reviewContent;

//    @JsonProperty("review_role_ids")
//    private String review_role_ids;

    @JsonProperty("role_list")
    private List<SensitiveReviewRole> roleList;
}
