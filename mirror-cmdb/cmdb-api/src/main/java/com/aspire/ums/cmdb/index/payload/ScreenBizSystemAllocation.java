package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenBisSystemAllocation
 * @Description 租户利用率大屏——资源业务系统分配实体
 * @Author luowenbo
 * @Date 2020/3/22 14:50
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenBizSystemAllocation {
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
     *  资源池
     * */
    private String resourcePool;
    /*
     *  POD池
     * */
    private String pod;
    /*
     *  业务系统
     * */
    private String bizSystem;
    /*
     *  使用量
     * */
    private String useAllocation;
    /*
     *  免考核资源量
     * */
    private String notInspectAllocation;
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
