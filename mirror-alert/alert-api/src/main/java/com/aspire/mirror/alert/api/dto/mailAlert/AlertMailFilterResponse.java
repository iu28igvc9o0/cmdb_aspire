package com.aspire.mirror.alert.api.dto.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
public class AlertMailFilterResponse {
    private String id;
    private String receiver;
    private String sender;
    @JsonProperty("title_include")
    private String titleInclude;
    @JsonProperty("content_include")
    private String contentInclude;
    private Integer active;
    private Integer times;
    @JsonProperty("last_send_time")
    private Date lastSendTime;
    @JsonProperty("strategies")
    private List<AlertMailFilterStrategyResponse> strategyRequestList;
    private String name;
}
