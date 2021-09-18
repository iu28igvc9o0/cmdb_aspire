package com.aspire.mirror.alert.server.vo.bpm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AlertBpmStartCallBack {
    private boolean status = true;
    private String message;
    private int success = 0;
    private int total = 0;
    private String orderIdList;
}
