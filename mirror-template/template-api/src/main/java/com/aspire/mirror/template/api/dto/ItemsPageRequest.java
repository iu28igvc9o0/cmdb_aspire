package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    ItemsPageRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/7/31 14:28
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class ItemsPageRequest {
    /**
     * 模板名称
     */
    @JsonProperty("template_name")
    private String templateName;

    /**
     * 监控项名称
     */
    private String name;

    /**
     * 监控项key
     */
    private String key;
    
    private String type; 		// SCRIPT: 巡检指标     INDEX
    
    /**
     * 功能类型
     */
    @JsonProperty("fun_type")
    private String funType;
    /**
     * 每页显示记录条数
     */
    @JsonProperty("page_size")
    private Integer pageSize;
    /**
     * 第几页
     */
    @JsonProperty("page_no")
    private Integer pageNo;

    /**
     * 模板ID
     */
    @JsonProperty("template_id")
    private String templateId;

    /**
     * 主题编码
     */
    @JsonProperty("theme_code")
    private String themeCode;
}
