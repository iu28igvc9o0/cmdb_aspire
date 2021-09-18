package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveRuleWhite.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/9 10:49
 * 版本:      v1.0
 */
@Data
public class SensitiveRuleWhite {
    @ApiModelProperty("敏感指令白名单ID")
    @JsonProperty("sensitive_rule_white_id")
    private Long sensitiveRuleWhiteId;

    @ApiModelProperty("敏感指令规则ID")
    @JsonProperty("sensitive_rule_id")
    private Long sensitiveRuleId;

    @ApiModelProperty("敏感指令规则名称")
    @JsonProperty("rule_name")
    private String ruleName;

    @ApiModelProperty("敏感指令")
    @JsonProperty("command")
    private String command;

    @ApiModelProperty("对象类型 script pipeline")
    @JsonProperty("object_type")
    private String objectType;

    @ApiModelProperty("对象ID")
    @JsonProperty("object_id")
    private Long objectId;

    @ApiModelProperty("对象名称")
    @JsonProperty("object_name")
    private String objectName;

    @ApiModelProperty("对象状态 1：绑定 2：解绑")
    @JsonProperty("object_status")
    private Integer objectStatus;

    @ApiModelProperty("解绑来源 1：自动解绑")
    @JsonProperty("untie_source")
    private Integer untieSource;

    @ApiModelProperty("修改时间")
    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
