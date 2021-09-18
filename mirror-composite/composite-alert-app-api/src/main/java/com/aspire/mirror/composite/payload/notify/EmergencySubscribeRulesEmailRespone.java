package com.aspire.mirror.composite.payload.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmergencySubscribeRulesEmailRespone {
    /**
     * 邮箱内容
     */
    private String emails;
    /**
     * 邮箱主题
     */
    private String subject;
}
