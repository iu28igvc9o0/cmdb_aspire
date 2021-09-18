package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecurityLeakScanReportVo {
    private String id;
    private String scanId;
    private String ip;
    private String reportPath;
    private Integer highLeaks;
    private Integer mediumLeaks;
    private Integer lowLeaks;
    private Double riskVal;
}
