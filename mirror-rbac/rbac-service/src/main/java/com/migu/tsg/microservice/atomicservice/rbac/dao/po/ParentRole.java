package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao.po <br>
 * 类名称: ParentRole.java <br>
 * 类描述: 一个角色对应多个父角色的中间表的实体类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月22日下午4:07:42 <br>
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentRole {

    /**
     * 角色UUID
     */
    private String roleUuid;

    /**
     * 父角色UUID
     */
    private String parentUuid;

    /**
     * 祖父角色UUID
     */
    private String grandParentUuid;
    
    /**
     * 曾祖父角色UUID
     */
    private String greatGrandUuid;

}
