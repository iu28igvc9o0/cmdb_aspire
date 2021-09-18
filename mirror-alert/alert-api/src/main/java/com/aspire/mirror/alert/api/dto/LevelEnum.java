package com.aspire.mirror.alert.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum LevelEnum {
	INFO("INFO",1),
	WARN("WARN",2),
	ERROR("ERROR",3),
	DEFAULT("DEFAULT",4);

    @Getter
    private String name;

    /**
     * 样式
     */
    @Getter
    private Integer value;
   

    public static LevelEnum getTopoEnumByName (String itemName) {
        for (LevelEnum topoEnum : LevelEnum.values()) {
            if (topoEnum.getName().equals(itemName)) {
                return topoEnum;
            }
        }
        return DEFAULT;
    }
}
