package com.aspire.ums.cmdb.deviceStatistic.payload;


import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class ConfigureChangeResp {
	 // 资源池名称
	 private String idcType;
     // 一级部门
	 private String department1;
	 // 二级部门
	 private String department2;
	 // 业务系统
  	 private String bizSystem;
     // 设备分类
  	 private String deviceClass;
  	 // 设备类型
  	 private String deviceType;
    // 设备数量
  	 private String number; 
}
