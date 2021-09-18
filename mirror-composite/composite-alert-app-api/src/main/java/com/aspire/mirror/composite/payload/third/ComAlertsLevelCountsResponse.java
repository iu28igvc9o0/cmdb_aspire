package com.aspire.mirror.composite.payload.third;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComAlertsLevelCountsResponse {

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer serious;


}
