package com.aspire.mirror.alert.server.dao.leakScan.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class SecurityLeakScanReportFile {
    private Integer id;
    private String scanId;
    private String fileName;
    private String ftpPath;
    private Date createTime;
}
