package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fanwenhui
 * @date 2020-07-13 10:19
 * @description ip存活和分配信息更新实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpInfo {
    private String ip;
    private String idcType;
    private String deviceIp;
    private String ipType;
    private String firstSurvivalTime; // 首次存活时间
    private String latestSurvivalTime; // 最近存活时间
    private String businessName1; // 一级业务线
    private String businessName2; // 二级业务线
    private String ip4IdcType; // ip和资源池拼接而成的字符串，用于判断是否唯一，eg: 10.2.137.65-呼和浩特资源池
}
