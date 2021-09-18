package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ComboBox implements Serializable {
    private static final long serialVersionUID = -6096402994576885027L;
    private String id;
    private String text;
    private boolean selected;

}