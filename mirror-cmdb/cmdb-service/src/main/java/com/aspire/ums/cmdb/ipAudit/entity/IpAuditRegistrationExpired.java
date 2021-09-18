package com.aspire.ums.cmdb.ipAudit.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述： 登记已过期IP
* @author huanggongrui
* @date 2020-05-22 14:33:58
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditRegistrationExpired {

    /**
     * 
     */
    private String id;
    /**
     * 检测需记录的ip
     */
    private String ip;
    /**
     * 检测的ip类型：内网IP，公网IP，IPv6
     */
    private String ipType;
    /**
     * 所属资源池
     */
    private String dc;
    /**
     * 检测时间
     */
    private Date checkTime;
    /**
     * 申请时间
     */
    private Date requestTime;
    /**
     * 使用期限(年)
     */
    private Double usefulLife;
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

    public IpAuditRegistrationExpired(Date checkTime, String isNotify, String processingStatus, Long checkNum, Integer isDelete) {
        this.checkTime = checkTime;
        this.isNotify = isNotify;
        this.processingStatus = processingStatus;
        this.checkNum = checkNum;
        this.isDelete = isDelete;
    }
}