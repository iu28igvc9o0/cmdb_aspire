package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    SensitiveRuleWhiteBatchCreateReq
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/12/21 17:38
 * 版本:      v1.0
 */
@Data
public class SensitiveRuleWhiteBatchCreateReq {
    @JsonProperty("rule_white_list")
    List<SensitiveRuleWhite> sensitiveRuleWhiteList;
}
