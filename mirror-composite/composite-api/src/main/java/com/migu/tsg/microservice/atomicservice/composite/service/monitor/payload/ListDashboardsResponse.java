package com.migu.tsg.microservice.atomicservice.composite.service.monitor.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 查询Dashboard列表响应payload
* Project Name:composite-api
* File Name:ListDashboardsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.dashboard.payload
* ClassName: ListDashboardsResponse <br/>
* date: 2017年10月9日 上午11:22:09 <br/>
* 查询Dashboard列表响应payload
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class ListDashboardsResponse {
    private Integer count;
    private Integer code    = 0;
    
    @JsonProperty("num_pages")
    private Integer currPageNum;      // 当前页
    
    @JsonProperty("page_size")
    private Integer pageSize;
    
    private String message  = "SUCCESS";
    
    @JsonProperty("data")
    private List<DashboardItem> dashboardList;
}
