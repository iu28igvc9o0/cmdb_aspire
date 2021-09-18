package com.aspire.mirror.elasticsearch.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum DeviceUsedRateEnum {
    PHYSICAL_CPU("X86服务器", "custom.cpu.avg5.pused", "cpu_pused"),
    PHYSICAL_MEMORY("X86服务器", "custom.memory.pused", "memory_pused"),
    PHYSICAL_DISK("X86服务器", "diskpused", "disk_pused"),

    VIRTUAL_CPU("云主机", "cpu.usage_average", "cpu_pused"),
    VIRTUAL_MEMORY("云主机", "mem.usage_average", "memory_pused"),
    VIRTUAL_DISK("云主机", "disk.usage_average", "disk_pused"),
    
    STORAGE_BLOCK("块存储", "disk.usage_average", "storage_block"),
    STORAGE_SAN("SAN存储", "disk.usage_average", "storage_san"),;
    /**
     * 设备类型
     */
    @Getter
    private String deviceType;

    /**
     * 指标名称
     */
    @Getter
    private String itemName;

    /**
     * 统一别名
     */
    @Getter
    private String itemAlias;

    public static String getItemAliasByItemName (String itemName) {
        for (DeviceUsedRateEnum deviceUsedRateEnum : DeviceUsedRateEnum.values()) {
            if (deviceUsedRateEnum.getItemName().equals(itemName)) {
                return deviceUsedRateEnum.getItemAlias();
            }
        }
        return null;
    }
    
    public static String getItemDeviceTypeByItemName (String itemName) {
        for (DeviceUsedRateEnum deviceUsedRateEnum : DeviceUsedRateEnum.values()) {
            if (deviceUsedRateEnum.getItemName().equals(itemName)) {
                return deviceUsedRateEnum.getDeviceType();
            }
        }
        return null;
    }


}
