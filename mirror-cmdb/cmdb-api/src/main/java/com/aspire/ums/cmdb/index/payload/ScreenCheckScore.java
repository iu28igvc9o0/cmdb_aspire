package com.aspire.ums.cmdb.index.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ScreenCheckScore
 * @Description 租户利用率考核扣分实体
 * @Author luowenbo
 * @Date 2020/4/9 13:47
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScreenCheckScore {
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
     *  类型
     * */
    private String scoreType;
    /*
     *  标准值
     * */
    private String standardValue;
    /*
     *  评估值
     * */
    private String assessedValue;
    /*
     *  扣分数值
     * */
    private String score;
    /*
     *  评估描述
     * */
    private String description;
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

    public ScreenCheckScore(ItCloudScreenRequest req) {
        this.department1 = req.getDepartment1();
        this.department2 = req.getDepartment2();
        this.bizSystem = req.getBizSystem();
        this.monthlyTime = req.getMonthlyTime();
    }
}
