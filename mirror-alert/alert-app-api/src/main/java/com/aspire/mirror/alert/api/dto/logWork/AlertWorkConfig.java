package com.aspire.mirror.alert.api.dto.logWork;

import lombok.Data;

@Data
public class AlertWorkConfig {

    private String uuid;
    private String dayStartTme;
    private String dayEndTme;
    private String nightStartTme;
    private String nightEndTme;
}
