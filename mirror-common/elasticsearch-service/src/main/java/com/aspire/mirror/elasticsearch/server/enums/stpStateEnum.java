package com.aspire.mirror.elasticsearch.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum stpStateEnum {
    disabled("disabled", 1),
    blocking("blocking", 2),
    listening("listening",3),

    leaning("leaning", 4),
    forwarding("forwarding",5),
    broken("broken",6);
    
    /**
     * 设备类型
     */
    @Getter
    private String name;

    /**
     * 指标名称
     */
    @Getter
    private int value;

    public static String getName (int value) {
        for (stpStateEnum stp : stpStateEnum.values()) {
            if (stp.getValue() == value) {
                return stp.getName();
            }
        }
        return null;
    }



}
