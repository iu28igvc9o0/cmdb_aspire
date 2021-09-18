package com.aspire.mirror.alert.api.dto.third;

import com.aspire.mirror.alert.api.dto.monthReport.AlertsLevelCountsResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsDeviceCountsDTO {
    private AlertsLevelCountsResponse x86;
    private AlertsLevelCountsResponse cloud;
    private AlertsLevelCountsResponse network;
    private AlertsLevelCountsResponse safety;
    private AlertsLevelCountsResponse storage;
}
