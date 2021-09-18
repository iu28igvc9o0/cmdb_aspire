package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProcessLoadResponseVo {
    private String runId;
    private String actInstId;
    private boolean result;
    private String message;
    private String status;
}
