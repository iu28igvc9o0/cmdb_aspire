package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    SensitiveReviewRole.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/3/1 19:23
 * 版本:      v1.0
 */
@Data
public class SensitiveReviewRole {
    @JsonProperty("review_id")
    private Long reviewId;

    @JsonProperty("role_id")
    private String roleId;

    @JsonProperty("role_name")
    private String RoleName;
}

