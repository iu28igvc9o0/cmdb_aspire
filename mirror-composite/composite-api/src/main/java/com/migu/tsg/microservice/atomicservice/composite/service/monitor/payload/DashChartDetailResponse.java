package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
* 创建Dashboard下Chart的响应payload
* Project Name:composite-api
* File Name:DashChartCreateResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: DashChartCreateResponse <br/>
* date: 2017年10月10日 上午10:44:35 <br/>
* 创建Dashboard下Chart的响应payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class DashChartDetailResponse {
    private String message;
    private Integer code;
    
    @JsonProperty("data")
    private DashChartDetailItem dashChartDetail;
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DashChartDetailItem extends DashChartCreateUpdateRequest {
        private String uuid;
        private Long created_at;
        private Long updated_at;
        private List<String> resource_actions;
    }
}
