package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* The response of filtering a list of mixed resource types and actions
* Project Name:composite-service
* File Name:AuthActionsBulkResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload.response
* ClassName: AuthActionsBulkResponse <br/>
* date: 2017年8月27日 下午9:32:07 <br/>
* The response of filtering a list of mixed resource types and actions
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompAuthFilterResponse {

    @JsonProperty("resource")
    private RbacResource resource;              // RABC形式资源

    @JsonProperty("actions")
    @JSONField(name = "actions")
    private List<String> resTypeActionList;     // 资源的操作集合，包含资源前缀  如  ["service:create", "service:view"]
}
