package com.aspire.mirror.alert.server.dao.bpm.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class AlertTuningRecord {

    private String alertId;

    private String orderId;

    private String orderType;

    private String orderStatus;

    private Date createTime;
}
