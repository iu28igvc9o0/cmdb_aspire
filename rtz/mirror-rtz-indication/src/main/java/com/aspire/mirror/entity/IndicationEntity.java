package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicationEntity {
    private String indicationId;
    private String indicationName;
    private String sysCode;
    private String themeCode;
    private String indicationOwner;
    private String indicationCatalog;
    private String indicationGroup;
    private String indicationCycle;
    private String indicationFrequency;
    private String indicationPosition;
    private String indicationUnit;
    private Integer indicationOrder;
    private IndicationLimitEntity limitEntity;
    private String bizThemeExp;
    private String indicationPrefix;

    public String toString() {
        return String.format("{indication:%s, indicationName:%s, sysCode:%s, themeCode:%s, indicationOwner:%s, " +
                        "indicationCatalog:%s, indicationGroup:%s, indicationCycle:%s, indicationFrequency:%s, indicationPosition:%s, " +
                        "indicationUnit:%s, indicationOrder:%s, limitEntity:%s, bizThemeExp:%s, indicationPrefix:%s}",
                getIndicationId(), getIndicationName(), getSysCode(), getThemeCode(), getIndicationOwner(),
                getIndicationCatalog(), getIndicationGroup(), getIndicationCycle(), getIndicationFrequency(), getIndicationPosition(),
                getIndicationUnit(), getIndicationOrder(), JSONObject.fromObject(getLimitEntity()), getBizThemeExp(), getIndicationPrefix());
    }
}
