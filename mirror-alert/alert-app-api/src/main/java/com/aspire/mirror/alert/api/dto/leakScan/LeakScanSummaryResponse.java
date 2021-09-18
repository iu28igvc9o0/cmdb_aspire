package com.aspire.mirror.alert.api.dto.leakScan;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 安全漏洞扫描数据汇总返回类    <br/>
 * Project Name:index-proxy
 * File Name:ItemMonitorEventCallBackRequest.java
 * Package Name:com.aspire.mirror.indexproxy.indexprocess.model
 * ClassName: ItemMonitorEventCallBackRequest <br/>
 * date: 2019年7月16日 下午21:25:37 <br/>
 * @author liangjun
 * @version
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class LeakScanSummaryResponse {

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
