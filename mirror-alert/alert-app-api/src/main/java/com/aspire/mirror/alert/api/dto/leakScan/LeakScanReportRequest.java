package com.aspire.mirror.alert.api.dto.leakScan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
public class LeakScanReportRequest {

    @NotNull
    @JsonProperty("scan_id")
    private String scanId;
    /**
     * 分页
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_no")
    private int pageNo;

    @JsonProperty("sort_name")
    private String sortName;

    private String order;
}
