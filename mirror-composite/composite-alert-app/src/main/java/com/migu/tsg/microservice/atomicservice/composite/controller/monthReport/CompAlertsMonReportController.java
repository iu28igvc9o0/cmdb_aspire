package com.migu.tsg.microservice.atomicservice.composite.controller.monthReport;

import com.aspire.mirror.alert.api.dto.monthReport.AlertEsDataRequest;
import com.aspire.mirror.composite.payload.alert.ComAlertEsDataRequest;
import com.aspire.mirror.composite.service.monthReport.ICompAlertsMonReportService;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monthReport.AlertMonReportServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.helper.ResourceAuthV2Helper;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CompAlertsMonReportController implements ICompAlertsMonReportService {

    @Autowired
    private AlertMonReportServiceClient client;

    @Override
    public Map<String,Object> viewByIdcType(@RequestBody Map<String,String> map) {
        return client.viewByIdcType(map);
    }

    @Override
    public List<Map<String, Object>> viewByIp(@RequestBody Map<String,String> map) {
        return client.viewByIp(map);
    }

    @Autowired
    protected ResourceAuthV2Helper resAuthHelper;

    @Override
    public List<Map<String, Object>> viewByKeyComment(@RequestBody Map<String,String> map) {
        return client.viewByKeyComment(map);
    }


    @Override
    public List<Map<String, Object>> getIdcTypeUserRate(@RequestBody ComAlertEsDataRequest request)
            throws ParseException {
        if (null == request) {
            log.warn("ComAlertRestfulController method[getIdcTypeUserRate] request  is empty");
            return null;
        }
        Map<String, Object> map = client
                .getIdcTypeUserRate(PayloadParseUtil.jacksonBaseParse(AlertEsDataRequest.class, request));

        List<Map<String, Object>> rsList = Lists.newArrayList();
        Map<String, Object> memMaxMap = Maps.newHashMap();
        memMaxMap.put("内存均峰值利用率", 0);
        memMaxMap.put("同比上月份", 0);
        Map<String, Object> memAvgMap = Maps.newHashMap();
        memAvgMap.put("内存均值利用率", 0);
        memAvgMap.put("同比上月份", 0);
        Map<String, Object> cpuMaxMap = Maps.newHashMap();
        cpuMaxMap.put("CPU均峰值利用率", 0);
        cpuMaxMap.put("同比上月份", 0);
        Map<String, Object> cpuAvgMap = Maps.newHashMap();
        cpuAvgMap.put("CPU均值利用率", 0);
        cpuAvgMap.put("同比上月份", 0);
        if (null != map) {
            for (Map.Entry<String, Object> m : map.entrySet()) {
                String key = m.getKey();
                Object value = m.getValue();
                if (key.equals("memory_max")) {
                    memMaxMap.put("内存均峰值利用率", value);
                }
                if (key.equals("compare_memory_max")) {
                    memMaxMap.put("同比上月份", value);
                }

                if (key.equals("memory_avg")) {
                    memAvgMap.put("内存均值利用率", value);
                }
                if (key.equals("compare_memory_avg")) {
                    memAvgMap.put("同比上月份", value);
                }

                if (key.equals("cpu_max")) {
                    cpuMaxMap.put("CPU均峰值利用率", value);
                }
                if (key.equals("compare_cpu_max")) {
                    cpuMaxMap.put("同比上月份", value);
                }

                if (key.equals("cpu_avg")) {
                    cpuAvgMap.put("CPU均值利用率", value);
                }
                if (key.equals("compare_cpu_avg")) {
                    cpuAvgMap.put("同比上月份", value);
                }

            }
        }
        rsList.add(cpuMaxMap);
        rsList.add(memMaxMap);
        rsList.add(cpuAvgMap);
        rsList.add(memAvgMap);
        return rsList;
    }

    @Override
    public List<Map<String, Object>> getIdcTypeTrends(@RequestBody ComAlertEsDataRequest request) {
        if (null == request) {
            log.warn("ComAlertRestfulController method[getIdcTypeTrends] request  is empty");
            return null;
        }
        String countType = request.getCountType();
        List<Map<String, Object>> list = client
                .getIdcTypeTrends(PayloadParseUtil.jacksonBaseParse(AlertEsDataRequest.class, request));
        List<Map<String, Object>> rsList = Lists.newArrayList();
        for (Map<String, Object> map : list) {
            Map<String, Object> rsMap = Maps.newHashMap();
            for (Map.Entry<String, Object> m : map.entrySet()) {
                String key = m.getKey();
                Object value = m.getValue();
                if (countType.equals("MAX")) {
                    if (key.equals("memory_max")) {
                        rsMap.put("内存均峰值利用率", value);
                    }
                    if (key.equals("cpu_max")) {
                        rsMap.put("CPU均峰值利用率", value);
                    }
                } else {
                    if (key.equals("memory_avg")) {
                        rsMap.put("内存均值利用率", value);
                    }

                    if (key.equals("cpu_avg")) {
                        rsMap.put("CPU均值利用率", value);
                    }
                }
                if (key.equals("day")) {
                    rsMap.put("日期", value);
                }

            }
            rsList.add(rsMap);
        }
        return rsList;
    }

    @Override
    @ResAction(resType="monitorIndexpage", action="view", loadResFilter=true)
    public Map<String, Object> getUserRateForZH(@RequestParam(value = "deviceType",required=false) String deviceType) throws ParseException {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        resAuthHelper.resourceActionVerify(authCtx.getUser(), new RbacResource(), authCtx.getResAction(), authCtx.getFlattenConstraints());
        AlertEsDataRequest request = new AlertEsDataRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //  Date sourceDate = sdf.parse(month);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        request.setMonth(sdf.format(cal.getTime()));
        request.setDeviceType(deviceType);
        return client.getUserRateForZH(request);
    }
}
