package com.aspire.mirror.alert.server.controller.monthReport;

import com.aspire.mirror.alert.api.dto.monthReport.AlertEsDataRequest;
import com.aspire.mirror.alert.api.service.monthReport.AlertsMonReportService;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.monthReport.AlertMonReportBiz;
import com.aspire.mirror.alert.server.clientservice.CmdbResfulClient;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.vo.monthReport.AlertEsDataVo;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AlertsMonReportController implements AlertsMonReportService {

    @Autowired
    private AlertMonReportBiz alertMonReportBiz;

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private AlertMonthReportSyncDao alertMonthReportSyncDao;

    @Autowired
    private CmdbResfulClient cmdbResfulClient;
    @Value("${cmdbQueryType:bpm_department_index_bizSystem_instance_stats}")
    private String   cmdbQueryName;

    @Override
    public Map<String,Object> viewByIdcType(@RequestBody Map<String,String> map) {
        return alertMonReportBiz.viewByIdcType(map);
    }

    @Override
    public List<Map<String, Object>> viewByIp(@RequestBody Map<String,String> map) {
        return alertMonReportBiz.viewByIp(map);
    }

    @Override
    public List<Map<String, Object>> viewByKeyComment(@RequestBody Map<String,String> map) {
        return alertMonReportBiz.viewByKeyComment(map);
    }


    @Override
    public Map<String, Object> getUserRateForZH(@RequestBody AlertEsDataRequest request) throws ParseException {
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        Map<String,String> map = authHelper.packSqlConditionByType(resFilterMap, 3);


        String month = request.getMonth();
        if (StringUtils.isBlank(month)) {
            log.warn("运营月报资源池利用率,月份不能为空！");
            return null;
        }
        List<Map<String,Object>> monthData = alertMonthReportSyncDao.queryMonthUseRateForZH(request.getMonth(),
                map, request.getDeviceType());
        if(null==monthData || monthData.size()==0) {
            return null;
        }

        Map<String, Object> monthMap = getMonthData(monthData,request.getDeviceType());

        String lastMonth = DateUtils.getLastMonth(month);

        List<Map<String,Object>> lastMonthData = alertMonthReportSyncDao.queryMonthUseRateForZH(lastMonth,
                map, request.getDeviceType());
        Map<String, Object> lastMonthMap = getMonthData(lastMonthData,request.getDeviceType());

        Float cpuAvg = monthMap.get("cpu_avg")==null?null:Float.parseFloat(monthMap.get("cpu_avg").toString());
        Float memAvg = monthMap.get("memory_avg")==null?null:Float.parseFloat(monthMap.get("memory_avg").toString());
        Float cpuMax = monthMap.get("cpu_max")==null?null:Float.parseFloat(monthMap.get("cpu_max").toString());
        Float memMax = monthMap.get("memory_max")==null?null:Float.parseFloat(monthMap.get("memory_max").toString());
        monthMap.put("last_null_flag", false);
        if(null==lastMonthData || lastMonthData.size()==0) {
            monthMap.put("last_null_flag", true);
            return monthMap;
        }
        if(null!=cpuAvg) {
            Float lastCpuAvg = lastMonthMap.get("cpu_avg")==null?null:Float.parseFloat(lastMonthMap.get("cpu_avg").toString());
            if(null!=lastCpuAvg) {
                float compareVal = getCompareVal(cpuAvg,lastCpuAvg);
                monthMap.put("cpu_avg", (float)Math.round(cpuAvg*100)/100);
                monthMap.put("compare_cpu_avg", compareVal);
            }
        }
        if(null!=memAvg) {
            Float lastMemAvg = lastMonthMap.get("memory_avg")==null?null:Float.parseFloat(lastMonthMap.get("memory_avg").toString());
            if(null!=lastMemAvg) {
                float compareVal = getCompareVal(memAvg,lastMemAvg);
                monthMap.put("memory_avg", (float)Math.round(memAvg*100)/100);
                monthMap.put("compare_memory_avg", compareVal);
            }
        }
        if(null!=cpuMax) {
            Float lastCpuMax = lastMonthMap.get("cpu_max")==null?null:Float.parseFloat(lastMonthMap.get("cpu_max").toString());
            if(null!=lastCpuMax) {
                float compareVal = getCompareVal(cpuMax,lastCpuMax);
                monthMap.put("cpu_max", (float)Math.round(cpuMax*100)/100);
                monthMap.put("compare_cpu_max", compareVal);
            }
        }
        if(null!=memMax) {
            Float lastMemMax =  lastMonthMap.get("memory_max")==null?null:Float.parseFloat(lastMonthMap.get("memory_max").toString());
            if(null!=lastMemMax) {
                float compareVal = getCompareVal(memMax,lastMemMax);
                monthMap.put("memory_max", (float)Math.round(memMax*100)/100);
                monthMap.put("compare_memory_max", compareVal);
            }
        }
        return monthMap;
    }

    @Override
    public Map<String, Object> getIdcTypeUserRate(@RequestBody AlertEsDataRequest request) throws ParseException {
        if(null==request)
        {
            log.warn("请求参数不能为空");
            return null;
        }
        return alertMonReportBiz.getIdcTypeUserRate(PayloadParseUtil.fastjsonBaseParse(AlertEsDataVo.class, request));
    }
    @Override
    public List<Map<String, Object>> getIdcTypeTrends(@RequestBody AlertEsDataRequest request) {
        if(null==request)
        {
            log.warn("请求参数不能为空");
            return null;
        }
        return alertMonReportBiz.getIdcTypeTrends(PayloadParseUtil.fastjsonBaseParse(AlertEsDataVo.class, request));
    }


    private Map<String, Object> getMonthData(List<Map<String, Object>> monthData,String deviceType) {
        List<String> bizList = Lists.newArrayList();
        Map<String, Map<String, Object>> bizMap = Maps.newHashMap();
        for(Map<String, Object> map:monthData) {
            String bizSystem = map.get("bizSystem")==null?null:map.get("bizSystem").toString();
            if(StringUtils.isBlank(bizSystem)) {
                continue;
            }
            bizList.add(bizSystem);
            bizMap.put(bizSystem, map);
        }
        if(bizList.size()==0) {
            return null;
        }
        StatisticRequestEntity entity = new StatisticRequestEntity();
        entity.setName(this.cmdbQueryName);
        Map<String, Object> params = Maps.newHashMap();
        params.put("device_type", deviceType);
        params.put("bizSystem", bizList);
        entity.setParams(params);
        entity.setResponseType("list");
        Object	value = cmdbResfulClient.getInstanceStatistics(entity);

        if(null!=value) {
            Map<String,Object> map = Maps.newHashMap();
            int sum = 0;
            Float cpuAvgSum = 0f;
            Float cpuMaxSum = 0f;
            Float memoryAvgSum = 0f;
            Float memoryMaxSum = 0f;
            List<Map<String,Object>> dataList = (List<Map<String,Object>>)value;
            /*
             * Map<String,Object> a = Maps.newHashMap(); a.put("bizSystem", "监控智能运营工具");
             * a.put("instance_count", 3490); dataList.add(a); Map<String,Object> b =
             * Maps.newHashMap(); b.put("bizSystem", "批量部署工具"); b.put("instance_count", 35);
             * dataList.add(b);
             */

            for(Map<String,Object> m:dataList) {
                String biz =m.get("bizSystem")==null?null:m.get("bizSystem").toString();
                if(StringUtils.isBlank(biz)) {
                    continue;
                }
                Map<String, Object> monthMap = bizMap.get(biz);
                if(null==monthMap) {
                    continue;
                }
                String cpuAvgValue = monthMap.get("cpu_avg")==null?null:monthMap.get("cpu_avg").toString();
                if(StringUtils.isEmpty(cpuAvgValue)) {
                    continue;
                }
                Float cpuAvg = Float.parseFloat(cpuAvgValue);
                Float memAvg = monthMap.get("memory_avg")==null?null:Float.parseFloat(monthMap.get("memory_avg").toString());
                Float cpuMax = monthMap.get("cpu_max")==null?null:Float.parseFloat(monthMap.get("cpu_max").toString());
                Float memMax = monthMap.get("memory_max")==null?null:Float.parseFloat(monthMap.get("memory_max").toString());
                int count = m.get("instance_count")==null?null:Integer.parseInt(m.get("instance_count").toString());
                sum +=count;
                cpuAvgSum += cpuAvg*count;
                cpuMaxSum += cpuMax*count;
                memoryAvgSum += memAvg*count;
                memoryMaxSum += memMax*count;

            }
            map.put("cpu_avg", cpuAvgSum/sum);
            map.put("memory_avg", memoryAvgSum/sum);
            map.put("cpu_max", cpuMaxSum/sum);
            map.put("memory_max", memoryMaxSum/sum);
            return map;
        }
        return null;
    }

    private float getCompareVal(float cpuAvg,float lastCpuAvg){
        float compareVal = cpuAvg-lastCpuAvg;
		/*if(lastCpuAvg>0) {
			compareVal = (float)compareVal*100/(float)lastCpuAvg;
		}*/
        compareVal = (float)Math.round(compareVal*100)/100;
        return compareVal;
    }
}
