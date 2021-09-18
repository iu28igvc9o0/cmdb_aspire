package com.aspire.mirror.zabbixintegrate.bean;

import com.aspire.mirror.zabbixintegrate.daoCmdb.po.CmdbInstance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 历史告警详情
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.api.dto
 * 类名称:    AlertsHisDetailResponse.java
 * 类描述:    历史告警详情
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:18
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuyanMonitorConfig {

    private String name_;

    private String sy_key_;
    
    private String key_;
    
    private String uint_;
    
    private String index_;
    
    private String sy_uint_;
    
    private String operator_;
    
    private String operator_value_;
    
    private String id;
   
}
