package com.aspire.ums.bills.screen.service.impl;

import com.aspire.ums.bills.calculate.service.CmdbFeginService;
import com.aspire.ums.bills.client.CmdbFeignClient;
import com.aspire.ums.bills.screen.mapper.CmdbBillsScreenMapper;
import com.aspire.ums.bills.screen.payload.ScreenResponse;
import com.aspire.ums.bills.screen.service.CmdbBillsScreenService;
import com.aspire.ums.bills.util.BigDecimalCalculateUtils;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @projectName: CmdbBillsScreenServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-05 19:50
 **/
@Service
public class CmdbBillsScreenServiceImpl implements CmdbBillsScreenService {
    @Autowired
    private CmdbBillsScreenMapper billsScreenMapper;
    @Autowired
    private CmdbFeginService feginService;
    @Value("${cmdb.schema.name}")
    private String cmdbTableName;
    @Autowired
    private CmdbFeignClient cmdbFeignClient;

    @Override
    public ScreenResponse<Map<String, Object>> getMonthBillsWithResourceType(String chargeTime,
                                                                             String department,
                                                                             String idcType,
                                                                             String resourceType) {
        Map<String,Object> dataMp = new HashMap<>();
        List<Map<String, Object>> baseList = billsScreenMapper.getMonthBillsWithResourceType(chargeTime, department, idcType, resourceType,cmdbTableName);
        String total = "0";
        // 为空
        if(CollectionUtils.isEmpty(baseList)) {
            List<Map<String, Object>> billsDeviceTypes = feginService.getBillsDeviceTypes(resourceType);
            for(Map<String, Object> item : billsDeviceTypes) {
                Map<String,Object> tmp = new HashMap<>();
                tmp.put("totalMoney",0);
                tmp.put("deviceType",item.get("value"));
                baseList.add(tmp);
            }
            dataMp.put("total",total);
            dataMp.put("list",baseList);
            return new ScreenResponse(true,dataMp);
        }
        for(Map<String, Object> item : baseList) {
            total = BigDecimalCalculateUtils.stringAddWithBigDecimal(total,item.get("totalMoney").toString());
        }
        dataMp.put("total",BigDecimalCalculateUtils.doubleAddWithBigDecimal("0",total));
        dataMp.put("list",baseList);
        return new ScreenResponse(true,dataMp);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithBiz(String chargeTime, String department) {
        List<Map<String, Object>> baseList = billsScreenMapper.getQuotaAndBillsWithBiz(chargeTime,department,cmdbTableName);
        return new ScreenResponse(true,baseList);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getQuotaAndBillsWithResourceType(String chargeTime,
                                                                                      String department,
                                                                                      String bizSystem,
                                                                                      String idcType,
                                                                                      String resourceType) {
        List<Map<String, Object>> baseList = billsScreenMapper.getQuotaAndBillsWithResourceType(chargeTime, department, bizSystem, idcType, resourceType,cmdbTableName);
        Map<String, Object> quota = billsScreenMapper.getQuotaWithBiz(bizSystem, idcType,department,cmdbTableName);
        for(Map<String, Object> item : baseList) {
            item.put("quota",quota.get(item.get("code").toString()));
            // 单位 来自()的内容
            Map<String,String> param = new HashMap<>();
            param.put("filedCode",item.get("code").toString());
            Map<String, String> simpleCode = cmdbFeignClient.getSimpleCode(param);
            String filedName = simpleCode.get("filedName");
            if(filedName.indexOf('(') != -1 && filedName.indexOf(')') != -1) {
                String unit = filedName.substring(filedName.indexOf('(') + 1,filedName.indexOf(')'));
                item.put("unit",unit);
            } else {
                item.put("unit","单位有误");
            }

        }
        return new ScreenResponse(true,baseList);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountRevenueRecord(String year,String department) {
        List<Map<String, Object>> baseList = billsScreenMapper.getAccountRevenueRecord(year,department);
        // 初始化，返回值
        Map<String, Object> unit = new LinkedHashMap<>();
        for(int i=1;i<=12;i++) {
            Map<String, Object> minxUnit = new HashMap<>();
            minxUnit.put("amountTotal",0);
            minxUnit.put("record",new ArrayList<Map<String,Object>>());
            String addKey = year + "." + (i < 10 ? "0" + i : "" + i);
            unit.put(addKey,minxUnit);
        }
        for(Map<String, Object> item : baseList) {
            String key = item.get("xKey").toString();
            if(unit.containsKey(key)) {
                Map<String, Object> minUnit = (Map<String, Object>) unit.get(key);
                minUnit.put("amountTotal",BigDecimalCalculateUtils
                        .doubleAddWithBigDecimal(minUnit.get("amountTotal").toString(),item.get("amount").toString()));
                List<Map<String,Object>> record = (List<Map<String, Object>>) minUnit.get("record");
                record.add(item);
            }
        }
        return new ScreenResponse(true,unit);
    }

    @Override
    public ScreenResponse<List<Map<String, Object>>> getUnitPrice(String bizSystem, String department, String idcType) {
        List<Map<String, Object>> baseList = billsScreenMapper.getUnitPrice(bizSystem, department, idcType);
        return new ScreenResponse(true,baseList);
    }

    @Override
    public ScreenResponse<Map<String, Object>> getAccountInfo(String department,String monthEnd) {
        String year = monthEnd.split("-")[0];
        String monthBegin = year + "-01";  // 起始 1月开始
        Map<String, Object> baseMp = billsScreenMapper.getAccountInfo(department, monthBegin, monthEnd,cmdbTableName);
        return new ScreenResponse(true,baseMp);
    }

    @Override
    public List<Map<String, Object>> exportBillsDetail(String department, String chargeTime) {
        // 分配
        List<Map<String, Object>> allQuota = billsScreenMapper.getAllQuota(department,cmdbTableName);
        Map<String,Object> priceIndexMp = dealPricesFormat();
        Map<String,Object> monthIndexMp = dealMonthBillsFormat(department, chargeTime);
        for(Map<String, Object> item : allQuota) {
            String idc = item.get("idc") == null ? "" : item.get("idc").toString();
            String biz = item.get("biz") == null ? "" : item.get("biz").toString();
            String pod = item.get("pod") == null ? "" : item.get("pod").toString();
            // 合并单价
            if(priceIndexMp.containsKey(idc)) {
                Map<String,String> price = (Map<String, String>) priceIndexMp.get(idc);
                item.putAll(price);
            }
            String monthKey = idc + biz + pod;
            // 合并月总价
            if(monthIndexMp.containsKey(monthKey)) {
                Map<String,String> month = (Map<String, String>) monthIndexMp.get(monthKey);
                item.putAll(month);
            }
        }
        return allQuota;
    }

    @Override
    public List<Map<String, Object>> getSpecificBillsWithResourceType(String department, String chargeTime) {
        List<Map<String, Object>> resourceTypes = feginService.getResourceTypes();
        List<Map<String,Object>> resultList = new ArrayList<>();
        for(Map<String, Object> item : resourceTypes) {
            // 基础数据
            String resourceTypeId = item.get("id").toString();
            String resourceTypeName = item.get("name").toString();
            Map<String,Object> resourceMp = new HashMap<>();
            List<Map<String, Object>> idcLists = new ArrayList<>();
            // 获取资源池级别的数据
            List<Map<String, Object>> monthBills = billsScreenMapper.getSpecificBillsWithResourceType(department, chargeTime, resourceTypeId,cmdbTableName);
            Map<String,List<Map<String,Object>>> billsBaseMp = new HashMap<>();
            for(Map<String, Object> bill : monthBills) {
                String idc = bill.get("idcName").toString();
                List<Map<String,Object>> tmpList = billsBaseMp.containsKey(idc) ? billsBaseMp.get(idc) : new ArrayList<>();
                tmpList.add(bill);
                billsBaseMp.put(idc,tmpList);
            }

            // 组装JSON返回格式
            for(Map.Entry<String,List<Map<String,Object>>> entry : billsBaseMp.entrySet()) {
                Map<String,Object> device = new HashMap<>();
                device.put("idcName",entry.getKey());
                device.put("billsDeviceType",entry.getValue());
                idcLists.add(device);
            }
            resourceMp.put("resourceType",resourceTypeName);
            resourceMp.put("idcNameList",idcLists);
            resultList.add(resourceMp);
        }
        return resultList;
    }

    // 单价格式处理，方便与基础数据合并
    private Map<String,Object> dealPricesFormat() {
        List<Map<String, Object>> allPrice = billsScreenMapper.getAllPrice(cmdbTableName);
        Map<String,Object> priceMp = new HashMap<>();
        for(Map<String, Object> item : allPrice) {
            String idc = item.get("idcId") == null ? "" : item.get("idcId").toString();
            String[] codes = (item.get("codes") == null ? "" : item.get("codes").toString()).split(",");
            String[] prices = (item.get("prices") == null ? "" : item.get("prices").toString()).split(",");
            Map<String,Object> price = new HashMap<>();
            for(int i=0;i<codes.length;i++) {
                price.put(codes[i]+"_price",prices[i]);
            }
            priceMp.put(idc,price);
        }
        return priceMp;
    }

    // 当月总价格式处理，方便与基础数据合并
    private Map<String,Object> dealMonthBillsFormat(String department, String chargeTime){
        List<Map<String, Object>> allMonthBills = billsScreenMapper.getAllMonthBills(department, chargeTime,cmdbTableName);
        Map<String,Object> monthBillsMp = new HashMap<>();
        for(Map<String, Object> item : allMonthBills) {
            String idc = item.get("idcId") == null ? "" : item.get("idcId").toString();
            String biz = item.get("bizId") == null ? "" : item.get("bizId").toString();
            String pod = item.get("podId") == null ? "" : item.get("podId").toString();
            String key = idc+ biz + pod;
            String[] codes = (item.get("codes") == null ? "" : item.get("codes").toString()).split(",");
            String[] pays = (item.get("pays") == null ? "" : item.get("pays").toString()).split(",");
            Map<String,Object> payMp = new HashMap<>();
            for(int i=0;i<codes.length;i++) {
                payMp.put(codes[i]+"_month",pays[i]);
            }
            payMp.put("totalMoney",item.get("totalMoney"));
            monthBillsMp.put(key,payMp);
        }
        return monthBillsMp;
    }
}
