package com.aspire.mirror.alert.api.dto.logWork;

import lombok.Data;

@Data
public class AlertWorkLogTaskResponse {

    // 等级
    private String alertLevel;
    // 操作类型
    private long operationType;
}
