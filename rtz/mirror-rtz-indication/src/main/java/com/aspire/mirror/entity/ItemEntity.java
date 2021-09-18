package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    private String name;
    private String type;
    private String handler;
    private String wsdl;
    private String method;
    private String timeType;
    private String mirrorColumn;
}
