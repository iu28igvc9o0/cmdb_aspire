package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: IP稽核列表数据通用实体类
 * @Date: create in 2020/5/23 15:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditCommonResp {

    private String id;
    /**
     * 检测需记录的ip
     */
    private String ip;
    /**
     * 所属资源池
     */
    private String dc;
    /**
     * 检测时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String checkTime;
    /**
     * 网关设备IP/资产管理IP
     */
    private String deviceIp;
    /**
     * 是否已通知:未通知,已通知
     */
    private String isNotify;
    /**
     * 处理状态
     */
    private String processingStatus;
    private String processingStatusDesc;
    /**
     * 原因说明
     */
    private String reason;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String operatingTime;
    /**
     * 工单号
     */
    private String orderNum;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updateTime;
    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 不同Ip地址库的主键
     */
    private String ipId;

    /**
     * IP地址库对应的网段Id主键
     */
    private String segmentId;
}
