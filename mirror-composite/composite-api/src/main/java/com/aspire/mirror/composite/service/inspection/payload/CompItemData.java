package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompItemData.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/12/26 15:04
 * 版本:      v1.0
 */
@Data
public class CompItemData {
    @JsonProperty("item_id")
    private String itemId;
    @JsonProperty("item_name")
    private String itemName;
}
