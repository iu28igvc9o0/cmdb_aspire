package com.aspire.mirror.alert.api.dto.third;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ThirdCreateAlertReq {
    @JsonProperty("moni_result")
    private String moniResult;        // 指标监测结果 1: 新增告警 2: 解除告警

    @JsonProperty("alert_id")
    private String alertId;
    private String source;
    @JsonProperty("object_type")
    private String objectType;

    //	private String	monitorSource;	// 第三方监控系统编码, 在第三方的告警中，当作proxyName
    @JsonProperty("device_ip")
    private String deviceIP;        // 所属设备
    @JsonProperty("serv_system")
    private String servSystem;        // 所属业务系统
    @Deprecated
    @JsonProperty("monitor_room")
    private String monitorRoom;    // 监控机房
    @JsonProperty("monitor_object")
    private String monitorObject;    // 监控对象
    @JsonProperty("monitor_index")
    private String monitorIndex;    // 监控指标
    @JsonProperty("alert_level")
    private String alertLevel;        // 告警级别
    @JsonProperty("alert_desc")
    private String alertDesc;        // 告警描述
    @JsonProperty("cur_moni_time")
    private String curMoniTime;    // 当前监测时间 yyyy-MM-dd HH:mm:ss
    @JsonProperty("cur_moni_value")
    private String curMoniValue;    // 当前监测值
    @JsonProperty("business_system")
    private String businessSystem;    // 在Zabbix系统告警中，当作proxyName
    @Deprecated
    @JsonProperty("host_name")
    private String hostName;
    @JsonProperty("item_id")
    private String itemId;
    @Deprecated
    @JsonProperty("action_id")
    private String actionId;
    /**
     * 告警开始时间
     */
    @JsonProperty("alert_start_time")
    private String alertStartTime;
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

    private Map<String, Object> ext;
}
