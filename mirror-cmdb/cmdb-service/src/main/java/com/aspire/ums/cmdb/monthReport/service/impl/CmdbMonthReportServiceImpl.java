package com.aspire.ums.cmdb.monthReport.service.impl;

import com.aspire.ums.cmdb.monthReport.mapper.CmdbMonthReportMapper;
import com.aspire.ums.cmdb.monthReport.service.ICmdbMonthReportService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbMonthReportServiceImpl
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbMonthReportServiceImpl implements ICmdbMonthReportService {

    @Autowired
    private CmdbMonthReportMapper monthReportMapper;
    @Override
    public LinkedHashMap<String, Object> countCompResource(String month, String idcType) {
        return monthReportMapper.countCompResource(month, idcType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countRateCompResource(String month, String idcType) {
        List<LinkedHashMap<String, Object>> rateList = new ArrayList<>();
        LinkedHashMap<String, Object> distRate = monthReportMapper.countDistRateCompare(month, idcType);
        LinkedHashMap<String, Object> useRate = monthReportMapper.countUseRateCompare(month, idcType);
        rateList.add(distRate);
        rateList.add(useRate);
        return rateList;
    }

    @Override
    public List<LinkedHashMap<String, Object>> countSixMonthTrend(String month, String idcType, Integer monthCount) {
        if (monthCount == null) {
            monthCount = 6;
        }
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        List<String> months = getMonths(month, monthCount);
        for (String m : months) {
            if (StringUtils.isEmpty(idcType)) {
                result.add(monthReportMapper.countRate(m, idcType));
            } else {
                result.add(monthReportMapper.countRateIdc(m, idcType));
            }
        }
        return result;
    }

    private List<String> getMonths(String month, Integer monthCount) {
        List<String> months = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = format.parse(month);
        } catch (ParseException e) {
            throw new RuntimeException( "月份" + month + "格式传参有误,参考格式：2020-01");
        }
        Calendar calendar = Calendar.getInstance();
        for (int i = monthCount-1; i > 0; i--) {
            // 设置为当前时间
            calendar.setTime(date);
            // 设置为上一个月
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - i);
            Date tempDate = calendar.getTime();
            months.add(format.format(tempDate));
        }
        months.add(month);
        return months;
    }

    @Override
    public LinkedHashMap<String, Object> countNetworkResource(String month, String idcType) {
        return monthReportMapper.countNetworkResource(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countStorageResource(String month, String idcType, String storageType) {
        return monthReportMapper.countStorageResource(month, idcType, storageType);
    }

    @Override
    public LinkedHashMap<String, Object> getIdcTypeStoreUseRate(String month, String idcType, String storageType) {
        return monthReportMapper.getIdcTypeStoreUseRate(month, idcType, storageType);
    }


    @Override
    public List<LinkedHashMap<String, Object>> countStorageSixMonthTrend(String month,Integer monthCount, String type, String idcType, String storageType) {
        if (monthCount == null) {
            monthCount = 6;
        }
        return monthReportMapper.countStorageSixMonthTrend(month, getMonths(month,monthCount),type, idcType, storageType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countRecycleResource(String month, String countType, String idcType, String recycleType) {
        return monthReportMapper.countRecycleResource(month, countType, idcType, recycleType);
    }

    @Override
    public List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(String month, String deviceType, String idcType) {
        List<LinkedHashMap<String, Object>> resultList = new ArrayList<>();
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
        if (StringUtils.isEmpty(idcType)) {
            resultMap = monthReportMapper.countPhyAvgMaxUtz(month, deviceType);
        } else {
            resultMap = monthReportMapper.countPhyAvgMaxUtzIdc(month, deviceType, idcType);
        }
        LinkedHashMap<String, Object> filedMap = new LinkedHashMap<>();
        filedMap.put("CPU均峰值利用率", resultMap.get("cpuMax"));
        filedMap.put("同比上月份", resultMap.get("cpuMaxCompare"));
        resultList.add(filedMap);
        filedMap = new LinkedHashMap<>();
        filedMap.put("内存均峰值利用率", resultMap.get("memoryMax"));
        filedMap.put("同比上月份", resultMap.get("memoryMaxCompare"));
        resultList.add(filedMap);
        filedMap = new LinkedHashMap<>();
        filedMap.put("CPU均值利用率", resultMap.get("cpuAvg"));
        filedMap.put("同比上月份", resultMap.get("cpuMaxCompare"));
        resultList.add(filedMap);
        filedMap = new LinkedHashMap<>();
        filedMap.put("内存均值利用率", resultMap.get("memoryAvg"));
        filedMap.put("同比上月份", resultMap.get("memoryAvgCompare"));
        resultList.add(filedMap);
        return resultList;
    }


    @Override
    public LinkedHashMap<String, Object> countAlert(String month, String idcType) {
        return monthReportMapper.countAlert(month, idcType);
    }

    @Override
    public LinkedHashMap<String, Object> countTrouble(String month, String idcType) {
        if (StringUtils.isEmpty(idcType)) {
            return monthReportMapper.countTrouble(month, idcType);
        } else {
            return monthReportMapper.countTroubleIdc(month, idcType);
        }
    }

    @Override
    public LinkedHashMap<String, Object> countOrder(String month, String idcType) {
        return monthReportMapper.countOrder(month, idcType);
    }
}
