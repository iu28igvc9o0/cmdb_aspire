package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenBizSystemNotInspect
 * @Description 租户利用率大屏——免考核资源详细信息实体
 * @Author luowenbo
 * @Date 2020/3/22 14:57
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenBizSystemNotInspect {
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
     *  设备类型
     * */
    private String deviceType;
    /*
     *  IP,唯一标识
     * */
    private String ip;
    /*
     *  资源开通时间
     * */
    private String resourceOpenDate;
    /*
     *  开始考核时间
     * */
    private String startAssessDate;
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
