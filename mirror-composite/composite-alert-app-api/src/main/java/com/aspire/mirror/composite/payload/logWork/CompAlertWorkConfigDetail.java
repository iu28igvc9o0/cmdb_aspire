package com.aspire.mirror.composite.payload.logWork;

import lombok.Data;

@Data
public class CompAlertWorkConfigDetail {

    private String uuid;
    private String dayStartTme;
    private String dayEndTme;
    private String nightStartTme;
    private String nightEndTme;
}
