package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ItCloudScreenBizSystemDeviceDetailRequest
 * @Description 租户业务系统具体设备请求类
 * @Author luowenbo
 * @Date 2020/5/13 14:22
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenBizSystemDeviceDetailRequest {
    /*
    *  设备类型
    * */
    private String deviceType;
    /*
    *  业务系统
    * */
    private String bizSystem;
    /*
    *  月报时间
    * */
    private String monthlyTime;
}
