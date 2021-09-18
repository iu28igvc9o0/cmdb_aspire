package com.aspire.mirror.alert.api.dto.alert;

import com.aspire.mirror.alert.api.dto.alert.AlertStatisticSummaryDTO;
import lombok.Data;

@Data
public class AlertMainSummaryResponse {

    private String deviceClass;

    private AlertStatisticSummaryDTO alertStatisticSummaryDTO;
}
