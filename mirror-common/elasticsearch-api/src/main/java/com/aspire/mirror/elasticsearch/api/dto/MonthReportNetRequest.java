package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;
import java.util.Map;

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
public class MonthReportNetRequest {
   
    private String startTime;
    
    private String endTime;
    
    private String idcType;
    
    private String deviceType;
    //cpu、memory、disk
    private String item;
    
    private String granularity;
  
    //统计的那个字段：department1、department2、bizSistem
    private String col;
    private String colValue;
    
    private int topn;
    
    private Map<String,Map<String,Object>> dataMap;
    
    private List<String> departments;
 
    private String ips;
    List<NetConfig>  netConfigs;
    
    boolean isIn;
    
}
