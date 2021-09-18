package com.aspire.mirror.alert.api.dto.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertStatisticSummaryDTO {

    private Integer summary = 0;

    private Integer tip = 0;

    private Integer low = 0;

    private Integer medium = 0;

    private Integer high = 0;

    private Integer confirmed = 0;

    private Integer serious = 0;

    private Integer observed = 0;
}
