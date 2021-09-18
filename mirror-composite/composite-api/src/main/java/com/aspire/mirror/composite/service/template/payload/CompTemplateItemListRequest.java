package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTemplateItemListRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/6 13:48
 * 版本:      v1.0
 */
@Data
public class CompTemplateItemListRequest {
    public static final String		PARAM_TEMPLATE_NAME	= "template_name";
    public static final String		PARAM_ITEM_KEY		= "item_key";
    public static final String		PARAM_ITEM_NAME		= "item_name";

    @JsonProperty("apiServerConfig")
    private CompMonitorApiServerConfig	apiServerConfig;

    @JsonProperty
    private Map<String, Object> params;

    public Object getParamValue(String paramName) {
        return params.get(paramName);
    }
}
