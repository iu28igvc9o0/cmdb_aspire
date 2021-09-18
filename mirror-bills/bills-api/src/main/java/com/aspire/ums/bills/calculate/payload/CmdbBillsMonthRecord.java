package com.aspire.ums.bills.calculate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsMonthRecord {
    private String      id;

    private String      departmentId;       // 部门ID

    private String      businessSystemId;   // 业务系统ID

    private String      idcId;              // 资源池ID

    private String      podId;              // POD池ID

    private String      deviceTypeId;       // 设备类型ID

    private String      accountId;          // 账号ID

    private Long      needPay;            // 应缴费用

    private Long      realPay;            // 实缴费用

    private Long      discountBeforePay;  // 折扣前费用

    private String      chargeTime;         // 录入时间

    private Date        insertTime;         // 录入时间

    private String      insertPerson;       // 录入人

    private Date        updateTime;         // 最后修改时间

    private String      updatePerson;       // 最后修改人

    private Integer     isDelete;           // 是否删除 1:已删除  0:未删除
}