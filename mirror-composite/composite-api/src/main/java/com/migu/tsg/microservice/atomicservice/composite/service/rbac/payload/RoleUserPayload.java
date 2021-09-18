package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* 角色添加用户请求实体
* Project Name:composite-api
* File Name:RoleUserPayload.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: CreateRoleUserPayload <br/>
* date: 2017年8月29日 下午11:57:54 <br/>
* 角色添加用户请求实体
* @author pengguihua
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@ApiModel
@JsonInclude(Include.NON_NULL)
public class RoleUserPayload {
    @ApiModelProperty(hidden = true)
    @JsonProperty("role_uuid")
    private String roleUuid;
    
    @ApiModelProperty(hidden = true)
    @JsonProperty("role_name")
    private String roleName;
    
    @ApiModelProperty(hidden = true)
    @JsonProperty("namespace")
    private String namespace;

    @ApiModelProperty("username")
    @JsonProperty("user")
    private String user;
    
    @ApiModelProperty(hidden = true)
    @JsonProperty("assigned_at")
    private String assignedTime;
}
