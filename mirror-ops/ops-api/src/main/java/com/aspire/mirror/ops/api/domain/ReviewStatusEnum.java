package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

/**
 * 敏感指令审批状态
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    ReviewStatusEnum.java
 * 类描述:    敏感指令审批状态枚举
 * 创建人:    JinSu
 * 创建时间:  2020/2/23 13:13
 * 版本:      v1.0
 */
@Getter
public enum  ReviewStatusEnum {
    STATUS_0(0, "待确认"), STATUS_1(1, "待审核"), STATUS_2(2, "审核通过"), STATUS_3(3, "审核不通过"),
    STATUS_9(9, "阻断");

    private ReviewStatusEnum(Integer statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }

    private Integer	statusCode;
    private String	label;
}
