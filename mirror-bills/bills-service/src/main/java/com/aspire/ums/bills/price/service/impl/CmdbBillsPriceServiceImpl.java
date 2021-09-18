package com.aspire.ums.bills.price.service.impl;

import com.aspire.ums.bills.config.BillLogAnnotation;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.service.BillLogService;
import com.aspire.ums.bills.price.mapper.CmdbBillsPriceMapper;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.service.CmdbBillsPriceService;
import com.aspire.ums.bills.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyihan
 * @date 2020-08-04 11:28
 */
@Slf4j
@Service
public class CmdbBillsPriceServiceImpl implements CmdbBillsPriceService {

    @Autowired
    private CmdbBillsPriceMapper billsPriceMapper;

    @Autowired
    private BillLogService billLogService;

    @Value("${cmdb.schema.name}")
    private String tablePrefix;

    @Override
    public List<Map<String, String>> queryIdcList() {
        return billsPriceMapper.queryIdcList(tablePrefix);
    }

    @Override
    public List<Map<String, String>> queryDeviceTypeList() {
        return billsPriceMapper.queryDeviceTypeList(tablePrefix);
    }

    @Override
    @BillLogAnnotation(content = "'调整' +#price.getIdcName()+'资源单价'+#price.getPrice()", type="新增", obj="单价管理")
    public Integer insertPrice(CmdbBillsPriceRequest price, String userId) {
        // 查询添加的资源池下该产品类型是否已有单价信息，避免重复添加
        CmdbBillsPriceResponse response = getPriceByCondition(price.getIdcId(), price.getDeviceTypeId());
        if (response == null) {
            price.setId(UUIDUtil.getUUID());
            price.setInsertTime(new Date());
            price.setInsertPerson((userId == null) ? "admin" : userId);
            log.info("新增单价，参数：{}", price);
            return billsPriceMapper.insertPrice(price);
        } else {
            throw new RuntimeException("当前资源池下该产品类型已有单价信息，请勿重复添加");
        }
    }

    @Override
    public CmdbBillsPriceResponse getPriceByCondition(String idcId, String deviceTypeId) {
        if (idcId != null && deviceTypeId != null) {
            List<CmdbBillsPriceResponse> prices =
                    billsPriceMapper.queryPriceByCondition(idcId, deviceTypeId, tablePrefix);
            if (prices.size() > 0) {
                return prices.get(0);
            }
        }
        return null;
    }

    @Override
    public CmdbBillsPriceResponse queryPriceDetailById(String id) {
        log.info("查询单价详情，id：{}", id);
        return billsPriceMapper.queryPriceDetailById(id, tablePrefix);
    }

    @Override
    public Integer updatePrice(CmdbBillsPriceRequest price, String userId) {
        log.info("更新单价，参数：{}", price);
        CmdbBillsPriceResponse response = getPriceByCondition(price.getIdcId(), price.getDeviceTypeId());
        if (response == null || response.getId().equals(price.getId())) {
            price.setUpdateTime(new Date());
            price.setUpdatePerson((userId == null) ? "admin" : userId);
            Integer result = billsPriceMapper.updatePrice(price);
            try {
                billLogService.saveBillLog(new BillsLog(null, null,"单价管理","更新", "调整"+ response.getIdcName() + "资源单价" + price.getPrice(), null));
            } catch (Exception e) {
                log.error("记录更新单价管理日志失败");
            }
            return result;
        } else {
            throw new RuntimeException("当前资源池下该产品类型已有单价信息，请勿重复添加");
        }
    }

    @Override
    public Integer deletePrice(String id) {
        log.info("删除单价，id：{}", id);
        Integer result = billsPriceMapper.deletePrice(id);
        CmdbBillsPriceResponse priceResponse = this.queryPriceDetailById(id);
        try {
            billLogService.saveBillLog(new BillsLog(null, null,"单价管理","删除", "删除"+ priceResponse.getIdcName() + "资源单价" + priceResponse.getPrice(), null));
        } catch (Exception e) {
            log.error("记录删除单价管理日志失败");
        }
        return result;
    }

    @Override
    public List<CmdbBillsPriceResponse> queryPriceList() {
        return billsPriceMapper.queryPriceList(tablePrefix);
    }

    @Override
    public List<Map<String, String>> getPriceUnitList() {
        return billsPriceMapper.getPriceUnitList(tablePrefix);
    }

    @Override
    public List<Map<String, Object>> getDevicePriceList(String idcId) {
        List<Map<String, String>> devicePriceList = billsPriceMapper.getDevicePriceList(idcId, tablePrefix);
        Map<String, List<Map<String, String>>> groupMap = devicePriceList.stream()
                .collect(Collectors.groupingBy(m -> m.get("parent")));
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, String>>> entry : groupMap.entrySet()) {
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("parent", entry.getKey());
            tempMap.put("value", entry.getValue());
            resultList.add(tempMap);
        }
        return resultList;
    }
}
