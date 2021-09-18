package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 指标项列表请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsPageRequest.java
 * 类描述:    指标项列表请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 15:21
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompItemsPageRequest implements Serializable {
    private static final long serialVersionUID = 6533791955850084247L;
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
    
    private String type;

    /**
     * 功能类型
     */
    @JsonProperty("fun_type")
    private String funType;

    private String status;
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
