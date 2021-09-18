package com.aspire.mirror.composite.service.cmdb.teamwork.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 18:41
 */
@Data
@AllArgsConstructor
public class ComResultVo<T> {
    private boolean success;
    private String msg;
    private T data;

    public ComResultVo() {
        this.success = true;
        this.msg = "success";
    }
    
    public ComResultVo(boolean success) {
        this.success = success;
    }

    public ComResultVo(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public static <T> ComResultVo<T> success() {
        return new ComResultVo<T>(true, "操作成功");
    }

    public static <T> ComResultVo<T> success(T data) {
        return new ComResultVo<T>(true, "操作成功", data);
    }

    public static <T> ComResultVo<T> success(String msg, T data) {
        return new ComResultVo<T>(true, msg, data);
    }

    public static <T> ComResultVo<T> fail() {
        return new ComResultVo<T>(false, "操作失败", null);
    }

    public static <T> ComResultVo<T> fail(String msg) {
        return new ComResultVo<T>(false, msg, null);
    }

    public static <T> ComResultVo<T> fail(String msg, T data) {
        return new ComResultVo<T>(false, msg, data);
    }

}
