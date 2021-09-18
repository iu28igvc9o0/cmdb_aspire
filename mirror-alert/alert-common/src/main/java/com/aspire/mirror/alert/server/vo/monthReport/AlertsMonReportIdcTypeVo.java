package com.aspire.mirror.alert.server.vo.monthReport;

import lombok.Data;

@Data
public class AlertsMonReportIdcTypeVo {

    private String idcType;
    private String alertLevel;
    private int serverCount = 0;
    private int fireWallCount = 0;
    private int routerCount = 0;
    private int exchangeCount = 0;
    private int loadBalanceCount = 0;
    private int cloudStorageCount = 0;
    private int sdnControllerCount = 0;

    private int diskArrayCount = 0;
    private int tapeLibraryCount = 0;
    private String mon;

}
