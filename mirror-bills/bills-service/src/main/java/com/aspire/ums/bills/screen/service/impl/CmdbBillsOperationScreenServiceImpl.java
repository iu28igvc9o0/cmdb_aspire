package com.aspire.ums.bills.screen.service.impl;

import com.aspire.ums.bills.screen.mapper.CmdbBillsOperationScreenMapper;
import com.aspire.ums.bills.screen.service.CmdbBillsOperationScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyihan
 * @date 2020-08-14 19:20
 */
@Service
public class CmdbBillsOperationScreenServiceImpl implements CmdbBillsOperationScreenService {

    @Value("${cmdb.schema.name}")
    private String tablePrefix;

    @Autowired
    private CmdbBillsOperationScreenMapper operationMapper;

    @Override
    public List<Map<String, String>> getDeptTypeList() {
        return operationMapper.getDeptTypeList(tablePrefix);
    }

    @Override
    public List<Map<String, String>> getTotalExpense(String chargeTime) {
        return operationMapper.getTotalExpense(chargeTime, tablePrefix);
    }

    @Override
    public Map<String, List<Map<String, String>>> getResourcesMonthBills(
                                        String idcId, String chargeTime, String deptTypeId) {
        List<Map<String, String>> resources =
                operationMapper.getResourcesMonthBills(idcId, chargeTime, deptTypeId, tablePrefix);
        return resources.stream().collect(Collectors.groupingBy(m -> m.get("parent")));
    }

    @Override
    public List<Map<String, String>> getMonthBills(String deptTypeId, String chargeTime) {
        return operationMapper.getMonthBills(deptTypeId, chargeTime, tablePrefix);
    }
}
