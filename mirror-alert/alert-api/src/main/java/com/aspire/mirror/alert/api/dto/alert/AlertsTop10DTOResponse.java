package com.aspire.mirror.alert.api.dto.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsTop10DTOResponse {

    private String colName;

    private double rate;//内容占比

    private Integer count;
}
