package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.kgEnv.KGMonitorIndexServiceClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.alert.ICompKGMonitorIndexService;
import com.aspire.mirror.composite.service.alert.payload.ComIdcTypePhysicalReq;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.EsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.CmdbIndexInstClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbHelper;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ICompKGMonitorIndexController implements ICompKGMonitorIndexService {

    @Autowired
    private KGMonitorIndexServiceClient client;
    @Autowired
    private InstanceClient instanceClient;
    @Autowired
    private EsIndexPageServiceClient esIndexPageServiceClient;
    @Value("${cmdb.kg.deviceType.phyServer:X86服务器}")
    private String phyServer;
    @Value("${cmdb.kg.deviceType.vmHost:虚拟机}")
    private String vmHost;
    @Value("${cmdb.kg.deviceType.suHost:宿主机}")
    private String suHost;
    @Autowired
    private ConfigDictClient configDictClient;
    @Autowired
    private CmdbIndexInstClient indexInstClient;
    @Autowired
    private AlertsIndexPageServiceClient indexPageServiceClient;
    
    @Autowired
	private CmdbHelper cmdbHelper;

    @Override
    public List<CmdbDeviceTypeCount> queryServiceCount() {
        List<CmdbDeviceTypeCount> list = instanceClient.queryServiceCountForKG();
        for (CmdbDeviceTypeCount cdtc : list) {
            String cpu = cdtc.getCpuCoreNumber();
            String mem = cdtc.getMemorySize();
            if (StringUtils.isNotBlank(cpu)) {
                if (cpu.contains(".")) {
                    double cpuNum = Double.parseDouble(cpu);
                    Double cpu1 = Math.round(cpuNum*100)/100.0 ;
                    if (cpu1.toString().contains("E")) {
                        Double dd = (Double) cpu1;
                        BigDecimal bd1 = new BigDecimal(dd);
                        cpu = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        cdtc.setCpuCoreNumber(cpu);
                    }else {
                        cdtc.setCpuCoreNumber(cpu1+"");
                    }

                }
            }
            if (StringUtils.isNotBlank(mem)) {
                if (mem.contains(".")) {
                    double memNum = Double.parseDouble(mem);
                    Double mem1 = Math.round(memNum*100)/100.0;
                    if (mem1.toString().contains("E")) {
                        Double dd = (Double) mem1;
                        BigDecimal bd1 = new BigDecimal(dd);
                        mem = bd1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        cdtc.setMemorySize(mem);
                    }else {
                        cdtc.setMemorySize(mem1+"");
                    }

                }
            }
//            if(cdtc.getDeviceType().equals("云主机")) {
//                cdtc.setCpuCoreNumber(198692+"");
//                cdtc.setMemorySize(1414812+"");
//            } else if (cdtc.getDeviceType().equals("X86服务器")){
//                cdtc.setMemorySize(2460384+"");
//                cdtc.setCpuCoreNumber(366618+"");
//            } else if (cdtc.getDeviceType().equals("宿主机")){
//                cdtc.setMemorySize(2460384+"");
//                cdtc.setCpuCoreNumber(366618+"");
//            }

        }
        return list;
    }

    @Override
    public Map<String, Object> deviceUsedRate(@RequestBody Map<String,String> param) {
        Map<String, Object> response = Maps.newHashMap();
        try {
            String startTime = param.get("startTime");
            String endTime = param.get("endTime");
            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
                String format = "yyyy-MM-dd HH:mm:ss";
                DateFormat dateFormat = new SimpleDateFormat(format);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                Date end = calendar.getTime();
                endTime = dateFormat.format(end);
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date start = calendar.getTime();
                startTime = dateFormat.format(start);
            }

            IdcTypePhysicalReq req = new IdcTypePhysicalReq();
            req.setStartDate(startTime);
            req.setEndDate(endTime);
            req.setSourceType(param.get("monitor"));
            req.setDeviceType(phyServer);
            Map<String, Object> phy = esIndexPageServiceClient.deviceUtilization(req);
            response.putAll(phy);
            req.setDeviceType(vmHost);
            Map<String, Object> vm = esIndexPageServiceClient.deviceUtilization(req);
            response.putAll(vm);
            req.setDeviceType(suHost);
            Map<String, Object> host = esIndexPageServiceClient.deviceUtilization(req);
            response.putAll(host);
        } catch (Exception e) {
            log.error("deviceUsedRate Error is {} >>>> {}", e);
        }
        return response;
    }

    @Override
    public List<Map<String, Object>> getAlertView(@RequestBody Map<String,Object> param) {
        return client.getAlertView(param);
    }

    @Override
    public List<Map<String, Object>> bizSystemDeviceUsedRate(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception {
        List<Map<String, Object>> maps =
                esIndexPageServiceClient.bizSystemDeviceUsedRateForKG(jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq));
        if (CollectionUtils.isNotEmpty(maps)) {
        	String bizKey = "bizSystem";
        	List<String> bizIdList = maps.stream().map(item ->MapUtils.getString(item, bizKey)).collect(Collectors.toList());
        	Map<String, String> dataMap = cmdbHelper.getDepartmentDataMapByTye(bizIdList,bizKey);
        	//设置业务系统名称
        	for(Map<String, Object> ll:maps) {
        		String key = MapUtils.getString(ll, bizKey);
        		if(dataMap.containsKey(key)) {
        			ll.put(bizKey, dataMap.get(key));
        		}
        	}
        	maps = maps.stream()
                    .sorted(
                            (m1, m2) -> {
                                double o1_value = null == m1.get("value") ? 0 : Double.parseDouble(String.valueOf(m1.get("value")));
                                double o2_value = null == m2.get("value") ? 0 : Double.parseDouble(String.valueOf(m2.get("value")));
                                return (int)(o2_value - o1_value);
                            }
                    )
                    .collect(Collectors.toList());
        	
        	
        	
        }
        return maps;
    }

    @Override
    public Map<String, Object> deviceUsedRateTrends(@RequestBody ComIdcTypePhysicalReq comIdcTypePhysicalReq) throws Exception {
        Map<String, Object> response =
                esIndexPageServiceClient.deviceUsedRateTrendsForKG(jacksonBaseParse(IdcTypePhysicalReq.class, comIdcTypePhysicalReq));
        if (null == response || response.isEmpty()) {
            response = Maps.newHashMap();
            response.put("xAxis",new ArrayList<>());
            Map<String, Object> series = Maps.newHashMap();
            series.put("avg",new ArrayList<>());
            series.put("max",new ArrayList<>());
            response.put("series",series);
        }
        return response;
    }

    @Override
    public Map<String, Object> storageUseView(@RequestBody Map<String,String> param) {
        return esIndexPageServiceClient.storageUseView(param);
    }

    @Override
    public List<Map<String, String>> getSegmentUseList() {
        List<ConfigDict> dictsByType = configDictClient.getDictsByType("segment_use", null, null, null);
        Map<String,List<String>> dictMap = Maps.newHashMap();
        for (ConfigDict configDict : dictsByType) {
            List<String> strings = dictMap.get(configDict.getValue());
            if (null == strings) {
                strings = Lists.newArrayList();
            }
            strings.add(configDict.getName());
            dictMap.put(configDict.getValue(),strings);
        }

        List<Map<String, String>> res = Lists.newArrayList();
        dictMap.keySet().forEach(key -> {
            List<String> strings = dictMap.get(key);
            List<String> collect = strings.stream().distinct().collect(Collectors.toList());
            String str = Joiner.on(",").join(collect);
            Map<String, String> m = Maps.newHashMap();
            m.put("field_name",key);
            m.put("field_value",str);
            res.add(m);
        });
        return res;
    }

    @Override
    public Map<String, Object> storageUsedRateForKG(@RequestBody Map<String,String> param) {

//        Map<String, Object> map1 = indexPageServiceClient.StorageUseRate();
        Map<String, Object> map1 = esIndexPageServiceClient.storageUsedRateForKG(param);
        if (null == map1 || map1.isEmpty()) {
            map1 = Maps.newHashMap();
            map1.put("san", 0);
            map1.put("sanMax", 0);
            map1.put("sanAvg", 0);
            map1.put("storage", 0);
            map1.put("blockMax", 0);
            map1.put("blockAvg", 0);
        }
        return map1;
    }
}
