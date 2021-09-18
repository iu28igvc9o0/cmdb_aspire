package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * extInfo基类.
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:11
 */
@Data
@ToString
public class BaseInstanceRequestExtInfo implements Serializable {
    private static final long serialVersionUID = 544613442061851352L;
    @JsonProperty("_pre_ts")
    @JSONField(name = "_pre_ts")
    private Long preTimestamp;

    @JsonProperty("_ts")
    @JSONField(name = "_ts")
    private Long timestamp;

    @JsonProperty("_version")
    @JSONField(name = "_version")
    private Integer version;

    @JsonProperty("instance_id")
    @JSONField(name = "instance_id")
    private String instanceId;

    @JsonProperty("instance_name")
    @JSONField(name = "instance_name")
    private String instanceName;

    @JsonProperty("object_id")
    @JSONField(name = "object_id")
    private String objectId;

    @JsonProperty("object_name")
    @JSONField(name = "object_name")
    private String objectName;

    @JsonProperty("object_version")
    @JSONField(name = "object_version")
    private Integer objectVersion;
}
