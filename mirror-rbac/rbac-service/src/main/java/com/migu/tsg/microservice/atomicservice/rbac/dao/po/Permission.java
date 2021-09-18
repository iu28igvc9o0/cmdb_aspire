package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao.po <br>
 * 类名称: Permissions.java <br>
 * 类描述: 权限实体类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月24日下午4:06:55 <br>
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    /**
     * 权限UUID
     */
    private String uuid;

    /**
     * 角色UUID
     */
    private String roleUuid;

    /**
     * 资源操作 例子：["service:*","application:*","cluster:*","build_config:*"]
     */
    private String actions;

    /**
     * 资源类型 例子：["web","web*"]
     */
    private String resources;

    /**
     * 资源字段约束 例子：[{"res:cluster":"test"},{"res:cluster":"dev"}]
     */
    private String constraints;

}
