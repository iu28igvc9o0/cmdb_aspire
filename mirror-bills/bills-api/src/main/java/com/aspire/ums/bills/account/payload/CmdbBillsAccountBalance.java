package com.aspire.ums.bills.account.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbBillsAccountBalance {
    private String id;

    private String departmentId;

    private String orgName;

    private Double balance;

    private String accountCode;

    private String accountManager;

    private String accountManagerPhone;

    private String accountManagerEmail;

    private Date insertTime;

    private String insertPerson;

    private Date updateTime;

    private String updatePerson;

    private Integer isDelete;

    private Integer number;
}