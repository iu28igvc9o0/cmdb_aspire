package com.aspire.ums.cmdb.networkCard.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CmdbInstanceNetworkCard{

    /**
     * id (UUID生成)
     */
    private String id;
    /**
     * 资产编号
     */
    private String instanceId;
    /**
     * 网卡名称
     */
    private String networkCardName;
    /**
     * 状态
     */
    private String networkCardStatus;
    /**
     * 端口类型
     */
    private String portType;
    /**
     * 端口速率
     */
    private String portRate;
    /**
     * IPv4地址
     */
    private String ipv4Address;
    /**
     * IPv6地址
     */
    private String ipv6Address;
    /**
     * mac地址
     */
    private String macAddress;
    /**
     * 默认网关
     */
    private String defaultGateway;
    /**
     * 是否DHCP
     */
    private String isDhcp;
    /**
     * DHCP地址
     */
    private String dhcpAddress;
    /**
     * DNS服务器
     */
    private String dnsServer;
    /**
     * 网卡描述
     */
    private String remark;
    /**
     * 0 未删除 1 已删除
     */
    private String isDelete;
}
