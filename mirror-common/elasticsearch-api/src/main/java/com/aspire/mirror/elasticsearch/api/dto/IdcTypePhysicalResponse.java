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
public class IdcTypePhysicalResponse {
    private String idcType;
    private float percent80;
    private float percent40;
    private float percent15;
    private float percentLess;
}
