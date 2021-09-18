package com.migu.tsg.microservice.atomicservice.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.exception <br>
 * 类名称: LdapInterfaceException.java <br>
 * 类描述: 【RBAC原子层】Ldap微服务接口异常 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月24日下午8:30:23 <br>
 * 版本: v1.0
 */
public class LdapInterfaceException extends RuntimeException {

    private static final long serialVersionUID = 6328020012613897428L;

    @Getter
    @Setter
    private int status;

    /**
     * Ldap接口异常构造方法
     * @param status HTTP响应状态
     * @param message 错误信息
     */
    public LdapInterfaceException(int status, String message) {
        super(message);
        this.status = status;
    }

}
