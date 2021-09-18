package com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 资源鉴权过滤请求对象
* Project Name:composite-service
* File Name:AuthFilterRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.rabc.serviceApi.payload
* ClassName: AuthFilterRequest <br/>
* date: 2017年8月27日 下午8:48:57 <br/>
* 资源鉴权过滤请求对象
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
public class CompAuthFilterRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("namespace")
    private String namespace;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("action")
    @JSONField(name = "action")
    private String resTypeAction;

    @JsonProperty("resources")
    private List<RbacResource> resources;

    @JsonProperty("context")
    private Map<String, String> context;
}
