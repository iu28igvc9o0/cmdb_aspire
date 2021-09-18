package com.migu.tsg.microservice.atomicservice.composite.controller.bills.screen;

import com.aspire.mirror.composite.service.bills.screen.CmdbBillsOperationScreenAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.screen.CmdbBillsOperationScreenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-20 08:48
 */
@RestController
public class CmdbBillsOperationScreenController implements CmdbBillsOperationScreenAPI {

    @Autowired
    private CmdbBillsOperationScreenClient operationScreenClient;


    @Override
    public List<Map<String, String>> getDeptTypeList() {
        return operationScreenClient.getDeptTypeList();
    }

    @Override
    public List<Map<String, String>> getTotalExpense(@RequestParam("chargeTime") String chargeTime) {
        return operationScreenClient.getTotalExpense(chargeTime);
    }

    @Override
    public Map<String, List<Map<String, String>>> getResourcesMonthBills(@RequestParam("idcId") String idcId,
                                                                         @RequestParam("chargeTime") String chargeTime,
                                                                         @RequestParam("deptTypeId") String deptTypeId) {
        return operationScreenClient.getResourcesMonthBills(idcId, chargeTime, deptTypeId);
    }

    @Override
    public List<Map<String, String>> getMonthBills(@RequestParam("deptTypeId") String deptTypeId,
                                                   @RequestParam("chargeTime") String chargeTime) {
        return operationScreenClient.getMonthBills(deptTypeId, chargeTime);
    }
}
