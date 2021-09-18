package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* 集群主机label响应model
* Project Name:composite-api
* File Name:CompRegionStatsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionStatsResponse <br/>
* date: 2017年9月21日 上午1:28:09 <br/>
* 集群主机label响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionNodeLabelResponse {
    private List<LabelItem> labels;
    
    @Data
    private static class LabelItem {
        private String key;
        private String value;
        private Boolean editable;
    }
}
