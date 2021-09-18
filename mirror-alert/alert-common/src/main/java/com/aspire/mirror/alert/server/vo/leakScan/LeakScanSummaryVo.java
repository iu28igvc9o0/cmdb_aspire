package com.aspire.mirror.alert.server.vo.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class LeakScanSummaryVo {
    private String id;
    private String department1;
    private String department2;
    private String bizLine;
    private String reportFileName;
    private String reportFileUrl;
    private Date scanDate;
    private String bpmId;
    private int repairStat;
    private int lowLeaks;
    private int mediumLeaks;
    private int highLeaks;
}
