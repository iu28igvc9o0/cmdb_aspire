package com.aspire.mirror.alert.server.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.constant
 * @Author: baiwenping
 * @CreateTime: 2020-07-13 15:49
 * @Description: ${Description}
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    TO_DO("0", "待处理"),
    DOING("1", "已确认"),
    FINISH("3", "已完成");
    private String code;
    private String desc;
}
