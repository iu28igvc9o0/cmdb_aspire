package com.aspire.ums.cmdb.maintenance.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 域名操作请求实体
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.ums.cmdb.allocate.util
 * 类名称:    AllocateDomainRequest.java
 * 类描述:    域名操作请求实体
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 11:08
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class MaintenHardwareRequest { 

     // id
 	 private String id;
 	
 	// 省份
     private String province;
     
     // 市/区
     private String city;
     
     // 资源池
     @JsonProperty("resource_pool")
     private String resourcePool;
     
     // 所属业务 
     @JsonProperty("system_name")
     private String systemName;
     
     //设备分类
     @JsonProperty("device_classify")
     private String deviceClassify;
     
     // 设备类型
     @JsonProperty("device_type")
     private String deviceType;
     
     // 设备型号
     @JsonProperty("device_model")
     private String deviceModel;
     
     // 设备名称
     @JsonProperty("device_name")
     private String deviceName;
     
     // 设备序列号
     @JsonProperty("device_serial_number")
     private String deviceSerialNumber;
     
     // 资产编号 
     @JsonProperty("assets_number")
     private String assetsNumber;
     
     // 出保日期 
     @JsonProperty("warranty_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date warrantyDate;
     
     // 是否购买维保  0-否 1-是 
     @JsonProperty("buy_mainten")
     private String buyMainten ;
     
     // 是否原厂维保  0-否 1-是 
     @JsonProperty("origin_buy")
     private String originBuy ;
     
     // 原厂维保购买必要性说明
     @JsonProperty("origin_buy_explain")
     private String originBuyExplain ;
     
     // 业务建议维保厂家
     @JsonProperty("advice_mainten_factory")
     private String adviceMaintenFactory;
     
     // 维保厂家
     @JsonProperty("mainten_factory")
     private String maintenFactory;
     
     // 维保供应商联系方式
     @JsonProperty("mainten_supply_contact")
     private String maintenSupplyContact;
     
      // 维保厂家联系方式
     @JsonProperty("mainten_factory_contact")
     private String maintenFactoryContact;
 	
     // 本期维保开始时间
     @JsonProperty("mainten_begin_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date maintenBeginDate;
     
     // 本期维保结束时间
     @JsonProperty("mainten_end_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date maintenEndDate;
     
     // 实际构买维保类型
     @JsonProperty("real_mainten_type")
     private String realMaintenType;
     
     // 管理员
     private String admin;
    
   
}
