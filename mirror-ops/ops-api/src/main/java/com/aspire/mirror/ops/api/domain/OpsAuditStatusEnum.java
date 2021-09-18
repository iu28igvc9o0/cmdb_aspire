package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

@Getter
public enum OpsAuditStatusEnum {
    STATUS_0("0", "待审核"), STATUS_1("1", "审核通过"), STATUS_2("2", "审核拒绝");

    private OpsAuditStatusEnum(String statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }

    private String	statusCode;
    private String	label;

    public static OpsStatusEnum of(String statusCode) {
        for (OpsStatusEnum enumItem : OpsStatusEnum.values()) {
            if (enumItem.getStatusCode().equals(statusCode)) {
                return enumItem;
            }
        }
        return null;
    }
}
