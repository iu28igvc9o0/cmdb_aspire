package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 告警创建实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsCreateRequest.java
 * 类描述:    告警创建实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class AlertsCreateRequest {
    /**  */

    /**  */
    @JsonProperty("r_alert_id")
    private String rAlertId;

    /**  */
    @NotBlank
    @JsonProperty("event_id")
    private String eventId;

    /**  */
    @NotBlank
    @JsonProperty("device_id")
    private String deviceId;

    /** 监控指标/内容，关联触发器name */
    @NotBlank
    @JsonProperty("moni_index")
    private String moniIndex;

    /** 监控对象 */
    @NotBlank
    @JsonProperty("moni_object")
    private String moniObject;

    /** 当前监控值 */
    @NotBlank
    @JsonProperty("cur_moni_value")
    private String curMoniValue;

    /** 当前监控时间 */
    @NotBlank
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
    @NotBlank
    @JsonProperty("alert_level")
    private String alertLevel;

    /**  */
    @NotBlank
    @JsonProperty("item_id")
    private String itemId;
    /** 1-未派单
     2-处理中
     3-已完成
     */
    @NotBlank
    @JsonProperty("order_status")
    private String orderStatus;

    /** 告警来源
     MIRROR
     ZABBIX
     */
    @NotBlank
    private String source;

    /** 机房/资源池 */
    @NotNull
    @JsonProperty("source_room")
    private String sourceRoom;

    @NotBlank
    @JsonProperty("object_id")
    private String objectId;
    /**
     *
     1-系统
     2-业务
     */
    @NotBlank
    @JsonProperty("object_type")
    private String objectType;

    /** 告警结束时间 */
    @JsonProperty("alert_end_time")
    private Date alertEndTime;

    /** 备注 */
    private String remark;

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
    @NotNull
    @JsonProperty("biz_sys_name")
    private String bizSysName;

    /** 业务系统 */
    @NotNull
    @JsonProperty("biz_sys")
    private String bizSys;
}
