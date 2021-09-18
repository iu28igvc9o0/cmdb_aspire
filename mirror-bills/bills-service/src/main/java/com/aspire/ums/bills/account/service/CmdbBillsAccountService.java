package com.aspire.ums.bills.account.service;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;

import java.util.List;

/**
 * @projectName: CmdbBillsAccountService
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-11 15:22
 **/
public interface CmdbBillsAccountService {

    // 缴费记录

    List<CmdbBillsAccountRecord> getAccountRecord(String account,String dealTime,String dealType);
    Result deleteAccountRecordById(String accountRecordId);
    Result updateAccountRecord(CmdbBillsAccountRecord accountRecord);
    Result insertAccountRecord(CmdbBillsAccountRecord accountRecord);

    // 账号

    CmdbBillsAccountBalance selectByDepartment(String department);
    Result insertAccountBalance(CmdbBillsAccountBalance accountBalance);
    Result updateAccountBalance(CmdbBillsAccountBalance accountBalance);
    String selectNextNumber();
}
