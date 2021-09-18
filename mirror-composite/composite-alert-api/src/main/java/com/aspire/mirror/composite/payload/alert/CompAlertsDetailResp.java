package com.aspire.mirror.composite.payload.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class CompAlertsDetailResp {
    @JsonProperty("alert_id")
    private String alertId;

    /**  */
    @JsonProperty("r_alert_id")
    private String rAlertId;

    /**  */
    @JsonProperty("event_id")
    private String eventId;

    /**  */
    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("device_class")
    private String deviceClass;

    /** 业务系统 */
    @JsonProperty("biz_sys")
    private String bizSys;

    /** 监控指标/内容，关联触发器name */
    @JsonProperty("moni_index")
    private String moniIndex;

    /** 监控对象 */
    @JsonProperty("moni_object")
    private String moniObject;

    /** 当前监控值 */
    @JsonProperty("cur_moni_value")
    private String curMoniValue;

    /** 当前监控时间 */
    @JsonProperty("cur_moni_time")
    private Date curMoniTime;

    /**
     * 告警开始时间
     */
    @JsonProperty("alert_start_time")
    private Date alertStartTime;
    /** 告警级别
     1-提示
     2-低
     3-中
     4-高
     5-严重
     */
    @JsonProperty("alert_level")
    private String alertLevel;
    /**
     * 警报操作状态:
     * 0-待确认
     * 1-已确认
     * 2-待解决
     * 3-已处理
     */
    @JsonProperty("operate_status")
    private Integer operateStatus;
    /**  */
    @JsonProperty("item_id")
    private String itemId;

    /** 告警结束时间 */
    @JsonProperty("alert_end_time")
    private Date alertEndTime;

    /** 备注 */
    private String remark;

    /** 1-未派单
     2-处理中
     3-已完成
     */
    @JsonProperty("order_status")
    private String orderStatus;

    /** 告警来源
     MIRROR
     ZABBIX
     */
    private String source;

    /** 机房/资源池 */
    @JsonProperty("source_room")
    private String sourceRoom;

    @JsonProperty("object_id")
    private String objectId;
    /**
     *
     1-系统
     2-业务
     */
    @JsonProperty("object_type")
    private String objectType;

    /**
     * 所属域/资源池
     */
    private String region;

    /**
     * ip
     */
    @JsonProperty("device_ip")
    private String deviceIp;

    /**
     * 工单类型
     * 1告警
     * 2故障
     */
    @JsonProperty("order_type")
    private String orderType;
    /**
     * 工单ID
     */
    @JsonProperty("order_id")
    private String orderId;
    /**
     * 业务系统名称
     */
    @JsonProperty("biz_sys_name")
    private String bizSysName;
    /**
     * 所属位置-资源池
     */
    @JsonProperty("idc_type")
    private String idcType;

    /**
     * 警告数量
     */
    @JsonProperty("alert_count")
    private Integer alertCount;

    /**
     * 通知方式
     * 0:短信
     * 1:邮件
     */
    @JsonProperty("report_type")
    private Integer reportType;
    /**
     * 通知状态
     * 0:失败
     * 1:成功
     */
    @JsonProperty("report_status")
    private Integer reportStatus;

    /**
     * 通知时间
     */
    @JsonProperty("report_time")
    private Date reportTime;
    /**
     * 转派人
     */
    @JsonProperty("trans_user")
    private String transUser;
    /**
     * 转派状态
     * 0:失败
     * 1:成功
     */
    @JsonProperty("trans_status")
    private Integer transStatus;
    /**
     * 转派时间
     */
    @JsonProperty("trans_time")
    private Date transTime;
    /**
     * 待确认人
     */
    @JsonProperty("to_confirm_user")
    private String toConfirmUser;
    /**
     * 确认人
     */
    @JsonProperty("confirmed_user")
    private String confirmedUser;
    /**
     * 确认时间
     */
    @JsonProperty("confirmed_time")
    private Date confirmedTime;
    /**
     * 确认内容
     */
    @JsonProperty("confirmed_content")
    private String confirmedContent;
    /**
     * 派单状态
     * 0:失败
     * 1:成功
     */
    @JsonProperty("deliver_status")
    private Integer deliverStatus;
    /**
     * 派单时间
     */
    @JsonProperty("deliver_time")
    private Date deliverTime;
    /**
     * 告警前缀，用于区分不同zabbix
     */
    private String prefix;

    @JsonProperty("message_send")
    private String messageSend;

    @JsonProperty("message_open")
    private String messageOpen;

    @JsonProperty("mail_send")
    private String mailSend;

    @JsonProperty("mail_open")
    private String mailOpen;
    /**
     * 设备类型
     */
    @JsonProperty("device_type")
    private String deviceType;
    /**
     * 设备提供商
     */
    @JsonProperty("device_mfrs")
    private String deviceMfrs;
    /**
     * 设备型号
     */
    @JsonProperty("device_model")
    private String deviceModel;
    /**
     * 主机名
     */
    @JsonProperty("host_name")
    private String hostName;
    /**
     * pod池
     */
    @JsonProperty("pod_name")
    private String podName;
    /**
     * 当前告警收敛数
     */
    @JsonProperty("intelligentCount")
    private Integer intelligentCount;
    /**
     *  项目名称
     */
    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("exec_user")
    private String execUser;
    @JsonProperty("exec_time")
    private Date execTime;
    @JsonProperty("expire_status")
    private String expireStatus;
}
