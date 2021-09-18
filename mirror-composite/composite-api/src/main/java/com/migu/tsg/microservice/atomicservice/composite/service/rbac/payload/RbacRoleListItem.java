package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* RBAC返回的role data
* Project Name:composite-service
* File Name:RabcRoleListItem.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RabcRoleListItem <br/>
* date: 2017年8月27日 下午12:22:30 <br/>
* RBAC返回的role data
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RbacRoleListItem {
	

    @JsonProperty("namespace")
    private String namespace;
    
    @JsonProperty("uuid")
    private String uuid;
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("admin_role")
    private boolean adminRole;

    @JsonProperty("created_at")
    private String createTime;

    @JsonProperty("updated_at")
    private String updateTime;
    
    @JsonProperty("describe")
    private String describe;

    // 以下字段为  组合加入的字段，不映射到RoleList的响应中
    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("project_uuid")
    private String projectUuid;
    
    @JsonProperty("role_type")
    private Integer roleType;

    @JsonProperty("space_name")
    private String spaceName;

    @JsonProperty("space_uuid")
    private String spaceUuid;

    @JsonProperty("resource_actions")
    private List<String> resTypeActionList;
}
