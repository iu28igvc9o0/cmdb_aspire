package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* 集群stats统计model
* Project Name:composite-api
* File Name:CompRegionStatsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionStatsResponse <br/>
* date: 2017年9月21日 上午1:28:09 <br/>
* 集群stats统计model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionStatsResponse {
    private Integer cpus_total;
    private Integer nodes_count;
    private Long containers_count;
    private Float cpus_allocated;
    private Long mem_total;
    private Long mem_allocated;
    private Long gpus_total;
    private Long gpus_allocated;
    private Long services_count;
    private Double mem_utilization;
    private Double cpus_utilization;
    private Double gpus_utilization;
}
