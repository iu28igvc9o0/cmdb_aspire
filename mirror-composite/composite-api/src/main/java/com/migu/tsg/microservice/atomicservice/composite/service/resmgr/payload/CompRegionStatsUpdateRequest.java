package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* 集群stats更新model
* Project Name:composite-api
* File Name:CompRegionStatsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionStatsResponse <br/>
* date: 2017年9月21日 上午1:28:09 <br/>
* 集群stats更新model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionStatsUpdateRequest {
    private List<Map<String, Object>> nodes;
    private String type;
    private Map<String, Object> region;
}
