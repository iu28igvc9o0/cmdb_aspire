package com.aspire.ums.bills.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbBillsAccountRecord {
    private String id;

    private String accountId;

    private Double amount;

    private Integer dealType;

    private String payMethod;

    private String dealMonth;

    private Date insertTime;

    private String insertPerson;

    private Date updateTime;

    private String updatePerson;

    private Integer isDelete;
}