package com.aspire.ums.bills.calculate.web;

import com.aspire.ums.bills.calculate.CmdbBillsRecordAPI;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import com.aspire.ums.bills.calculate.service.CmdbBillsDayRecordService;
import com.aspire.ums.bills.calculate.service.CmdbBillsGeneratorService;
import com.aspire.ums.bills.calculate.service.CmdbBillsMonthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsRecordController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-03 19:50
 **/
@RestController
public class CmdbBillsRecordController implements CmdbBillsRecordAPI {
    @Autowired
    private CmdbBillsMonthRecordService monthRecordService;
    @Autowired
    private CmdbBillsDayRecordService dayRecordService;
    @Autowired
    private CmdbBillsGeneratorService generatorService;

    @Override
    public List<Map<String,Object>> listBillMonthRecord(@RequestParam("chargeTime") String chargeTime) {
        return monthRecordService.listBillsWithMonth(chargeTime);
    }

    @Override
    public Map<String, Object> updateBillMonth(@RequestBody CmdbBillsMonthBill monthBill) {
        return monthRecordService.updateBillMonth(monthBill);
    }

    @Override
    public Map<String, Object> manuallyGeneratedMonthBills(@RequestParam("chargeTime") String chargeTime) {
        return generatorService.manuallyGeneratedMonthBills(chargeTime);
    }

    @Override
    public Map<String, Object> manuallyGeneratedDayBills(@RequestParam("chargeTime") String chargeTime) {
        return generatorService.manuallyGeneratedDayBills(chargeTime);
    }

    @Override
    public Map<String, Object> manuallyGeneratedDayBillsByMonth(@RequestParam("chargeTime") String chargeTime) {
        return generatorService.manuallyGeneratedDayBillsByMonth(chargeTime);
    }
}
