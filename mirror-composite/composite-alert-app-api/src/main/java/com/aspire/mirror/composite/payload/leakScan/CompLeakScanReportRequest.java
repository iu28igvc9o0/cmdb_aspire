package com.aspire.mirror.composite.payload.leakScan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 安全漏洞扫描数据汇总返回类    <br/>
 * Project Name:index-proxy
 * File Name:CompLeakScanReportRequest.java
 * Package Name:com.aspire.mirror.composite.service.alert.payload
 * ClassName: CompLeakScanSummaryResponse <br/>
 * date: 2019年7月20日 下午17:05:37 <br/>
 * @author liangjun
 * @version
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class CompLeakScanReportRequest {
    @NotNull
    @JsonProperty("scan_id")
    private String scanId;
    /**
     * 分页
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("page_no")
    private int pageNo;

    @JsonProperty("sort_name")
    private String sortName;

    private String order;
}
