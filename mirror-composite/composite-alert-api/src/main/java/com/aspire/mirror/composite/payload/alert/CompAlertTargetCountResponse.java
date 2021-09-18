package com.aspire.mirror.composite.payload.alert;

import lombok.Data;

@Data
public class CompAlertTargetCountResponse {

    // 设备IP
    private String deviceIp;
    // 资源池
    private String idcType;
    // 业务系统
    private String bizSystem;
    // 监控指标
    private String moniIndex;
    // 监控对象
    private String moniObject;
    // 数量
    private String count;
}
