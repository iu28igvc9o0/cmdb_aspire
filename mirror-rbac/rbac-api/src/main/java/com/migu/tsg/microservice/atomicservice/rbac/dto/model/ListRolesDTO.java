package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: ListRolesDTO.java <br>
 * 类描述: 【RBAC原子层】查询对于给定的UUID的角色列表响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日下午1:54:55 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListRolesDTO {

    /**
     * 角色UUID
     */
    private String uuid;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole = false;

    /**
     * 空间名称
     */
    private String namespace;

    /**
     * 创建时间
     */
    @JsonProperty("created_at")
    private String createdAt;

    /**
     * 更新时间
     */
    @JsonProperty("updated_at")
    private String updatedAt;
    
    @JsonProperty("role_type")
    private Integer roleType;
    
    
    @JsonProperty("describe")
    private String describe;

}
