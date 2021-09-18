package com.migu.tsg.microservice.atomicservice.composite.exception;

import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;

/**
 * 项目名称: rbac-service 包: com.migu.tsg.microservice.atomicservice.rbac.exception
 * 类名称: BadRequestUrlException.java 类描述: 请求URL无效 创建人: WangSheng 创建时间:
 * 2017年7月28日上午9:09:23 版本: v1.0
 */
public class BadRequestUrlException extends BaseException {
    private static final long serialVersionUID = -4829734597196992060L;

    /**
     * 错误请求url异常构造方法
     * @param resultErrorEnum
     *            异常错误枚举类
     */
    public BadRequestUrlException(final ResultErrorEnum resultErrorEnum) {
        super(LastLogCodeEnum.FRAMEWORK_ERROR, resultErrorEnum);
    }

}
