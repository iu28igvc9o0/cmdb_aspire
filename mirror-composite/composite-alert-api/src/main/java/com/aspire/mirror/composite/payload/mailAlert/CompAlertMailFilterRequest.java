package com.aspire.mirror.composite.payload.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class CompAlertMailFilterRequest {
    private String id;
    private Integer recipientId;
    private String receiver;
    private String sender;
    @JsonProperty("title_include")
    private String titleInclude;
    @JsonProperty("content_include")
    private String contentInclude;
    private Integer active;
    private Integer times;
    @JsonProperty("strategies")
    private List<CompAlertMailFilterStrategyRequest> strategyRequestList;
    private String name;
}
