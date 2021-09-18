package com.aspire.ums.cmdb.ipAudit.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fanwenhui
 * @Description: 存活未规划IP列表响应实体类 （公网IP）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationExpiredPublicIpResp extends IpAuditCommonResp {
    private String networkName; // 网段名称
    private String idcManageFlag; // 是否资源池管理,0:属于资源池管理,1:非资源池管理
    private String circuitType; // 线路类型
    private String addressUse; // 地址用途
    private String businessName1; // 分配一级业务
    private String businessName2; // 分配独立业务
    private String requestPerson; // 申请人
    private String requestTime; // 申请时间
    private String expirationDate; // 有效期
}
