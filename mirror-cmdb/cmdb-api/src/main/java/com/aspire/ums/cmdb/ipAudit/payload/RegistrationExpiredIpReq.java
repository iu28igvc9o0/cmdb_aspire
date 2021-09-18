package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: 登记已过期IP请求实体
 * @Date: create in 2020/5/23 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationExpiredIpReq extends IpAuditCommonReq {

    /**
     * ip类型：内网IP，公网IP，IPv6
     */
    private String ipType;
    /**
     * 是否资源池管理:是,否
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
     * 是否录入CMDB：是,否
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String requestTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String requestTimeEnd;
    /**
     * 有效期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String expirationDateBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String expirationDateEnd;
}
