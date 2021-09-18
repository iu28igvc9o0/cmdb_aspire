package com.aspire.mirror.composite.service.alert.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
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
public class ComTenantRateRequest {
   
    private String startTime;
    
    private String endTime;
    //按照avg/max值排序
    private String type;
    
    private String deviceType;
    
    private String item;
    //统计字段  department1 department2 bizSystem
    private String col;
    //是否取全部true全部
    private boolean orderflag = true;
    //排序方式 desc、asc
    private String sort;
    
    int topNum ;
    
 

}
