package com.aspire.mirror.elasticsearch.api.dto;

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
public class RouteQueryRequest {
   
    private String idcType;
    
    private String ip;
    
    private String collectDate;
    
    private String routeProto;
    
    private String routeDest;
    
    private String routeMask;
    
    private String routeNextHop;
    
   private Integer pageNum;
    
    private Integer pageSize;

}
