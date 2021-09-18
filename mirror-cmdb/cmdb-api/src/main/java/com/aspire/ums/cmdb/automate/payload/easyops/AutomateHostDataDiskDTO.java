package com.aspire.ums.cmdb.automate.payload.easyops;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fanwenhui
 * @date 2020-08-25 10:21
 * @description 主机模型-磁盘信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDataDiskDTO implements Serializable {

    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = 6309425985601936752L;

    @JsonProperty("device")
    @JSONField(name = "device")
    private String device;

    @JsonProperty("fstype")
    @JSONField(name = "fstype")
    private String fstype;

    @JsonProperty("mountpoint")
    @JSONField(name = "mountpoint")
    private String mountpoint;

    @JsonProperty("size")
    @JSONField(name = "size")
    private String size;

    @JsonProperty("provider")
    @JSONField(name = "provider")
    private String provider;
}
