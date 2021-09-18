package com.aspire.ums.cmdb.openstack.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/11/26 16:05
 */
@Slf4j
public enum OpenStackEnums {

    /** openStack server. */
    OPENSTACK_SERVERS("OPENSTACK_SERVERS", "openStack server"),

    /** openStack admin. */
    OPENSTACK_ADMIN("OPENSTACK_ADMIN", "openStack admin"),

    /** openStack server. */
    OPENSTACK_IMAGE("OPENSTACK_IMAGE", "openStack image"),

    /** openStack network. */
    OPENSTACK_NETWORK("OPENSTACK_NETWORK", "openStack network"),

    /** openStack subnet. */
    OPENSTACK_SUBNET("OPENSTACK_SUBNET", "openStack subnet"),

    ;

    private String key;

    private String value;

    OpenStackEnums() {}

    OpenStackEnums(String key, String value) {
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

    public static OpenStackEnums fromKey(String temp) {
        for (OpenStackEnums element : OpenStackEnums.values()) {
            if (temp.equalsIgnoreCase(element.getKey())) {
                return element;
            }
        }
        // throw new RuntimeException("InstanceRequestEnum类型错误， 不支持的类型[" + key + "]!");
        log.info("InstanceRequestEnum类型错误， 不支持的类型[" + temp + "]!");
        return null;
    }
}
