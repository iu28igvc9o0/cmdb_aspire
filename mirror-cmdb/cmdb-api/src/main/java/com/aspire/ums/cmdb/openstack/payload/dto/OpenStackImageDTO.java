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
 * OPENSTACK_IMAGE subnet
 *
 * @author jiangxuwen
 * @date 2020/11/16 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenStackImageDTO extends BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = -7523640690096770720L;

    @JsonProperty("hname")
    @JSONField(name = "hname")
    private String hname;

    @JsonProperty("minDisk")
    @JSONField(name = "minDisk")
    private String minDisk;

    @JsonProperty("minRam")
    @JSONField(name = "minRam")
    private String minRam;

    @JsonProperty("size")
    @JSONField(name = "size")
    private String size;

    @JsonProperty("sname")
    @JSONField(name = "sname")
    private String sname;

    @JsonProperty("status")
    @JSONField(name = "status")
    private String status;

    @JsonProperty("OPENSTACK_ADMIN")
    @JSONField(name = "OPENSTACK_ADMIN")
    private List<OpenStackAdminDTO> openStackAdminList = Lists.newArrayList();

    @JsonProperty("OPENSTACK_SERVERS")
    @JSONField(name = "OPENSTACK_SERVERS")
    private List<OpenStackServerDTO> openStackServerList = Lists.newArrayList();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
