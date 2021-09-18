package com.aspire.mirror.alert.server.vo.bpm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertAutoOrderLogVo {

    /**
     *
     */
    private String alertId;
    /**
     * 告警自动派单规则
     */
    private String configId;
    private String configName;
    /**
     *
     */
    private String deviceId;
    /**
     * 设备类型
     */
    private String deviceClass;
    /**
     * 业务系统
     */
    private String bizSys;
    /**
     * 监控指标/内容，关联触发器name
     */
    private String moniIndex;
    /**
     * 监控对象
     */
    private String moniObject;
    /**
     * 当前监控值
     */
    private String curMoniValue;
    /**
     * 当前监控时间
     */
    private String curMoniTime;
    /**
     * 告警级别
     * 1-提示
     * 2-低
     * 3-中
     * 4-高
     * 5-严重
     */
    private String alertLevel;
    /**
     *
     */
    private String itemId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 告警来源
     * MIRROR
     * ZABBIX
     */
    private String source;
    /**
     * 所属位置-资源池
     */
    private String idcType;
    /**
     * 机房/资源池
     */
    private String sourceRoom;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备提供商
     */
    private String deviceMfrs;
    /**
     * 主机名
     */
    private String hostName;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 告警类型
     * 1-系统
     * 2-业务
     */
    private String objectType;
    /**
     * 对象ID，如果是设备告警则是设备ID，如果是业务则是业务系统code
     */
    private String objectId;
    /**
     * 域/资源池code
     */
    private String region;
    /**
     *
     */
    private String deviceIp;
    /**
     * 告警开始时间
     */
    private String alertStartTime;
    /**
     * 告警前缀，用于多套告警系统时候区分标识
     */
    private String prefix;
    /**
     * 派单时间
     */
    private String orderTime;
    /**
     * 派单类型
     */
    private String orderType;
    /**
     * 工单号
     */
    private String orderId;
    /**
     * 派单状态
     */
    private String status;
    private Date createTime;
}