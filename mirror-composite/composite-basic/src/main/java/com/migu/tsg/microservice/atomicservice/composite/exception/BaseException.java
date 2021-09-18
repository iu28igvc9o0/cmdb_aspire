package com.migu.tsg.microservice.atomicservice.composite.exception;

import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;

import com.migu.tsg.microservice.atomicservice.composite.vo.common.ErrorsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
//import scala.actors.threadpool.Arrays;

/**
 * 项目名称: rbac-service
 * 包: com.migu.tsg.microservice.atomicservice.rbac.exception
 * 类名称: BaseExistException.java
 * 类描述: 基础异常类
 * 创建人: WangSheng
 * 创建时间: 2017年7月26日下午3:33:46
 * 版本: v1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -5408878145521704434L;
    protected LastLogCodeEnum lastCodeEnum;
    protected ResultErrorEnum resultEnum;
    protected String topMessage;
    protected Object[] params;

    public Object[] getParams() {
        Object[] temp = params;
        return temp;
    }

    public void setParams(Object[] params) {


//		Object[] temp = params;

//        this.params = Arrays.copyOf(params, params.length);
        this.params = params.clone();
    }

    public BaseException(LastLogCodeEnum lastCodeEnum, ResultErrorEnum resultEnum) {
        this(null, lastCodeEnum, resultEnum, (Object[]) null);
    }

    public BaseException(LastLogCodeEnum lastCodeEnum, ResultErrorEnum resultEnum, Throwable cause) {
        this(null, lastCodeEnum, resultEnum, cause);
    }

    public BaseException(String topMessage, LastLogCodeEnum lastCodeEnum,
                         ResultErrorEnum resultEnum, Object... params) {
        super(resultEnum.getMessage());
        this.lastCodeEnum = lastCodeEnum;
        this.resultEnum = resultEnum;
        this.topMessage = topMessage;
        this.params = params;
        resolveExecptionCause(params);
    }

    /**
     * 处理参数中的异常链 <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param params
     */
    private void resolveExecptionCause(final Object[] params) {
        if (params == null || params.length == 0) {
            return;
        }
        Object lastParam = params[params.length - 1];
        if (lastParam instanceof Throwable) {
            super.initCause((Throwable) lastParam);
//            this.params = Arrays.copyOf(params, params.length - 1);
            this.params = params.clone();
        }
    }

    public String getCode() {
        return this.resultEnum.getCode();
    }

    public int getHttpCode() {
        return this.resultEnum.getHttpCode();
    }

    public String getTopMessage() {
        return topMessage;
    }

    // 如果子类需要提供JSON格式的错误提示，需要覆盖此方法
    public ErrorsResponse getErrorsResponse() {
        return new ErrorsResponse();
    }
}
