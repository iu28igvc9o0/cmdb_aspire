package com.aspire.ums.cmdb.resource.entity;

import lombok.Data;

import java.util.List;

@Data
public class ResourceEstimateResponse {
    private long total;
    private List rows;
}
