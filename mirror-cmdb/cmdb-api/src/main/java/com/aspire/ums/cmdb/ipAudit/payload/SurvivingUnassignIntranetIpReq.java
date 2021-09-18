package com.aspire.ums.cmdb.ipAudit.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: huanggongrui
 * @Description: 存活未分配IP列表请求实体类
 * @Date: create in 2020/5/23 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurvivingUnassignIntranetIpReq extends IpAuditCommonReq {
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
     * 是否录入CMDB:是,否
     */
    private String isCmdbManager;
    /**
     * 使用方独立业务
     */
    private String onlineBusiness;
    /**
     * 分配一级业务
     */
    private List<String> firstBusinessSystem;
    /**
     * 分配独立业务
     */
    private List<String> aloneBusinessSystem;
}
