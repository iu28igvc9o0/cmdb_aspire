package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReciverDto {
    /**
     * uuid
     */
    private String id;
    /**
     * 通知人手机号
     */
    private String telephone;
    /**
     * 规则id
     */
    private String alertSubscribeRulesId;
    /**
     * 通知对象的email
     */
    private String email;
    /**
     * 通知对象
     */
    private String notifyObjInfo;
}
