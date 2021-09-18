package com.aspire.mirror.alert.api.dto.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class AlertMailFilterQueryReq {
    private String receiver;
    private String sender;
    @JsonProperty(value = "send_time_start")
    private String sendTimeRangeStart;
    private Date sendStartTime;
    @JsonProperty(value = "send_time_end")
    private String sendTimeRangeEnd;
    private Date sendEndTime;
    private Integer active;
    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize = 20;
    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo = 1;
    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;

    private Integer recipientId;
    
    private String name;
}
