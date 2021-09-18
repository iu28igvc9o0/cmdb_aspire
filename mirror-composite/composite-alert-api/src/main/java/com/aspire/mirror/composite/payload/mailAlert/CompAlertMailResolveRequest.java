package com.aspire.mirror.composite.payload.mailAlert;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
@NoArgsConstructor
public class CompAlertMailResolveRequest {
    @JsonProperty("filter_id")
    private String filterId;
    @JsonProperty("gather_time_range_start")
    private String gatherTimeRangeStart;
    private Date gatherTimeStart;
    @JsonProperty("gather_time_range_end")
    private String gatherTimeRangeEnd;
    private Date gatherTimeEnd;
    @JsonProperty("send_time_range_start")
    private String sendTimeRangeStart;
    private Date sendTimeStart;
    @JsonProperty("send_time_range_end")
    private String sendTimeRangeEnd;
    private Date sendTimeEnd;
    @JsonProperty("mail_title")
    private String mailTitle;
    @JsonProperty("mail_content")
    private String mailContent;
    @JsonProperty("alert_id")
    private String alertId;
    @NotNull
    @JsonProperty("page_size")
    private Integer pageSize = 20;
    @NotNull
    @JsonProperty("page_no")
    private Integer pageNo = 1;
    @NotEmpty
    @JsonProperty("sort_name")
    private String sortName;
}
