package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.order.dto <br>
 * 类名称: RoleSchemaActions.java <br>
 * 类描述: 资源模式-资源操作实体类PO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:06:15<br>
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceSchemaActions {

    /**
     * 资源类型
     */
    private String resource;

    /**
     * 资源操作
     */
    private String action;
    

    /** 
     * 操作名称
     */ 
    private String actionName;

    /** 
     * 操作名称
     */ 
    private String actionType;

}
