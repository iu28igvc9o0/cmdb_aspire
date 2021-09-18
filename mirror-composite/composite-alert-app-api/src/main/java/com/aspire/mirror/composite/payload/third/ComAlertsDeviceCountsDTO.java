package com.aspire.mirror.composite.payload.third;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComAlertsDeviceCountsDTO {
    private ComAlertsLevelCountsResponse x86;
    private ComAlertsLevelCountsResponse cloud;
    private ComAlertsLevelCountsResponse network;
    private ComAlertsLevelCountsResponse safety;
    private ComAlertsLevelCountsResponse storage;
}
