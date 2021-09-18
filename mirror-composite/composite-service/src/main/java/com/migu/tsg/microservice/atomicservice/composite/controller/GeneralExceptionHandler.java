package com.migu.tsg.microservice.atomicservice.composite.controller;

import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.APP_GENERAL_LOG_CODE;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.BAD_REQUEST_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.COMPOSITE_BUSINESS_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.FRAMEWORK_LOG_CODE;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.FRAMEWORK_VALIDATION_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.GENERAL_LANG_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.GENERAL_SERVLET_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.GENERAL_SPRING_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.HTTP_MESSAGE_CONVERSION_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.HYSTIRX_BAD_REQUEST_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.HYSTIRX_RUNTIME_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.HYSTIRX_TIMEOUT_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.RESULT_CODE_CONTACT;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.SERVLET_REQUEST_BINDING_EXCEPTION;
import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.SPRING_DAO_EXCEPTION;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedIOException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.migu.tsg.microservice.atomicservice.composite.vo.common.ErrorsResponse;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.ClientServiceHttpStatusErrorException;
import com.migu.tsg.microservice.atomicservice.composite.common.KeyValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeHelper;
import com.migu.tsg.microservice.atomicservice.composite.exception.BadRequestUrlException;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

/**
* 统一异常处理
* Project Name:composite-service
* File Name:GeneralExceptionHandler.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller
* ClassName: GeneralExceptionHandler <br/>
* date: 2017年8月29日 下午7:27:04 <br/>
* 统一异常处理
* @author pengguihua
* @version
* @since JDK 1.6
*/
@ControllerAdvice
public class GeneralExceptionHandler {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /**
     * message 错误描述
     */
    private static final String MESSAGE = "message";

    /**
     * source 供内部排查使用
     */
    private static final String SOURCE = "source";

    /**
     * code 错误码
     */
    private static final String CODE = "code";

    /**
     * 编码解码字符集
     */
    private static final String CHARACTER_ENCODING = "UTF-8";

    /**
     * 请求地址无效返回错误静态页面
     * @param model
     *            封装返回参数对象
     * @param request
     *            请求对象
     * @return 对象
     */
    @ExceptionHandler(value = { BadRequestUrlException.class, NoHandlerFoundException.class })
    public String handleToError(
            final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
        LOGGER.error(BAD_REQUEST_EXCEPTION, logCode, getRequestURI(request));
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute(MESSAGE, logCode);
        return "error";
    }
    
    private String getRequestURI(HttpServletRequest request) {
        String reqUri = null;
        try {
            reqUri = URLDecoder.decode(request.getRequestURI(), CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("The system does't support the encoding: {} ", CHARACTER_ENCODING);
        }
        try {
            reqUri = URLDecoder.decode(request.getRequestURI(), Charset.defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("The system does't support the encoding: {}", Charset.defaultCharset().name());
        }
        reqUri = request.getRequestURI();
        return request.getMethod() + " " + reqUri;
    }
    
	/**
	* 异常处理
	* @param e 异常
	* @param request 请求对象
	* @param response 响应对象
	* @return 对象
	*/
    @ExceptionHandler(value = HystrixRuntimeException.class)
	@ResponseBody
	public Object handle(
			final HystrixRuntimeException e,
			final HttpServletRequest request,
			final HttpServletResponse response) {
        
        String logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.RPC_INVOKE_ERROR);
        LOGGER.error(HYSTIRX_RUNTIME_EXCEPTION, logCode, getRequestURI(request), e);
        
        Throwable cause = e.getCause();
	    if (cause instanceof ClientServiceHttpStatusErrorException) {
	        ClientServiceHttpStatusErrorException ex = (ClientServiceHttpStatusErrorException) cause;
	        response.setStatus(ex.getHttpCode());
	        return ex.getErrorTip();
	    }
	    return packageErrorResponse(ResultErrorEnum.FEIGN_UNKNOW_ERROR, request, response);
	}

    @ExceptionHandler(value = HystrixBadRequestException.class)
    @ResponseBody
    public Object handle(final HystrixBadRequestException e,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        
        String logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.RPC_INVOKE_ERROR);
        LOGGER.error(HYSTIRX_BAD_REQUEST_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(ResultErrorEnum.FEIGN_BAD_REQUEST, request, response);
    }

    @ExceptionHandler(value = HystrixTimeoutException.class)
    @ResponseBody
    public Object handle(final HystrixTimeoutException e,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        
        String logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.RPC_INVOKE_ERROR);
        LOGGER.error(HYSTIRX_TIMEOUT_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(ResultErrorEnum.FEIGN_TIMEOUT_EXCEPTION, request, response);
    }

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public Object handle(final BaseException be,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        String logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.GENERAL_ERROR);
        KeyValue<String, Object[]> logItems = buildBaseExceptionLogItems(be, logCode, getRequestURI(request));
        LOGGER.error(logItems.getKey(), logItems.getValue());
        return packageErrorResponse(be, response);
    }
    
