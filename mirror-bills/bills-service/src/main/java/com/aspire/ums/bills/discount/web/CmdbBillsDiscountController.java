package com.aspire.ums.bills.discount.web;

import com.aspire.ums.bills.config.RequestAuthContext;
import com.aspire.ums.bills.discount.CmdbBillsDiscountAPI;
import com.aspire.ums.bills.discount.payload.CmdbBillsDiscountRequest;
import com.aspire.ums.bills.discount.service.CmdbBillsDiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 09:51
 */
@Slf4j
@RestController
public class CmdbBillsDiscountController implements CmdbBillsDiscountAPI {

    @Autowired
    CmdbBillsDiscountService billsDiscountService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getDeptTreeData() {
        log.info("获取租户折扣树数据");
        return billsDiscountService.getDeptTreeData();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getPoolTreeData() {
        log.info("获取资源池折扣树数据");
        return billsDiscountService.getPoolTreeData();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getResourceTreeData() {
        log.info("获取资源折扣树数据");
        return billsDiscountService.getResourceTreeData();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getBusinessList(String dept1, String dept2) {
        log.info("获取部门下挂载的业务系统列表，一级部门id：{}，二级部门id：{}", dept1, dept2);
        return billsDiscountService.getBusinessList(dept1, dept2);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, Object>> getDiscountList(String type) {
        log.info("获取折扣列表，类型：{}", type);
        return billsDiscountService.getDiscountList(type);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Integer commitDiscount(@RequestBody CmdbBillsDiscountRequest discountRequest) {
        log.info("新增折扣，参数：{}", discountRequest);
        String userId = RequestAuthContext.getRequestHeadUserName();
        return billsDiscountService.commitDiscount(discountRequest.getDiscountList(), userId);
    }

    @Override
    public Map<String, Object> queryDiscount(@RequestBody List<String> ids) {
        log.info("查询折扣，ids：{}", ids);
        return billsDiscountService.queryDiscount(ids);
    }


}
