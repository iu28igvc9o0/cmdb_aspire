package com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload;

import lombok.Data;


/**
* 更新集群主机类型请求model
* Project Name:composite-api
* File Name:CompRegionNodeUpdateTypeRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.resmgr.payload
* ClassName: CompRegionNodeUpdateTypeRequest <br/>
* date: 2017年9月28日 下午4:31:03 <br/>
* 更新集群主机类型请求model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
public class CompRegionNodeUpdateTypeRequest {
    private String type;
}
