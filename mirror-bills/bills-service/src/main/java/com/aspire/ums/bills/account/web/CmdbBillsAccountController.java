package com.aspire.ums.bills.account.web;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.CmdbBillsAccountAPI;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.service.CmdbBillsAccountService;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.calculate.service.CmdbBillsMonthRecordService;
import com.aspire.ums.bills.calculate.service.CmdbFeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsAccountController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-11 15:24
 **/
@RestController
public class CmdbBillsAccountController implements CmdbBillsAccountAPI {
    @Autowired
    private CmdbBillsMonthRecordService monthRecordService;
    @Autowired
    private CmdbBillsAccountService accountService;
    @Autowired
    private CmdbFeginService feginService;

    @Override
    public List<Map<String, Object>> getAccountPayRecord(@RequestParam("type") String type ,
                                                         @RequestParam("chargeTime") String chargeTime) {
        String department = feginService.getInnerDepartmentFlag();
        return monthRecordService.listMonthBillsWithDepartment(type,department,chargeTime);
    }

    @Override
    public Map<String, Object> getAccountPayRecordDetail(@RequestParam("departmentId")String departmentId,
                                                         @RequestParam("chargeTime")String chargeTime) {
        return monthRecordService.listMonthBillsByDepartment(departmentId, chargeTime);
    }

    @Override
    public Result updateAccountRecord(@RequestBody CmdbBillsMonthRecord monthRecord) {
        return monthRecordService.updateRealPay(monthRecord);
    }

    @Override
    public CmdbBillsAccountBalance getAccountBydepartment(@RequestParam("department") String departmentId) {
        return accountService.selectByDepartment(departmentId);
    }
}
