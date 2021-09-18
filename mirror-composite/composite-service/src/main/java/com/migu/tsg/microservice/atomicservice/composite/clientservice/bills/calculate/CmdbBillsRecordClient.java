// package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.calculate;
//
// import java.util.Map;
//
// import org.springframework.cloud.netflix.feign.FeignClient;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;
//
// import com.aspire.mirror.common.entity.PageResult;
// import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
//
/// **
// * @projectName: CmdbBillsRecordClient
// * @description: 类
// * @author: luowenbo
// * @create: 2020-08-05 17:23
// **/
// @FeignClient(value = "BILLS", url = "http://10.12.70.39:2228")
// public interface CmdbBillsRecordClient {
//
// @RequestMapping(value = "/v1/cmdb/bill/monthRecord/list", method = RequestMethod.GET)
// PageResult<Map<String, Object>> listBillMonthRecord(@RequestParam("chargeTime") String chargeTime,
// @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize);
//
// @RequestMapping(value = "/v1/cmdb/bill/monthRecord/update", method = RequestMethod.PUT)
// Map<String, Object> updateBillMonth(@RequestBody CmdbBillsMonthBill monthBill);
//
// @RequestMapping(value = "/v1/cmdb/bill/monthRecord/manuallyCreate", method = RequestMethod.PUT)
// Map<String, Object> manuallyGeneratedMonthBills(@RequestParam("chargeTime") String chargeTime);
// }
