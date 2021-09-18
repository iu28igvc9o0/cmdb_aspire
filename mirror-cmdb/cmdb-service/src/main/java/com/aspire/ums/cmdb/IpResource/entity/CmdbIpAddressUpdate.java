package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 资产管理流程-IP地址库更新
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/28 14:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpAddressUpdate {
    // 资源池
    private String idcVal;
    // IP
    private List<String> ips;
    // 分配状态
    private String assignStatus;
    // 分配人
    private String assignUser;
    // 分配时间
    private String assignTime;
    // 存活状态
    private String survivalStatus;
    // 首次存活时间
    private String firstSurvivalTime;
    // 最近存活时间
    private String latestSurvivalTime;
    // 申请人
    private String applicater;
    // 申请工单
    private String applicatOrder;
    // 申请时间
    private String applicatTime;
    // 使用期限(年),保留小数点后2位
    private String useLife;
    // 是否录入CMDB
    private String isCmdbManager;
    // 独立业务
    private String onlineBusiness;
    // 业务子模块
    private String subBusinessModule;
    // 接口人
    // 关联设备

    // 是否清空该Ip的其他信息:1-IP信息正常更新,2-清空IP其他信息
    private String ipUpdateFlag;
}
