package com.aspire.mirror.composite.service.template.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 指标修改返回对象
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.template.payload
 * 类名称:    CompItemsUpdateResponse.java
 * 类描述:    指标修改返回对象
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 15:07
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
@ToString
public class CompItemsUpdateResponse implements Serializable {
    private static final long serialVersionUID = 5051136267840968026L;
    /**
     * 监控项ID
     */
    @JsonProperty("item_id")
    private String itemId;

    /**
     * 监控项名称
     */
    private String name;
}
