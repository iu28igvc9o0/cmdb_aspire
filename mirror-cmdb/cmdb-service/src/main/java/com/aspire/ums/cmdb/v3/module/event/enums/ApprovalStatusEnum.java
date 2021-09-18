package com.aspire.ums.cmdb.v3.module.event.enums;

import lombok.Getter;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ApprovalStatusEnum
 * Author:   hangfang
 * Date:     2020/5/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Getter
public enum ApprovalStatusEnum {
    STATUS_0(0, "未审批"),
    STATUS_1(1, "审批通过"),
    STATUS_2(2, "审批驳回");

    ApprovalStatusEnum(Integer statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }
    public static String getLabel(int statusCode) {
        for (ApprovalStatusEnum enumItem : ApprovalStatusEnum.values()) {
            if (enumItem.getStatusCode() == statusCode) {
                return enumItem.getLabel();
            }
        }
        return null;
    }

    public Integer getCode(String label) {
        for (ApprovalStatusEnum enumItem : ApprovalStatusEnum.values()) {
            if (enumItem.getLabel().equals(label)) {
                return enumItem.getStatusCode();
            }
        }
        return null;
    }

    private Integer	statusCode;
    private String	label;
}
