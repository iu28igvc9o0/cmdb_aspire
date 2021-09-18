package com.aspire.mirror.composite.payload.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComAlertsTop10DTOResponse {

    private String colName;

    private double rate;//内容占比

    private Integer count;
}
