package com.aspire.mirror.alert.server.dao.dashboard.po;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class DeviceItemInfo {
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("device_class")
    private String device_class;
    
    @JsonProperty("device_type")
    private String device_type;

    @JsonProperty("subclass")
    private String subclass;
    
    @JsonProperty("moniter_item_name")
    private String moniter_item_name;
    
    @JsonProperty("moniter_item_key")
    private String moniter_item_key;

    @JsonProperty("comment")
    private String comment;
    
    @JsonProperty("alert_level")
    private String alert_level;

    @JsonProperty("is_create_alert")
    private String is_create_alert;

    @JsonProperty("default_value")
    private String default_value;

    @JsonProperty("monitor_rate")
    private String monitor_rate;
    
    private String protocol;
    
    private String is_show_idcType;
    private String is_show_room;
    private String is_show_frame;
    private String note;
    
    private String alert_tips;
    private String object_type;
    private String is_create_order;
    


}
