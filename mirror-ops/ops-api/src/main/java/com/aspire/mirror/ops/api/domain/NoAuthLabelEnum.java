package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

@Getter
public enum NoAuthLabelEnum {
    AUTO_REPAIR("autoRepair", "selfhealing"), INSPECTION("cruisecheck", "inspection"), VULNERABILITY("vulnerability", "vulnerability");
    NoAuthLabelEnum(String labelId, String userName) {
        this.labelId = labelId;
        this.userName = userName;
    }
    public static String getUserNameByLabelId(String labelId) {
        for (NoAuthLabelEnum noAuthLabelEnum : NoAuthLabelEnum.values()) {
            if (noAuthLabelEnum.getLabelId().equals(labelId)) {
                return noAuthLabelEnum.getUserName();
            }
        }
        return null;
    }
    private String labelId;
    private String userName;
}
