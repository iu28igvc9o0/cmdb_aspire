package com.aspire.mirror.alert.server.vo.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsStatisticClassifyVo {
    private String deviceType;

    private Integer alertNum;
}
