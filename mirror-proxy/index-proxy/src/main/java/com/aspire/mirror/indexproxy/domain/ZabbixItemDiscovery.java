package com.aspire.mirror.indexproxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    ZabbixItemDiscovery
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/12/14 20:28
 * 版本:      v1.0
 */
@Data
@ToString
public class ZabbixItemDiscovery {
    @JsonProperty("itemid")
    private String itemId;

    @JsonProperty("parent_itemid")
    private String parentItemId;
}
