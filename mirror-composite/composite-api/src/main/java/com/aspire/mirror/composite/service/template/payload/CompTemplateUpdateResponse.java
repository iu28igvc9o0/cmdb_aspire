package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模板修改返回VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompTemplateUpdateResponse.java
 * 类描述:    模板修改返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/6 11:04
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompTemplateUpdateResponse implements Serializable {
    private static final long serialVersionUID = 7917755367556790231L;
    /**
     * 模版ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 模版名称
     */
    private String name;
}
