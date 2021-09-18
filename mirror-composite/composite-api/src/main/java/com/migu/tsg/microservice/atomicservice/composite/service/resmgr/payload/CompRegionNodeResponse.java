package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* 集群节点Data
* Project Name:composite-api
* File Name:CompRegionNodeResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionNodeResponse <br/>
* date: 2017年9月21日 上午1:54:23 <br/>
* 集群节点Data
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionNodeResponse {
    private Map<String, Object> attr;
    private List<Map<String, Object>> labels;
    private String node_tag;
    private String state;
    private String private_ip;
    private String type;
    private Map<String, Object> resources;
    
    private List<String> resource_actions;
}
