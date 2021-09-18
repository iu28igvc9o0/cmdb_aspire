package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsStatisticOverview {

    private String deviceClass;

    private String alertLevel;

    private Integer operateStatus;

    private Integer count;
}
