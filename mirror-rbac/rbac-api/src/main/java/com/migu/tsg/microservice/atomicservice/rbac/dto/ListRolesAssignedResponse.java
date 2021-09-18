package com.migu.tsg.microservice.atomicservice.rbac.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: ListRolesAssignedResponse.java <br>
 * 类描述: 【RBAC原子层】查询已经分配给用户的角色列表的响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日上午10:30:58 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListRolesAssignedResponse {

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
     * 空间名
     */
    private String namespace;

    /**
     * 成员名称
     */
    private String user;

    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;

}
