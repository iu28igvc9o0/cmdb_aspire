package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 创建监控项返回结果
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsCreateResponse.java
 * 类描述:    创建监控项返回结果
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 14:56
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompItemsCreateResponse implements Serializable {
    private static final long serialVersionUID = 291464456452694571L;
    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId;

    /** 监控项名称 */
    private String name;
}
