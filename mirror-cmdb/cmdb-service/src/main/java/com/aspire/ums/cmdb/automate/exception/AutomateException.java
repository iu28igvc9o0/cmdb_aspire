package com.aspire.ums.cmdb.automate.exception;

import com.aspire.ums.cmdb.automate.enums.AutomateResCodeEnum;

/**
 * @author fanwenhui
 * @date 2020-08-21 11:19
 * @description API异常类
 */
public class AutomateException extends RuntimeException {

    private Integer errorCode;
    private String errorMessage;

    public AutomateException(AutomateResCodeEnum responseCodeEnum, String errorMessage) {
        super(String.format("%s : %s", responseCodeEnum.getErrorCode(), errorMessage));
        this.errorCode = responseCodeEnum.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public AutomateException(AutomateResCodeEnum error) {
        this(error, error.getErrorMsg());
    }

    public AutomateException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AutomateException(String message, int errorCode, String errorMessage) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AutomateException(Throwable cause) {
        super(cause);
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
