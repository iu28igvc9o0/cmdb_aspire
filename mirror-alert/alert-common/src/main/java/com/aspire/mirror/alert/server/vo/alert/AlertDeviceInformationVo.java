package com.aspire.mirror.alert.server.vo.alert;



public class AlertDeviceInformationVo {
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

    public String getCabinetColumnName() {
        return cabinetColumnName;
    }

    public void setCabinetColumnName(String cabinetColumnName) {
        this.cabinetColumnName = cabinetColumnName;
    }

    public Integer getCabinetCount() {
        return cabinetCount;
    }

    public void setCabinetCount(Integer cabinetCount) {
        this.cabinetCount = cabinetCount;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public int getBizSysCount() {
        return bizSysCount;
    }

    public void setBizSysCount(int bizSysCount) {
        this.bizSysCount = bizSysCount;
    }

    public int getCabinetColumnAlertCount() {
        return cabinetColumnAlertCount;
    }

    public void setCabinetColumnAlertCount(int cabinetColumnAlertCount) {
        this.cabinetColumnAlertCount = cabinetColumnAlertCount;
    }

    public int getCabinetAlertCount() {
        return cabinetAlertCount;
    }

    public void setCabinetAlertCount(int cabinetAlertCount) {
        this.cabinetAlertCount = cabinetAlertCount;
    }

    public int getDeviceAlertCount() {
        return deviceAlertCount;
    }

    public void setDeviceAlertCount(int deviceAlertCount) {
        this.deviceAlertCount = deviceAlertCount;
    }

    public int getBizSystemCount() {
        return bizSystemCount;
    }

    public void setBizSystemCount(int bizSystemCount) {
        this.bizSystemCount = bizSystemCount;
    }

    public int getMonitorCount() {
        return monitorCount;
    }

    public void setMonitorCount(int monitorCount) {
        this.monitorCount = monitorCount;
    }
}
