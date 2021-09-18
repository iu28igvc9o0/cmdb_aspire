package com.aspire.ums.cmdb.maintain.entity;

import lombok.Data;

@Data
public class HandUpInstance {
    String oldCircleId;
    String circleId;
    String[] instanceIds;
}
