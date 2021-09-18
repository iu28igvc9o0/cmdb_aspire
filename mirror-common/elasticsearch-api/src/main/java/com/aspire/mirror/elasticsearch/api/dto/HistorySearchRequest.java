package com.aspire.mirror.elasticsearch.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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
public class HistorySearchRequest {
    /**
     * 统计方式
     */
    private String countType;

    /**
     * 监控项列表
     */
    private List<String> itemList;
    /**
     * key为资源池
     * value为ip列表
     */
    private Map<String, List<String>> ipMap;
    
    private String kpi;
    /**
             * 设备id列表
     */
    private List<String> idList;
    
    private String startTime;
    
    private String endTime;
    
    private String idcType;

}
