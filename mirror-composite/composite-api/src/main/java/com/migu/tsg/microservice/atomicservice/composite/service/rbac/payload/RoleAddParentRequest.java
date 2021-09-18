package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* RoleAddParentRequest类
* Project Name:composite-api
* File Name:RoleAddParentRequest.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RoleAddParentRequest <br/>
* date: 2017年9月1日 下午12:36:03 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
public class RoleAddParentRequest {
    @JsonProperty("uuid")
    private String uuid;       // 父角色uuid

    @JsonProperty("name")
    private String roleName;   // 父角色name
}
