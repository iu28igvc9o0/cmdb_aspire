package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenStoreLowUtilization
 * @Description 存储低利用率实体
 * @Author luowenbo
 * @Date 2020/4/21 16:40
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenStoreLowUtilization {
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
    private String storeType;
    /*
     *  存储利用率
     * */
    private String utilization;
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
}
