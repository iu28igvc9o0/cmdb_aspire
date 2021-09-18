package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: InsertRoleDTO.java <br>
 * 类描述: 【RBAC原子层】新增多个角色DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月22日上午10:51:39 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertRoleDTO {

    /**
     * 角色UUID
     */
    private String uuid;

    /**
     * 角色名
     */
    private String name;

    /**
     * 空间名称
     */
    private String namespace;
    
    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole = false;
    
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
    
    /**
     * 分配时间
     */
    @JsonProperty("assigned_at")
    private String assignedAt;
    

    @JsonProperty("role_type")
    private Integer roleType;
    
    
    @JsonProperty("describe")
    private String describe;


    @JsonProperty("resources")
    private List<String> resources;

    /**
     * 父角色集合
     */
    private List<RoleParentsDTO> parents;

    /**
     * 权限集合
     */
    private List<PermissionDTO> permissions;
}
