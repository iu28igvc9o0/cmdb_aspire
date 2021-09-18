package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 已分配未存活IP列表响应实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedUnsurvivingIntranetIpResp extends IpAuditCommonResp {
    /**
     * 网段类型
     */
    private String networkSegmentType;
    /**
     * 网段子类
     */
    private String networkSegmentSubType;
    /**
     * 地址用途
     */
    private String ipUse;
    /**
     * 分配一级业务
     */
    private String firstBusinessSystem;
    /**
     * 分配独立业务
     */
    private String aloneBusinessSystem;
    /**
     * 分配状态
     */
    private String assignStatus;
    /**
     * 分配人
     */
    private String assignUser;
    /**
     * 分配时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String assignTime;
    /**
     * 申请人
     */
    private String requestPerson;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String requestTime;
    /**
     * 存活状态
     */
    private String surviveStatus;
    /**
     * 最近发现时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String lastSurviveTime;
    /**
     * 未存活时长(小时)
     */
    private String unsurvivedDuration;
}
