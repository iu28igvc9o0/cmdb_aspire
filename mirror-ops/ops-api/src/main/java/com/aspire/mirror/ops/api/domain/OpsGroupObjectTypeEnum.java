package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

@Getter
public enum OpsGroupObjectTypeEnum {
    PILELINE("pipeline", "作业"), SCRIPT("script", "脚本"), YUM("yum", "yum源"), YUM_CONFIG("yum_config", "yum源配置"), 
    SCENES("scenes", "场景"), AP_SCHEME("ap_scheme", "自愈方案"), FILE("file", "文件"), PARAM("param", "参数");

    private OpsGroupObjectTypeEnum(String statusCode, String label) {
        this.statusCode = statusCode;
        this.label = label;
    }

    public static OpsGroupObjectTypeEnum fromStatusCode(String statusCode) {
        for (OpsGroupObjectTypeEnum groupType : OpsGroupObjectTypeEnum.values()) {
            if (groupType.getStatusCode().equals(statusCode)) {
                return groupType;
            }
        }
        return null;
    }

    private String	statusCode;
    private String	label;
}
