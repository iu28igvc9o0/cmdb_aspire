package com.migu.tsg.microservice.atomicservice.rbac.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: AuthFilterMixedResponse.java <br>
 * 类描述: 批量添加资源操作的资源列表的请求对象 <br>
 * 创建人: jiagaolei <br>
 * 创建时间: 2017年8月30日下午4:18:41 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthFilterMixedResponse {
    /**
     * 单个资源对象
     * 例子：{"uuid":"4e00bc66-7ef1-4382-8fb1-6341c1e913d5", "name":"test", "res:cluster":"dev", "res:project":"dev"}
     */
    private Map<String, String> resource;

    /**
     * 资源操作
     * 例子：["service:*","service:create","service:update","service:view"]
     */
    private List<String> actions;
}
