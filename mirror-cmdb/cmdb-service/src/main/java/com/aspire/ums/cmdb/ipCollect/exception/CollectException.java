package com.aspire.ums.cmdb.ipCollect.exception;

import com.aspire.ums.cmdb.ipCollect.enums.ResponseCodeEnum;

/**
 * API异常类.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/21 16:04
 */
public class CollectException extends RuntimeException {
    private static final long serialVersionUID = 6849691130782352223L;

    private Integer errorCode;

    private String errorMessage;

    public CollectException(ResponseCodeEnum responseCodeEnum, String errorMessage) {
        super(String.format("%s : %s", responseCodeEnum.getErrorCode(), errorMessage));
        this.errorCode = responseCodeEnum.getErrorCode();
        this.errorMessage = errorMessage;
    }

    public CollectException(ResponseCodeEnum error) {
        this(error, error.getErrorMsg());
    }

// -----------------------------------------------

    public CollectException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CollectException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CollectException(Throwable cause) {
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
