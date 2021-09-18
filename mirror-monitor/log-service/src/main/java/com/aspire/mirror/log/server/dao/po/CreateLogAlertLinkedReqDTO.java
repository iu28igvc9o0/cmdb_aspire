package com.aspire.mirror.log.server.dao.po;

import lombok.Data;

@Data
public class CreateLogAlertLinkedReqDTO {

    /**
     * 日志告警规则id
     */
    private String logAlertRuleUuid;
    /**
     * 告警id
     */
    private String AlertId;
}
