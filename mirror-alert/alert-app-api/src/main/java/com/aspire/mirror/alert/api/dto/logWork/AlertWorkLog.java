package com.aspire.mirror.alert.api.dto.logWork;

import lombok.Data;

@Data
public class AlertWorkLog {

    private String uuid;

    private String workConfigId;

    private String alertCount;

    private String alertOrder;

    private String alertClear;

    private String alertConfirm;

    private String alertTransfer;

    private String alertNotify;

    // 夜班时间段的符号 0 无 1 18:00-23:00 2 18:00-09:00 3 00:00-09:00
    private int isWork;
}
