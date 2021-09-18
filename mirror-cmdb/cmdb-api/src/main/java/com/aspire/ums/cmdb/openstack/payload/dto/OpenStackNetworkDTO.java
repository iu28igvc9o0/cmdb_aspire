package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OPENSTACK_NETWORK subnet
 *
 * @author jiangxuwen
 * @date 2020/11/16 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenStackNetworkDTO extends BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = -4253684407652082547L;

    /**
     *
     */
    @JsonProperty("hname")
    @JSONField(name = "hname")
    private String hname;

    /**
     *
     */
    @JsonProperty("isAdminStateUp")
    @JSONField(name = "isAdminStateUp")
    private String isAdminStateUp;

    /**
     *
     */
    @JsonProperty("isRouterExternal")
    @JSONField(name = "isRouterExternal")
    private String isRouterExternal;

    /**
    *
    */
    @JsonProperty("isShared")
    @JSONField(name = "isShared")
    private String isShared;

    /**
    *
    */
    @JsonProperty("mtu")
    @JSONField(name = "mtu")
    private String mtu;

    /**
    *
    */
    @JsonProperty("providerNetworkType")
    @JSONField(name = "providerNetworkType")
    private String providerNetworkType;

    /**
    *
    */
    @JsonProperty("providerSegmentationId")
    @JSONField(name = "providerSegmentationId")
    private String providerSegmentationId;

    /**
    *
    */
    @JsonProperty("sname")
    @JSONField(name = "sname")
    private String sname;

    /**
    *
    */
    @JsonProperty("status")
    @JSONField(name = "status")
    private String status;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
