package com.aspire.mirror.alert.api.dto.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertSummaryResponse {
    private Integer summary;

    private Integer tip;

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer confirmed;

    private Integer serious;

    private Integer toBeConfirm;
}
