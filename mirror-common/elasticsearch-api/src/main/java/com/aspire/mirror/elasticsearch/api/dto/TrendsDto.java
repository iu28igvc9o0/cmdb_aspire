package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * @author baiwp
 * @title: TrendDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2116:18
 */
@Data
public class TrendsDto {
    private String itemid;
    private Long clock;
    private String num;
    private String valueMin;
    private String valueAvg;
    private String valueMax;
    private String prefix;
}
