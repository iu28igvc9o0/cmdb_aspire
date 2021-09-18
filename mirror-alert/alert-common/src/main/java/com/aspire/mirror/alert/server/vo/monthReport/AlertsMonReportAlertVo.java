package com.aspire.mirror.alert.server.vo.monthReport;

import lombok.Data;

@Data
public class AlertsMonReportAlertVo {

    private String idcType;
    private String alertLevel;
    private String alertIndex;
    private String deviceModel;
    private String deviceMrfs;
    private String pod;
    private int sCount;
    private int rank;
    private String month;

    // 监控指标名称
    private String alertIndexValue;
    // 占比
    private String rate;

    /**
     * 组装字段
     */
    // 设备厂商 + 设别型号 + 位置
    private String mrfsModelOption;
    // 资源池 + pod池
    private String idcPod;

}
