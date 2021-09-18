package com.migu.tsg.microservice.atomicservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.common.enums <br>
 * 类名称: ResultErrorEnum.java <br>
 * 类描述: 错误码 通常 RESTful 服务是通过 HTTP 的错误状态码来标示错误，
 * 但是状态码无法用来表示具体业务逻辑错误，多种业务逻辑错误通常会对应一样的状态码。
 * 因此，我们在状态码的基础之上针对业务逻辑制定了错误码，来帮助 API的调用者和程序更加方便地定位错误。 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月24日上午11:30:15<br>
 * 版本: v1.0
 */
@AllArgsConstructor
public enum ResultErrorEnum {

    /**
     * 请求参数不正确
     */
    INVALID_ARGS("1088", "invalid_args", 400, "Invalid parameters were passed", "1"),

    /**
     * 请求参数格式不正确(非json或其它规定的格式)
     */
    MALFORMED_REQUEST("1088", "malformed_request", 400, "The requested parameter format is incorrect", "2"),

    /**
     * 请求的数据格式不支持(通常我们只接收json格式的请求数据)
     */
    UNSUPPORTED_MEDIA_TYPE("1088", "unsupported_media_type", 415, "Requested data format is not supported",
            "2"),

    /**
     * 请求数据无效
     */
    BAD_REQUEST("1088", "bad_request", 400, "Required data not valid", "2"),

    /**
     * 请求方法?不被允许,注:?为占位符
     */
    METHOD_NOT_ALLOWED("1088", "method_not_allowed", 405, "Method '?' not allowed", "3"),

    /**
     * 请求地址无效
     */
    BAD_REQUEST_URL("1088", "bad_request_url", 404, "Bad request url", "4"),
    
    /**
     * SQL语法错误
     */
    BAD_SQL_GRAMMAR("1088", "bad_sql_grammar", 500, "Bad SQL grammar", "5"),

    /**
     * 未知错误
     */
    UNKNOWN_ISSUE("1088", "unknown_issue", 500, "Unknown issue", "9"),;

    /**
     * 供内部排查使用
     */
    @Getter
    private String source;

    /**
     * 错误码
     */
    @Getter
    private String code;

    /**
     * HTTP 状态码
     */
    @Getter
    private int httpCode;

    /**
     * 错误描述
     */
    @Getter
    private String message;

    /**
     * 错误类型
     */
    @Getter
    private String type;

}
