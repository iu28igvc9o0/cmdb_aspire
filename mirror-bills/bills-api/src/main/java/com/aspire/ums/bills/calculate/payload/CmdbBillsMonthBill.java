package com.aspire.ums.bills.calculate.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbBillsMonthBill {
    private String id;
    private String departmentId;       // 部门ID
    private String orgName;
    private String accountId; //缴费账号
    private Long needPay;            // 应缴费用
    private Long realPay;            // 实缴费用
    private String chargeTime;  //归属月份
    private String description; // 说明
    private Integer isDelete;   //是否删除 1:已删除  0:未删除
}