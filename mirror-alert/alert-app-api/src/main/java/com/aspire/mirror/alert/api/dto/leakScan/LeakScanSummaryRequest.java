package com.aspire.mirror.alert.api.dto.leakScan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;
/**
 * 安全漏洞扫描数据汇总请求类    <br/>
 * Project Name:index-proxy
 * File Name:ItemMonitorEventCallBackRequest.java
 * Package Name:com.aspire.mirror.indexproxy.indexprocess.model
 * ClassName: ItemMonitorEventCallBackRequest <br/>
 * date: 2019年7月17日 上午10:16:37 <br/>
 * @author liangjun
 * @version
 * @since JDK 1.8
 */
@Data
@NoArgsConstructor
public class LeakScanSummaryRequest {
    /**
     * 业务系统
     * 全部
     * ......
     */
    private String bizSys;
    private List<String> bizSysList;

    private String ip;
    /**
     * 漏洞等级
     */
    private String level;
    /**
     * 修复状态
     */
    private Integer repairStat;
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
    
    @JsonProperty("beginDate")
    private String beginDate;
    
    @JsonProperty("endDate")
    private String endDate;
}
