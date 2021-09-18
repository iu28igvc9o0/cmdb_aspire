package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 告警转化类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    AlertsHis.java
 * 类描述:    历史告警po类
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertsHis {
    /**
     * 告警ID
     */
    private String alertId;

    /**
     * 关联告警ID
     */
    private String rAlertId;

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 设备ID
     */
    private String deviceId;

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

    /**  */
    private String itemId;

    /**
     * 告警结束时间
     */
    private Date alertEndTime;

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

    /**
     * 机房/资源池
     */
    private String sourceRoom;

    private String objectId;
    /**
     * 1-系统
     * 2-业务
     */
    private String objectType;

    /**
     * 所属域/资源池
     */
    private String region;

    /**
     * ip
     */
    private String deviceIp;
    
   
    /**
     * 工单类型
     * 1告警
     * 2故障
     */
    private String orderType;
    /**
     * 工单ID
     */
    private String orderId;
    /**
     * 业务系统名称
     */
    private String bizSysName;
    
    
    private Integer operateStatus;

    /**
     * 所属位置-资源池
     */
    private String idcType;

    /**
     * 警告数量
     */
    private Integer alertCount;

    /**
     * 设备分类
     */
    private String deviceClass;
    /**
     * 通知方式
     * 0:短信
     * 1:邮件
     */
    private Integer reportType;
    /**
     * 通知状态
     * 0:失败
     * 1:成功
     */
    private Integer reportStatus;

    /**
     * 通知时间
     */
    private Date reportTime;
    /**
     * 转派人
     */
    private String transUser;
    /**
     * 转派状态
     * 0:失败
     * 1:成功
     */
    private Integer transStatus;
    /**
     * 转派时间
     */
    private Date transTime;
    /**
     * 待确认人
     */
    private String toConfirmUser;
    /**
     * 确认人
     */
    private String confirmedUser;
    /**
     * 确认时间
     */
    private Date confirmedTime;
    /**
     * 确认内容
     */
    private String confirmedContent;
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
     * 清除人
     */
    private String clearUser;
    /**
     * 清除时间
     */
    private Date clearTime;
    /**
     * 清除描述
     */
    private String clearContent;

    private String actionId;
    /**
     * 告警前缀，用于区分不同zabbix
     */
    private String prefix;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备提供商
     */
    private String deviceMfrs;
    /**
     * 设备型号
     */
    private String deviceModel;
    /**
     * 主机名
     */
    private String hostName;
    /**
     * pod池
     */
    private String podName;
    /**
     * 工程期数
     */
    private String projectName;

    /**
     * 监控项
     */
    private String	itemKey;

    /**
     * 监控项
     */
    private String	keyComment;
}
