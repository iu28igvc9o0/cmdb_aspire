package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-05-28 17:51
 * @description 公网IP 列表返回实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnassignPublicIpResp extends IpAuditCommonResp{
    // 关联公网地址库查询的关联数据
    private String networkName; // 网段名称
    private String idcManageFlag; // 是否资源池管理,0:属于资源池管理,1:非资源池管理
    private String circuitType; // 线路类型
    private String addressUse; // 地址用途
    private String businessName1; // 分配一级业务
    private String businessName2; // 分配独立业务
}
