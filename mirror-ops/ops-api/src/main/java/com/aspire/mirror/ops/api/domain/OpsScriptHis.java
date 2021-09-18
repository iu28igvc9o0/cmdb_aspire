package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OpsScriptHis extends OpsScript{
    private Long id;

    @JsonProperty("step_his_id")
    private Long stepHisId;
}
