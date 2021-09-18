package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsStatisticTrend {

    private String level;

    private String section;

    private Integer alertNum;
}
