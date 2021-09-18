package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;



/**
* RolePermission类
* Project Name:composite-api
* File Name:RolePermission.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RolePermission <br/>
* date: 2017年9月1日 下午12:38:35 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RolePermission {
    @JsonProperty("resource")
    private List<String> resourceList;

    @JsonProperty("actions")
    private List<String> resTypeActionList;

    @JsonProperty("constraints")
    private List<Map<String, String>> constraints;

    // 只包含在响应消息中
    @JsonProperty("uuid")
    private String uuid;

    // 只包含在响应消息中
    @JsonProperty("role_uuid")
    private String roleUuid;

    //只包含在日志中
    private String name;
}
