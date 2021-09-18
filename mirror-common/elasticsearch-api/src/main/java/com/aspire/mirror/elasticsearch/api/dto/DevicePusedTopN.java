package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class DevicePusedTopN {
    private List<String> resourceIds;
    private String idcType;
    private int size = 10;
}
