package com.aspire.mirror.composite.payload.alert;

import lombok.Data;

@Data
public class AlertDeviceInformationResponse {

    //列头柜名称
    private String cabinetColumnName;
    //管理机柜数
    private Integer cabinetCount;
    //管理设备数
    private int deviceCount;
    //相关业务系统数
    private int bizSysCount;
    //列头柜告警
    private int cabinetColumnAlertCount;

    private int cabinetAlertCount;

    private int deviceAlertCount;
    //告警相关的业务系统数
    private int bizSystemCount;
    //管理设备数
    private int monitorCount;

}
