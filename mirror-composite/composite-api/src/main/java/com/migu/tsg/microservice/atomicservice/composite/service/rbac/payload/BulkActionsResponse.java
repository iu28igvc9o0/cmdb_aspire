package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* The response with the request for 'Used to attribute a list of resources with their respective actions'
* Project Name:composite-service
* File Name:BulkActionsResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: BulkActionsResponse <br/>
* date: 2017年8月27日 下午10:52:19 <br/>
* The response with the request for 'Used to attribute a list of resources with their respective actions'
*
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class BulkActionsResponse {
    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("name")
    private String name;
    
    //resource_actions被修改为result
    @JsonProperty("resource_actions")
    private List<String> result;
}
