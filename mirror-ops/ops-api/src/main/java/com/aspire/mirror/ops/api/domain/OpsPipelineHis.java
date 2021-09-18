package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpsPipelineHis extends OpsPipelineDTO{
    @JsonProperty("id")
    protected Long				id;
}
