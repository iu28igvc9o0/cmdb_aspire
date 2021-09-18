package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
* 描述： 维保项目的实体类
* @author
* @date 2019-07-29 22:31:46
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbMaintenanceProject {

    /**
     * ID
     */
    private String id;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编码
     */
    private String projectNo;
    /**
     * 维保厂商信息
     */
    private String maintenProduce;
    /**
     * 维保厂商联系人名称
     */
    private String maintenProduceName;
    /**
     * 维保厂商联系人邮件
     */
    private String maintenProduceEmail;
    /**
     * 维保厂商联系人电话
     */
    private String maintenProducePhone;
    /*
     *  合同供应商
     * */
    private String contractProduce;
    /*
     *  合同供应商联系人名称
     * */
    private String contractProduceName;
    /*
     *  合同供应商联系人邮件
     * */
    private String contractProduceEmail;
    /*
     *  合同供应商联系人电话
     * */
    private String contractProducePhone;
    /**
     * 服务类型
     */
    private String serviceType;
    /**
     * 服务开始时间
     */
    private String serviceStartTime;
    /**
     * 服务结束时间
     */
    private String serviceEndTime;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 维保类型
     */
    private String maintenanceType;
    /*
    *  设备区域
    * */
    private String deviceArea;
    /*
     *  维保对象类型
     * */
    private String maintenanceProjectType;
    /*
     *  采购类型
     * */
    private String procureType;
    /*
     *  金额
     * */
    private BigDecimal money;
    /*
     *  设备类型
     * */
    private String deviceType;
    /*
    *  税前金额
    * */
    private BigDecimal preTax;
    /*
     *  税率
     * */
    private String taxRate;
    /*
     *  税后金额
     * */
    private BigDecimal afterTax;
    /*
     *  单价
     * */
    private BigDecimal unitPrice;
    /*
     *  总价
     * */
    private BigDecimal totalPrice;
    /*
     *  结算方式
     * */
    private String payMethod;
    /*
     *  折扣后金额
     * */
    private BigDecimal discountAmount;
    /*
     *  折扣率
     * */
    private String discountRate;
    /*
     *  季度标识
     * */
    private String quarterFlag;
    /*
    *  服务数量
    * */
    private List<CmdbMaintenanceServiceNum> serviceNums;
    /**
     * 维保设备列表
     */
    private List<CmdbMaintenanceProjectInstance> projectInstanceList;
}