package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OPENSTACK_ADMIN subnet
 *
 * @author jiangxuwen
 * @date 2020/11/16 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenStackAdminDTO extends BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = -7523640690096770720L;

    /**
     * 默认可用IP
     */
    @JsonProperty("_orderIP_controller")
    @JSONField(name = "_orderIP_controller")
    private String orderIpController;

    /**
     * 所属ESXI宿主机
     */
    @JsonProperty("controller")
    @JSONField(name = "controller")
    private String controller;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
