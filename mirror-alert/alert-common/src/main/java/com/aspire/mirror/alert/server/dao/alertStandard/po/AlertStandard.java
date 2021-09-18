package com.aspire.mirror.alert.server.dao.alertStandard.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @projectName: AlertStandard
 * @description: PO类——告警标准化
 * @author: luowenbo
 * @create: 2020-06-10 14:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertStandard {
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

    /*
     * 操作人员
     * */
    private String insertPerson;

    /*
     * 新增时间
     * */
    private Date insertTime;

    /*
     * 是否删除
     * */
    private String isDelete;
}
