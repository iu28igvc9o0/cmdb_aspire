package com.aspire.mirror.composite.service.alert.payload;

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
    private String itemId;
    private Long clock;
    private String num;
    private String valueMin;
    private String valueAvg;
    private String valueMax;
    private String prefix;
}
