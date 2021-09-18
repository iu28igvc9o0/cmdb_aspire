package com.aspire.mirror.composite.payload.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.alert.payload
 * 类名称:    CompAlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 20:05
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class CompAlertsHisDetailResponse implements Serializable {

    private static final long serialVersionUID = 5554449075528429339L;
    /**
     * 告警ID
     */
    @JsonProperty("alert_id")
    private String alertId;

    /**
     * 关联告警ID
     */
    @JsonProperty("r_alert_id")
    private String rAlertId;

    /**
     *  时间ID
     */
    @JsonProperty("event_id")
    private String eventId;

    /**
     * 设备ID
     */
    @JsonProperty("device_id")
    private String deviceId;

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

    /**
     告警级别
     1-提示
     2-低
     3-中
     4-高
     5-严重
     */
    @JsonProperty("alert_level")
    private String alertLevel;

    /**  */
    @JsonProperty("item_id")
    private String itemId;

    /** 告警结束时间 */
    @JsonProperty("alert_end_time")
    private Date alertEndTime;

    /** 备注 */
    private String remark;

    /**
     1-未派单
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

    @JsonProperty("clear_time")
    private Date clearTime;

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
     * 告警时长
     */
    @JsonProperty("alert_time")
    private String alertTime;

    @JsonProperty("biz_sys_name")
    private String bizSysName;

    @JsonProperty("source_room_name")
    private String sourceRoomName;
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
     * 所属位置-资源池
     */
    @JsonProperty("idc_type_name")
    private String idcTypeName;
    /**
     * pod池
     */
    @JsonProperty("pod_name")
    private String podName;
}
