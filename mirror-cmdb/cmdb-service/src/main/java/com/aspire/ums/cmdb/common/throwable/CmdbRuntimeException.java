package com.aspire.ums.cmdb.common.throwable;

/**
 * 说明: CMDB通用异常类
 * 工程: CMDB
 * 作者: zhujuwang
 * 时间: 2020/9/3 10:03
 */
public class CmdbRuntimeException extends RuntimeException {

    public CmdbRuntimeException() {
        super();
    }

    public CmdbRuntimeException(String message) {
        super(message);
    }

    public CmdbRuntimeException(String message, Throwable throwable){
        super(message, throwable);
    }

}
