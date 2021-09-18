package com.aspire.ums.cmdb.maintenance.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 类名称: HardwareUse
 * 类描述: 硬件维保使用结果实体
 * 创建人: PJX
 * 创建时间:
 * 版本:      v1.0
 */
@NoArgsConstructor
@Data
public class HardwareUse {

     // id
 	 private String id;
     // 硬件维保ID
 	 private String projectId;

 	 private String projectName;

     private String projectInstanceId;

     // 资源池
     private String resourcePool;

     // 一级部门
     private String department1;

     // 二级部门
     private String department2;

     // 业务系统
     private String systemName;

     // 设备分类
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

     // 服务厂商
     private String serverProduce;

     // 服务人员
     private String serverPerson;

     private String hardWareId;

     // 服务级别
     private String serverLevel;

     // 开始时间
     @JsonProperty("start_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date startDate;

     // 结束时间
     @JsonProperty("end_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date endDate;

     // 处理时长
     private String processTime;

     // 实际人天
     private String actualManDay;

     // 移动审批人
     private String mobileApprover;

     // 运维审批人
     private String operateApprover;

     // 创建人
     private String creater;

     // 创建时间
//     @JsonProperty("create_time")
//     @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone="GMT+8")
//     private Date createTime;

     // 出保时间
     @JsonProperty("warranty_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date warrantyDate;

}
