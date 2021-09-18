package com.aspire.ums.cmdb.deviceStatistic.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 低效无效资源接口类
 * <p>
 * 项目名称:   mirror平台
 * 包:        com.aspire.ums.cmdb.deviceStatistic.payload
 * 类名称:    InvalidResourceRequest.java
 * 类描述:    低效无效资源接口类
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class InvalidResourceRequest {
    
	 
    
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
     
  
   
}
