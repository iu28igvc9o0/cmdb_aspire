package com.aspire.ums.bills.screen.service;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-14 19:19
 */
public interface CmdbBillsOperationScreenService {

    List<Map<String, String>> getDeptTypeList();

    List<Map<String, String>> getTotalExpense(String chargeTime);

    Map<String, List<Map<String, String>>> getResourcesMonthBills(String idcId, String chargeTime, String deptTypeId);

    List<Map<String, String>> getMonthBills(String deptTypeId, String chargeTime);

}
