package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-05-28 17:45
 * @description 公网IP 请求入参
 */
@EqualsAndHashCode(callSuper=true)
@Data
public class UnassignPublicIpReq extends IpAuditCommonReq{
    private String idcManageFlag; // 是否资源池管理,0:是，1:否
    private String circuitType; // 线路类型
    private String businessId1; // 分配一级业务线ID
    private String businessName1; // 分配一级业务线名称
    private String businessId2; // 分配独立业务线ID
    private String businessName2; // 分配独立业务线名称
    private String networkName; // 网段名称
    private String addressUse; // 地址用途
}
