package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 关联关系基类.
 *
 * @author jiangxuwen
 * @date 2020/3/13 18:45
 */
@Data
public class BaseInstanceRelationRequestExtInfo extends BaseInstanceRequestExtInfo implements Serializable {

    private static final long serialVersionUID = 6139660319568598258L;

    /** 实例ID. */
    @JsonProperty("dst_instance_id")
    @JSONField(name = "dst_instance_id")
    private String dstInstanceId;

    /** 实例名称. */
    @JsonProperty("dst_instance_name")
    @JSONField(name = "dst_instance_name")
    private String dstInstanceName;

    /** 模型ID. */
    @JsonProperty("dst_object_id")
    @JSONField(name = "dst_object_id")
    private String dstObjectId;

    /** 模型名称. */
    @JsonProperty("dst_object_name")
    @JSONField(name = "dst_object_name")
    private String dstObjectName;

    /** 实例ID. */
    @JsonProperty("instance_version")
    @JSONField(name = "instance_version")
    private String instanceVersion;

    /** 实例ID. */
    @JsonProperty("relation_description")
    @JSONField(name = "relation_description")
    private String relationDescription;

    /** 实例ID. */
    @JsonProperty("relation_id")
    @JSONField(name = "relation_id")
    private String relationId;

    /** 实例ID. */
    @JsonProperty("relation_instance_id")
    @JSONField(name = "relation_instance_id")
    private String relationInstanceId;

    /** 实例ID. */
    @JsonProperty("relation_name")
    @JSONField(name = "relation_name")
    private String relationName;

    /** 实例ID. */
    @JsonProperty("relation_version")
    @JSONField(name = "relation_version")
    private Integer relationVersion;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
