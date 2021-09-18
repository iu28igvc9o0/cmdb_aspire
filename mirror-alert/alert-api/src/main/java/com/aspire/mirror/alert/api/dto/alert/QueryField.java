package com.aspire.mirror.alert.api.dto.alert;

import lombok.Data;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.api.v2.dto
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 00:20
 * @Description: ${Description}
 */
@Data
public class QueryField {
    private String fieldName;
    private String fieldType;
    private String fieldValue;
}
