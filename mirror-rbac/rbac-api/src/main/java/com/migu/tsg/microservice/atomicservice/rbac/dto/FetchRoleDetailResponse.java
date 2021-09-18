package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.PermissionDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.RoleParentDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.order.dto <br>
 * 类名称: FetchRoleDetailResponse.java <br>
 * 类描述: 查询单个角色详细信息的响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:06:15<br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchRoleDetailResponse {

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

    /**
     * 该角色对应的多个父角色
     */
    private List<RoleParentDTO> parents;

    /**
     * 权限集合
     */
    private List<PermissionDTO> permissons;

    /**
     * 项目UUID
     */
    @JsonProperty("project_uuid")
    private String projectUuid = "";

    /**
     * 项目名称
     */
    @JsonProperty("project_name")
    private String projectName = "";

    /**
     * 资源空间名称
     */
    @JsonProperty("space_name")
    private String spaceName = "";

    /**
     * 资源空间UUID
     */
    @JsonProperty("space_uuid")
    private String spaceUuid = "";

    /**
     * 角色对应的资源操作,Jakiro层封装此字段,暂时保留
     * 例子：["role:assign","role:create","role:delete","role:revoke","role:update","role:view"]
     */
    @JsonProperty("resource_actions")
    private List<String> resourceActions;

}
