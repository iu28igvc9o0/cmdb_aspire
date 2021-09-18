package com.aspire.mirror.composite.payload.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertsOverViewResponse {
    private Integer tip;

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer serious;

    private Integer confirmed;

    private Integer summary;

    private Integer observed;
}
