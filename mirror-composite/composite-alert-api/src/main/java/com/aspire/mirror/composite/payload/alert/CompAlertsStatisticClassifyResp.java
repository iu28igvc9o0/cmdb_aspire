package com.aspire.mirror.composite.payload.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertsStatisticClassifyResp {
    private String deviceType;

    private Integer alertNum;
}
