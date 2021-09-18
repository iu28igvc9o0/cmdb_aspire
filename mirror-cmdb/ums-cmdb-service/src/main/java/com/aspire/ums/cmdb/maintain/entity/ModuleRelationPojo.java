package com.aspire.ums.cmdb.maintain.entity;

import lombok.Data;

@Data
public class ModuleRelationPojo {
    String restriction;
    String relationId;
    String[] moduleIds;
    String targetModuleId;
    String sourceModuleId;
}
