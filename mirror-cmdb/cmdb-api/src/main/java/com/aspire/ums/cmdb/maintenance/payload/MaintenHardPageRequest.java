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
public class MaintenHardPageRequest {
    
	 
	// 分页页标
    @JsonProperty("page_size")
    private String pageSize;

    // 每页数量
    @JsonProperty("page_no")
    private String pageNo;
	
    
    // 所属业务 
    @JsonProperty("system_name")
    private String systemName;
    
    // 设备名称
    @JsonProperty("device_name")
    private String deviceName;
    
    
    // 出保日期 开始
    @JsonProperty("warranty_date_before")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date warrantyDateBefore;
    
    // 出保日期 结束
    @JsonProperty("warranty_date_after")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date warrantyDateAfter;
    
    // 设备序列号
    @JsonProperty("device_serial_number")
    private String deviceSerialNumber;
    
    // 资产编号 
    @JsonProperty("assets_number")
    private String assetsNumber;
    
    // 资源池
    @JsonProperty("resource_pool")
    private String resourcePool;
    
  
    // 本期维保开始时间开始
    @JsonProperty("mainten_begin_date_before")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenBeginDateBefore;
    
    
    // 本期维保开始时间结束
    @JsonProperty("mainten_begin_date_after")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenBeginDateAfter;
    
   
    // 本期维保结束时间开始
    @JsonProperty("mainten_end_date_before")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenEndDateBefore;
    
    // 本期维保结束时间结束
    @JsonProperty("mainten_end_date_after")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date maintenEndDateAfter;
      
    
    //设备分类
    @JsonProperty("device_classify")
    private String deviceClassify;
    
    // 设备类型
    @JsonProperty("device_type")
    private String deviceType;
    
    // 设备型号
    @JsonProperty("device_model")
    private String deviceModel;
    
    
    // 是否购买维保  0-否 1-是 
    @JsonProperty("buy_mainten")
    private String buyMainten ;
    
    
    // 是否原厂维保  0-否 1-是 
    @JsonProperty("origin_buy")
    private String originBuy ;
    
     
    // 维保厂家
    @JsonProperty("mainten_factory")
    private String maintenFactory;
    
    
    // 实际构买维保类型
    @JsonProperty("real_mainten_type")
    private String realMaintenType;
    
    // 管理员
    private String admin;

 
   
}
