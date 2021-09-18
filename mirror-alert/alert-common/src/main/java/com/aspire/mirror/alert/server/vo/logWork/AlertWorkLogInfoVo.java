package com.aspire.mirror.alert.server.vo.logWork;

import lombok.Data;

@Data
public class AlertWorkLogInfoVo {

    // 等级
    private String alertLevel;
    // 操作类型
    private long operationType;
}
