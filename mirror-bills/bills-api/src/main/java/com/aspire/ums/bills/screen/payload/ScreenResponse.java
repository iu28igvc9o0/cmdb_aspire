package com.aspire.ums.bills.screen.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @projectName: ScreenResponse
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-05 19:56
 **/
@Data
@AllArgsConstructor
public class ScreenResponse<T> {

    private static final boolean FLAG_TRUE = true;
    private static final boolean FLAG_FALSE = false;
    private static final String SUCCESS = "success";

    private boolean flag;

    private T data;

    private String message;

    public ScreenResponse(boolean flag,T data) {
        this.flag = flag;
        this.data = data;
        this.message = ScreenResponse.SUCCESS;
    }

}
