package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: fanwenhui
 * @Description: 登记已过期IP请求实体 （ipv6）
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationExpiredIpv6Req extends IpAuditCommonReq {

    /**
     * ip类型：内网IP，公网IP，IPv6
     */
    private String ipType;
    private String networkSegmentType; // 网段类型
    private String networkSegmentSubType; // 网段子类
    private String idcManageFlag; // 是否资源池管理,0:是，1:否
    private String businessId1; // 分配一级业务线ID
    private String businessName1; // 分配一级业务线名称
    private String businessId2; // 分配独立业务线ID
    private String businessName2; // 分配独立业务线名称
    private String addressUse; // 地址用途
    private String cmdbManageFlag; // 是否录入CMDB
    private String businessNameUse; // 使用方独立业务
    private String requestPerson; // 申请人
    private String requestTimeBegin; // 申请开始时间
    private String requestTimeEnd; // 申请结束时间
    private String expirationDateBegin; // 有效期开始时间
    private String expirationDateEnd; // 有效期结束时间
}
