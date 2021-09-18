package com.aspire.ums.cmdb.maintenance.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名称:
 * 类描述:
 * 创建人:
 * 创建时间:
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class HardwareRequest {

     // 分页页标
     private int pageSize;

     // 每页数量
     private int pageNum;

     //开始的下标
     private int startPageNum;

     @JsonProperty("assets_number")
     private String assetsNumber;

     @JsonProperty("buy_mainten")
     private String buyMainten;

     @JsonProperty("device_name")
     private String deviceName;

     @JsonProperty("device_serial_number")
     private String deviceSerialNumber;

     @JsonProperty("origin_buy")
     private String originBuy;

     @JsonProperty("resource_pool")
     private String resourcePool;

     @JsonProperty("system_name")
     private String systemName;

     @JsonProperty("warranty_date_after")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private String warrantyDateAfter;

     @JsonProperty("warranty_date_before")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private String warrantyDateBefore;

     private String type;

     @JsonProperty("project_name")
     private String projectName;

     @JsonProperty("device_sn")
     private String deviceSn;

     public void setStartPageNum() {
          if (0 < this.pageNum) {
               this.startPageNum = (this.pageNum - 1) * this.pageSize;
          } else {
               this.startPageNum = 0;
          }
     }
    
   
}
