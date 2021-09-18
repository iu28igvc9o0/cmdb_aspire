package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * vmware Instance请求基类.
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:16
 */
@Data
@ToString
public class BaseInstanceRequest <T extends BaseInstanceRequestExtInfo> implements Serializable {
    private static final long serialVersionUID = -1142432642286527669L;

    @JsonProperty("system")
    @JSONField(name = "system")
    private String system;

    @JsonProperty("topic")
    @JSONField(name = "topic")
    private String topic;

    @JsonProperty("data")
    @JSONField(name = "data")
    private InstanceRequestData<T> data;

    @JsonIgnore
    @JSONField(serialize = false)
    private transient String requestBody;
}
