package com.aspire.ums.bills.screen.web;

import com.aspire.ums.bills.screen.CmdbBillsOperationScreenAPI;
import com.aspire.ums.bills.screen.service.CmdbBillsOperationScreenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-14 19:17
 */
@Slf4j
@RestController
public class CmdbBillsOperationScreenController implements CmdbBillsOperationScreenAPI {

    @Autowired
    private CmdbBillsOperationScreenService operationService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getDeptTypeList() {
        log.info("获取租户类别列表");
        return operationService.getDeptTypeList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getTotalExpense(String chargeTime) {
        log.info("租户月度费用统计，时间：{}", chargeTime);
        return operationService.getTotalExpense(chargeTime);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<Map<String, String>>> getResourcesMonthBills(String idcId, String chargeTime, String deptTypeId) {
        log.info("各类产品月度总账单展示，资源池id：{}，时间：{}", idcId, chargeTime);
        return operationService.getResourcesMonthBills(idcId, chargeTime, deptTypeId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getMonthBills(String deptTypeId, String chargeTime) {
        log.info("租户月度账单列表，租户类型id：{}，时间：{}", deptTypeId, chargeTime);
        return operationService.getMonthBills(deptTypeId, chargeTime);
    }
}
