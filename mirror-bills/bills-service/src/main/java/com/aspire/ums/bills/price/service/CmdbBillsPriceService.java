package com.aspire.ums.bills.price.service;

import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-04 11:27
 */
public interface CmdbBillsPriceService {

    List<Map<String, String>> queryIdcList();

    List<Map<String, String>> queryDeviceTypeList();

    Integer insertPrice(CmdbBillsPriceRequest price, String userId);

    CmdbBillsPriceResponse getPriceByCondition(String idcId, String deviceTypeId);

    CmdbBillsPriceResponse queryPriceDetailById(String id);

    Integer updatePrice(CmdbBillsPriceRequest price, String userId);

    Integer deletePrice(String id);

    List<CmdbBillsPriceResponse> queryPriceList();

    List<Map<String, String>> getPriceUnitList();

    List<Map<String, Object>> getDevicePriceList(String idcId);

}
