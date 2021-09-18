package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OPENSTACK_SUBNET subnet
 *
 * @author jiangxuwen
 * @date 2020/11/16 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenStackSubnetDTO extends BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = -4253684407652082547L;

    /**
     * 云环境
     */
    @JsonProperty("hname")
    @JSONField(name = "hname")
    private String hname;

    /**
     *
     */
    @JsonProperty("cidr")
    @JSONField(name = "cidr")
    private String cidr;

    /**
     *
     */
    @JsonProperty("ipVersion")
    @JSONField(name = "ipVersion")
    private String ipVersion;

    /**
     * 子网名称
     */
    @JsonProperty("sname")
    @JSONField(name = "sname")
    private String sname;

    /**
     * DHCP已启用
     */
    @JsonProperty("isDhcpEnable")
    @JSONField(name = "isDhcpEnable")
    private String dhcpEnable;

    /**
     * 网关IP
     */
    @JsonProperty("gatewayIP")
    @JSONField(name = "gatewayIP")
    private String gatewayIp;

    @JsonProperty("allocationPools")
    @JSONField(name = "allocationPools")
    private List<OpenStackAllocationPoolDTO> allocationPoolList = Lists.newArrayList();

    @JsonProperty("OPENSTACK_ADMIN")
    @JSONField(name = "OPENSTACK_ADMIN")
    private List<OpenStackAdminDTO> openStackAdminList = Lists.newArrayList();

    @JsonProperty("OPENSTACK_SERVERS")
    @JSONField(name = "OPENSTACK_SERVERS")
    private List<OpenStackServerDTO> openStackServerList = Lists.newArrayList();

    @JsonProperty("OPENSTACK_NETWORK")
    @JSONField(name = "OPENSTACK_NETWORK")
    private List<OpenStackNetworkDTO> openStackNetworkList = Lists.newArrayList();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
