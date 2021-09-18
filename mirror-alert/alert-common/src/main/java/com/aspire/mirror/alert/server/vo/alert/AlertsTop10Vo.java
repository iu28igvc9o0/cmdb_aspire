package com.aspire.mirror.alert.server.vo.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsTop10Vo {

    private String colName;

    private double rate;//内容占比

    private Integer count;
}
