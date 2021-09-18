package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import net.sf.json.JSONObject;


/**
* 集群详情model ---- 集群列表也使用此model
* Project Name:composite-api
* File Name:CompRegionListResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionListResponse <br/>
* date: 2017年9月18日 下午7:55:10 <br/>
* 集群详情model ---- 集群列表也使用此model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionDetailResponse {
    private String id;
    private String name;
    private String display_name;
    private String namespace;
    private String state;
    private JSONObject attr;
    private Features features;
    private String created_at;
    private String updated_at;
    private String env_uuid;
    private String container_manager;
    
    private String type;
    private List<String> resource_actions;
    
    @Data
    @JsonInclude(Include.NON_NULL)
    public static class Features {
        private JSONObject node;
        private JSONObject lb;
        private JSONObject service;
        private JSONObject tunnel;
        private JSONObject network_modes;
        private JSONObject cd;
        private JSONObject volume;
    }
}
