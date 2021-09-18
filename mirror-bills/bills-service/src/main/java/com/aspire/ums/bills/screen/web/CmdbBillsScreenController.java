package com.aspire.ums.bills.screen.web;

import com.aspire.ums.bills.screen.CmdbBillsScreenAPI;
import com.aspire.ums.bills.screen.payload.ScreenResponse;
import com.aspire.ums.bills.screen.service.CmdbBillsScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsScreenController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-05 19:50
 **/
@RestController
public class CmdbBillsScreenController implements CmdbBillsScreenAPI {

    @Autowired
    private CmdbBillsScreenService billsScreenService;

    @Override
    public ScreenResponse<Map<String, Object>> getMonthBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                             @RequestParam("department") String department,
                                                                             @RequestParam("idcType") String idcType,
                                                                             @RequestParam("resourceType") String resourceType) {
        return billsScreenService.getMonthBillsWithResourceType(chargeTime,department,idcType,resourceType);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(@RequestParam("chargeTime") String chargeTime,
                                                                             @RequestParam("department") String department) {
        return billsScreenService.getQuotaAndBillsWithBiz(chargeTime,department);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithResourceType(@RequestParam("chargeTime") String chargeTime,
                                                                                      @RequestParam("department") String department,
                                                                                      @RequestParam("bizSystem") String bizSystem,
                                                                                      @RequestParam("idcType") String idcType,
                                                                                      @RequestParam("resourceType") String resourceType) {
        return billsScreenService.getQuotaAndBillsWithResourceType(chargeTime, department, bizSystem, idcType, resourceType);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountRevenueRecord(@RequestParam("year") String year,
                                                                       @RequestParam("department") String department) {
        return billsScreenService.getAccountRevenueRecord(year,department);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getUnitPrice(@RequestParam("bizSystem") String bizSystem,
                                                                  @RequestParam("department") String department,
                                                                  @RequestParam("idcType") String idcType) {
        return billsScreenService.getUnitPrice(bizSystem, department, idcType);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountInfo(@RequestParam("department") String department,
                                                              @RequestParam("monthEnd") String monthEnd) {
        return billsScreenService.getAccountInfo(department, monthEnd);
    }

    @Override
    public List<Map<String, Object>> exportBillsDetail(@RequestParam("department") String department,
                                                       @RequestParam("chargeTime") String chargeTime) {
        return billsScreenService.exportBillsDetail(department,chargeTime);
    }

    @Override
    public List<Map<String, Object>> getSpecificBillsWithResourceType(String department, String chargeTime) {
        return billsScreenService.getSpecificBillsWithResourceType(department, chargeTime);
    }
}
