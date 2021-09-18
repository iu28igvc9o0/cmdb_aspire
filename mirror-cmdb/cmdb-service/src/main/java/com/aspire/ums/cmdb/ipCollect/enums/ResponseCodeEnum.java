package com.aspire.ums.cmdb.ipCollect.enums;

/**
 * 错误（错误码与错误信息）枚举。
 *
 * @author jiangxuwen
 * @date 2019/6/21 16:44
 */
public enum ResponseCodeEnum {

    /** 参数错误. */
    INVALID_PARAM(400, "参数错误！"),
    
    /** 参数错误. */
    INVALID_PARAM_RESOURCE_ID_MANDATORY(401, "参数错误,resourceId不能为空！"),
    
    /** 参数错误. */
    INVALID_PARAM_RESOURCE_ID_LIMIT(402, "参数错误,resourceId最大不能超过500个！"),

    /** 参数错误. */
    TOKEN_MANDATORY(403, "参数错误：缺少token"),

    /** 参数错误. */
    USERNAME_MANDATORY(404, "参数错误：缺少userName"),

    /** 设备IP不存在. */
    DEVICE_NOT_FOUND(405, "设备资源不存在！"),

    /** token已过期. */
    TOKEN_EXPIRED(500, "token已过期"),

    /** token不存在. */
    TOKEN_NOT_EXSITS(501, "token不存在"),

    /** token验签失败. */
    TOKEN_VERIFY_FAILED(503, "token验签失败"),

    /** token验签失败. */
    TOKEN_USER_ID_VERIFY_FAILED(504, "token验签失败,userId验证失败！"),

    /** token验签失败. */
    TOKEN_USER_NAME_VERIFY_FAILED(506, "token验签失败,userName验证失败！"),

    /** 内部错误. */
    INTERNAL_ERROR(505, "服务器内部错误！"),

    RESULT_OK(200, "处理正常"), ;

    private int errorCode;

    private String errorMsg;

    ResponseCodeEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
