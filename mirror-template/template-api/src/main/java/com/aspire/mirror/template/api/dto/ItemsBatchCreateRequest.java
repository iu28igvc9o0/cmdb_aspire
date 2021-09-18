package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量创建监控项请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.template.api.dto
 * 类名称:    ItemsBatchCreateRequest.java
 * 类描述:    批量创建监控项请求
 * 创建人:    JinSu
 * 创建时间:  2018/7/31 19:29
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class ItemsBatchCreateRequest {
    @JsonProperty("list_item")
    private List<ItemsCreateRequest> listItem;
}
