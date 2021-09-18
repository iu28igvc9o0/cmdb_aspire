package com.aspire.ums.cmdb.openstack.payload.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 描述：
 * 
 * @author
 * @date 2020-11-13 17:00:59
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CmdbOpenstackSubnet extends BaseOpenStackEntity implements Serializable {

    private static final long serialVersionUID = -1799494283223373629L;

    /**
     * 子网ID
     */
    private String subnetId;

    /**
     * 云环境
     */
    private String hname;

    /**
     * IP版本
     */
    private String ipVersion;

    /**
     * 对应IP地址库的网段地址
     */
    private String cidr;

    /**
     * 
     */
    private String org;

    /**
     * 
     */
    private String sname;

    /**
     * DHCP已启用
     */
    private String dhcpEnable;

    /**
     * 网关IP
     */
    private String gatewayIp;

}
