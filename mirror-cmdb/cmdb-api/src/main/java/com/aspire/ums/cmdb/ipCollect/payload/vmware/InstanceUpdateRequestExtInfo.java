package com.aspire.ums.cmdb.ipCollect.payload.vmware;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 云管更新实例.
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 15:08
 */
@Data
@ToString
public class InstanceUpdateRequestExtInfo extends BaseInstanceRequestExtInfo implements Serializable {
    private static final long serialVersionUID = 8777257250351624981L;

    @JsonProperty("_change_fields")
    @JSONField(name = "_change_fields")
    private List<String> changeFields;

    @JsonProperty("diff_data")
    @JSONField(name = "diff_data")
    private LinkedHashMap<String, Object> changeData;
}
