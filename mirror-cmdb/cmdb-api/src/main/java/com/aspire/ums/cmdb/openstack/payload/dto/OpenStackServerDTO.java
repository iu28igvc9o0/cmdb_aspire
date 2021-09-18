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
public class OpenStackServerDTO extends BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = -7523640690096770720L;

    @JsonProperty("description")
    @JSONField(name = "description")
    private String description;

    @JsonProperty("hname")
    @JSONField(name = "hname")
    private String hname;

    @JsonProperty("launchedAt")
    @JSONField(name = "launchedAt")
    private String launchedAt;

    @JsonProperty("powerState")
    @JSONField(name = "powerState")
    private String powerState;

    @JsonProperty("rootDeviceName")
    @JSONField(name = "rootDeviceName")
    private String rootDeviceName;

    @JsonProperty("sname")
    @JSONField(name = "sname")
    private String sname;

    @JsonProperty("status")
    @JSONField(name = "status")
    private String status;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
