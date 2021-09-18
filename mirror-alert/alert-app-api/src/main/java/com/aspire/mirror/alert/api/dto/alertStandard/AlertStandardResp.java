package com.aspire.mirror.alert.api.dto.alertStandard;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: AlertStandardResp
 * @description: 类
 * @author: luowenbo
 * @create: 2020-06-10 16:39
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertStandardResp {
    /*
     * ID
     * */
    private Long id;

    /*
     * 设备分类
     * */
    private String deviceClass;

    /*
     * 设备类型
     * */
    private String deviceType;

    /*
     * 监控指标KEY
     * */
    private String monitorKey;

    /*
     * 标准名称
     * */
    private String standardName;

    /*
     * 告警描述
     * */
    private String alertDesc;

    /*
     * 状态 (启动 || 禁用)
     * */
    private String status;

    /*
     * 告警级别(低 || 中 || 高 || 重大)
     * */
    private String alertLevel;
}
