package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* Dashboard下新增Chart的请求payload
* Project Name:composite-api
* File Name:DashChartCreateRquest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: DashChartCreateUpdateRequest <br/>
* date: 2017年10月10日 上午10:34:24 <br/>
* Dashboard下新增Chart的请求payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class DashChartCreateUpdateRequest {
    private String display_name;
    
    @JsonProperty(value = "dashboard_id")
    private String dashboardUuid;
    
    @JsonProperty("metrics")
    private List<MetricItem> metricList;
    
    @Data
    public static class MetricItem {
        private String type;
        private String metric;
        private String over;
        private String group_by;
        private String aggregator;
    }
}
