package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * cmdb资产DTO
 *
 * @author jiangxuwen
 * @date 2020/5/13 10:56
 */
@Data
public class CmdbDeviceAssetDTO implements Serializable {

    private static final long serialVersionUID = -2361584297243172172L;
    
    private String id;
    
    /** 部门id-字典表 */
    private String departmentId;
    
    /** 部门--对应新版cmdb的二级部门. */
    private String departmentName;
    
    /** 财务业务线id. */
    private String financialBusinessId;
    
    /** 财务业务线名称. */
    private String financialBusinessName;
    
    /** 管理IP. */
    private String deviceIp;
    
    /** 主备. */
    private String hostBackup;
    
    /** 一级业务线. */
    private String businessLevel1;
    
    /** 一级业务线id. */
    private String businessId1;
    
    /** 二级业务线id. */
    private String businessId2;
    
    /** 二级业务线. */
    private String businessLevel2;
    
    /** 业务线id. */
    private String businessId;
    
    /** 业务Ip1. */
    private String businessIp1;
    
    /** 业务Ip2. */
    private String businessIp2;
    
    /** 资源池. */
    private String idc;
    
    /** 所属位置. */
    private String comeFrom;
    
    /** 机房位置. */
    private String idcLocation;
    
    /** 机柜号. */
    private String deviceCell;
    
    /** 项目归属. */
    private String projectBelongTo;
    
    /** 设备分类ID. */
    private String deviceClassId;
    
    /** 设备类型ID. */
    private String deviceTypeId;
    
    /** 设备型号ID. */
    private String deviceModelId;
    
    /** 设备分类ID. */
    private String deviceClassName;
    
    /** 设备类型ID. */
    private String deviceTypeName;
    
    /** 设备型号ID. */
    private String deviceModelName;
    
    /** 设备网络层级. */
    private String deviceNetworkLayerId;
    
    /** 设备网络层级. */
    private String deviceNetworkLayerName;
    
    /** 设备状态. */
    private String deviceStatus;
    
    /** 设备规格. */
    private String deviceStandard;
    
    /** 操作系统类型. */
    private String deviceOsTypeId;
    
    /** 操作系统类型. */
    private String deviceOsTypeName;
    
    /** 设备厂家. */
    private String deviceFactoryId;
    
    /** 设备厂家. */
    private String deviceFactoryName;
    
    /** 设备风险等级. */
    private String deviceRiskLevel;
    
    /** 设备序列号. */
    private String deviceSn;
    
    /** 资产标签号. */
    private String assetNumber;
    
    /** 设备逻辑名. */
    private String deviceLogName;
    
    private String consoleIp;
    
    private String consoleVlan;
    
    private String consoleMask;
    
    private String consoleGw;
    
    private String consoleUser;
    
    private String consolePassword;
    
    /** 业务VLAN. */
    private String businessVlan;
    
    /** 本地磁盘大小. */
    private String localDisk;
    
    /** 外挂存储大小. */
    private String mountDisk;
    
    /** 网络区域. */
    private String networkArea;
    
    // private String deviceMaintenanceId;
    
    // private String serverClass1;
    
    // private String serverType1;
    
    // private String serverModel1;
    
    /** 块存储(GB). */
    private String blockSize;
    
    // private String serverOsType1;
    
    // private String managedByAnsible;
    
    /** 上线时间. */
    private String onlineTime;
    
    /** 其他IP. */
    private String otherIp;
    
    /** 刀箱号. */
    private String boxNum;
    
    /** 槽位号. */
    private String slotNum;
    
    /** 刀箱管理IP. */
    private String boxMgrIp;
    
    /** 所在宿主机IP. */
    private String exsiIp;
    
    /** 承载虚拟机名称. */
    private String vmName;
    
    /** 承载虚拟机IP. */
    private String vmIp;
    
    /** 板卡序列号. */
    private String bcardSn;
    
    /** 分布式存储(GB). */
    private String disStorage;
    
    /** 分布式存储挂载目录. */
    private String disStDir;
    
    /** 是否资源池管理. */
    private String mgrByPool;
    
    /** 备注. */
    private String remark;
    
    /** 分布式存储类型. */
    private String disStType;
    
    /** 资源计划性. */
    private String resourcePlan;
    
    /** 按比例分摊日期. */
    private String prorateDate;
    
    /** 使用年限. */
    private String serviceLife;
    
    /** 转资成本. */
    private String transCost;
    
    /** 单价. */
    private String unitPrice;
    
    // 应用模块
    private String applicationModuleId;
    
    private String applicationModuleName;
    
    // 虚拟机的创建时间
    private String vmCreationDate;
    
    /** ------------------------自动化采集 start.---------------------------- */
    
    /** zabbixAgent安装状态. */
    private String zabbixAgentInstallStatus;
    
    /** 自动化agent安装状态. */
    private String sncAgentStatus;
    
    /** filebeat安装状态. */
    private String filebeatInstallStatus;
    
    /** 是否需安装zabbix. */
    private String zabbixAgentMonitorFlag;
    
    /** 是否需安装自动化agent. */
    private String sncAgentMonitorFlag;
    
    /** 是否需安装filebeat */
    private String filebeatAgentMonitorFlag;
    
    /** zabbixAgent健康监测状态. */
    private String zabbixAgentHealthCheckStatus;
    
    /** 监控项监测状态. */
    private String itemMonitorStatus;
    
    /** 自动化agent健康监测状态. */
    private String sncAgentHealthCheckStatus;
    
    /** filebeat健康监测状态. */
    private String fileBeatHealthCheckStatus;
    
