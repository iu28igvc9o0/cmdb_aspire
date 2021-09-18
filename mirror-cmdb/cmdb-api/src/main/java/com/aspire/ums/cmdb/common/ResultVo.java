package com.aspire.ums.cmdb.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 18:41
 */
@Data
@AllArgsConstructor
public class ResultVo<T> {
    private boolean success;
    private String msg;
    private T data;

    public ResultVo() {
        this.success = true;
        this.msg = "success";
    }
    
    public ResultVo(boolean success) {
        this.success = success;
    }

    public ResultVo(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static <T> ResultVo<T> success() {
        return new ResultVo<T>(true, "操作成功");
    }

    public static <T> ResultVo<T> success(T data) {
        return new ResultVo<T>(true, "操作成功", data);
    }

    public static <T> ResultVo<T> success(String msg, T data) {
        return new ResultVo<T>(true, msg, data);
    }

    public static <T> ResultVo<T> fail() {
        return new ResultVo<T>(false, "操作失败", null);
    }

    public static <T> ResultVo<T> fail(String msg) {
        return new ResultVo<T>(false, msg, null);
    }

    public static <T> ResultVo<T> fail(String msg, T data) {
        return new ResultVo<T>(false, msg, data);
    }

}
