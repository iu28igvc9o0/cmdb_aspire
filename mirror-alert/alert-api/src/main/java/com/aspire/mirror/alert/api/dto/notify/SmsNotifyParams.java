package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.dto
 * @Author: baiwenping
 * @CreateTime: 2020-03-05 16:40
 * @Description: ${Description}
 */
@Data
public class SmsNotifyParams {
    private List<String> mobiles;
    private Map<String, Object> params;
}
