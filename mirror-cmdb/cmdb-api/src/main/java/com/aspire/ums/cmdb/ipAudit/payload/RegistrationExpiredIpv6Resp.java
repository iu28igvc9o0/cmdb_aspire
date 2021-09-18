package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fanwenhui
 * @Description: 登记已过期IP列表响应实体类 （ipv6）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationExpiredIpv6Resp extends IpAuditCommonResp {
    private String networkName; // 网段名称
    private String idcManageFlag; // 是否资源池管理,0:属于资源池管理,1:非资源池管理
    private String networkSegmentType; // 网段类型
    private String networkSegmentSubType; // 网段子类
    private String addressUse; // 地址用途
    private String businessName1; // 分配一级业务
    private String businessName2; // 分配独立业务
    private String cmdbManageFlag; // 是否录入CMDB
    private String businessNameUse; // 使用方独立业务
    private String requestPerson; // 申请人
    private String requestTime; // 申请时间
    private String expirationDate; // 有效期
}
