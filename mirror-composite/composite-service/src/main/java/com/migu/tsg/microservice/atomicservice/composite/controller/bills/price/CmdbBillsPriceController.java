package com.migu.tsg.microservice.atomicservice.composite.controller.bills.price;

import com.aspire.mirror.composite.service.bills.price.CmdbBillsPriceAPI;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.payload.Response;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.price.CmdbBillsPriceClient;
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
public class CmdbBillsPriceController implements CmdbBillsPriceAPI {

    @Autowired
    private CmdbBillsPriceClient priceClient;

    @Override
    public List<Map<String, String>> loadIdcList() {
        return priceClient.loadIdcList();
    }

    @Override
    public List<Map<String, String>> loadDeviceTypeList() {
        return priceClient.loadDeviceTypeList();
    }

    @Override
    public Response insertPrice(@RequestBody CmdbBillsPriceRequest priceRequest) {
        return priceClient.insertPrice(priceRequest);
    }

    @Override
    public CmdbBillsPriceResponse getPriceByCondition(@RequestParam("idcId") String idcId,
                                                      @RequestParam("deviceTypeId") String deviceTypeId) {
        return priceClient.getPriceByCondition(idcId, deviceTypeId);
    }

    @Override
    public CmdbBillsPriceResponse getPriceDetailById(@RequestParam("id") String id) {
        return priceClient.getPriceDetailById(id);
    }

    @Override
    public Integer updatePrice(@RequestBody CmdbBillsPriceRequest priceRequest) {
        return priceClient.updatePrice(priceRequest);
    }

    @Override
    public Integer deletePrice(@RequestParam("id") String id) {
        return priceClient.deletePrice(id);
    }

    @Override
    public List<CmdbBillsPriceResponse> getPriceList() {
        return priceClient.getPriceList();
    }

    @Override
    public List<Map<String, String>> getPriceUnitList() {
        return priceClient.getPriceUnitList();
    }

    @Override
    public List<Map<String, Object>> getDevicePriceList(@RequestParam("idcId") String idcId) {
        return priceClient.getDevicePriceList(idcId);
    }
}
