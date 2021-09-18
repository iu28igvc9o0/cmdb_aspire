package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import lombok.Data;


/**
* 集群components
* Project Name:composite-api
* File Name:CompRegionComponentsResp.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionComponentsResp <br/>
* date: 2017年10月26日 下午4:40:35 <br/>
* 集群components
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompRegionComponentsResp {
    private List<RegionComponentItem> components;
    
    @Data
    public static class RegionComponentItem {
        private String type;
        private String status;
        private List<String> hosts;
    }
}
