package com.aspire.mirror.zabbixintegrate.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.api.dto
 * 类名称:    HistorySearchRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 20:18
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertSuyanMonitor {
    
	private String resourceId;

    private String  metricName;
    
    private String metricNodeName;
    
    private long datetime;
    
    private long time;
    
    private Object value;
 

}
