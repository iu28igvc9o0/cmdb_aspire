package com.aspire.mirror.composite.payload.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertMailFilterStrategyRequest {
    private Integer id;
    @JsonProperty("filter_id")
    private String filterId;
    @JsonProperty("mail_field")
    private Integer mailField;
    @JsonProperty("alert_field")
    private String alertField;
    @JsonProperty("field_match_value")
    private String fieldMatchValue;
    @JsonProperty("use_reg")
    private Integer useReg;
    @JsonProperty("field_match_reg")
    private String fieldMatchReg;
    @JsonProperty("field_match_target")
    private String fieldMatchTarget;
}
