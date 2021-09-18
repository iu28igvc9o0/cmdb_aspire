package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 冲突IP 记录表
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/23 14:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpClashRecordEntity {
    // 主键ID
    private String id;
    // 关联主表ID
    private String mainId;
    // IP地址
    private String ip;
    // 上次绑定的MAC地址
    private String oldMac;
    // 当前绑定的MAC地址
    private String nowMac;
    // 网关设备IP
    private String gateway;
    // 所属资源池
    private String resource;
    // 是否已通知 0-否,1-是
    private String isNotify;
    // 工单号
    private String jobNumber;
    // 检测时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;
    // 创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    // 修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 统计标识: 0-系统比对,1-复核比对
    private String statisticFlag;
    // IP检测时间戳
    private String timeKey;
}
