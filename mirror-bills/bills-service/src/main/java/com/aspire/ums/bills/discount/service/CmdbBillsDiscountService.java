package com.aspire.ums.bills.discount.service;

import com.aspire.ums.bills.discount.payload.CmdbBillsDiscount;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 10:11
 */
public interface CmdbBillsDiscountService {

    List<Map<String, String>> getDeptTreeData();

    List<Map<String, String>> getPoolTreeData();

    List<Map<String, String>> getResourceTreeData();

    List<Map<String, String>> getBusinessList(String dept1, String dept2);

    List<Map<String, Object>> getDiscountList(String type);

    Integer commitDiscount(List<CmdbBillsDiscount> discountList, String userId);

    Map<String, Object> queryDiscount(List<String> idList);

}
