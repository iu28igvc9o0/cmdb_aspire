package com.aspire.ums.cmdb.ipAudit.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: huanggongrui
 * @Description: 已分配未存活IP列表请求实体类
 * @Date: create in 2020/5/23 15:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedUnsurvivingIntranetIpReq extends IpAuditCommonReq {
    /**
     * 网段类型
     */
    private String networkSegmentType;
    /**
     * 网段子类
     */
    private String networkSegmentSubType;
    /**
     * 地址用途
     */
    private String ipUse;
    /**
     * 分配一级业务
     */
    private List<String> firstBusinessSystemList;
    /**
     * 分配独立业务
     */
    private List<String> aloneBusinessSystemList;
    /**
     * 分配状态:已分配,未分配
     */
    private String assignStatus;
    /**
     * 分配人
     */
    private String assignUser;
    /**
     * 分配时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String assignTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String assignTimeEnd;
    /**
     * 申请人
     */
    private String requestPerson;
    /**
     * 申请时间起止
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String requestTimeBegin;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
    private String requestTimeEnd;
    /**
     * 存活状态: 未存活, 已存活
     */
    private String surviveStatus;
    /**
     * 最近发现时间
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
}
