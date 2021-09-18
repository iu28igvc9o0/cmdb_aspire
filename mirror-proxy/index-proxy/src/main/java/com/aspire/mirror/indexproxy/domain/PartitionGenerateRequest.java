package com.aspire.mirror.indexproxy.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    PartitionGenerateRequest
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/20 18:06
 * 版本:      v1.0
 */
@Data
public class PartitionGenerateRequest {
    @JsonProperty("index_type")
    private String indexType;
}
