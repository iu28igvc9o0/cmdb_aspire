package com.aspire.ums.cmdb.ipAudit.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述： IP地址库内网IP实体
* @author huanggongrui
* @date 2020-05-21 15:27:29
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpRepositoryInnerIp {

    /**
     * 资产标识
     */
    private String id;
    /**
     * 删除标识
     */
    private Integer isDelete;
    /**
     * IP
     */
    private String ip;
    /**
     * 所属资源池
     */
    private String dc;
    /**
     * 分配状态
     */
    private String assignStatus;
    /**
     * 分配人
     */
    private String assignUser;
    private String assignTime;
    /**
     * 存活状态
     */
    private String survivalStatus;
    /**
     * 网关设备名称
     */
    private String deviceName;
    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 首次存活时间
     */
    private String firstSurvivalTime;
    /**
     * 最近存活时间
     */
    private String latestSurvivalTime;
    /**
     * 申请人
     */
    private String requestPerson;
    /**
     * 申请工单
     */
    private String requestProcessId;
    /**
     * 申请时间
     */
    private String requestTime;
    /**
     * 使用期限(年)
     */
    private String usefulLife;
    /**
     * 是否录入CMDB
     */
    private String isCmdbManager;
    /**
     * 独立业务
     */
    private String onlineBusiness;
    /**
     * 业务子模块
     */
    private String subBusinessModule;
    /**
     * 归属网段地址
     */
    private String networkSegmentOwner;

    private String innerIpUse;

    // ========网段信息==========
    private String segmentId;
    private String networkGataway;
    private String networkSegmentAddress;
    private String ipType;
    private String freeIpCount;
    private String vlanNumber;
    private String safeRegion;
    private String firstBusinessSystem;
    private String activeIpCount;
    private String assignIpCount;
    private String aloneBusinessSystem;
    private String innerSegmentType;
    private String innerSegmentSubType;
    private String innerSegmentIpType;
    private String isPool;
}