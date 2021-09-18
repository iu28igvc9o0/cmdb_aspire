package com.migu.tsg.microservice.atomicservice.composite.controller.logcontext;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 10位日志合成编码最后一位错误码枚举 Project Name:composite-service File
 * Name:LastLogCodeEnum.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.controller.logcontext
 * ClassName: LastLogCodeEnum <br/>
 * date: 2017年12月8日 下午2:20:52 <br/>
 * 
 * @author pengguihua
 * @version
 * @since JDK 1.6
 */
@AllArgsConstructor
public enum LastLogCodeEnum {
    /**
     * 框架异常
     */
    FRAMEWORK_ERROR(1, "servlet or framework error"),
    /**
     * 验证错误
     */
    VALIDATE_ERROR(2, "error with validation"),
    /**
     * 资源没有权限
     */
    PERMISSION_ERROR(3, "error with resource permission"),
    /**
     * 数据库异常
     */
    DAO_ERROR(4, "Dao exception"),
    /**
     * 资源不存在
     */
    RESOURCE_NOT_EXIST(5, "the resource not exist"),
    /**
     * 资源已存在
     */
    RESOURCE_ALREADY_EXIST(6, "the resource is already exist"),
    /**
     * 调用第三方服务异常
     */
    RPC_INVOKE_ERROR(8, "error with third party service invoke"),
    /**
     * 缺省错误
     */
    GENERAL_ERROR(9, "general error");

    @Getter
    private Integer errorCode;
    @Getter
    private String desc;
}
