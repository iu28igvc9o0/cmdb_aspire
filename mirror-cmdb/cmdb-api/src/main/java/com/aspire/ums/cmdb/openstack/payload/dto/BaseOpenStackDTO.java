package com.aspire.ums.cmdb.openstack.payload.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * vmware基类.
 *
 * @author jiangxuwen
 * @date 2020/3/9 16:00
 */
@Data
public class BaseOpenStackDTO implements Serializable {

    private static final long serialVersionUID = 1607846277568391261L;

    @JsonProperty("id")
    @JSONField(name = "id")
    private String id;

    /** 唯一ID. */
    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    @JsonProperty("_object_id")
    @JSONField(name = "_object_id")
    private String objectId;

    @JsonProperty("instanceId")
    @JSONField(name = "instanceId")
    private String instanceId;

    @JsonProperty("org")
    @JSONField(name = "org")
    private Long org;

    @JsonProperty("_object_version")
    @JSONField(name = "_object_version")
    private Integer objectVersion;

    @JsonProperty("_ts")
    @JSONField(name = "_ts")
    private Long timestamp;

    @JsonProperty("_version")
    @JSONField(name = "_version")
    private Integer version;

    @JsonProperty("creator")
    @JSONField(name = "creator")
    private String creator;

    @JsonProperty("ctime")
    @JSONField(name = "ctime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    @JsonProperty("mtime")
    @JSONField(name = "mtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @JsonProperty("modifier")
    @JSONField(name = "modifier")
    private String modifier;

    @JsonIgnore
    @JSONField(serialize = false)
    private Date updateTime;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
