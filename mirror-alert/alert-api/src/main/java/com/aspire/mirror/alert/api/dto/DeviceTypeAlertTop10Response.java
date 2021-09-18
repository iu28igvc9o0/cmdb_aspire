package com.aspire.mirror.alert.api.dto;

import java.util.Date;

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
public class DeviceTypeAlertTop10Response {
    
    @JsonProperty("idc_type")
    private String idcType;
    
    @JsonProperty("device_type")
    private String deviceType;
    
    @JsonProperty("alert_level")
    private String alertLevel;
    
    @JsonProperty("order")
    private String order;
    
    @JsonProperty("ip")
    private String ip;
    
    @JsonProperty("number")
    private String number;
    
    //设备厂家
    @JsonProperty("device_Cmp")
    private String deviceCmp;
    
    @JsonProperty("idcType_pod")
    private String idcTypePod;
    
    @JsonProperty("mac")
    private Date mac;
    
}
