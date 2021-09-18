package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.screen;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-20 08:50
 */
@FeignClient("BILLS")
public interface CmdbBillsOperationScreenClient {

    @GetMapping("/v1/cmdb/bill/screen/operation/loadDeptTypeList")
    List<Map<String, String>> getDeptTypeList();

    @GetMapping("/v1/cmdb/bill/screen/operation/expenses")
    List<Map<String, String>> getTotalExpense(@RequestParam("chargeTime") String chargeTime);

    @GetMapping("/v1/cmdb/bill/screen/operation/resources")
    Map<String, List<Map<String, String>>> getResourcesMonthBills(@RequestParam("idcId") String idcId,
                                                                  @RequestParam("chargeTime") String chargeTime,
                                                                  @RequestParam("deptTypeId") String deptTypeId);
    @GetMapping("/v1/cmdb/bill/screen/operation/list")
    List<Map<String, String>> getMonthBills(@RequestParam("deptTypeId") String deptTypeId,
                                            @RequestParam("chargeTime") String chargeTime);

}
