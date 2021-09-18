package com.aspire.ums.cmdb.allocate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllocateIpConfigListReq {

    //页数
    private int pageNum;
    //开始的下标
    private int startPageNum;
    //条数
    private int pageSize;
    //域名
    private int vpnId;
    private String vpnName;
    //网段
    private int networkId;
    private String network;
    //业务系统
    private String bizSystem;
    //分配IP地址
    private String ip;
    private List<String> ips;
    //私有云ip
    private String privateIp;

}
