package com.aspire.ums.bills.price.payload;

/**
 * @author wangyihan
 * @date 2020-08-12 15:32
 */
public class Response<T> {

    private boolean success;

    private String message;

    private String error;

    private T entity;

    public Response() {

    }

    public Response(boolean success) {
        this.success = success;
    }

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, String error) {
        this.success = success;
        this.message = message;
        this.error = error;
    }

    public Response(boolean success, String message, T entity) {
        this.success = success;
        this.message = message;
        this.entity = entity;
    }

    public Response(boolean success, String message, T entity, String error) {
        this.success = success;
        this.message = message;
        this.entity = entity;
        this.error = error;
    }

    public static Response fail(String message) {
        return new Response(false, message);
    }

    public static Response fail(String message, String error) {
        return new Response(false, message, error);
    }

    public static <T> Response<T> success(T entity) {
        return new Response(true, null, entity);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

}
