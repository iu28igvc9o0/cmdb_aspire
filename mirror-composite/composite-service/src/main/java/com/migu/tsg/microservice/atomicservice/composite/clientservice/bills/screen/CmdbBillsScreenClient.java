package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.screen;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.bills.screen.payload.ScreenResponse;

/**
 * @projectName: CmdbBillsScreenClient
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-08-07 10:46
 **/
@FeignClient(value = "BILLS", url = "http://10.12.70.39:2228")
public interface CmdbBillsScreenClient {

    @RequestMapping(value = "/v1/cmdb/bill/screen/resourceType",method = RequestMethod.GET)
    ScreenResponse<Map<String,Object>> getMonthBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                     @RequestParam("department") String department,
                                                                     @RequestParam("idcType") String idcType,
                                                                     @RequestParam("resourceType") String resourceType);


    @RequestMapping(value = "/v1/cmdb/bill/screen/quotaAndBill/biz",method = RequestMethod.GET)
    ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(@RequestParam("chargeTime") String chargeTime,
                                                                      @RequestParam("department") String department);

    @RequestMapping(value = "/v1/cmdb/bill/screen/quotaAndBill/resource",method = RequestMethod.GET)
    ScreenResponse<List<Map<String,Object>>> getQuotaAndBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                              @RequestParam("department") String department,
                                                                              @RequestParam("bizSystem") String bizSystem,
                                                                              @RequestParam("idcType") String idcType,
                                                                              @RequestParam("resourceType") String resourceType);

    @RequestMapping(value = "/v1/cmdb/bill/screen/account/record",method = RequestMethod.GET)
    ScreenResponse<Map<String,Object>> getAccountRevenueRecord(@RequestParam("year") String year,
                                                               @RequestParam("department") String department);

    @RequestMapping(value = "/v1/cmdb/bill/screen/unitPrice",method = RequestMethod.GET)
    ScreenResponse<List<Map<String,Object>>> getUnitPrice(@RequestParam("bizSystem") String bizSystem,
                                                          @RequestParam("department") String department,
                                                          @RequestParam("idcType") String idcType);

    @RequestMapping(value = "/v1/cmdb/bill/screen/account/info",method = RequestMethod.GET)
    ScreenResponse<Map<String,Object>> getAccountInfo(@RequestParam("department") String department,
                                                      @RequestParam("monthEnd") String monthEnd);


    @RequestMapping(value = "/v1/cmdb/bill/screen/download/bills",method = RequestMethod.GET)
    List<Map<String,Object>> exportBillsDetail(@RequestParam("department") String department,
                                               @RequestParam("chargeTime") String chargeTime);

    @RequestMapping(value = "/v1/cmdb/bill/screen/bills/detail",method = RequestMethod.GET)
    List<Map<String,Object>> getSpecificBillsWithResourceType(@RequestParam("department") String department,
                                                              @RequestParam("chargeTime") String chargeTime);
}
