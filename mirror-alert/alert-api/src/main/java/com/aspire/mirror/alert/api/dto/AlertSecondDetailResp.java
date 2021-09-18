package com.aspire.mirror.alert.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsDetailResponse.java
 * 类描述:    告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:09
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertSecondDetailResp {
	
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
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
     * 设备类型
     */
    @JsonProperty("device_type")
    private String deviceType;

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

    private String prefix;
    @JsonProperty("device_class")
    private String deviceClass;
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
     * 主机名
     */
    @JsonProperty("device_description")
    private String deviceDescription;
    /**
     *  项目名称
     */
    @JsonProperty("project_name")
    private String projectName;
    /**
     * 机柜名称
     */
    @JsonProperty("idc_cabinet")
    private String idcCabinet;

    /**
     * 清除时间
     */
    @JsonProperty("clear_time")
    private Date clearTime;
}
