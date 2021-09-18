package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmergencySubscribeRulesEmailRequestDto {
    /**
     * 邮箱内容
     */
    private String emails;
    /**
     * 邮箱主题
     */
    private String subject;
    /**
     * 获取到告警id
     */
    private String AlertIds;

}
