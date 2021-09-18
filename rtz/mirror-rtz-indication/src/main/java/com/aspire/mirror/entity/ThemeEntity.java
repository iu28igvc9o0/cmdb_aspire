package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeEntity {
    private String themeCode;
    private String sysCode;
    private String type;
    private String owner;
    private String wsdl;
    private CommonItemEntity commonEntity;
    private List<ItemEntity> itemList;
}
