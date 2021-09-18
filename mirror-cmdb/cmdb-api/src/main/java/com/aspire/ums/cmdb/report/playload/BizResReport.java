package com.aspire.ums.cmdb.report.playload;

import lombok.Data;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.report.playload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/6/18 10:05
 * 版本: v1.0
 */
@Data
public class BizResReport {
    private String bizSystem;
    private String department1;
    private String department2;
    private String idcType;
    private String podName;
    private String deviceClass;
    private String deviceType;
    private String totalPlannedApplication;//计划申请总量
    private String totalAllocatedEquipment;//已分配设备总量
    private String deliveryCycle;//交付周期
    private String deliveryRatio;//交付比例
    private String vcpu;//虚拟机核数
    private String totalMemory;//总内存
    private String createTime;
    private String allocatedPhysical;//已分配物理计算资源
    private String allocatedVM;//已分配虚拟计算资源
}
