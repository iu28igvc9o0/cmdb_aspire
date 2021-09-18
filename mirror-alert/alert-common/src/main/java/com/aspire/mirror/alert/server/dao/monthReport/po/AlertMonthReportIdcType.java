package com.aspire.mirror.alert.server.dao.monthReport.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自动化派单类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao.po
 * 类名称:    AlertConfig.java
 * 类描述:    自动化告警po类
 * 创建人:    qianch
 * 创建时间:  2019/1/24 16:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class AlertMonthReportIdcType {
	
	private String idcType;
   
    private String cpu_max;
    
   
    private String cpu_avg;
    
    private String memory_avg;

    /**
     * 创建时间
     */
    private String memory_max;

    /**
     * IS_START
     */
    private String deviceType;//X86服务器或者云主机
    
    private String cpu_eighty_ratio;//40-80
    
    private String cpu_fourty_ratio;//15-40
    
    private String cpu_fifteen_ratio;//<15
    
    private String cpu_eighty_more_ratio;//>80
    
    private String memory_eighty_ratio;
    
    private String memory_fourty_ratio;
    
    private String memory_fifteen_ratio;
    
    private String memory_eighty_more_ratio;
    
    private String month;
    
    private String create_time;
    
    private String bizSystem;
    
    private String day;
    
    private String type;
    
    private String block_rate;
    
    private String san_rate;

}
