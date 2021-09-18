package com.aspire.ums.bills.calculate.service;

import java.util.Map;

/**
 * @projectName: CmdbBillsGeneratorService
 * @description: 账单生成
 * @author: luowenbo
 * @create: 2020-08-12 20:18
 **/
public interface CmdbBillsGeneratorService {

    /**
     * 手工生成月账单
     * @param chargeTime
     * @return
     */
    Map<String,Object> manuallyGeneratedMonthBills(String chargeTime);

    /**
     * 手工生成日账单
     * @param chargeTime
     * @return
     */
    Map<String,Object> manuallyGeneratedDayBills(String chargeTime);


    Map<String,Object> manuallyGeneratedDayBillsByMonth(String chargeTime);

}
