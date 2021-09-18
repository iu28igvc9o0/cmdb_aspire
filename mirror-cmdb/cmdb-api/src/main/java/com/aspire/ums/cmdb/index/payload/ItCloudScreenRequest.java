package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ItScreenRequest
 * @Description It租户大屏的请求类
 * @Author luowenbo
 * @Date 2020/2/26 21:49
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItCloudScreenRequest {
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
     *  设备类型
     * */
    private String deviceType;
    /*
    *  业务系统
    * */
    private String bizSystem;
    /*
    *  硬件类型
    * */
    private String hardwareType;
    /*
    *  moduleFlag :
    *       1: CPU 和 内存 都低于30%
    *       2: CPU低于30%
    *       3: 内存低于30%
    * */
    private String moduleFlag;

    /*
    *  表名称 :  max || avg
    *       max: screen_max_utilization
    *       avg: screen_avg_utilization
    * */
    private String tableType;

    /*
    *  资源池
    * */
    private String resourcePool;

    /*
    *  POD池
    * */
    private String pod;

    /*
    *  IP
    * */
    private String ip;

    /*
    * 扣分项类型
    * */
    private String scoreType;

    /*
    *  分页
    * */
    private Integer pageNo;
    private Integer pageSize;

    /*
    *  月份累计
    * */
    private String[] monthArray;
}
