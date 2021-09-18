package com.aspire.mirror.composite.service.cmdb.payload;

 

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CompDeviceStatisticResp {
	
	 
		// 资源池名称
	     @JsonProperty("idc_type")
		 private String idcType;
	    
	    
	    // POD名称
	    @JsonProperty("pod_name")
		 private String podName;
	    
	    
	    // 设备数量
		 private String number;
	    
	    
	    // 服务器数量
	    @JsonProperty("server_number")
		 private String serverNumber;
	    
	    // 网络设备数量
	    @JsonProperty("network_number")
		 private String networkNumber;
	    
	    // 存储设备数量
	    @JsonProperty("storage_number")
		 private String storageNumber;
	    
	    // 安全设备数量
	    @JsonProperty("safety_number")
		 private String safetyNumber;
	    
	    // 其他设备数量
	     @JsonProperty("other_number")
		 private String otherNumber;
	    
	    
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
