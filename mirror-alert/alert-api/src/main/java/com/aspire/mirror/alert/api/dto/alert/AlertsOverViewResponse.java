package com.aspire.mirror.alert.api.dto.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsOverViewResponse {
    private Integer tip;

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer serious;

    private Integer confirmed;

    private Integer summary;

    private Integer observed;
}
