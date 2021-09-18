package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    TemplateObjectBatchCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/7 15:11
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TemplateObjectBatchCreateRequest {
    @JsonProperty("template_object_list")
    private List<TemplateObjectCreateRequest> templateObjectList;
}
