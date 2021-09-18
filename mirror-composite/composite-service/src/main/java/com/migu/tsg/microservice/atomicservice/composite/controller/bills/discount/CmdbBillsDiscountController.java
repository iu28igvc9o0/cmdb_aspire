package com.migu.tsg.microservice.atomicservice.composite.controller.bills.discount;

import com.aspire.ums.bills.discount.payload.CmdbBillsDiscountRequest;

import com.aspire.mirror.composite.service.bills.discount.CmdbBillsDiscountAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.discount.CmdbBillsDiscountClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-13 11:05
 */
@RestController
public class CmdbBillsDiscountController implements CmdbBillsDiscountAPI {

    @Autowired
    private CmdbBillsDiscountClient discountClient;


    @Override
    public List<Map<String, String>> getDeptTreeData() {
        return discountClient.getDeptTreeData();
    }

    @Override
    public List<Map<String, String>> getPoolTreeData() {
        return discountClient.getPoolTreeData();
    }

    @Override
    public List<Map<String, String>> getResourceTreeData() {
        return discountClient.getResourceTreeData();
    }

    @Override
    public List<Map<String, String>> getBusinessList(@RequestParam("dept1") String dept1,
                                                     @RequestParam("dept2") String dept2) {
        return discountClient.getBusinessList(dept1, dept2);
    }

    @Override
    public List<Map<String, Object>> getDiscountList(@RequestParam("type") String type) {
        return discountClient.getDiscountList(type);
    }

    @Override
    public Integer commitDiscount(@RequestBody CmdbBillsDiscountRequest discountRequest) {
        return discountClient.commitDiscount(discountRequest);
    }

    @Override
    public Map<String, Object> queryDiscount(@RequestBody List<String> ids) {
        return discountClient.queryDiscount(ids);
    }
}
