package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: PermissionsDTO.java <br>
 * 类描述: 权限DTO实体类,查询单个角色详细信息时,使用该类封装对应的单个权限信息 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月24日下午2:03:50<br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDTO {

    /**
     * 权限UUID
     */
    private String uuid;

    /**
     * 角色UUID
     */
    @JsonProperty("role_uuid")
    private String roleUuid;

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private List<String> actions;

    /**
     * 资源名称 
     * 例子：["web","web*"]
     */
    private List<String> resource;

    /**
     * 资源字段约束
     * 例子：[{"res:cluster":"test", "res:project":"dev"},{"res:cluster":"dev", "res:project":"dev"}]
     */
    private List<Map<String, String>> constraints;

}
