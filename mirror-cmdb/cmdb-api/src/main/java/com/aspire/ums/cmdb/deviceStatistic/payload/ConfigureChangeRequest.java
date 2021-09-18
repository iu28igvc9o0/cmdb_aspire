package com.aspire.ums.cmdb.deviceStatistic.payload;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配置项变更统计请求实体
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.ums.cmdb.deviceStatistic.payload
 * 类名称:    ConfigureChangeRequest.java
 * 类描述:    配置项变更统计请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class ConfigureChangeRequest extends GeneralAuthConstraintsAware {
    
	 
	
	  // 开始时间
	 @JsonProperty("start_date")
	 private String startDate;
	 
	 
	 // 结束时间
	 @JsonProperty("end_date")
	 private String endDate;
	
    
	 // 资源池名称
     @JsonProperty("idc_type")
	 private String idcType;
     
     
     // 一级部门
  	 private String department1;
  	 
  	 // 二级部门
  	 private String department2;
  	 
  	 
  	 // 业务系统 
  	 @JsonProperty("biz_system")
  	 private String bizSystem;
  	 
      
     // 设备分类
  	 @JsonProperty("device_class")
  	 private String deviceClass;
  	 
  	 
  	 // 设备类型
  	 @JsonProperty("device_type")
  	 private String deviceType;
  	 
  	  
   
}
