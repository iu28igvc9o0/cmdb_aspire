package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 其他IP.
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:08
 */
@Data
public class CmdbDeviceAssetIpDTO implements Serializable {

    private static final long serialVersionUID = 6031236842709710924L;

    @JsonProperty("id")
    @JSONField(name = "id")
    private String id;

    @JsonProperty("deviceid")
    @JSONField(name = "deviceid")
    private String deviceId;

    @JsonProperty("ip_address")
    @JSONField(name = "ip_address")
    private String ipAddress;

    @JsonProperty("network_type")
    @JSONField(name = "network_type")
    private String networkType;

}
