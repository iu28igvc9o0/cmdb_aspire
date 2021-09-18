package com.aspire.ums.cmdb.deviceStatistic.payload;

 

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class InvalidResourceResp {
 
     
	
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
  	 
  	 
      // POD名称
     @JsonProperty("pod_name")
	 private String podName;
     
     // 物理计算资源(台)
     @JsonProperty("physical_number")
	 private String physicalNumber;
     
     
     // 虚拟计算资源(台)
     @JsonProperty("virtual_number")
	 private String virtualNumber;
     
     
     // 计划资源回收/清理时间
     @JsonProperty("plan_time")
	 private String planTime;
     
     
     // 实际资源回收/清理时间
     @JsonProperty("reality_time")
	 private String realityTime;
     
     
 		

}
