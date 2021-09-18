package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 物理机、虚拟机资源申请表-（IP列表）
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/16 17:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpAddressEntity implements Serializable {
    private static final long serialVersionUID = -5532730483697872829L;
    // 实例ID
    private String instanceId;
    // 模型ID
    private String moduleId;
    // IP
    private String ip;
    // 地址用途
    private String innerIpUse;
    // 用途子类
    private String innerIpSubUse;
}
