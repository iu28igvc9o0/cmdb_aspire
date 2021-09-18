package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
* Dashboard详情的响应payload
* Project Name:composite-api
* File Name:DashboardDetailResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: DashboardDetailResponse <br/>
* date: 2017年10月9日 下午3:08:34 <br/>
* Dashboard详情的响应payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class DashboardDetailResponse {
    private Integer code;
    private String message;
    @JsonProperty("data")
    private DashboardItemDetail dashboardDetail;
     
    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class DashboardItemDetail extends DashboardItem {
        @JsonProperty("charts")
        private List<DashboardChart> dashChartList;
    }
    
    @Data
    public static class DashboardChart {
        private String uuid;

        @JsonProperty(value = "display_name")
        private String displayName;

        @JsonProperty(value = "created_at")
        private Long createdAt;

        @JsonProperty(value = "updated_at")
        private Long updatedAt;

        // 将页面的监控指标 统计方式 分组 图表类型组装成一个json数组保存
        private Object metrics;
        
        private List<String> resource_actions;
        
        @JsonGetter("name")
        public String getChartName() {
            return displayName;
        }
    }
}
