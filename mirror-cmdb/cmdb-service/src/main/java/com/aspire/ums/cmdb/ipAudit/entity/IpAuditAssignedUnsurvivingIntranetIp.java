package com.aspire.ums.cmdb.ipAudit.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述： 已分配未存活IP
* @author huanggongrui
* @date 2020-05-21 20:22:15
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditAssignedUnsurvivingIntranetIp {

    /**
     * 
     */
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
    private Date checkTime;
    /**
     * 最近存活时间
     */
    private Date lastSurviveTime;
    /**
     * 是否已通知:未通知,已通知
     */
    private String isNotify;
    /**
     * 处理状态
     */
    private String processingStatus;
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
    private Date operatingTime;
    /**
     * 工单号
     */
    private String orderNum;
    /**
     * 检测次数
     */
    private Long checkNum;
    /**
     * 删除标志 1 删除
     */
    private Integer isDelete;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updatePerson;

    // =========IP地址库字段============
    private String innerIpId;
    private String innerSegmentId;
    private String innerSegmentType;
    private String innerSegmentSubType;
    private String innerIpUse;
    private String firstBusinessSystem;
    private String aloneBusinessSystem;
    private String assignStatus;
    private String assignTime;
    private String assignUser;
    private String requestPerson;
    private String requestTime;

    public IpAuditAssignedUnsurvivingIntranetIp(Date checkTime, String isNotify, String processingStatus, Long checkNum, Integer isDelete) {
        this.checkTime = checkTime;
        this.isNotify = isNotify;
        this.processingStatus = processingStatus;
        this.checkNum = checkNum;
        this.isDelete = isDelete;
    }
}