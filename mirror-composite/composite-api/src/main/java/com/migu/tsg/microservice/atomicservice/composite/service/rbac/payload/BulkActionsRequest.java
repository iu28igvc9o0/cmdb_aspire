package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* bulkAction请求model
* Project Name:composite-api
* File Name:BulkActionsRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: BulkActionsRequest <br/>
* date: 2017年9月1日 下午3:12:21 <br/>
* bulkAction请求model
* @author pengguihua
* @version
* @since JDK 1.6
*/

@NoArgsConstructor
@Data
public class BulkActionsRequest {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("action")
    private String resTypeAction;
}
