package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao.po
 * @Author: baiwenping
 * @CreateTime: 2020-02-25 15:30
 * @Description: ${Description}
 */
@Data
@NoArgsConstructor
public class AlertsV2 {

    /**
     * 告警ID
     */
    private String alertId;

    /**
     * clock
     */
    private String clock;

    /**
     * 关联告警ID
     */
    private String rAlertId;

    /**
     * 时间ID
     */
    private String eventId;

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
    private Date curMoniTime;

    /**
     * 告警开始时间
     */
    private Date alertStartTime;

    /**
     * 告警级别
     * 1-提示
     * 2-低
     * 3-中
     * 4-高
     * 5-严重
     */
    private String alertLevel;

    private Integer operateStatus;

    /**  */
    private String itemId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1-未派单
     * 2-处理中
     * 3-已完成
     */
    private String orderStatus;

    /**
     * 告警来源
     * MIRROR
     * ZABBIX
     */
    private String source;

    private String objectId;
    /**
     * 1-系统
     * 2-业务
     */
    private String objectType;

    /**
     * ip
     */
    private String deviceIp;

    private Date createTime;
    
    private String deviceId;
    /**
     * 工单类型
     * 1告警
     * 2故障
     * 3维保
     */
    private String orderType;
    /**
     * 工单ID
     */
    private String orderId;
    /**
     * 所属位置-资源池
     */
    private String idcType;
    /**
     * 警告数量
     */
    private Integer alertCount;
    /**
     * 更新时候数量是否+1，默认否
     */
    private boolean updateCount = false;

    /**
     * 派单状态
     * 0:失败
     * 1:成功
     */
    private Integer deliverStatus;
    /**
     * 派单时间
     */
    private Date deliverTime;

    /**
     * 监控项
     */
    private String	itemKey;

    /**
     * 监控项
     */
    private String	keyComment;
    /**
     * 通知操作状态:
     * 0-未通知
     * 1-已通知
     */
    private String notifyStatus;
}
