package com.aspire.mirror.elasticsearch.api.dto;

import lombok.Data;

/**
 * @author baiwp
 * @title: TriggerDto
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2110:00
 */
@Data
public class TriggerDto {
    private String itemId;
    private String triggerId;
    private String proxyName;
    private String idc;
    private String expression;
    private String description;
    private String priority;
    private String prefix;
}
