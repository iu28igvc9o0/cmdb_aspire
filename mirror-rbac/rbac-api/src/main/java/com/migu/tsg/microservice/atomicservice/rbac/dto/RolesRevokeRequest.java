package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: RolesAssignedRequest.java <br>
 * 类描述: 【RBAC原子层】撤销一个或多个用户的一个或多个角色的请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日下午5:07:59 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolesRevokeRequest {

    /**
     * 角色UUID
     */
    @JsonProperty("role_uuid")
    private String roleUuid;

    /**
     * 角色名称
     */
    @JsonProperty("role_name")
    private String roleName;

    /**
     * 用户名称
     */
    private String user;

    /**
     * 空间名称
     */
    private String namespace;
}
