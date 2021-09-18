package com.aspire.mirror.alert.server.dao.leakScan.po;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SecurityLeakScanReport {
    private String id;
    private String scanId;
    private String ip;
    private String reportPath;
    private Integer highLeaks;
    private Integer mediumLeaks;
    private Integer lowLeaks;
    private Double riskVal;
    private String idcType;
}
