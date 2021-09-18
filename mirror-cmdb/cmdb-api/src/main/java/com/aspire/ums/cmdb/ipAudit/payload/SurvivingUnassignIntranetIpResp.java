package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 存活未分配IP列表响应实体类
 * @Date: create in 2020/5/23 15:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurvivingUnassignIntranetIpResp extends IpAuditCommonResp {
    /**
     * VLAN号
     */
    private String vlanNumber;
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
     * 是否资源池管理
     */
    private String isPool;
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
}
