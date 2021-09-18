package com.aspire.ums.bills.account.service.impl;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.mapper.CmdbBillsAccountBalanceMapper;
import com.aspire.ums.bills.account.mapper.CmdbBillsAccountRecordMapper;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;
import com.aspire.ums.bills.account.service.CmdbBillsAccountService;
import com.aspire.ums.bills.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @projectName: CmdbBillsAccountServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-11 15:23
 **/
@Service
public class CmdbBillsAccountServiceImpl implements CmdbBillsAccountService {
    @Autowired
    private CmdbBillsAccountBalanceMapper accountBalanceMapper;
    @Autowired
    private CmdbBillsAccountRecordMapper accountRecordMapper;
    @Value("${cmdb.schema.name}")
    private String cmdbTableName;

    @Override
    public List<CmdbBillsAccountRecord> getAccountRecord(String account, String dealTime, String dealType) {
        return accountRecordMapper.getAccountRecord(account,dealTime,dealType);
    }

    @Override
    public Result deleteAccountRecordById(String accountRecordId) {
        CmdbBillsAccountRecord record = new CmdbBillsAccountRecord();
        record.setId(accountRecordId);
        record.setIsDelete(1);
        Result result = new Result();
        if (accountRecordMapper.updateByPrimaryKeySelective(record) > 0) {
            result = Result.success();
        } else {
            result.setResultCode("202");
            result.setResultDes("delete fail.");
        }
        return result;
    }

    @Override
    public Result updateAccountRecord(CmdbBillsAccountRecord accountRecord) {
        Result result = new Result();
        if (accountRecordMapper.updateByPrimaryKeySelective(accountRecord) > 0) {
            result = Result.success();
        } else {
            result.setResultCode("202");
            result.setResultDes("update fail.");
        }
        return result;
    }

    @Override
    public Result insertAccountRecord(CmdbBillsAccountRecord accountRecord) {
        if(null != accountRecord) {
            accountRecord.setId(UUIDUtil.getUUID());
            accountRecord.setIsDelete(0);
            accountRecord.setInsertTime(new Date());
        }
        Result result = new Result();
        if (accountRecordMapper.insert(accountRecord) > 0) {
            result = Result.success();
        } else {
            result.setResultCode("202");
            result.setResultDes("insert fail.");
        }
        return result;
    }

    @Override
    public CmdbBillsAccountBalance selectByDepartment(String department) {
        return accountBalanceMapper.selectByDepartment(cmdbTableName, department);
    }

    @Override
    public Result insertAccountBalance(CmdbBillsAccountBalance accountBalance) {
        Result result = new Result();
        if(null != accountBalance) {
            accountBalance.setId(UUIDUtil.getUUID());
            accountBalance.setIsDelete(0);
            accountBalance.setInsertTime(new Date());
            if (accountBalanceMapper.insert(accountBalance) > 0) {
                result.setResultCode("200");
                result.setResultDes(accountBalance.getId());
            } else {
                result.setResultCode("202");
                result.setResultDes("insert fail.");
            }
        }
        return result;
    }

    @Override
    public Result updateAccountBalance(CmdbBillsAccountBalance accountBalance) {
        Result result = new Result();
        if (accountBalanceMapper.updateByPrimaryKeySelective(accountBalance) > 0) {
            result = Result.success();
        } else {
            result.setResultCode("202");
            result.setResultDes("update fail.");
        }
        return result;
    }

    @Override
    public String selectNextNumber() {
        return accountBalanceMapper.selectNextNumber();
    }
}
