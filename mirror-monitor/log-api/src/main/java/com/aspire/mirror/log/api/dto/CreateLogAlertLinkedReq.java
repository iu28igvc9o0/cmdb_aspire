package com.aspire.mirror.log.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLogAlertLinkedReq {

    /**
     * 日志告警规则id
     */
    private String logAlertRuleUuid;
    /**
     * 告警id
     */
    private String AlertId;
}
