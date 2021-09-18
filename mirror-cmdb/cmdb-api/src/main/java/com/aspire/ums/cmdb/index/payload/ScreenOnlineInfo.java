package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenOnlineInfo
 * @Description 业务系统上线信息实体
 * @Author luowenbo
 * @Date 2020/4/23 17:05
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenOnlineInfo {
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
     *  预计部署上线时间
     * */
    private String targetDeployDate;
    /*
     *  实际部署上线时间
     * */
    private String actualDeployDate;
    /*
     *  预计上线通知时间
     * */
    private String targetInformDate;
    /*
     *  实际上线通知时间
     * */
    private String actualInformDate;
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
