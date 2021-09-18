package com.aspire.mirror.alert.server.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.config
 * @Author: baiwenping
 * @CreateTime: 2020-07-14 20:13
 * @Description: ${Description}
 */
@ConfigurationProperties(prefix = "alert.order.overtime")
@Data
public class OrderOvertimeProperties {
    private Map<String, Integer> todo;
    private Map<String, Integer> doing;

    private Map<String, Integer> todoRemind;
    private Map<String, Integer> doingRemind;
    private Map<String, String> filterTrans;
}
