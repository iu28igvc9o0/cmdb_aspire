package com.aspire.mirror.template.api.dto.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ZabbixTemplateSyncVo extends TemplateVO {
//    private String temp
    private List<ItemsVO> items;

    @JsonProperty("index_type")
    private String indexType;

    private List<MirrorHostVO> hosts;
}
