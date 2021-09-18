package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;


/**
* create集群响应model
* Project Name:composite-api
* File Name:ICompRegionCreateResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: ICompRegionCreateResponse <br/>
* date: 2017年9月18日 下午9:14:03 <br/>
* create集群响应model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class CompRegionCreateResponse {
    private String id;
    private String name;
    private String display_name;
    private String namespace;
    private String state;
    private Map<String, Object> attr;
    private Map<String, Object> features;
    private String created_at;
    private String updated_at;
    private String env_uuid;
    private String container_manager;
}
