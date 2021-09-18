package com.migu.tsg.microservice.atomicservice.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称: rbac-api <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dto <br>
 * 类名称: AuthVerifyResponse.java <br>
 * 类描述: 权限验证响应对象 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月31日下午1:42:38 <br>
 * 版本: v1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthVerifyResponse {

    /**
     * 验证给定资源名称和资源约束是否可以执行特定资源操作
     */
    private Boolean ok;
}
