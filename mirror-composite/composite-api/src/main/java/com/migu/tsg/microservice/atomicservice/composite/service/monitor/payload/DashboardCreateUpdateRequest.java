package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
* 创建Dashboard请求payload
* Project Name:composite-api
* File Name:CreateDashboardRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: CreateDashboardRequest <br/>
* date: 2017年10月9日 下午2:50:48 <br/>
* 创建Dashboard请求payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class DashboardCreateUpdateRequest {
    @JsonProperty("dashboard_name")
    private String dashboardName;
    
    @JsonProperty("display_name")
    private String displayName;
    
    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("region_name")
    private String regionName;

    /**
     * 跨域字段
     */
    @JsonProperty("region_id")
    private String regionId;
}
