package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

@Getter
public enum RiskLevelEnum {
    HIGH("1", "高危险"), MID("2", "中危险"), LOW("3", "低危险"), INFO("4", "信息"), UNKNOW("99", "未知");

    private RiskLevelEnum(String statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }

    public static RiskLevelEnum fromStatusCode(String statusCode) {
        for (RiskLevelEnum groupType : RiskLevelEnum.values()) {
            if (groupType.getStatusCode().equals(statusCode)) {
                return groupType;
            }
        }
        return UNKNOW;
    }
    public static RiskLevelEnum fromLabel(String label) {
        for (RiskLevelEnum groupType : RiskLevelEnum.values()) {
            if (groupType.getLabel().equals(label)) {
                return groupType;
            }
        }
        return UNKNOW;
    }
    private String	statusCode;
    private String	label;
}
