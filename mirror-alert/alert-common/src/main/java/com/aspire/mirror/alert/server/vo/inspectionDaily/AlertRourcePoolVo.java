package com.aspire.mirror.alert.server.vo.inspectionDaily;

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
public class AlertRourcePoolVo {
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_no")
    private Integer pageNo;


    
    /*资源池名称*/
    @JsonProperty("idc_type")
    private String[] idcType;
    
    /*归属月份*/
    @JsonProperty("month")
    private String month;
    
    /*告警等级*/
    @JsonProperty("alert_level")
    private Integer alertLevel;
    
    /*设备分类*/
    @JsonProperty("device_type")
    private String deviceType;
    
    private String deviceDictType;
    
    private String networkType;
    
    private String physicServer;
    
    private String other;
    
    private String sourceZabbix;
    
    private String sourcePrometheus;
    
    private String physicserverZabbix;
    
    private String physicserverPrometheus;
}
