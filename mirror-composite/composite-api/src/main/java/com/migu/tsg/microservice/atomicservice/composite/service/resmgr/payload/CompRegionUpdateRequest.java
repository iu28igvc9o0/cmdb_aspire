package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
* 集群更新请求model
* Project Name:composite-api
* File Name:CompRegionUpdateRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionUpdateRequest <br/>
* date: 2017年9月19日 上午12:02:57 <br/>
* 集群更新请求model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompRegionUpdateRequest {
    private String version;
    private String is_compute;
    private String username;
    private List<Object> slave;
    private List<Object> features;
    private String cidr;
    private String slave_username;
    private String container_manager;
    private String template_type;
    private Object master;
    private Map<String, Object> haproxy;
    private Map<String, Object> nginx;
    private Map<String, Object> cni;
    private Map<String, Object> registry;
    private Map<String, Object> volume;
    private Map<String, Object> controller_haproxy;
    private Map<String, Object> over_commit;
    @JsonProperty("token")
    private String token;
    private String namespace;
    
    @JsonGetter("root_token")
    public String getRootToken() {
        return "bc25da01572cd2af5d2b1181c3bdfce723bc5789";
    }
}
