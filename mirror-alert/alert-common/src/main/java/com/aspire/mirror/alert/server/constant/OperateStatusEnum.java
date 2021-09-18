package com.aspire.mirror.alert.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.constant
 * @Author: baiwenping
 * @CreateTime: 2020-06-20 16:53
 * @Description: ${Description}
 */
@Getter
@AllArgsConstructor
public enum OperateStatusEnum {
    TO_DO(0, "待处理"),
    DOING(1, "已确认"),
    FINISH(3, "已完成"),
    TO_WATCH(4, "待观察"),
    BUSINESS_ALERT(5, "业务告警"),
    EXCEPTION_ALERT(6, "异常信息");

    private Integer code;
    private String desc;
}
