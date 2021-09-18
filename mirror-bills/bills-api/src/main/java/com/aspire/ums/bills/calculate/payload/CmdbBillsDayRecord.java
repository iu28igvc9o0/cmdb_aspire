package com.aspire.ums.bills.calculate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsDayRecord {
    private String      id;

    private String      departmentId;       // 部门ID

    private String      businessSystemId;   // 业务系统ID

    private String      idcId;              // 资源池ID

    private String      podId;              // POD池ID

    private String      deviceTypeId;       // 设备类型ID

    private String     quota;              // 配额

    private Double      discount;           // 折扣

    private Double      price;              // 每日单价

    private Double      discountBefore;      // 折扣前总价

    private Double      totalMoney;         // 当天总价

    private Date        chargeTime;         // 计费日期

    private Date        insertTime;         // 录入时间

    private String      insertPerson;       // 录入人

    private Date        updateTime;         // 最后修改时间

    private String      updatePerson;       // 最后修改人

    private Integer     isDelete;           // 是否删除 1:已删除  0:未删除
}