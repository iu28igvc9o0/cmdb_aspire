package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 模板创建返回VO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.service.template.payload
 * 类名称:    CompTemplateCreateResponse.java
 * 类描述:    模板创建返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/2 15:31
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompTemplateCreateResponse implements Serializable {
    private static final long serialVersionUID = -175338246015803173L;
    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;
    /**
     * 模板名称
     */
    private String name;
}
