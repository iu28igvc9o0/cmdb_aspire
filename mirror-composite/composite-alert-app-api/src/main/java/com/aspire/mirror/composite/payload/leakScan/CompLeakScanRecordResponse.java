package com.aspire.mirror.composite.payload.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 安全漏洞扫描数据汇总返回类    <br/>
 * Project Name:index-proxy
 * File Name:CompLeakScanReportResponse.java
 * Package Name:com.aspire.mirror.composite.service.alert.payload
 * ClassName: CompLeakScanReportResponse <br/>
 * date: 2019年7月16日 下午21:25:37 <br/>
 * @author liangjun
 * @version
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class CompLeakScanRecordResponse {
    private String id;
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
