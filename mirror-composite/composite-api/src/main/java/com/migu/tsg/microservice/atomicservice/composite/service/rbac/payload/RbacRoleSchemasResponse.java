package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* RbacRoleSchemasResponse类
* Project Name:composite-api
* File Name:RbacRoleSchemasResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RbacRoleSchemasResponse <br/>
* date: 2017年9月1日 下午12:34:43 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RbacRoleSchemasResponse {

    private String resource;

    private Boolean general;

    /** 
     * 资源名称
     */ 
    private String name;
    
    /** 
     * 父资源
     */ 
    private String parentResource;

    @JsonProperty("actions")
    private Map<String, String> actionsList;

    @JsonProperty("constraints")
    private Map<String, String> constraintMap;

}
