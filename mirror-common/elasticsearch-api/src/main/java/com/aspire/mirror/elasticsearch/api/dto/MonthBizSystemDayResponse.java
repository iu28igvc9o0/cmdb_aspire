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
public class MonthBizSystemDayResponse {
   
    private String date;//2020-04-01
    
    private String dateDisplsy;//1号/2号
    
    private String idcType;
    
    private Float cpuAvg;
    
    private Float cpuMax;
    
    private Float memoryAvg;
    
    private Float memoryMax;
    
    private String ip;
    private String resourceId;
    private String deviceType;

}
