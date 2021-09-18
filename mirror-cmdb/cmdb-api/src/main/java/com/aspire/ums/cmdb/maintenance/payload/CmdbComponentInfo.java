package com.aspire.ums.cmdb.maintenance.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/*
*  维保部件信息实体
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbComponentInfo {
    // 主键
    private String id;
    // 维保合同ID(部件绑定的合同)
    private String maintenanceId;
    // 部件规格型号
    private String specificModel;
    // 配置描述
    private String configDesc;
    // 部件序列号
    private String serialNumber;
    // 部件名称
    private String componentName;
    // 单位
    private String unit;
    // 数量
    private String quantity;
    // 不含税单价
    private BigDecimal unitNotTax;
    // 不含税价格
    private BigDecimal moneyNotTax;
    // 增值税率或征收率
    private String addTax;
    //增值税税额
    private BigDecimal addAmount;
    // 含税价格
    private BigDecimal moneyWithTax;
}
