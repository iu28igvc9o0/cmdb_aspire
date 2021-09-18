package com.aspire.mirror.alert.server.biz.monthReport.impl;

import com.aspire.mirror.alert.server.biz.monthReport.AlertMonReportBiz;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.dao.monthReport.AlertsMonReportDao;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.vo.monthReport.AlertEsDataVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AlertsMonReportBizImpl implements AlertMonReportBiz{

    @Autowired
    private AlertsMonReportDao alertsMonReportDao;

    @Autowired
    private AlertMonthReportSyncDao alertMonthReportSyncDao;


    private void getMonDay (Map<String, String> map,String mon) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
            Date date = sdf1.parse(mon);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取当前月第一天：
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
            String first = sdf2.format(calendar.getTime());
            map.put("first",first);
            //获取当前月最后一天
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String last = sdf2.format(calendar.getTime());
            map.put("last",last);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String,Object> viewByIdcType(Map<String, String> map) {
        if (StringUtils.isNotEmpty(map,map.get("month"))) {
            getMonDay(map,map.get("month"));
        }
        List<Map<String, Object>> maps = alertsMonReportDao.viewByIdcType(map);
        Map<String, Object> res = Maps.newLinkedHashMap();
        int sum = 0;
        int sCount = 0;
        int hCount = 0;
        int mCount = 0;
        int lCount = 0;
        res.put("资源池", map.get("idcType"));
        for (Map<String, Object> item : maps) {
            sum += Integer.valueOf(String.valueOf(item.get("sum")));
            sCount += Integer.valueOf(String.valueOf(item.get("sCount")));
            hCount += Integer.valueOf(String.valueOf(item.get("hCount")));
            mCount += Integer.valueOf(String.valueOf(item.get("mCount")));
            lCount += Integer.valueOf(String.valueOf(item.get("lCount")));
        }
        res.put("告警总量", sum);
        Map<String, Object> data = Maps.newLinkedHashMap();
        data.put("严重告警", sCount);
        data.put("重要告警", hCount);
        data.put("次要告警", mCount);
        data.put("提醒告警", lCount);
        res.put("data", data);
        return res;
    }

    @Override
    public List<Map<String, Object>> viewByIp(Map<String, String> map) {
        if (StringUtils.isNotEmpty(map,map.get("month"))) {
            getMonDay(map,map.get("month"));
        }
        List<Map<String, Object>> res = Lists.newLinkedList();
        List<Map<String, Object>> maps = alertsMonReportDao.viewByIp(map);
        maps.forEach(item -> {
            Map<String, Object> m = Maps.newHashMap();
            m.put("设备名称",item.get("host_name"));
            m.put("告警条数",item.get("sCount"));
            res.add(m);
        });
        return res;
    }

    @Override
    public List<Map<String, Object>> viewByKeyComment(Map<String, String> map) {
        if (StringUtils.isNotEmpty(map,map.get("month"))) {
            getMonDay(map,map.get("month"));
        }
        List<Map<String, Object>> res = Lists.newLinkedList();
        List<Map<String, Object>> maps = alertsMonReportDao.viewByKeyComment(map);
        maps.forEach(item -> {
            Map<String, Object> m = Maps.newHashMap();
            m.put("指标名称",item.get("key_comment"));
            m.put("告警条数",item.get("sCount"));
            res.add(m);
        });
        return res;
    }


    @Override
    public Map<String, Object> getIdcTypeUserRate(AlertEsDataVo request) throws ParseException {
        String month = request.getMonth();
        if (org.apache.commons.lang.StringUtils.isBlank(month)) {
            log.warn("运营月报资源池利用率,月份不能为空！");
            return null;
        }
        Map<String, Object> monthData = alertMonthReportSyncDao.queryMonthIdcTypeUseRate(request.getMonth(),
                request.getIdcType(), request.getDeviceType());
        if(null==monthData) {
            return null;
        }


        String lastMonth = DateUtils.getLastMonth(month);

        Map<String, Object> lastMonthData = alertMonthReportSyncDao.queryMonthIdcTypeUseRate(lastMonth,
                request.getIdcType(), request.getDeviceType());
        Float cpuAvg = monthData.get("cpu_avg")==null?null:Float.parseFloat(monthData.get("cpu_avg").toString());
        Float memAvg = monthData.get("memory_avg")==null?null:Float.parseFloat(monthData.get("memory_avg").toString());
        Float cpuMax = monthData.get("cpu_max")==null?null:Float.parseFloat(monthData.get("cpu_max").toString());
        Float memMax = monthData.get("memory_max")==null?null:Float.parseFloat(monthData.get("memory_max").toString());
        monthData.put("last_null_flag", false);
        if(null==lastMonthData) {
            monthData.put("last_null_flag", true);
            return monthData;
        }
        if(null!=cpuAvg) {
            Float lastCpuAvg = lastMonthData.get("cpu_avg")==null?null:Float.parseFloat(lastMonthData.get("cpu_avg").toString());
            if(null!=lastCpuAvg) {
                float compareVal = getCompareVal(cpuAvg,lastCpuAvg);
                monthData.put("compare_cpu_avg", compareVal);
            }
        }
        if(null!=memAvg) {
            Float lastMemAvg = lastMonthData.get("memory_avg")==null?null:Float.parseFloat(lastMonthData.get("memory_avg").toString());
            if(null!=lastMemAvg) {
                float compareVal = getCompareVal(memAvg,lastMemAvg);
                monthData.put("compare_memory_avg", compareVal);
            }
        }
        if(null!=cpuMax) {
            Float lastCpuMax = lastMonthData.get("cpu_max")==null?null:Float.parseFloat(lastMonthData.get("cpu_max").toString());
            if(null!=lastCpuMax) {
                float compareVal = getCompareVal(cpuMax,lastCpuMax);
                monthData.put("compare_cpu_max", compareVal);
            }
        }
        if(null!=memMax) {
            Float lastMemMax =  lastMonthData.get("memory_max")==null?null:Float.parseFloat(lastMonthData.get("memory_max").toString());
            if(null!=lastMemMax) {
                float compareVal = getCompareVal(memMax,lastMemMax);
                monthData.put("compare_memory_max", compareVal);
            }
        }
        return monthData;
    }

    public float getCompareVal(float cpuAvg,float lastCpuAvg){
        float compareVal = cpuAvg-lastCpuAvg;
		/*if(lastCpuAvg>0) {
			compareVal = (float)compareVal*100/(float)lastCpuAvg;
		}*/
        compareVal = (float)Math.round(compareVal*100)/100;
        return compareVal;
    }

    @Override
    public List<Map<String, Object>> getIdcTypeTrends(AlertEsDataVo request) {
        return alertMonthReportSyncDao.queryMonthIdcTypeTrends(request.getMonth(), request.getIdcType(),
                request.getDeviceType());
    }
}
