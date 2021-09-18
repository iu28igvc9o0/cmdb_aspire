package com.aspire.ums.cmdb.allocate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocateIpConfigDetail {

    //业务系统
    private String bizSystem;
    //生效开始时间
    private String startTime;
    //生效结束时间
    private String endTime;
    //用户名称
    private String userName;
    //用户电话
    private String userT;
    //域名
    private int vpnId;
    private String vpnName;
    //网段
    private int networkId;
    private String network;
    //分配ip
    private String ip;
    //私有云ip
    private String privateIp;
    //是否删除 0 未删除 1 已删除
    private int flag;

}
