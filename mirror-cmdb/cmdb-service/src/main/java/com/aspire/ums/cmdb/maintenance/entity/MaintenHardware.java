package com.aspire.ums.cmdb.maintenance.entity;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 硬件维保
 */
@Data
@NoArgsConstructor
public class MaintenHardware {  

		// id
		private String id;
		
		// 省份
	    private String province;
	    
	    // 市/区
	    private String city;
	    
	    // 资源池
	    private String resourcePool;
	    
	    // 所属业务 
	    private String systemName;
	    
	    //设备分类
	    private String deviceClassify;
	    
	    // 设备类型
	    private String deviceType;
	    
	    // 设备型号
	    private String deviceModel;
	    
	    // 设备名称
	    private String deviceName;
	    
	    // 设备序列号
	    private String deviceSerialNumber;
	    
	    // 资产编号 
	    private String assetsNumber;
	    
	    // 出保日期 
	    private Date warrantyDate;
	    
	    // 是否购买维保  0-否 1-是 
	    private String buyMainten ;
	    
	    // 是否原厂维保  0-否 1-是 
	    private String originBuy ;
	    
	    // 原厂维保购买必要性说明
	    private String originBuyExplain ;
	    
	    // 业务建议维保厂家
	    private String adviceMaintenFactory;
	    
	    // 维保厂家
	    private String maintenFactory;
	    
	    // 维保供应商联系方式
	    private String maintenSupplyContact;
	    
	     // 维保厂家联系方式
	    private String maintenFactoryContact;
		
	    // 本期维保开始时间
	    private Date maintenBeginDate;
	    
	    // 本期维保结束时间
	    private Date maintenEndDate;
	    
	    // 实际构买维保类型
	    private String realMaintenType;
	    
	    // 管理员
	    private String admin;
	    
      
}
