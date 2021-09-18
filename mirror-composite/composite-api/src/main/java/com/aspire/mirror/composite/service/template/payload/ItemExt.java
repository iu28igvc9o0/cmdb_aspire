package com.aspire.mirror.composite.service.template.payload;

import com.aspire.mirror.ops.api.domain.OpsParamReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.sf.json.JSONArray;

import java.util.List;

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
public class ItemExt {
    private Long id;
    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("script_param")
    private String scriptParam;
    @JsonProperty("customize_param")
    private String customizeParam;
}
