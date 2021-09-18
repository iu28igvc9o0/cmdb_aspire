package com.aspire.mirror.composite.service.alert.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String status;

    // 以下是扩展的属性
    private String threshold;
    @JsonProperty("alert_level")
    private String alertLevel;
}
