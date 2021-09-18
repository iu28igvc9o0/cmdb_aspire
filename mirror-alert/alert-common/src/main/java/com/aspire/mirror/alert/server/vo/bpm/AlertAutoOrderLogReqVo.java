package com.aspire.mirror.alert.server.vo.bpm;

import lombok.Data;

@Data
public class AlertAutoOrderLogReqVo {

    // 派单规则
    private String orderRule;
    // 告警设备
    private String deviceIp;
    // 资源池
    private String idcType;
    // 告警等级
    private String alertLevel;
    // 派单开始时间
    private String orderStartTime;
    // 派单结束时间
    private String orderEndTime;
    // 告警内容
    private String alertDescription;
    // 派单类型
    private String orderType;
    // 业务系统
    private String bizSystem;
    // 告警来源
    private String alertSource;

    /**
     * 分页
     */
    private Integer pageNum;
    private Integer pageSize;
}
