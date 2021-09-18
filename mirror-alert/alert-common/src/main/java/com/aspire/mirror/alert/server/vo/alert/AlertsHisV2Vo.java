package com.aspire.mirror.alert.server.vo.alert;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.domain
 * @Author: baiwenping
 * @CreateTime: 2020-02-26 14:48
 * @Description: ${Description}
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertsHisV2Vo {

    /**  */
    @JsonProperty("alert_id")
    private String alertId;

    @JsonProperty("ext_id")
    private String extId;

    /**  */
    @JsonProperty("r_alert_id")
    private String rAlertId;

    /**
     * 业务系统
     */
    @JsonProperty("biz_sys")
    private String bizSys;

    /**
     * 监控指标/内容，关联触发器name
     */
    @JsonProperty("moni_index")
    private String moniIndex;

    /**
     * 监控对象
     */
    @JsonProperty("moni_object")
    private String moniObject;

    /**
     * 当前监控值
     */
    @JsonProperty("cur_moni_value")
    private String curMoniValue;

    /**
     * 当前监控时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("cur_moni_time")
    private Date curMoniTime;

    /**
     * 告警开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("alert_start_time")
    private Date alertStartTime;

    /**
     * 告警级别
     * 1-提示
     * 2-低
     * 3-中
     * 4-高
     * 5-严重
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

    @JsonProperty("alert_type")
    private String alertType;    // 告警  or 消警

    /**  */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 告警结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("alert_end_time")
    private Date alertEndTime;

    /**
     * 备注
     */
    @JsonProperty("remark")
    private String remark;

    /**
     * 1-未派单
     * 2-处理中
     * 3-已完成
     */
    @JsonProperty("order_status")
    private String orderStatus;    // 默认未派单

    /**
     * 告警来源
     * MIRROR
     * ZABBIX
     */
    @JsonProperty("source")
    private String source;

    /**
     * 1-系统
     * 2-业务
     */
    @JsonProperty("object_type")
    private String objectType;

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
    @JsonProperty("item_key")
    private String	itemKey;

    /**
     * 监控项
     */
    @JsonProperty("key_comment")
    private String	keyComment;

    /**
     * 清除人
     */
    @JsonProperty("clear_user")
    private String clearUser;
    /**
     * 清除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("clear_time")
    private Date clearTime;
    /**
     * 清除描述
     */
    @JsonProperty("clear_content")
    private String clearContent;
    /**
     * 通知操作状态:
     * 0-未通知
     * 1-已通知
     */
    @JsonProperty("notify_status")
    private String notifyStatus;

}
