package com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.price;

import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.payload.Response;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangyihan
 * @date 2020-08-13 09:52
 */
@FeignClient("BILLS")
public interface CmdbBillsPriceClient {

    @RequestMapping(value = "/v1/cmdb/bill/price/loadIdcList",method = RequestMethod.GET)
    List<Map<String, String>> loadIdcList();

    @RequestMapping(value = "/v1/cmdb/bill/price/loadDeviceTypeList",method = RequestMethod.GET)
    List<Map<String, String>> loadDeviceTypeList();

    @RequestMapping(value = "/v1/cmdb/bill/price",method = RequestMethod.POST)
    Response insertPrice(@RequestBody CmdbBillsPriceRequest priceRequest);

    @RequestMapping(value = "/v1/cmdb/bill/price",method = RequestMethod.GET)
    CmdbBillsPriceResponse getPriceByCondition(@RequestParam("idcId") String idcId,
                                               @RequestParam("deviceTypeId") String deviceTypeId);

    @RequestMapping(value = "/v1/cmdb/bill/price/detail",method = RequestMethod.GET)
    CmdbBillsPriceResponse getPriceDetailById(@RequestParam("id") String id);

    @RequestMapping(value = "/v1/cmdb/bill/price",method = RequestMethod.PUT)
    Integer updatePrice(@RequestBody CmdbBillsPriceRequest priceRequest);

    @RequestMapping(value = "/v1/cmdb/bill/price",method = RequestMethod.DELETE)
    Integer deletePrice(@RequestParam("id") String id);

    @RequestMapping(value = "/v1/cmdb/bill/price/list",method = RequestMethod.GET)
    List<CmdbBillsPriceResponse> getPriceList();

    @RequestMapping(value = "/v1/cmdb/bill/price/unit",method = RequestMethod.GET)
    List<Map<String, String>> getPriceUnitList();

    @RequestMapping(value = "/v1/cmdb/bill/price/device",method = RequestMethod.GET)
    List<Map<String, Object>> getDevicePriceList(@RequestParam("idcId") String idcId);
}
