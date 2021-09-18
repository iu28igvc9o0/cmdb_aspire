package com.aspire.mirror.alert.server.vo.bpm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertBpmCallBack {

    private String status = "0";
    private String message;
    private String runId;

}
