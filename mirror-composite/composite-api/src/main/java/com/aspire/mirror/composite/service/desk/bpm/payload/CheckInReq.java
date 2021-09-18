package com.aspire.mirror.composite.service.desk.bpm.payload;

import lombok.Data;

import java.util.Date;

/**
 * @author menglinjie
 */
@Data
public class CheckInReq {
    /**
     * 账户
     */
    private String account;
    /**
     * 签到类型 1签到 2签退
     */
    private Integer checkType;
    /**
     * 签到时间
     */
    private String checkTime;

    /**
     * 签到日期
     */
    private Date checkDate;
    /**
     * 签到方式
     */
    private Integer checkWay;
}
