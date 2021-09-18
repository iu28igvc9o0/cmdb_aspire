package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 创建Dashboard的响应payload
* Project Name:composite-api
* File Name:ListDashboardsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: DashboardCreateUpdateResponse <br/>
* date: 2017年10月9日 上午11:22:09 <br/>
* 创建Dashboard的响应payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class DashboardCreateUpdateResponse {
    private Integer code;
    private String message;
    @JsonProperty("data")
    private DashboardItem dashboardItem;
}


