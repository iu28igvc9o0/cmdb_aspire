package com.aspire.mirror.composite.payload.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompAlertStatisticSummaryResp {
    /**
     * 待确认
     */
    private AlertStatisticSummaryDTO toBeConfirmed;
    /**
     * 已确认
     */
    private AlertStatisticSummaryDTO confirmed;
    /**
     * 待解决
     */
    private AlertStatisticSummaryDTO toBeResolved;
    /**
     * 已解除
     */
    private AlertStatisticSummaryDTO resolved;
    /**
     * 已清除
     */
    private AlertStatisticSummaryDTO cleared;
}
