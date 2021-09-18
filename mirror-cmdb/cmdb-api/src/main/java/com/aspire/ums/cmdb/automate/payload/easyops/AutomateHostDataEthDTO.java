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
 * @date 2020-08-25 10:24
 * @description 主机模型-网卡信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDataEthDTO implements Serializable {

    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = 364325148346718116L;

    @JsonProperty("name")
    @JSONField(name = "name")
    private String name;

    @JsonProperty("status")
    @JSONField(name = "status")
    private String status;

    @JsonProperty("ip")
    @JSONField(name = "ip")
    private String ip;

    @JsonProperty("mask")
    @JSONField(name = "mask")
    private String mask;

    @JsonProperty("speed")
    @JSONField(name = "speed")
    private String speed;

    @JsonProperty("MAC")
    @JSONField(name = "MAC")
    private String MAC;

    @JsonProperty("broadcast")
    @JSONField(name = "broadcast")
    private String broadcast;

}
