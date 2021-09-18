package com.aspire.ums.cmdb.ipCollect.service;

/**
 * 全量同步
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/21 11:14
 */
public interface VmwareFullSyncApiService {

    /**
     * 存活IP-网络设备
     */
    void syncIpAddressPool();

    /**
     * 存活IP-网络设备配置文件解析
     */
    void syncIpConfPool();

    /**
     * 存活IP-arp扫描网段地址
     */
    void syncIpArpPool();

    /**
     * 虚拟IP
     */
    void syncVipAddressPool();

}
