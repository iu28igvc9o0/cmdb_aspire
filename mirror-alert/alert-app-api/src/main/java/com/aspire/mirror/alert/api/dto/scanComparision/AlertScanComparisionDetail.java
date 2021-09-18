package com.aspire.mirror.alert.api.dto.scanComparision;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertScanComparisionDetail {

    // id
    private String id;
    // 资源池
    private String idcType;
    // 设备ip
    private String deviceIp;
    // 开始扫描时间
    private String startScanTime;
    // 最新扫描时间
    private String curScanTime;
    // 同步状态
    private String synStatus;
    // 最新同步时间
    private String curSynTime;
    // 最新告警时间时间
    private String curMoniTime;
}
