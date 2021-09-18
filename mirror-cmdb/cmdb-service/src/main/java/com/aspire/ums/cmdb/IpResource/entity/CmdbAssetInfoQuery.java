package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 资产管理流程-资产查询实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbAssetInfoQuery {
    // 资源列表Id
    private String[] assetIdArr;
    // 管理IP
    private String[] manageIpArr;
    // 所属资源池
    private String idcVal;
    // 主备
    private String hostBack;
    // 是否资源池管理
    private String isPool;
    // 设备分类
    private String deviceClass;
    // 设备类型
    private String deviceType;
    // 机房位置
    private String machineRoom;
    // 独立业务线ID
    private String aloneBusiness;
    // 独立业务线子模块ID
    private String aloneBusiness2;
    // 独立业务线Code
    private String aloneBusinessCode;
    // 独立业务线子模块Code
    private String aloneBusinessCode2;

    // IP类型
    // private String ipType;
    private String[] ipTypeArr;
    // IP
    private String[] ips;

    private int pageNo;
    private int pageSize;

    private String recoveryType;

}
