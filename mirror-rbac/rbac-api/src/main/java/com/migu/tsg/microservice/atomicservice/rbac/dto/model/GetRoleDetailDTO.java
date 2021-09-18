package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: GetRoleDetailDTO.java <br>
 * 类描述: 【RBAC原子层】查询单个角色详细信息的DTO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日下午4:44:17 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetRoleDetailDTO {

    /**
     * 角色UUID
     */
    private String uuid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 空间名
     */
    private String namespace;

    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole;

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

    /**
     * 该角色对应的多个父角色
     */
    private List<ParentRoleDTO> parents;

    /**
     * 权限集合
     */
    private List<PermissionDTO> permissions;

}
