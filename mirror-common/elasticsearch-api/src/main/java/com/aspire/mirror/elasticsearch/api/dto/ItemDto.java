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
public class ItemDto {

    private String itemId;
    private String ip;
    private String proxyName;
    private String idcType;
    private String moniObject;
    private String name;
    private String key;
    private String delay;
    private String value;
    private String units;
    private String description;
    private String prefix;
    private String deviceClass;
    private String deviceType;
    private String bizSystem;
    private String roomId;
    private String valueType;
    private String resourceId;
    private String status;
}
