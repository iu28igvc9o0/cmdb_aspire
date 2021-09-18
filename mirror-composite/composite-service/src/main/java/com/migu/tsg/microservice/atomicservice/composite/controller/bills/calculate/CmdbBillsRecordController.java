// package com.migu.tsg.microservice.atomicservice.composite.controller.bills.calculate;
//
// import com.aspire.mirror.composite.service.bills.calculate.CmdbBillsRecordAPI;
// import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
// import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.calculate.CmdbBillsRecordClient;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.List;
// import java.util.Map;
//
/// **
// * @projectName: CmdbBillsRecordController
// * @description: 类
// * @author: luowenbo
// * @create: 2020-08-05 17:22
// **/
// @RestController
// public class CmdbBillsRecordController implements CmdbBillsRecordAPI {
// @Autowired
// private CmdbBillsRecordClient billsRecordClient;
//
// @Override
// public List<Map<String, Object>> listBillMonthRecord(@RequestParam("chargeTime") String chargeTime) {
// return billsRecordClient.listBillMonthRecord(chargeTime);
// }
//
// @Override
// public Map<String, Object> updateBillMonth(@RequestBody CmdbBillsMonthBill monthBill) {
// return billsRecordClient.updateBillMonth(monthBill);
// }
//
// @Override
// public Map<String, Object> manuallyGeneratedMonthBills(@RequestParam("chargeTime") String chargeTime) {
// return billsRecordClient.manuallyGeneratedMonthBills(chargeTime);
// }
// }
