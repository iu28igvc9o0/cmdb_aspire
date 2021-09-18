package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationDimEntity {
    private String themeId;
    private String themeCode;
    private String themeName;
    private String dimId;
    private String dimCode;
    private String dimName;
    private String dimOrder;
    private String dimType;
    private String dimReg;
    private String matchFlag;
    private String handlerClass;
    private String handlerWsdl;
    private String handlerMethod;
    private List<IndicationEntity> indicationList;
}
