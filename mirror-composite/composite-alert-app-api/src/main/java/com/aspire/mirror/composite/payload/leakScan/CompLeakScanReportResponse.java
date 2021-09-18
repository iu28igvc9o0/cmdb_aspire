package com.aspire.mirror.composite.payload.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CompLeakScanReportResponse {
    private String id;
    private String scanId;
    private String ip;
    private String reportPath;
    private Integer highLeaks;
    private Integer mediumLeaks;
    private Integer lowLeaks;
    private Double riskVal;
}
