package com.aspire.mirror.alert.server.vo.alert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsStatisticSourceClassifyVo {

    private String idcType;

    private String sourceRoom;

    private Integer alertNum;
}
