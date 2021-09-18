package com.aspire.mirror.composite.service.cmdb.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  设备统计请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.cmdb.payload
 * 类名称:    CompDeviceStatisticRequest.java
 * 类描述:    域名操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class CompDeviceStatisticRequest {
    
 
		// 资源池名称
		@JsonProperty("idc_type")
		private String idcType;
		
		
		// POD名称
		@JsonProperty("pod_name")
		private String podName;
		
		
		 // 设备分类
		 @JsonProperty("device_class")
		 private String deviceClass;
		 
		 // 设备类型
		 @JsonProperty("device_type")
		 private String deviceType;
		 
		 //  品牌
		 @JsonProperty("device_mfrs")
		 private String deviceMfrs;
		
		
		 // 业务系统 
		 @JsonProperty("biz_system")
		 private String bizSystem;
		 	 
		   
		 // 一级部门
		 private String department1;
		 
		 // 二级部门
		 private String department2;

		
   
}
