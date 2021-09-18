package com.aspire.ums.cmdb.IpResource.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 主机资源资产表实体
 *
 * @Author: fanshenquan
 * @Datetime: 2020/6/18 16:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbInstanceEntity {
    // 资源列表Id
    private String assetId;
    // 管理IP
    private String manageIp;
    // 所属资源池
    private String idcVal;
    // 主备
    private String hostBack;
    // 是否资源池管理
    private String isPool;
    // 设备分类
    private String deviceClassName;
    // 设备类型
    private String deviceTypeName;
    // 独立业务线
    private String aloneBusiness;
    // 独立业务线子模块
    private String aloneBusiness2;
    // 机房位置
    private String machineRoomName;
    // 项目归属
    private String projectBelongto;
    // 型号
    private String deviceModel;
    // 配置
    private String deviceConfig;
    // 操作系统
    private String deviceOsType;
    // 机柜
    private String deviceCell;
    // 序列号
    private String devicesn;
    // 资产编号
    private String assetNum;
    // consoleip
    private String consoleip;

}
