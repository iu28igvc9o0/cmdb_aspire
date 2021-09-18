package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model <br>
 * 类名称: AuthResourceActionDTO.java <br>
 * 类描述: 过滤资源列表DTO对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月31日下午4:47:00 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResourceActionDTO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 2005513436583419525L;

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private List<String> actions;

    /**
     * 单个资源对象
     * 例子：{"uuid":"4e00bc66-7ef1-4382-8fb1-6341c1e913d5", "name":"test", "res:cluster":"dev", "res:project":"dev"}
     */
    private Map<String, String> resource;

}
