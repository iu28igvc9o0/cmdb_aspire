package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompAllocateIpConfigRes {

    // id
    private long id;
    // 域名
    private String vpnName;
    // 网段
    private String network;
    // 业务系统
    private String bizSystem;
    // 分配ip
    private String allocateIp;
    // 私有云ip
    private String cloudsIp;
    // 生效时间
    private String useTime;
    // 用户
    private String userInfo;

}
