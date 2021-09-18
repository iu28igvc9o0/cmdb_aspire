package com.aspire.ums.cmdb.ipAudit.payload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-05-28 17:51
 * @description 存活未分配IP 列表返回实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnassignIpV6Resp extends IpAuditCommonResp{
    // 关联ipv6地址库查询数据，cmdb_ip_repository_ipv6_segment
    private String networkName; // 网段名称
    private String idcManageFlag; // 是否资源池管理,0:属于资源池管理,1:非资源池管理
    private String networkSegmentType; // 网段类型
    private String networkSegmentSubType; // 网段子类
    private String addressUse; // 地址用途
    private String businessName1; // 分配一级业务
    private String businessName2; // 分配独立业务
    private String cmdbManageFlag; // 是否录入CMDB
    private String businessNameUse; // 使用方独立业务
}
