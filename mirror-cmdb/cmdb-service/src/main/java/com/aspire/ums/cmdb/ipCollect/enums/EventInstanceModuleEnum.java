package com.aspire.ums.cmdb.ipCollect.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/18 14:49
 */
@Slf4j
public enum EventInstanceModuleEnum {

    /** 网络设备. */
    IP_ADDRESS_POOL("IP_ADDRESS_POOL", "网络设备"),

    /** 网络设备配置文件解析. */
    IP_CONF_POOL("IP_CONF_POOL", "网络设备配置文件解析"),

    /** 网络设备配置文件解析. */
    IP_ARP_POOL("IP_ARP_POOL", "arp扫描网段地址"),

    /** 网络设备配置文件解析. */
    VIP_ADDRESS_POOL("VIP_ADDRESS_POOL", "虚拟ip采集"),

    /** VMware资源池. */
    VMWARE_NETWORK_PORTGROUP("VMWARE_NETWORK_PORTGROUP", "VMware网段与端口组"),
    ;

    private String key;

    private String value;

    EventInstanceModuleEnum() {}

    EventInstanceModuleEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static EventInstanceModuleEnum fromKey(String temp) {
        // hangfang 2020.07.30 硬编码加密密钥 key->temp
        for (EventInstanceModuleEnum element : EventInstanceModuleEnum.values()) {
            if (temp.equalsIgnoreCase(element.getKey())) {
                return element;
            }
        }
        // throw new RuntimeException("InstanceRequestEnum类型错误， 不支持的类型[" + key + "]!");
        log.info("InstanceRequestEnum类型错误， 不支持的类型[" + temp + "]!");
        return null;
    }
}
