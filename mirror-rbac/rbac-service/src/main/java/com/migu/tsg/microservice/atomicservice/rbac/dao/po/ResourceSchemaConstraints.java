package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.order.dto <br>
 * 类名称: RoleSchemaConstraints.java <br>
 * 类描述: 资源模式-资源约束实体类PO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:06:15<br>
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSchemaConstraints {

    /**
     * 资源类型
     */
    private String resource;

    /**
     * 资源约束Key
     */
    private String constKey;

    /**
     * 资源约束Value
     */
    private String constValue;

}
