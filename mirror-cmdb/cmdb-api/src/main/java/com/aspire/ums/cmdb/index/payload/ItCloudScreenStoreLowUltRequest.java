package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ItCloudScreenStoreLowUltRequest
 * @Description 云存储低利用率请求类
 * @Author luowenbo
 * @Date 2020/4/24 19:32
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenStoreLowUltRequest {
    /*
     *  系统标题
     * */
    private String systemTitle;
    /*
     *  一级部门
     * */
    private String department1;
    /*
     *  二级部门
     * */
    private String department2;
    /*
     *  月报时间
     * */
    private String monthlyTime;
    /*
     *  存储类型
     * */
    private String storeType;
    /*
     *  业务系统
     * */
    private String bizSystem;
    /*
     *  资源池
     * */
    private String resourcePool;

    /*
     *  POD池
     * */
    private String pod;

    /*
    *  内部租户 || 外部租户
    * */
    private String depType;
}
