package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.*;

import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-05-28 17:45
 * @description 存活未分配IPv6 请求入参
 */
@EqualsAndHashCode(callSuper=true)
@Data
public class UnassignIpV6Req extends IpAuditCommonReq{
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
}
