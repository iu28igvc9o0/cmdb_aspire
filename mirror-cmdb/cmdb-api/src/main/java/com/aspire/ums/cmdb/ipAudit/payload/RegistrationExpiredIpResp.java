package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 存活未规划IP列表响应实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationExpiredIpResp extends IpAuditCommonResp {
    /**
     * 是否资源池管理
     */
    private String mgrByPool;
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
     * 安全域
     */
    private String safeRegion;
    /**
     * 是否录入CMDB
     */
    private String isCmdbManager;
    /**
     * 使用方独立业务
     */
    private String onlineBusiness;
    /**
     * 分配一级业务
     */
    private String firstBusinessSystem;
    /**
     * 分配独立业务
     */
    private String aloneBusinessSystem;
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
     * 有效期
     */
    private String expirationDate;
}
