package com.aspire.ums.cmdb.automate.enums;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanwenhui
 * @date 2020-08-24 9:45
 * @description 模型枚举字段
 */
@Slf4j
public enum AutomateInstanceModuleEnum {

    HOST("HOST", "主机"),
    ;
    private String key;
    private String value;

    AutomateInstanceModuleEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }

    public static AutomateInstanceModuleEnum fromKey(String temp) {
        for (AutomateInstanceModuleEnum element : AutomateInstanceModuleEnum.values()) {
            if (temp.equalsIgnoreCase(element.getKey())) {
                return element;
            }
        }
        // throw new RuntimeException("InstanceRequestEnum类型错误， 不支持的类型[" + key + "]!");
        log.info("InstanceRequestEnum类型错误， 不支持的类型[" + temp + "]!");
        return null;
    }
}
