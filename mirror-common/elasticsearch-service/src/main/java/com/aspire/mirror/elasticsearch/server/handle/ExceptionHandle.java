package com.aspire.mirror.elasticsearch.server.handle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aspire.mirror.elasticsearch.api.enums.ResultErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.aspire.mirror.elasticsearch.api.dto.ErrorsResponse;
import com.aspire.mirror.elasticsearch.server.exception.*;

/**
 * 项目名称: ldap-service <br>
 * 包: com.aspire.mirror.elasticsearch.server.handle <br>
 * 类名称: ExceptionHandle.java <br>
 * 类描述: 所有控制层抛出异常处理类 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月24日下午1:44:25<br>
 * 版本: v1.0
 */
@ControllerAdvice
@Controller
public class ExceptionHandle {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

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
     * resultCode 供日志模块使用
     */
    public static final String RESULT_CODE = "resultCode";

    /**
     * fields 错误字段
     */
    private static final String FIELDS = "fields";

    /**
     * 异常处理
     * @param e 异常
     * @param request 请求对象
     * @param response 响应对象
     * @return 错误响应对象
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorsResponse handle(final Exception e, final HttpServletRequest request,
            final HttpServletResponse response) {

        if (e instanceof HttpRequestMethodNotSupportedException) { // 调用不支持接口的异常
            return packageErrorResponse(e, ResultErrorEnum.METHOD_NOT_ALLOWED, request, response);
        } else if (e instanceof MissingServletRequestParameterException) { // 缺失必须的查询参数的异常
            return packageErrorResponse(e, ResultErrorEnum.BAD_REQUEST, request, response);
        } else if (e instanceof BadRequestUrlException) { // 请求URL无效,不能含有任何不可见字符,包括空格、制表符、换页符等等
            return packageErrorResponse(e, ResultErrorEnum.BAD_REQUEST_URL, request, response);
        } else if (e instanceof BadRequestFieldException) {
            return packageErrorResponse(e, ResultErrorEnum.BAD_REQUEST, request, response);
        } else if (e instanceof CustomizedException) {
            return packageErrorResponse(e, ResultErrorEnum.UNKNOWN_ISSUE, request, response);
        } else if (e instanceof HttpMessageNotReadableException) { // Required request body is missing 缺少所需的请求JSON主体
            if (e.getCause() instanceof JsonMappingException) { // JSON映射异常
                return packageErrorResponse(e, ResultErrorEnum.BAD_REQUEST, request, response);
            } else if (e.getCause() instanceof JsonParseException) { // JSON解析异常
                return packageErrorResponse(e, ResultErrorEnum.MALFORMED_REQUEST, request, response);
            } else {
                return packageErrorResponse(e, ResultErrorEnum.UNSUPPORTED_MEDIA_TYPE, request, response);
            }
        } else {
            return packageErrorResponse(e, ResultErrorEnum.UNKNOWN_ISSUE, request, response);
        }
    }

    /**
     * 封装无效字段自定义异常的错误响应
     * @param badRequestFieldException 无效字段异常
     * @param request 请求对象
     * @param response 响应对象
     * @return 错误响应对象
     */
    private ErrorsResponse packageErrorResponse(final BadRequestFieldException badRequestFieldException,
            final HttpServletRequest request, final HttpServletResponse response) {
        Object resultCode = request.getAttribute(RESULT_CODE);
        StringBuffer sb = new StringBuffer();
        if (resultCode != null && !resultCode.toString().isEmpty()) {
            sb.append(resultCode.toString()).append(badRequestFieldException.getType());
        }
        Map<String, Object> error = new HashMap<>();
        error.put(RESULT_CODE, sb.toString());
        error.put(SOURCE, badRequestFieldException.getSource());
        error.put(CODE, badRequestFieldException.getCode());
        error.put(MESSAGE, badRequestFieldException.getMessage());
        List<Map<String, List<String>>> fields = new ArrayList<>();
        Map<String, List<String>> field = new HashMap<>();
        field.put(badRequestFieldException.getFieldName(), badRequestFieldException.getTipInfo());
        fields.add(field);
        error.put(FIELDS, fields);
        response.setStatus(badRequestFieldException.getHttpCode());
        List<Map<String, Object>> errorList = new ArrayList<>();
        errorList.add(error);
        ErrorsResponse errorsResponse = new ErrorsResponse(errorList);
        LOGGER.error(errorsResponse.toString(), badRequestFieldException);
        return errorsResponse;
    }

    /**
     * 封装自定义异常的错误响应
     * @param baseException 自定义异常的父类
     * @param request 请求对象
     * @param response 响应对象
     * @return 错误响应对象
     */
    private ErrorsResponse packageErrorResponse(final BaseException baseException,
            final HttpServletRequest request, final HttpServletResponse response) {
        Object resultCode = request.getAttribute(RESULT_CODE);
        StringBuffer sb = new StringBuffer();
        if (resultCode != null && !resultCode.toString().isEmpty()) {
            sb.append(resultCode.toString()).append(baseException.getType());
        }
        Map<String, Object> error = new HashMap<>();
        error.put(RESULT_CODE, sb.toString());
        error.put(SOURCE, baseException.getSource());
        error.put(CODE, baseException.getCode());
        error.put(MESSAGE, baseException.getMessage());
        response.setStatus(baseException.getHttpCode());
        List<Map<String, Object>> errorList = new ArrayList<>();
        errorList.add(error);
        ErrorsResponse errorsResponse = new ErrorsResponse(errorList);
        LOGGER.error(errorsResponse.toString(), baseException);
        return errorsResponse;
    }

    /**
     * 封装非自定义异常的错误响应
     * @param exception 异常对象
     * @param resultErrorEnum 错误枚举对象
     * @param request 请求对象
     * @param response 响应对象
     * @return 错误响应对象
     */
    private ErrorsResponse packageErrorResponse(final Exception exception,
            final ResultErrorEnum resultErrorEnum, final HttpServletRequest request,
            final HttpServletResponse response) {
        Object resultCode = request.getAttribute(RESULT_CODE);
        StringBuffer sb = new StringBuffer();
        if (resultCode != null && !resultCode.toString().isEmpty()) {
            sb.append(resultCode.toString()).append(resultErrorEnum.getType());
        }
        Map<String, Object> error = new HashMap<>();
        error.put(RESULT_CODE, sb.toString());
        error.put(SOURCE, resultErrorEnum.getSource());
        error.put(CODE, resultErrorEnum.getCode());
        if (ResultErrorEnum.METHOD_NOT_ALLOWED.equals(resultErrorEnum)) {
            error.put(MESSAGE, resultErrorEnum.getMessage().replace("?", request.getMethod()));
        } else {
            error.put(MESSAGE, resultErrorEnum.getMessage());
        }
        response.setStatus(resultErrorEnum.getHttpCode());
        List<Map<String, Object>> errorList = new ArrayList<>();
        errorList.add(error);
        ErrorsResponse errorsResponse = new ErrorsResponse(errorList);
        LOGGER.error(errorsResponse.toString(), exception);
        return errorsResponse;
    }

}
