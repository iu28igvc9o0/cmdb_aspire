package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: ModifyRoleRequest.java <br>
 * 类描述: 【RBAC原子层】修改单个角色请求对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日下午5:34:36 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifyRoleRequest {

    /**
     * 是否为管理员角色
     */
    @JsonProperty("admin_role")
    private Boolean adminRole = false;

    @JsonProperty("name")
    private String name;
    
    
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
