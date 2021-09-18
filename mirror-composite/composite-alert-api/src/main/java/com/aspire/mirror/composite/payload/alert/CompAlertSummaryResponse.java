package com.aspire.mirror.composite.payload.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertSummaryResponse {
    private Integer summary;

    private Integer tip;

    private Integer low;

    private Integer medium;

    private Integer high;

    private Integer confirmed;

    private Integer serious;

    private Integer toBeConfirm;
}
