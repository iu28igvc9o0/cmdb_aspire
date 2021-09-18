package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * OPENSTACK_NETWORK subnet
 *
 * @author jiangxuwen
 * @date 2020/11/16 15:50
 */
@Data
public class OpenStackAllocationPoolDTO implements Serializable {

    private static final long serialVersionUID = -4253684407652082547L;

    /**
     * 开始地址
     */
    @JsonProperty("start")
    @JSONField(name = "start")
    private String start;

    /**
     * 结束地址
     */
    @JsonProperty("end")
    @JSONField(name = "end")
    private String end;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
