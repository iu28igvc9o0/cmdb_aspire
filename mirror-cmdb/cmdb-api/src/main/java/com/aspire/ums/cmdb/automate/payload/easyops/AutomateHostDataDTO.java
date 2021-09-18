package com.aspire.ums.cmdb.automate.payload.easyops;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-24 16:53
 * @description 主机配置详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AutomateHostDataDTO implements Serializable {

    @JsonIgnore
    @JSONField(serialize = false)
    private static final long serialVersionUID = 7355927994330470022L;

    @JsonProperty("_object_id")
    @JSONField(name = "_object_id")
    private String objectId;

    @JsonProperty("instanceId")
    @JSONField(name = "instanceId")
    private String instanceId;

    @JsonProperty("ip")
    @JSONField(name = "ip")
    private String ip;

    @JsonProperty("hostname")
    @JSONField(name = "hostname")
    private String hostname;

    @JsonProperty("_agentHeartBeat")
    @JSONField(name = "_agentHeartBeat")
    private String agentHeartBeat;

    @JsonProperty("_agentStatus")
    @JSONField(name = "_agentStatus")
    private String agentStatus;

    @JsonProperty("_environment")
    @JSONField(name = "_environment")
    private String environment;

    @JsonProperty("_mac")
    @JSONField(name = "_mac")
    private String mac;

    @JsonProperty("_startU")
    @JSONField(name = "_startU")
    private String startU;

    @JsonProperty("_occupiedU")
    @JSONField(name = "_occupiedU")
    private String occupiedU;

    @JsonProperty("_uuid")
    @JSONField(name = "_uuid")
    private String automateUuid;

    @JsonProperty("agentVersion")
    @JSONField(name = "agentVersion")
    private String agentVersion;

    @JsonProperty("cpuHz")
    @JSONField(name = "cpuHz")
    private String cpuHz;

    @JsonProperty("cpuModel")
    @JSONField(name = "cpuModel")
    private String cpuModel;

    @JsonProperty("cpus")
    @JSONField(name = "cpus")
    private String cpus;

    @JsonProperty("diskSize")
    @JSONField(name = "diskSize")
    private String diskSize;

    @JsonProperty("memSize")
    @JSONField(name = "memSize")
    private String memSize;

    @JsonProperty("memo")
    @JSONField(name = "memo")
    private String memo;

    @JsonProperty("osArchitecture")
    @JSONField(name = "osArchitecture")
    private String osArchitecture;

    @JsonProperty("osDistro")
    @JSONField(name = "osDistro")
    private String osDistro;

    @JsonProperty("osRelease")
    @JSONField(name = "osRelease")
    private String osRelease;

    @JsonProperty("osSystem")
    @JSONField(name = "osSystem")
    private String osSystem;

    @JsonProperty("osVersion")
    @JSONField(name = "osVersion")
    private String osVersion;

    @JsonProperty("provider")
    @JSONField(name = "provider")
    private String provider;

    @JsonProperty("status")
    @JSONField(name = "status")
    private String status;

    @JsonProperty("customer")
    @JSONField(name = "customer")
    private String customer;

    @JsonProperty("isSinglePower")
    @JSONField(name = "isSinglePower")
    private String isSinglePower;

    @JsonProperty("product")
    @JSONField(name = "product")
    private String product;

    @JsonProperty("propertyid")
    @JSONField(name = "propertyid")
    private String propertyid;

    @JsonProperty("use")
    @JSONField(name = "use")
    private String use;

    @JsonProperty("installedPatch")
    @JSONField(name = "installedPatch")
    private String installedPatch;

    @JsonProperty("belongProxy")
    @JSONField(name = "belongProxy")
    private String belongProxy;

    @JsonProperty("isProxy")
    @JSONField(name = "isProxy")
    private String isProxy;

    @JsonProperty("_version")
    @JSONField(name = "_version")
    private String version;

    @JsonProperty("cpu")
    @JSONField(name = "cpu")
    private AutomateHostDataCpuDTO cpu;

    @JsonProperty("disk")
    @JSONField(name = "disk")
    private List<AutomateHostDataDiskDTO> disk;

    @JsonProperty("eth")
    @JSONField(name = "eth")
    private List<AutomateHostDataEthDTO> eth;

    @JsonProperty("service")
    @JSONField(name = "service")
    private List<AutomateHostDataServiceDTO> service;
}
