package com.migu.tsg.microservice.atomicservice.composite.controller.monthReport;

import com.aspire.mirror.alert.api.dto.monthReport.AlertEsDataRequest;
import com.aspire.mirror.composite.service.cmdb.monthReport.ICmdbMonthReportAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.monthReport.CmdbMonthReportClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monthReport.AlertMonReportServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompMonthReportController
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CompMonthReportController implements ICmdbMonthReportAPI {

    @Autowired
    private CmdbMonthReportClient monthReportClient;

    @Autowired
    private AlertMonReportServiceClient alertMonReportServiceClient;

    @Override
    public Map<String, Object> countCompResource(@RequestParam("month") String month,
                                                 @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportClient.countCompResource(month, idcType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countRateCompResource(@RequestParam("month") String month,
                                                                     @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportClient.countRateCompResource(month, idcType);
    }

    @Override
    public List<Map<String, Object>> countSixMonthTrend(@RequestParam("month") String month,
                                                        @RequestParam(value = "idcType", required = false) String idcType,
                                                        @RequestParam(value = "monthCount", required = false) Integer monthCount) {
        return monthReportClient.countSixMonthTrend(month, idcType, monthCount);
    }

    @Override
    public Map<String, Object> countStorageResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType,
                                                    @RequestParam("storageType") String storageType) {
        return monthReportClient.countStorageResource(month, idcType, storageType);
    }

    @Override
    public LinkedHashMap<String, Object> getIdcTypeStoreUseRate(@RequestParam("month") String month,
                                                                @RequestParam(value = "idcType", required = false) String idcType,
                                                                @RequestParam("storageType") String storageType) {
        return monthReportClient.getIdcTypeStoreUseRate(month, idcType, storageType);
    }

    @Override
    public List<Map<String, Object>> countStorageSixMonthTrend(@RequestParam("month") String month,
                                                               @RequestParam(value = "monthCount", required = false) Integer monthCount,
                                                               @RequestParam(value = "type") String type,
                                                               @RequestParam(value = "idcType", required = false) String idcType,
                                                               @RequestParam("storageType") String storageType) {
        return monthReportClient.countStorageSixMonthTrend(month,monthCount, type, idcType, storageType);
    }


    @Override
    public Map<String, Object> countNetworkResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportClient.countNetworkResource(month, idcType);
    }

    @Override
    public List<Map<String, Object>> countRecycleResource(@RequestParam("month") String month,
                                                          @RequestParam("countType") String countType,
                                                    @RequestParam(value = "idcType", required = false) String idcType,
                                                    @RequestParam("recycleType") String recycleType) {
        return monthReportClient.countRecycleResource(month, countType, idcType, recycleType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(@RequestParam("month") String month,
                                                                 @RequestParam("deviceType") String deviceType,
                                                                 @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportClient.countPhyAvgMaxUtz(month, deviceType, idcType);
    }

    @Override
    public List<Map<String, Object>> countPhyUtzTrend(@RequestParam("month") String month,
                                                      @RequestParam("countType") String countType,
                                                      @RequestParam("deviceType") String deviceType,
                                                      @RequestParam(value = "idcType", required = false) String idcType) {
        AlertEsDataRequest request = new AlertEsDataRequest();
        request.setMonth(month);
        request.setDeviceType(deviceType);
        request.setIdcType(idcType);
        return alertMonReportServiceClient.getIdcTypeTrends(request);
    }

    @Override
    public LinkedHashMap<String, Object> countAlert(@RequestParam("month")String month,
                                                    @RequestParam(value = "idcType", required = false)String idcType) {
        return monthReportClient.countAlert(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countTrouble(@RequestParam("month")String month,
                                                      @RequestParam(value = "idcType", required = false)String idcType) {
        return monthReportClient.countTrouble(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countOrder(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportClient.countOrder(month, idcType);
    }
}
