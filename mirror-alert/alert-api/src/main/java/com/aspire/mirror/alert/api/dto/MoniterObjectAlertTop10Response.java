package com.aspire.mirror.alert.api.dto;

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
public class MoniterObjectAlertTop10Response {
    
    @JsonProperty("idc_type")
    private String idcType;
    
    @JsonProperty("device_type")
    private String device_type;
    
    @JsonProperty("alert_level")
    private String alertLevel;
    
    @JsonProperty("order")
    private String order;
    
    @JsonProperty("moniter_object")
    private String moniterObject;
    
    @JsonProperty("number")
    private String number;
    
    @JsonProperty("rate")
    private String rate;
    
}
