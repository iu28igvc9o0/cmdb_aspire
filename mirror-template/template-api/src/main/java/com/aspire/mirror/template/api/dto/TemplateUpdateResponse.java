package com.aspire.mirror.template.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板修改对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    TemplateUpdateResponse.java
 * 类描述:    模板修改响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class TemplateUpdateResponse implements Serializable {

    private static final long serialVersionUID = -9095358425358629274L;

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