    // private String sncAgentInstallInfo;
    //
    // private String zabbixAgentInstallInfo;
    //
    // private String filebeatInstallInfo;
    
    /** filebeat 采集目录. */
    private String filebeatLogPath;
    
    /** zabbix 代理IP */
    private String zabbixProxyIp;
    
    /** server运行时长. */
    private Integer serverRunTime;
    
    /** 自动化agent运行时长. */
    private Integer sncAgentRunTime;
    
    /** 自动化监测hostname. */
    private String sncAgentHostname;
    
    /** filebeat更新时间. */
    private String filebeatUpdateTime;
    
    private String sncAgentInstallInfo;
    
    private String zabbixAgentInstallInfo;
    
    private String filebeatInstallInfo;
    
    /** ------------------------自动化采集 end.---------------------------- */
    
    /** 录入移动负责人. */
    private String createCmMgr;
    
    /** 录入时间. */
    private String createTime;
    
    /** ---------------------工程交维 start.------------------- */
    
    /** 录入人. */
    private String createUser;
    
    /** 工程交维ID. */
    private String projectBatchId;
    
    /** 工程交维设备ID. */
    private String equipmentId;
    
    /** U位. */
    private String serverUnum;
    
    /** 机架位置. */
    private String serverRackLocation;
    
    /** CPU核心数. */
    private Integer physicalDeviceCpuCores;
    
    /** 物理机内存. */
    private Integer physicalDeviceMemorySize;
    
    /** 应用类型. */
    private String applicationTypeId;
    
    private String applicationTypeName;
    
    /** 应用配置. */
    private String applicationConfId;
    
    private String applicationConfName;
    
    /** 主机名. */
    private String deviceHostName;
    
    /** 存储角色ID. */
    private String storageTypeId;
    
    private String storageTypeName;
    
    /** 存储业务归属. */
    private String storageBusinessId;
    
    private String storageBusinessName;
    
    /** 外挂存储类型ID. */
    private String deviceMountTypeId;
    
    private String deviceMountTypeName;
    
    /** 外挂磁盘大小. */
    private Integer physicalDeviceMountDiskSize;
    
    /** 外挂磁盘大小2. */
    private Integer physicalDeviceMountDiskSize2;
    
    /** 外挂存储类型2ID. */
    private String deviceMountTypeId2;
    
    private String deviceMountTypeName2;
    
    /** 父设备IP. */
    private String deviceParentIp;
    
    /** 父设备名称. */
    private String deviceParentName;
    
    /** 管理网段. */
    private String deviceMgrNetworkSegment;
    
    /** 管理WEB登录地址. */
    private String deviceMgrWebUrl;
    
    /** 设备逻辑名. */
    private String deviceLogicName;
    
    /** 公网IP. */
    private String publicIp;
    
    /** 所在集群IP. */
    private String clusterIp;
    
    /** 所在集群名称. */
    private String clusterName;
    
    /** 宿主机IP. */
    private String esxiName;
    
    /** 宿主机CPU. */
    private Integer esxiCpuCores;
    
    /** 宿主机内存. */
    private Integer esxiMemorySize;
    
    /** 宿主机操作系统. */
    private String esxiOsType;
    
    /**
     * 管理地址.
     */
    private String deviceMgrConsoleUrl;
    
    /** 网络设备安装自动化AGENT必须提供的PROXYIP. */
    private String proxyIp;
    
    /**
     * 外挂存1储厂家id,字典表
     */
    private String physicalDeviceMountDiskFactory1;
    
    private String physicalDeviceMountDiskFactoryName1;
    
    /**
     * 外挂存2储厂家id,字典表
     */
    private String physicalDeviceMountDiskFactory2;
    
    private String physicalDeviceMountDiskFactoryName2;
    
    /**
     * 外挂存储1挂载目录
     */
    private String physicalDeviceMountDiskDir1;
    
    /**
     * 外挂存储2挂载目录
     */
    private String physicalDeviceMountDiskDir2;
    
    // private String businessLineId;
    
    /** 资源申请人. */
    private String resourceApplicant;
    
    /** -------------------维保信息.------------------------ */
    
    /** 出保时间. */
    private String maintenceEndTime;
    
    /** 实际购买维保类型. */
    private String maintenceType;
    
    /** 本期维保开始时间. */
    private String maintenceTermStartDate;
    
    /** 本期维保结束时间. */
    private String maintenceTermEndDate;
    
    /** 实际购买维保类型. */
    private String maintencePurchaseType;
    
    /** 原厂维保购买必要性说明. */
    private String maintencePurchaseDesc;
    
    /** 是否购买维保. */
    private String maintencePurchaseFlag;
    
    /** 是否需原厂维保. */
    private String venderMaintenceFlag;
    
    /** 维保管理员. */
    private String maintenceAdmin;
    
    /** 维保厂家名称. */
    private String maintenceFactoryName;
    
    /** 维保供应商联系方式. */
    private String maintenceProviderContact;
    
    /** 维保厂家联系方式. */
    private String maintenceFactoryContact;
    
    /**-----------------维保管理 结束------------------*/
    
    private String ipType;
    
    private String ipv6;
    
    private String initialNatIp;
    
    /** IP承载网. */
    private String carrierNetwork;

//    private CmdbDeviceMaintenceInfoDTO maintenceInfoDTO;

    /**
     * 标准化物理设备
     */
    private String standardPhysicalEquipment;

    /**
     * 标准化物理设备id
     */
    private String standardPhysicalEquipmentId;

    /**
     * 存活状态
     */
    private String surviveStatus;

    /**
     * 最近存活时间
     */
    private String recentSurviveTime;

}
