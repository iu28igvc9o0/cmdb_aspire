package com.aspire.mirror.alert.server.vo.primarySecondary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertPrimarySecondaryAlertsVo {

    /**
     *
     */
    private String alertId;
    /**
     * 主次ID
     */
    private Long primarySecondaryId;
    /**
     * 主次告警ID
     */
    private String primarySecondaryAlertId;
    /**
     *
     */
    private String rAlertId;
    /**
     *
     */
    private String eventId;
    /**
     *
     */
    private Integer actionId;
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
    private String[] bizSyss;
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
    private Date curMoniTime;
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
    private String[] sources;
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
    private Date alertStartTime;
    /**
     * 告警前缀，用于多套告警系统时候区分标识
     */
    private String prefix;
    /**
     *
     */
    private Integer alertCount;
    /**
     * 创建时间
     */
    private Date createTime;

    private String primarySecondaryTimeFrom;
    private String primarySecondaryTimeTo;
    /**
     * 告警结果，1-告警，2-消警
     */
    private String alertType;

    private int begin;
    private int pageSize;
}