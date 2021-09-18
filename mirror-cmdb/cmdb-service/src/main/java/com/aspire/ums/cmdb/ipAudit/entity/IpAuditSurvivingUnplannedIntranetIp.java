package com.aspire.ums.cmdb.ipAudit.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：ip稽核--存活未规划IP（CMDB或者全量存活ip存在但ip地址库不存在为未录入地址库IP）
* @author huanggongrui
* @date 2020-05-15 16:53:39
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IpAuditSurvivingUnplannedIntranetIp {

    /**
     * 
     */
    private String id;
    /**
     * 检测需记录的ip
     */
    private String ip;
    /**
     * 稽核来源是cmdb则为资产ip，稽核来源是存活IP扫描则为网络设备ip
     */
    private String deviceIp;
    /**
     * 所属资源池
     */
    private String dc;
    /**
     * 检测时间
     */
    private Date checkTime;
    /**
     * 稽核来源（0：CMDB、1：存活IP扫描）
     */
    private String auditSource;
    /**
     * 是否已通知 0 未通知 1已通知
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

    public IpAuditSurvivingUnplannedIntranetIp(Date checkTime, String auditSource, String isNotify, String processingStatus, Long checkNum, Integer isDelete) {
        this.checkTime = checkTime;
        this.auditSource = auditSource;
        this.isNotify = isNotify;
        this.processingStatus = processingStatus;
        this.checkNum = checkNum;
        this.isDelete = isDelete;
    }
}