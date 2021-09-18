package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RolePermissionDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: InsertRoleRequest.java <br>
 * 类描述: 【RBAC原子层】新增多个角色请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日下午3:30:46 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertRoleRequest {

    /**
     * 角色名
     */
    private String name;
    

    private String describe;

    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole = false;
    
    @JsonProperty("role_type")
    private Integer roleType;

    /**
     * 空间名称
     */
    private String namespace;
    
    private List<String> resources;

    /**
     * 父角色集合
     */
    private List<RoleParentsDTO> parents;

    /**
     * 权限集合
     */
    private List<RolePermissionDTO> permissions;
}
