package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveRule.java
 * 类描述:    敏感指令规则
 * 创建人:    JinSu
 * 创建时间:  2020/2/17 10:33
 * 版本:      v1.0
 */
@Data
public class SensitiveRule {
    @JsonProperty("sensitive_rule_id")
    private Long sensitiveRuleId;

    @JsonProperty("sensitive_config_id")
    private Long sensitiveConfigId;

    @JsonProperty("rule_name")
    private String ruleName;

    @JsonProperty("white_list")
    private List<SensitiveRuleWhite> whiteList;
    /**
     * 规则范围json
     */
    @JsonProperty("rule_range")
    private String ruleRange;

    private Integer status;
    /**
     * 敏感指令
     */
    private String command;

}
