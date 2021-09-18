package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/**
* 获取集群支持的docker版本
* Project Name:composite-api
* File Name:DockerVersionResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: DockerVersionResponse <br/>
* date: 2017年10月17日 上午10:29:20 <br/>
* 获取集群支持的docker版本
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class DockerVersionResponse {
    @JsonProperty("default")
    private String defaultVerion;
    
    @JsonProperty("versions")
    private List<String> versionList;
}
