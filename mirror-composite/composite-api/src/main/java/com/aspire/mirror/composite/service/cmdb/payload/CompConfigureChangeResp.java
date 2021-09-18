package com.aspire.mirror.composite.service.cmdb.payload;

 

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CompConfigureChangeResp {
	
	 
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
 	 
 	 
    // 设备数量
 	 private String number; 
	

}
