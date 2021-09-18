package com.aspire.mirror.alert.api.dto.monthReport;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsLevelCountsResponse {

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer serious;


}
