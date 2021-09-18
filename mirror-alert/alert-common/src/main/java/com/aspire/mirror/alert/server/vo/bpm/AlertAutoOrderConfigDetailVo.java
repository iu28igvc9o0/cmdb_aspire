package com.aspire.mirror.alert.server.vo.bpm;

import lombok.Data;

@Data
public class AlertAutoOrderConfigDetailVo {

    // uuid
    private String uuid;
    // 告警配置名称
    private String configName;
    // 生效类型 1-永久生效 2-范围生效
    private String configTimeType;
    // 范围生效开始时间
    private String startTime;
    // 范围生效结束时间
    private String endTime;
    // 用户
    private String userName;
    // 描述
    private String configDescription;
    // 配置状态 1-开启 2-关闭
    private String isOpen;
    // 告警过滤
    private String alertFilter;
    // 工单类型 1-告警工单 2-故障工单 3-维保工单 4-调优工单
    private String orderType;
    // 派单间隔时间
    private int orderTimeSpace;
    // 派单时段
    private String orderTimeInterval;
    // 创建人
    private String creator;
    // 创建时间
    private String createTime;
    // 更新人
    private String updater;
    // 更新时间
    private String updateTime;
    // 删除状态，0-未删除，1-已删除
    private String is_delete;

}
