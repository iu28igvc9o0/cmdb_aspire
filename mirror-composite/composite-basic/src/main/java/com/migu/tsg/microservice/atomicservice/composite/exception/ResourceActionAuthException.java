package com.migu.tsg.microservice.atomicservice.composite.exception;

import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;

/**
* User does'nt have the permission
* Project Name:composite-service2
* File Name:BadUserAuthException.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.exception
* ClassName: BadUserAuthException <br/>
* date: 2017年8月30日 下午4:34:14 <br/>
* User does'nt have the permission
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public class ResourceActionAuthException extends BaseException {
        
    private static final long serialVersionUID = 629856949135073184L;

    public ResourceActionAuthException() {
        super(LastLogCodeEnum.PERMISSION_ERROR, ResultErrorEnum.BIZ_RESOURCE_ACTION_DENY);
    }
    
    public ResourceActionAuthException(String tipMsg) {
        super(tipMsg, LastLogCodeEnum.PERMISSION_ERROR, ResultErrorEnum.BIZ_RESOURCE_ACTION_DENY);
    }
}
