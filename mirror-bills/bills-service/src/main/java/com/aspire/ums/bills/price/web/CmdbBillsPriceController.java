package com.aspire.ums.bills.price.web;

import com.aspire.ums.bills.config.RequestAuthContext;
import com.aspire.ums.bills.price.CmdbBillsPriceAPI;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceRequest;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.payload.Response;
import com.aspire.ums.bills.price.service.CmdbBillsPriceService;
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
 * @date 2020-08-04 10:33
 */
@Slf4j
@RestController
public class CmdbBillsPriceController implements CmdbBillsPriceAPI {

    @Autowired
    CmdbBillsPriceService billsPriceService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> loadIdcList() {
        log.info("获取资源池列表");
        return billsPriceService.queryIdcList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> loadDeviceTypeList() {
        log.info("获取设备类别列表");
        return billsPriceService.queryDeviceTypeList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Response insertPrice(@RequestBody CmdbBillsPriceRequest priceRequest) {
        try {
            log.info("新增单价，参数：{}", priceRequest);
            // 获取用户信息
            String userId = RequestAuthContext.getRequestHeadUserName();
            return Response.success(billsPriceService.insertPrice(priceRequest, userId));
        } catch (Exception ex){
            log.info("同资源池下、同设备类型已有单价信息");
            return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage());
        }
    }

    @Override
    public CmdbBillsPriceResponse getPriceByCondition(String idcId, String deviceTypeId) {
        log.info("条件获取单价，资源池id：{}，设备类别id：{}", idcId, deviceTypeId);
        return billsPriceService.getPriceByCondition(idcId, deviceTypeId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public CmdbBillsPriceResponse getPriceDetailById(String id) {
        log.info("获取单价详情，单价id：{}", id);
        return billsPriceService.queryPriceDetailById(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Integer updatePrice(@RequestBody CmdbBillsPriceRequest priceRequest) {
        log.info("更新单价，参数：{}", priceRequest);
        // 获取用户信息
        String userId = RequestAuthContext.getRequestHeadUserName();
        return billsPriceService.updatePrice(priceRequest, userId);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Integer deletePrice(String id) {
        log.info("删除单价，单价id：{}", id);
        return billsPriceService.deletePrice(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<CmdbBillsPriceResponse> getPriceList() {
        log.info("获取单价列表");
        return billsPriceService.queryPriceList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getPriceUnitList() {
        return billsPriceService.getPriceUnitList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, Object>> getDevicePriceList(String idcId) {
        log.info("查询设备月单价列表，资源池id：{}", idcId);
        return billsPriceService.getDevicePriceList(idcId);
    }
}
