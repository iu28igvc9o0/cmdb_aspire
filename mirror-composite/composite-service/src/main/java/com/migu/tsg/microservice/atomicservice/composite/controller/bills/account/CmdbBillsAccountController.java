package com.migu.tsg.microservice.atomicservice.composite.controller.bills.account;

import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.composite.service.bills.account.CmdbBillsAccountAPI;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.account.CmdbBillsAccountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsAccountAPI
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-12 20:26
 **/
@RestController
public class CmdbBillsAccountController implements CmdbBillsAccountAPI {
    @Autowired
    private CmdbBillsAccountClient accountClient;

    @Override
    public List<Map<String, Object>> getAccountPayRecord(@RequestParam("type") String type ,
                                                         @RequestParam("chargeTime") String chargeTime) {
        return accountClient.getAccountPayRecord(type, chargeTime);
    }

    @Override
    public Map<String, Object> getAccountPayRecordDetail(@RequestParam("departmentId")String departmentId,
                                                         @RequestParam("chargeTime")String chargeTime) {
        return accountClient.getAccountPayRecordDetail(departmentId, chargeTime);
    }

    @Override
    public Result updateAccountRecord(@RequestBody CmdbBillsMonthRecord monthRecord) {
        return accountClient.updateAccountRecord(monthRecord);
    }

    @Override
    public CmdbBillsAccountBalance getAccountBydepartment(@RequestParam("department") String departmentId) {
        return accountClient.getAccountBydepartment(departmentId);
    }
}
