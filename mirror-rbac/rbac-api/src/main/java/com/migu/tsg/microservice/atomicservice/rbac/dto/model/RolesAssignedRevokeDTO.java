package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: RolesAssignedRevokeDTO.java <br>
 * 类描述: 【RBAC原子层】分配一个或多个角色给一个或者多个用户和撤销一个或多个用户的一个或多个角色的DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日下午7:58:15 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RolesAssignedRevokeDTO {

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
