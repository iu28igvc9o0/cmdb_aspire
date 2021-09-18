package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: InsertRolePermissionsDTO.java <br>
 * 类描述: 【RBAC原子层】新增角色单个权限DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月23日下午2:42:44 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertRolePermissionsDTO {

    /**
     * 权限UUID
     */
    private String uuid;

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
