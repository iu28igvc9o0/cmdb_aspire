package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * @author baiwp
 * @title: ItemDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/219:58
 */
@Data
public class NetPerformanceAnalysis {

    private String ip;
    private Double cpuMax;
    private Double cpuAvg;
    private Double cpuMin;
    private Double MemMax;
    private Double MemAvg;
    private Double MemMin;
}
