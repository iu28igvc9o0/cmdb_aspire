package com.aspire.mirror.composite.payload.logWork;

import lombok.Data;

import java.util.Date;

@Data
public class ComAlertWorkConfigData {

    private String uuid;
    private String dayStartTme;
    private String dayEndTme;
    private String nightStartTme;
    private String nightEndTme;

}
