package com.aspire.ums.cmdb.maintenance.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名称: HardwareUseRequest
 * 类描述: 硬件维保使用查询实体
 * 创建人: PJX
 * 创建时间:
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class HardwareUseRequest {

     // 分页页标
     private int pageSize;

     // 每页数量
     private int pageNum;

     // 所属业务
     @JsonProperty("system_name")
     private String systemName;

     // 设备名称
     private String deviceName;
     // 设备类型
     private String deviceType;

     // 设备型号
     private String deviceModel;


     // 开始时间
     @JsonProperty("warranty_date_before")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private String warrantyDateBefore;

     // 结束时间
     @JsonProperty("warranty_date_after")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private String warrantyDateAfter;

     // 资产编号
     @JsonProperty("assets_number")
     private String assetsNumber;

     // 厂商
     private String serverProduce;

     // 设备序列号
     private String deviceSerialNumber;

}
