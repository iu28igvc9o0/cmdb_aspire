package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 云管vmware实例操作data.
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:18
 */
@Data
@ToString
public class InstanceRequestData<T extends BaseInstanceRequestExtInfo> implements Serializable {
    private static final long serialVersionUID = 8693380826142691048L;
    @JsonProperty("event")
    @JSONField(name = "event")
    private String event;

    @JsonProperty("event_id")
    @JSONField(name = "event_id")
    private String eventId;

    /**
     * 操作描述.
     */
    @JsonProperty("memo")
    @JSONField(name = "memo")
    private String optDescription;

    @JsonProperty("operator")
    @JSONField(name = "operator")
    private String operator;

    @JsonProperty("target_category")
    @JSONField(name = "target_category")
    private String targetCategory;

    @JsonProperty("target_id")
    @JSONField(name = "target_id")
    private String targetId;

    @JsonProperty("target_name")
    @JSONField(name = "target_name")
    private String targetName;

    @JsonProperty("ext_info")
    @JSONField(name = "ext_info")
    private T extInfo;
}
