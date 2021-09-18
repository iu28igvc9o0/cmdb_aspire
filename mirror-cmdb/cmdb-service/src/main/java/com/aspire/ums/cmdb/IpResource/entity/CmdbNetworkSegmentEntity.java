package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 物理机、虚拟机资源申请表-（网段列表）
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 16:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbNetworkSegmentEntity implements Serializable {
    private static final long serialVersionUID = -5711173126697704535L;
    // 实例ID
    private String instanceId;
    // 模型ID
    private String moduleId;
    // 网段地址
    private String segmentAddress;
    // VLAN号
    private String vlanNumber;
    // 未分配IP数
    private Integer freeIpCount;
    // 所属一级业务线
    private String firstBusinessSystem;
    // 分配独立业务
    private String aloneBusinessSystem;
    // 资源池
    private String idcVal;
    // 是否资源池管理
    private String isPool;
    // 是否VC纳管
    private String isVc;
    // 网段类型
    private String innerSegmentType;
    // 网段子类
    private String innerSegmentSubType;
}
