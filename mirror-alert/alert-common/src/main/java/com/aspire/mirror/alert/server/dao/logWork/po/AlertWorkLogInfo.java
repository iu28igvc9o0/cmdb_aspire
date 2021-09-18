package com.aspire.mirror.alert.server.dao.logWork.po;

import lombok.Data;

@Data
public class AlertWorkLogInfo {

    // 值班人员
    private String workName;
    // 夜班时间段的符号 -1 白班 1 18:00-23:00 2 18:00-09:00 3 00:00-09:00
    private int isWork;
    // 值班开始时间
    private String startTime;
    private String startTimeFrom0;
    // 值班结束时间
    private String endTime;
    private String endTimeTo24;
}
