package com.aspire.ums.cmdb.automate.enums;

/**
 * @author fanwenhui
 * @date 2020-08-21 11:22
 * @description 自动化回参
 */
public enum AutomateResCodeEnum {

    INVALID_PARAM(400, "参数错误！"),
    INVALID_PARAM_RESOURCE_ID_MANDATORY(401, "参数错误,resourceId不能为空！"),
    INVALID_PARAM_RESOURCE_ID_LIMIT(402, "参数错误,resourceId最大不能超过500个！"),
    TOKEN_MANDATORY(403, "参数错误：缺少token"),
    USERNAME_MANDATORY(404, "参数错误：缺少userName"),
    DEVICE_NOT_FOUND(405, "设备资源不存在！"),
    TOKEN_EXPIRED(500, "token已过期"),
    TOKEN_NOT_EXSITS(501, "token不存在"),
    TOKEN_VERIFY_FAILED(503, "token验签失败"),
    TOKEN_USER_ID_VERIFY_FAILED(504, "token验签失败,userId验证失败！"),
    TOKEN_USER_NAME_VERIFY_FAILED(506, "token验签失败,userName验证失败！"),
    INTERNAL_ERROR(505, "服务器内部错误！"),
    RESULT_OK(200, "处理正常"), ;

    private int errorCode;

    private String errorMsg;

    AutomateResCodeEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
