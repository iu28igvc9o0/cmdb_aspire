package com.aspire.mirror.alert.api.dto.monthReport;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class AlertMonthReportAllDTO {
	
	private String idcType;
	private String bizSystem;
	
	
    private String phy_cpu_max;
    private String phy_cpu_avg;
    private String phy_memory_avg;
    private String phy_memory_max;
    private String phy_cpu_eighty_ratio;//40-80
    private String phy_cpu_fourty_ratio;//15-40
    private String phy_cpu_fifteen_ratio;//<15
    private String phy_cpu_eighty_more_ratio;//>80
    private String phy_memory_eighty_ratio;
    private String phy_memory_fourty_ratio;
    private String phy_memory_fifteen_ratio;
    private String phy_memory_eighty_more_ratio;
  
    
    
    private String vm_cpu_max;
    private String vm_cpu_avg;
    private String vm_memory_avg;
    private String vm_memory_max;
    private String vm_cpu_eighty_ratio;//40-80
    private String vm_cpu_fourty_ratio;//15-40
    private String vm_cpu_fifteen_ratio;//<15
    private String vm_cpu_eighty_more_ratio;//>80
    private String vm_memory_eighty_ratio;
    private String vm_memory_fourty_ratio;
    private String vm_memory_fifteen_ratio;
    private String vm_memory_eighty_more_ratio;
    
    private String month;
    private int low;

    private int medium;

    private int high;

    private int serious;
    
    private int sum;
    

    
 
    

}
