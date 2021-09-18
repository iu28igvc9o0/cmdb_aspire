package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BusinessLineCorrespondence {

    private int id;

    private String business_line_new;

    private String business_level1_name;
}
