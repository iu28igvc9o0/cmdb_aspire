package com.aspire.mirror.composite.payload.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 安全漏洞扫描数据汇总返回类    <br/>
 * Project Name:index-proxy
 * File Name:CompLeakScanSummaryResponse.java
 * Package Name:com.aspire.mirror.composite.service.alert.payload
 * ClassName: CompLeakScanSummaryResponse <br/>
 * date: 2019年7月20日 下午17:05:37 <br/>
 * @author liangjun
 * @version
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class CompLeakScanSummaryResponse {
    private String id;
    private String department1;
    private String department2;
    private String bizLine;
    private int highLeaks;
    private int mediumLeaks;
    private int lowLeaks;
    private String reportFileName;
    private String reportFileUrl;
    private Date scanDate;
    private String bpmId;
    private int repairStat;
}
