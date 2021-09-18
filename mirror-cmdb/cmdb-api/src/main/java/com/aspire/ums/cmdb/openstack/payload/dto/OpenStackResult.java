package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 云管VMware.
 *
 * @author jiangxuwen
 * @date 2020/3/9 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenStackResult<T extends BaseOpenStackDTO> implements Serializable {

    private static final long serialVersionUID = -1680354360176668835L;

    @JsonProperty("code")
    @JSONField(name = "code")
    private Integer code;

    @JsonProperty("data")
    @JSONField(name = "data")
    private OpenStackDataDTO<T> data;

    @JsonProperty("error")
    @JSONField(name = "error")
    private String error;

    @JsonProperty("message")
    @JSONField(name = "message")
    private String message;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
