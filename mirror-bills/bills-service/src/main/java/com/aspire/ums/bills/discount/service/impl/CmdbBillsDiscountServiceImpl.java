package com.aspire.ums.bills.discount.service.impl;

import com.aspire.ums.bills.discount.mapper.CmdbBillsDiscountMapper;
import com.aspire.ums.bills.discount.payload.CmdbBillsDiscount;
import com.aspire.ums.bills.discount.service.CmdbBillsDiscountService;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.service.BillLogService;
import com.aspire.ums.bills.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyihan
 * @date 2020-08-04 10:12
 */
@Slf4j
@Service
public class CmdbBillsDiscountServiceImpl implements CmdbBillsDiscountService {

    @Autowired
    private CmdbBillsDiscountMapper billsDiscountMapper;
    @Autowired
    private BillLogService billLogService;

    @Value("${cmdb.schema.name}")
    private String tablePrefix;

    @Override
    public List<Map<String, String>> getDeptTreeData() {
        List<Map<String, String>> list = billsDiscountMapper.queryDeptTreeData(tablePrefix);
        return format(list);
    }

    @Override
    public List<Map<String, String>> getPoolTreeData() {
        return billsDiscountMapper.getPoolTreeData(tablePrefix);
    }

    @Override
    public List<Map<String, String>> getResourceTreeData() {
        List<Map<String, String>> list = billsDiscountMapper.getResourceTreeData(tablePrefix);
        return format(list);
    }

    private List<Map<String, String>> format(List<Map<String, String>> list) {
        Map<String, List<Map<String, String>>> groupMap = list.stream().collect(Collectors.groupingBy(m -> m.get("pId")));
        List<Map<String, String>> resultList = new LinkedList<>();
        List<Map<String, String>> deptList1 = groupMap.get("0");
        Map<String, String> tempMap;
        // 遍历一级部门
        for (Map<String, String> map : deptList1) {
            String id = map.get("id");
            tempMap = map;
            String name = map.get("name");
            tempMap.remove("name");
            tempMap.put("name1", name);
            resultList.add(tempMap);
            if (groupMap.containsKey(id) && groupMap.get(id).size() > 0) {
                resultList.addAll(groupMap.get(id));
            }
        }
        return resultList;
    }

    @Override
    public List<Map<String, String>> getBusinessList(String dept1, String dept2) {
        if (StringUtils.isEmpty(dept1)) {
            dept1 = null;
        }
        if (StringUtils.isEmpty(dept2)) {
            dept2 = null;
        }
        return billsDiscountMapper.getBusinessList(tablePrefix, dept1, dept2);
    }

    @Override
    public List<Map<String, Object>> getDiscountList(String type) {
        return billsDiscountMapper.getDiscountList(type);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public Integer commitDiscount(List<CmdbBillsDiscount> discountList, String userId) {
        // 查询当前类别下的所有折扣
        List<CmdbBillsDiscount> current = billsDiscountMapper.queryDiscounts();
        Map<String, List<CmdbBillsDiscount>> collect = current.stream().collect(Collectors.groupingBy(CmdbBillsDiscount::getId));
        List<CmdbBillsDiscount> list = new ArrayList<>();
        for (CmdbBillsDiscount discount : discountList) {
            if (collect.containsKey(discount.getId())) {
                if (!discount.equals(collect.get(discount.getId()).get(0))) {
                    addUpdateDiscountList(userId, list, discount);
                }
            } else {
                addUpdateDiscountList(userId, list, discount);
            }
        }
        if (list.size() > 0) {
            billsDiscountMapper.commitDiscount(list);
            try {
                for (CmdbBillsDiscount discount : list) {
                    Map<String, Object> discountInfo = billsDiscountMapper.getDiscountById(tablePrefix, discount.getId());
                    BillsLog billsLog = new BillsLog(null,null,"折扣","更新","调整"+ discountInfo.get("resName") +"折扣" + discountInfo.get("discount"),null);
                    billLogService.saveBillLog(billsLog);
                }
            } catch (Exception e) {
                log.error("折扣更新记录日志失败,error:{}", e.getMessage());
            }
        }
        return null;
    }

    private void addUpdateDiscountList(String userId, List<CmdbBillsDiscount> list, CmdbBillsDiscount discount) {
        discount.setResId(UUIDUtil.getUUID());
        discount.setInsertPerson((userId == null) ? "admin" : userId);
        discount.setInsertTime(new Date());
        discount.setUpdatePerson((userId == null) ? "admin" : userId);
        discount.setUpdateTime(new Date());
        list.add(discount);
    }

    @Override
    public Map<String, Object> queryDiscount(List<String> idList) {
        return billsDiscountMapper.queryDiscount(idList);
    }

}
