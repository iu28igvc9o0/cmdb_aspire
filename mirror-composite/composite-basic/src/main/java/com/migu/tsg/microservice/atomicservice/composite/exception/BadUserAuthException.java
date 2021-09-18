package com.migu.tsg.microservice.atomicservice.composite.exception;

import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;

/**
 * 调用请求用户认证信息异常
 * Project Name:composite-service2
 * File Name:BadUserAuthException.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.exception
 * ClassName: BadUserAuthException <br/>
 * date: 2017年8月30日 下午4:34:14 <br/>
 * 调用请求用户认证信息异常
 *
 * @author pengguihua
 * @since JDK 1.6
 */
public class BadUserAuthException extends BaseException {
    private static final long serialVersionUID = -6399755987065081663L;

    public BadUserAuthException() {
        super(LastLogCodeEnum.PERMISSION_ERROR, ResultErrorEnum.BIZ_ABSENT_USER_AUTH);
    }

    public BadUserAuthException(String tipMsg) {
        super(tipMsg, LastLogCodeEnum.PERMISSION_ERROR, ResultErrorEnum.BIZ_ABSENT_USER_AUTH);
    }
}
