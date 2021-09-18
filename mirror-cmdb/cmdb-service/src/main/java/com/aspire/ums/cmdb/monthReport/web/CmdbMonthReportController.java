package com.aspire.ums.cmdb.monthReport.web;

import com.aspire.ums.cmdb.monthReport.ICmdbMonthReportAPI;
import com.aspire.ums.cmdb.monthReport.service.ICmdbMonthReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbMonthReportController
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbMonthReportController implements ICmdbMonthReportAPI {

    @Autowired
    private ICmdbMonthReportService monthReportService;

    @Override
    public LinkedHashMap<String, Object> countCompResource(@RequestParam("month") String month,
                                                           @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportService.countCompResource(month, idcType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countRateCompResource(@RequestParam("month") String month,
                                                                     @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportService.countRateCompResource(month, idcType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countSixMonthTrend(@RequestParam("month") String month,
                                                        @RequestParam(value = "idcType", required = false) String idcType,
                                                        @RequestParam(value = "monthCount", required = false) Integer monthCount) {
        return monthReportService.countSixMonthTrend(month, idcType, monthCount);
    }

    @Override
    public LinkedHashMap<String, Object> countNetworkResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportService.countNetworkResource(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countStorageResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType,
                                                    @RequestParam("storageType") String storageType) {
        return monthReportService.countStorageResource(month,idcType,storageType);
    }

    @Override
    public LinkedHashMap<String, Object> getIdcTypeStoreUseRate(@RequestParam("month") String month,
                                                                @RequestParam(value = "idcType", required = false) String idcType,
                                                                @RequestParam("storageType") String storageType) {
        return monthReportService.getIdcTypeStoreUseRate(month, idcType, storageType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countStorageSixMonthTrend(@RequestParam("month") String month,
                                                                         @RequestParam(value = "monthCount", required = false) Integer monthCount,
                                                                         @RequestParam(value = "type", required = false) String type,
                                                               @RequestParam(value = "idcType", required = false) String idcType,
                                                               @RequestParam("storageType") String storageType) {
        return monthReportService.countStorageSixMonthTrend(month ,monthCount, type, idcType, storageType);
    }



    @Override
    public List<LinkedHashMap<String, Object>> countRecycleResource(@RequestParam("month") String month,
                                                                    @RequestParam("countType") String countType,
                                                    @RequestParam(value = "idcType", required = false) String idcType,
                                                    @RequestParam("recycleType") String recycleType) {
        return monthReportService.countRecycleResource(month, countType, idcType, recycleType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(@RequestParam("month") String month,
                                                                 @RequestParam("deviceType") String deviceType,
                                                                 @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportService.countPhyAvgMaxUtz(month, deviceType, idcType);
    }

//    @Override
//    public List<Map<String, String>> countPhyUtzTrend(@RequestParam("month") String month,
//                                                                @RequestParam("countType") String countType,
//                                                                @RequestParam("deviceType") String deviceType,
//                                                                @RequestParam(value = "idcType", required = false) String idcType) {
//        List<Map<String, String>> result = new ArrayList<>();
//        Map<String, Object> req = new HashMap<>();
//        req.put("month", month);
//        req.put("countType", countType);
//        req.put("deviceType", deviceType);
//        req.put("idcType", idcType);
//        List<Map<String, String>> returnMap = alertClient.getIdcTypeTrends(req);
//        for (Map<String, String> res :  returnMap) {
//            Map<String, String> rsMap = new HashMap<>();
//            if ("MAX".equals(countType)) {
//                rsMap.put("内存均峰值利用率", res.get("memory_max"));
//                rsMap.put("CPU均峰值利用率",  res.get("cpu_max"));
//            } else {
//                rsMap.put("内存均值利用率",  res.get("memory_avg"));
//                rsMap.put("CPU均值利用率",  res.get("cpu_avg"));
//            }
//            rsMap.put("日期",  res.get("day"));
//            result.add(rsMap);
//        }
//        return result;
//    }

    @Override
    public LinkedHashMap<String, Object> countAlert(@RequestParam("month")String month,
                                                    @RequestParam(value = "idcType", required = false)String idcType) {
        return monthReportService.countAlert(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countTrouble(@RequestParam("month")String month,
                                                      @RequestParam(value = "idcType", required = false)String idcType) {
        return monthReportService.countTrouble(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countOrder(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType) {
        return monthReportService.countOrder(month, idcType);
    }
}
