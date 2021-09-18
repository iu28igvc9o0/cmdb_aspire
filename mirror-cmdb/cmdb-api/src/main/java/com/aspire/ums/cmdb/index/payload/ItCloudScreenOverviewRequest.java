package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ItCloudScreenOverviewRequest
 * @Description It租户大屏总览请求类
 * @Author luowenbo
 * @Date 2020/4/16 11:00
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenOverviewRequest {

    /*
    *  租户类型 (内部租户||外部租户)
    * */
    private String depType;

    /*
    *  月报时间
    * */
    private String monthlyTime;

    /*
    *  设备类型
    * */
    private String deviceType;

    /*
     *  moduleFlag :
     *      1:
     *      2:
     *      3:
     * */
    private String moduleFlag;

    /*
    *  资源类型
    * */
    private String moduleType;

    /*
    *  硬件类型（CPU||内存）
    * */
    private String hardwareType;

    /*
    *  计算类型（MAX || AVG）
    * */
    private String calculateType;

    /*
    *  累计考核情况（月报时间数组）
    * */
    private String[] monthArray;

    /*
    *  标识数据计算中不包含“基础平台部”
    * */
    private String excludeDep;

    /*
    *  设备类型列表（不区分物理机虚拟机时，带两者）
    * */
    private List<String> deviceTypeList;
}
