package com.aspire.mirror.alert.server.vo.alert;

import lombok.Data;

@Data
public class AlertMainSummaryVo {

    private String deviceClass;

    private AlertStatisticSummaryVo alertStatisticSummaryVo;
}
