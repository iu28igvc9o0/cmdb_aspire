package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.order.dto <br>
 * 类名称: ResourceSchemaDTO.java <br>
 * 类描述: 资源模式(资源类型,资源操作,资源约束)实体类DTO <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午10:06:15<br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResourceSchemaDTO {

    /**
     * 是否为全局资源
     */
    private Boolean general;
    

    /** 
     * 资源名称
     */ 
    private String name;
    
    /** 
     * 父资源
     */ 
    private String parentResource;

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private Map<String, String> actions;

    /**
     * 资源类型
     * 例子：*, alarm, application, role, service
     */
    private String resource;

    /**
     * 资源字段约束
     * 例子：{"res:cluster":"test", "res:project":"dev"}
     */
    private Map<String, String> constraints;
}
