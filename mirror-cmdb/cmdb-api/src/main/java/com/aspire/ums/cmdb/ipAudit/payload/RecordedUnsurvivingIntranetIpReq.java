package com.aspire.ums.cmdb.ipAudit.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: 已录CMDB未存活列表请求实体类
 * @Date: create in 2020/5/23 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordedUnsurvivingIntranetIpReq extends IpAuditCommonReq {

    /**
     * IP类型：管理IP、业务IP1、业务IP2、IPV6、其他IP、consoleIP
     */
    private String ipType;
    /**
     * 主备: 主, 备
     */
    private String hostBackup;
    /**
     * 是否资源池管理: 是,否
     */
    private String mgrByPool;
    /**
     * 所属独立业务
     */
    private String businessLevel1;
    /**
     * 独立业务子模块
     */
    private String businessLevel2;
    /**
     * 存活状态:已存活,未存活
     */
    private String surviveStatus;
    /**
     * 最近存活时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String lastSurviveTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String lastSurviveTimeEnd;
    /**
     * 未存活时长(小时)
     */
    private Double unsurvivedDurationBegin;
    private Double unsurvivedDurationEnd;
    /**
     * 设备状态：上电/下电
     */
    private String deviceStatus;
}
