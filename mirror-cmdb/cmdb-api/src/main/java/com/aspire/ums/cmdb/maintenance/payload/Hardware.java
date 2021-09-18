package com.aspire.ums.cmdb.maintenance.payload;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class Hardware {

     // id
     private String id;

     private String projectInstanceId;

     private String instanceId;

     private String moduleId;

     private String moduleCode;

     // 设备序列号
     private String deviceSerialNumber;

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

     // IP
     private String ip;

     // 资产编号
     private String assetsNumber;

     // 合同编号
     private String projectNo;

     // 合同供应商
     private String contractProduce;

     // 合同供应商联系人名称
     private String contractProduceName;

     // 合同供应商联系人邮箱
     private String contractProduceEmail;

     // 合同供应商联系人电话
     private String contractProducePhone;

     // 维保供应商
     private String maintenProduce;

     // 维保供应商联系人名称
     private String maintenProduceName;

     // 维保供应商联系人邮箱
     private String maintenProduceEmail;

     // 维保供应商联系人电话
     private String maintenProducePhone;

     /**
      * 维保类型
      */
     private String maintenanceType;
     /**
      *  设备区域
      * */
     private String deviceArea;
     /**
      *  维保对象类型
      * */
     private String maintenanceProjectType;
     /**
      *  采购类型
      * */
     private String procureType;
     /**
      *  金额
      * */
     private BigDecimal money;
     // 项目名称ID
     private String projectId;

     // 项目名称
     private String projectName;

     // 服务类型
     private String serviceType;

     // 服务开始时间
     @JsonProperty("service_start_time")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date serviceStartTime;
     // 出保时间
     @JsonProperty("warranty_date")
     @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
     private Date warrantyDate;

     // 是否购买维保  0-否 1-是
     private String buyMainten ;

     // 是否原厂维保  0-否 1-是
     private String originBuy ;

     // 原厂维保购买必要性说明
     private String originBuyExplain ;

     // 维保管理员
     private String admin;

     // 税前
     private BigDecimal preTax;

     // 税率
     private String taxRate;

     // 税后
     private BigDecimal afterTax;

     // 单价
     private BigDecimal unitPrice;

     // 总价
     private BigDecimal totalPrice;

     // 结算方式
     private String payMethod;

     // 折扣钱金额
     private BigDecimal discountAmount;

     // 折扣率
     private String discountRate;
}
