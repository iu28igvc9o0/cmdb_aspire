package com.aspire.mirror.elasticsearch.api.dto;

import java.util.List;

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
public class TenantRateRequest {
   
    private String startTime;
    
    private String endTime;
    
    private String type;
    
    private String deviceType;
    
    private String department1;
    
    private String item;
    
    private String col;
    
    private boolean orderflag;
    
    private String sort;
    
    private int topNum ;
    
    private String idcType;
    
    private List<String> department1List;
    private List<String> department2List;
 

}
