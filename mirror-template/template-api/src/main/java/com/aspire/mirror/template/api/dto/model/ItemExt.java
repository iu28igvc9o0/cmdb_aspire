package com.aspire.mirror.template.api.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 指标扩展
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    ItemExt
 * 类描述:    指标扩展
 * 创建人:    JinSu
 * 创建时间:  2020/12/22 14:17
 * 版本:      v1.0
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ItemExt {
    private Long id;
    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("script_param")
    private String scriptParam;
    @JsonProperty("customize_param")
    private String customizeParam;
}
