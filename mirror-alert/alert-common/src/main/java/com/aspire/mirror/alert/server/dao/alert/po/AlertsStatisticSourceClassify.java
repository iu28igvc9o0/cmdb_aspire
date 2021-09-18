package com.aspire.mirror.alert.server.dao.alert.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertsStatisticSourceClassify {

    private String idcType;

    private String sourceRoom;

    private Integer alertNum;
}
