package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenBizSystemDeviceDetail
 * @Description 大屏业务系统具体的设备
 * @Author luowenbo
 * @Date 2020/5/13 13:37
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenBizSystemDeviceDetail {
    private String id;
    /*
    *  设备类型
    * */
    private String deviceType;
    /*
     *  业务系统
     * */
    private String bizSystem;
    /*
     *  IP
     * */
    private String ip;
    /*
     *  资源池
     * */
    private String resourcePool;
    /*
     *  日期展示
     * */
    private String dateDisplay;
    /*
     *  具体日期
     * */
    private String statistDate;
    /*
     *  CPU均峰值
     * */
    private String cpuMax;
    /*
     *  内存均峰值
     * */
    private String memoryMax;
    /*
     *  CPU均值
     * */
    private String cpuAvg;
    /*
     *  内存均值
     * */
    private String memoryAvg;
    /*
     *  新增时间
     * */
    private Date insertTime;

    /*
    *  CI中设备的ID
    * */
    private String instanceId;
}
