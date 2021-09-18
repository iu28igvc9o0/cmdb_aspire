package com.aspire.ums.cmdb.automate.payload;

import com.aspire.ums.cmdb.automate.payload.base.BaseAutomateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author fanwenhui
 * @date 2020-08-24 11:19
 * @description 自动化主机模型字段
 */
@Data
@ToString(callSuper = true)
@ApiModel(value = "主机模型实体")
public class AutomateHostEntity extends BaseAutomateEntity implements Serializable {

    private static final long serialVersionUID = 8283756973017655496L;

    @ApiModelProperty(name = "ip", notes = "设备IP")
    private String ip;
    @ApiModelProperty(name = "hostname", notes = "主机名称")
    private String hostname;
    @ApiModelProperty(name = "agentHeartBeat", notes = "agent心跳")
    private String agentHeartBeat;
    @ApiModelProperty(name = "agentStatus", notes = "agent状态")
    private String agentStatus;
    @ApiModelProperty(name = "environment", notes = "主机环境")
    private String environment;
    @ApiModelProperty(name = "mac", notes = "物理地址")
    private String mac;
    @ApiModelProperty(name = "occupiedU", notes = "占用U数")
    private String occupiedU;
    @ApiModelProperty(name = "startU", notes = "起始U位")
    private String startU;
    @ApiModelProperty(name = "automateUuid", notes = "自动化平台uuid")
    private String automateUuid;
    @ApiModelProperty(name = "agentVersion", notes = "agent版本")
    private String agentVersion;
    @ApiModelProperty(name = "cpuHz", notes = "CPU频率")
    private String cpuHz;
    @ApiModelProperty(name = "cpuModel", notes = "CPU型号")
    private String cpuModel;
    @ApiModelProperty(name = "cpus", notes = "总物理核心数")
    private String cpus;
    @ApiModelProperty(name = "diskSize", notes = "磁盘大小")
    private String diskSize;
    @ApiModelProperty(name = "memSize", notes = "内存大小")
    private String memSize;
    @ApiModelProperty(name = "memo", notes = "备注")
    private String memo;
    @ApiModelProperty(name = "osArchitecture", notes = "操作系统架构")
    private String osArchitecture;
    @ApiModelProperty(name = "osDistro", notes = "操作系统发行版本")
    private String osDistro;
    @ApiModelProperty(name = "osRelease", notes = "操作系统内核发行版本")
    private String osRelease;
    @ApiModelProperty(name = "osSystem", notes = "操作系统类型")
    private String osSystem;
    @ApiModelProperty(name = "osVersion", notes = "操作系统")
    private String osVersion;
    @ApiModelProperty(name = "provider", notes = "供应商")
    private String provider;
    @ApiModelProperty(name = "status", notes = "运营状态")
    private String status;
    @ApiModelProperty(name = "customer", notes = "所属客户")
    private String customer;
    @ApiModelProperty(name = "isSinglePower", notes = "是否单电源")
    private String isSinglePower;
    @ApiModelProperty(name = "product", notes = "所属产品")
    private String product;
    @ApiModelProperty(name = "propertyid", notes = "资产编号")
    private String propertyid;
    @ApiModelProperty(name = "use", notes = "用途")
    private String use;
    @ApiModelProperty(name = "installedPatch", notes = "已安装的补丁信息")
    private String installedPatch;
    @ApiModelProperty(name = "belongProxy", notes = "所属代理")
    private String belongProxy;
    @ApiModelProperty(name = "isProxy", notes = "是否是PROXY")
    private String isProxy;
    @ApiModelProperty(name = "version", notes = "自动化版本")
    private String version;
    @ApiModelProperty(name = "cpuJson", notes = "CPU信息Json串")
    private String cpuJson;
    @ApiModelProperty(name = "diskJson", notes = "磁盘信息Json串")
    private String diskJson;
    @ApiModelProperty(name = "ethJson", notes = "网卡信息Json串")
    private String ethJson;
    @ApiModelProperty(name = "serviceJson", notes = "服务信息Json串")
    private String serviceJson;


    // json对象信息
    @ApiModelProperty(name = "cpu", notes = "CPU信息")
    private AutomateHostCpuEntity cpu;
    // 数组信息
    @ApiModelProperty(name = "disk", notes = "磁盘信息")
    private List<AutomateHostDiskEntity> disk;
    @ApiModelProperty(name = "eth", notes = "网卡信息")
    private List<AutomateHostEthEntity> eth;
    @ApiModelProperty(name = "service", notes = "服务信息")
    private List<AutomateHostServiceEntity> service;
}
