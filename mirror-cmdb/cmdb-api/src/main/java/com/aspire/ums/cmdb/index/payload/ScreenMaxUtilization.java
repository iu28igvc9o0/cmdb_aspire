package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenMaxUtilization
 * @Description 大屏——资源均峰值实体
 * @Author luowenbo
 * @Date 2020/2/27 21:44
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenMaxUtilization {
    private String id;
    /*
     *  大屏标题ID
     * */
    private String systemTitleId;
    /*
     *  一级部门
     * */
    private String department1;
    /*
     *  二级部门
     * */
    private String department2;
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
    *  设备类型
    * */
    private String deviceType;
    /*
     *  均峰值利用率
     * */
    private String maxUtilization;
    /*
     *  硬件类型
     * */
    private String hardwareType;
    /*
     *  统计日期
     * */
    private String statistDate;
    /*
     *  月报时间 年-月
     * */
    private String monthlyTime;
    /*
     *  新增时间
     * */
    private Date insertTime;
    /*
     *  是否删除
     * */
    private String isDelete;

    public ScreenMaxUtilization(ScreenMaxUtilization obj) {
        this.id = obj.id;
        this.systemTitleId = obj.systemTitleId;
        this.department1 = obj.department1;
        this.department2 = obj.department2;
        this.bizSystem = obj.bizSystem;
        this.resourcePool = obj.resourcePool;
        this.pod = obj.pod;
        this.deviceType = obj.deviceType;
        this.maxUtilization = obj.maxUtilization;
        this.hardwareType = obj.hardwareType;
        this.statistDate = obj.statistDate;
        this.monthlyTime = obj.monthlyTime;
        this.insertTime = obj.insertTime;
        this.isDelete = obj.isDelete;
    }
}