    private KeyValue<String, Object[]> buildBaseExceptionLogItems(BaseException be, String logCode, String reqUri) {
        String topMsg = COMPOSITE_BUSINESS_EXCEPTION;
        topMsg = be.getTopMessage() == null 
                ? topMsg : topMsg + RESULT_CODE_CONTACT +  be.getTopMessage();
        
        Object[] finalParams = null;
        Object[] bizParams = be.getParams() == null ? new Object[0] : be.getParams();
        finalParams = be.getParams() == null ? new Object[3] : new Object[bizParams.length + 3];
        System.arraycopy(bizParams, 0, finalParams, 2, bizParams.length);
        finalParams[0] = logCode;
        finalParams[1] = reqUri;
        finalParams[finalParams.length - 1] = be;
        return new KeyValue<>(topMsg, finalParams);
    }
    
    @ExceptionHandler(value = ServletRequestBindingException.class)
    @ResponseBody
    public ErrorsResponse handle(final ServletRequestBindingException e, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.replaceCurrLogCode(FRAMEWORK_LOG_CODE);
        LOGGER.error(SERVLET_REQUEST_BINDING_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(logCode, ResultErrorEnum.BAD_REQUEST_BINDING, request, response);
    }
    
    @ExceptionHandler(value = HttpMessageConversionException.class)
    @ResponseBody
    public ErrorsResponse handle(final HttpMessageConversionException e, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.replaceCurrLogCode(FRAMEWORK_LOG_CODE);
        LOGGER.error(HTTP_MESSAGE_CONVERSION_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(logCode, ResultErrorEnum.HTTP_MESSAGE_CONVERSION_ERROR, request, response);
    }
    
    @ExceptionHandler(value = ServletException.class)
    @ResponseBody
    public ErrorsResponse handle(final ServletException e, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.replaceCurrLogCode(FRAMEWORK_LOG_CODE);
        LOGGER.error(GENERAL_SERVLET_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(logCode, ResultErrorEnum.GENERAL_SERVLET_EXCEPTION, request, response);
    }
    
    @ExceptionHandler(value = DataAccessException.class)
    @ResponseBody
    public ErrorsResponse handle(final DataAccessException e, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.DAO_ERROR);
        LOGGER.error(SPRING_DAO_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(logCode, ResultErrorEnum.SPRING_DAO_EXCEPTION, request, response);
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorsResponse handle(final MethodArgumentNotValidException validateError, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
        ObjectError errorWrap = validateError.getBindingResult().getAllErrors().get(0);
        LOGGER.error(FRAMEWORK_VALIDATION_EXCEPTION, logCode, getRequestURI(request), errorWrap.toString());
        Map<String, Object> replaceProps = new HashMap<String, Object>();
        replaceProps.put(MESSAGE, errorWrap.getDefaultMessage());
        return packageErrorResponse(logCode, ResultErrorEnum.UNKNOWN_ISSUE, request, response, replaceProps);
    }
    
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public ErrorsResponse handle(final ConstraintViolationException validateError, 
            final HttpServletRequest request, final HttpServletResponse response) {
        String logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
        List<String> violateMsg = new ArrayList<String>();
        for (ConstraintViolation<?> violate : validateError.getConstraintViolations()) {
            violateMsg.add(violate.getMessage());
        }
        String errorTip = StringUtils.join(violateMsg, "|");
        LOGGER.error(FRAMEWORK_VALIDATION_EXCEPTION, logCode, getRequestURI(request), errorTip);
        Map<String, Object> replaceProps = new HashMap<String, Object>();
        replaceProps.put(MESSAGE, errorTip);
        return packageErrorResponse(logCode, ResultErrorEnum.UNKNOWN_ISSUE, request, response, replaceProps);
    }

    /**
     * 异常处理
     * @param e
     *            异常
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @return 对象
     */
    @ExceptionHandler(value = ClientServiceHttpStatusErrorException.class)
    @ResponseBody
    public ErrorsResponse handle(final ClientServiceHttpStatusErrorException e,
                                 final HttpServletRequest request, final HttpServletResponse response) {

        String logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
        String errorTip = e.getErrorTip();
        LOGGER.error(FRAMEWORK_VALIDATION_EXCEPTION, logCode, getRequestURI(request), errorTip);
        Map<String, Object> replaceProps = new HashMap<String, Object>();
        replaceProps.put(MESSAGE, errorTip);
        return packageErrorResponse(logCode, ResultErrorEnum.FEIGN_RETURN_ERROR, request, response, replaceProps);
    }

    /**
     * 异常处理
     * @param e
     *            异常
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @return 对象
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ErrorsResponse handle(final RuntimeException e,
                                 final HttpServletRequest request, final HttpServletResponse response) {

        String logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
        String errorTip = e.getMessage();
        LOGGER.error(FRAMEWORK_VALIDATION_EXCEPTION, logCode, getRequestURI(request), errorTip);
        Map<String, Object> replaceProps = new HashMap<String, Object>();
        replaceProps.put(MESSAGE, errorTip);
        return packageErrorResponse(logCode, ResultErrorEnum.FEIGN_RETURN_ERROR, request, response, replaceProps);
    }
    /**
     * 异常处理
     * @param e
     *            异常
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @return 对象
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorsResponse handle(final Exception e, 
            final HttpServletRequest request, final HttpServletResponse response) {
        
        String logCode = null;
        if (e instanceof NestedRuntimeException
                || e instanceof NestedCheckedException
                || e instanceof NestedIOException) {
            logCode = LogCodeHelper.appendLogCodeIfNotExist(FRAMEWORK_LOG_CODE);
            logCode = LogCodeHelper.appendLastLogCodeIfNotExist(LastLogCodeEnum.FRAMEWORK_ERROR);
            LOGGER.error(GENERAL_SPRING_EXCEPTION, logCode, getRequestURI(request), e);
            return packageErrorResponse(logCode, ResultErrorEnum.BAD_REQUEST, request, response);
        }
        // 通用异常
        logCode = APP_GENERAL_LOG_CODE;
        LOGGER.error(GENERAL_LANG_EXCEPTION, logCode, getRequestURI(request), e);
        return packageErrorResponse(logCode, ResultErrorEnum.UNKNOWN_ISSUE, request, response);
    }

    /**
     * 封装自定义异常的错误响应
     * @param baseException
     *            自定义异常的父类
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @return
     */
    private ErrorsResponse packageErrorResponse(final BaseException baseException,
            final HttpServletResponse response) {
        Map<String, Object> error = new HashMap<>();
        error.put(SOURCE, LogCodeHelper.getCurrentLogCode());
        error.put(CODE, baseException.getCode());
        error.put(MESSAGE, baseException.getMessage());
        response.setStatus(baseException.getHttpCode());
        List<Map<String, Object>> errorList = new ArrayList<>();
        errorList.add(error);
        return new ErrorsResponse(errorList);
    }

    /**
     * 封装非自定义异常的错误响应
     * @param resultErrorEnum
     *            错误枚举对象
     * @param request
     *            请求对象
     * @param response
     *            响应对象
     * @return
     */
    private ErrorsResponse packageErrorResponse(final ResultErrorEnum resultErrorEnum, 
            final HttpServletRequest request, final HttpServletResponse response) {
        return packageErrorResponse(LogCodeHelper.getCurrentLogCode(), resultErrorEnum, request, response);
    }
    
    /**
    * 封装非自定义异常的错误响应.<br/>
    *
    * 作者： pengguihua
    * @param logCode
    *           10位错误记录码
    * @param resultErrorEnum
    *           错误信息枚举对象
    * @param request
    *           请求对象  
    * @param response
    *           响应对象
    * @return
    */
    private ErrorsResponse packageErrorResponse(final String logCode, final ResultErrorEnum resultErrorEnum, 
            final HttpServletRequest request, final HttpServletResponse response) {
        return  packageErrorResponse(logCode, resultErrorEnum, request, response, null);
    }
    
    /**
     * 封装非自定义异常的错误响应.<br/>
     *
     * 作者： pengguihua
     * @param logCode
     *           10位错误记录码
     * @param resultErrorEnum
     *           错误信息枚举对象
     * @param request
     *           请求对象  
     * @param response
     *           响应对象
     * @param replaceProps 
     *           强制指定的属性值
     * @return
     */
    private ErrorsResponse packageErrorResponse(final String logCode, final ResultErrorEnum resultErrorEnum, 
            final HttpServletRequest request, final HttpServletResponse response, Map<String, Object> replaceProps) {
        Map<String, Object> error = new HashMap<>();
        error.put(SOURCE, logCode);
        error.put(CODE, resultErrorEnum.getCode());
        if (ResultErrorEnum.METHOD_NOT_ALLOWED.equals(resultErrorEnum)) {
            error.put(MESSAGE, resultErrorEnum.getMessage().replace("?", request.getMethod()));
        } else {
            error.put(MESSAGE, resultErrorEnum.getMessage());
        }
        response.setStatus(resultErrorEnum.getHttpCode());
        // replace the old properties values with the specified 'replaceProps'
        if (replaceProps != null) {
            error.putAll(replaceProps);
        }
        List<Map<String, Object>> errorList = new ArrayList<>();
        errorList.add(error);
        return new ErrorsResponse(errorList);
    }
}
