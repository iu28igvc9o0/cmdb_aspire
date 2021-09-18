package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.*;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.EsIndexPageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix.ZabbixItemServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix.ZabbixTrendsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.zabbix.ZabbixTriggerServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.common.StringUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.composite.service.alert.ICompAlertItemService;
import com.aspire.mirror.composite.service.alert.ICompKGMonitorIndexService;
import com.aspire.mirror.composite.service.alert.payload.ItemDto;
import com.aspire.mirror.composite.service.alert.payload.TrendsDto;
import com.aspire.mirror.composite.service.alert.payload.TriggerDto;
import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.CmdbIndexInstClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;

/**
 * @author baiwp
 * @title: CompAlertItemController
 * @projectName msp-composite
 * @description: TODO
 * @date 2019/6/2415:00
 */
@RestController
@Slf4j
public class CompZabbixAlertItemController implements ICompAlertItemService {
    @Autowired
    private ZabbixItemServiceClient zabbixItemServiceClient;
    @Autowired
    private ZabbixTrendsServiceClient zabbixTrendsServiceClient;
    @Autowired
    private ZabbixTriggerServiceClient zabbixTriggerServiceClient;
    
    @Autowired
    private CmdbIndexInstClient indexInstClient;
    @Autowired
	private AlertsIndexPageServiceClient indexPageServiceClient;
    
    @Autowired
	private EsIndexPageServiceClient esIndexPageServiceClient;
    @Autowired
	private ICompKGMonitorIndexService compKGMonitorIndexService;
    @Autowired
    private AlertRestfulClient alertRestfulClient;
    
    
    @Value("${indexPage.count.realDataFlag:false}")
	private boolean  realDataFlag;
    @Override
    public PageResult<ItemDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                                     @RequestParam(value = "idc", required = false) String idc,
                                     @RequestParam(value = "prefix", required = false) String prefix,
                                     @RequestParam(value = "ip", required = false) String ip,
                                     @RequestParam(value = "resourceId", required = false) String resourceId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        PageResult itemPage = zabbixItemServiceClient.query(itemId, idc, prefix, ip, resourceId, pageNum, pageSize);
        List<ItemDto> itemList = PayloadParseUtil.jacksonBaseParse(ItemDto.class, itemPage.getResult());
        PageResult<ItemDto> page = new PageResult<>();
        page.setPageSize(itemPage.getPageSize());
        page.setCurPage(itemPage.getCurPage());
        page.setCount(itemPage.getCount());
        page.setPageCount(page.getPageCount());
        page.setResult(itemList);
        return page;
    }

    @Override
    public List<TrendsDto> queryTrends(@RequestParam(value = "itemId") String itemId,
                                       @RequestParam(value = "prefix", required = false) String prefix,
                                       @RequestParam(value = "startTime") Long startTime,
                                       @RequestParam(value = "endTime") Long endTime) {
        return PayloadParseUtil.jacksonBaseParse(TrendsDto.class, zabbixTrendsServiceClient.queryTrends(itemId, prefix, startTime, endTime));
    }

    @Override
    public PageResult<TriggerDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                                        @RequestParam(value = "idc", required = false) String idc,
                                        @RequestParam(value = "prefix", required = false) String prefix,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        PageResult triggerPage = zabbixTriggerServiceClient.query(itemId, idc, prefix, pageNum, pageSize);
        List<TriggerDto> triggerList = PayloadParseUtil.jacksonBaseParse(TriggerDto.class, triggerPage.getResult());
        PageResult<TriggerDto> page = new PageResult<>();
        page.setPageSize(triggerPage.getPageSize());
        page.setCurPage(triggerPage.getCurPage());
        page.setCount(triggerPage.getCount());
        page.setPageCount(page.getPageCount());
        page.setResult(triggerList);
        return page;
    }

    @Override
    public Map<String, Object> deviceUsedRate(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                                              @RequestParam(value = "deviceType", required = false) String deviceType) {
        return zabbixTrendsServiceClient.deviceUsedRate(bizSystem, deviceType);
    }
    
    @Override
    public Map<String, Object> deviceUsedRateByMonitor(
			@RequestParam(value = "bizSystem", required = false) String bizSystem,
			@RequestParam(value = "monitor", required = false) String monitor) throws Exception{
    	String format = "yyyy-MM-dd HH:mm:ss";
    	DateFormat returndf = new SimpleDateFormat(format);
    	Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date end = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date start = calendar.getTime();
		IdcTypePhysicalReq req = new IdcTypePhysicalReq();
		req.setBizSystem(bizSystem);
		req.setSourceType(monitor);
		req.setStartDate(returndf.format(start));
		req.setEndDate(returndf.format(end));
		req.setDeviceType("X86服务器");
    	 Map<String, Object> map = esIndexPageServiceClient.deviceUtilization(req);
    	 
    	 req.setDeviceType("云主机");
    	 Map<String, Object> map1 = esIndexPageServiceClient.deviceUtilization(req);
    	 map1.putAll(map);
    	 return map1;
    }
    
    @Override
    public Map<String, Object> storageUsedRate(@RequestParam(value = "startTime",required=false) String startTime,
			@RequestParam(value = "endTime",required=false) String endTime,
			@RequestParam(value = "bizSystem", required = false) String bizSystem) throws ParseException {
    	if(this.realDataFlag) {
    		Map<String,String> param = Maps.newHashMap();
    		param.put("startDate", "");
    		param.put("endDate", "");
    		 Map<String, Object> data = compKGMonitorIndexService.storageUsedRateForKG(param);
    		 data.put("SAN", data.get("san"));
    		 data.put("块存储", data.get("storage"));
    		return data;
    	}else {
    		Map<String, Object> map = indexInstClient.countDiskSize();
        	Map<String, Object> map1 = indexPageServiceClient.StorageUseRate();
        	map1.putAll(map);
        	map1.put("SAN", 8060.54);
        	map1.put("块存储", 18401);
        	
        		return map1;
    	}
    	
    }

    @Override
    @Authentication(anonymous = true)
    public String deviceUsedRateForKG() {
        Map<String, Object> response = new HashMap<>();
        try {
            String format = "yyyy-MM-dd HH:mm:ss";
            DateFormat returndf = new SimpleDateFormat(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date end = calendar.getTime();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            Date start = calendar.getTime();
            IdcTypePhysicalReq req = new IdcTypePhysicalReq();
            req.setStartDate(returndf.format(start));
            req.setEndDate(returndf.format(end));
            // cpu
            Map<String, String> cpu = this.getDeviceUtilization(req, "cpu");
            // memory
            Map<String, String> memory = this.getDeviceUtilization(req, "memory");
            memory.putAll(cpu);
            List<String> keyList = new ArrayList<>(memory.keySet());
            List<String> valueList = Lists.newArrayList();
            for (String str : keyList) {
                valueList.add(memory.get(str));
            }
            List<List<String>> data = Lists.newArrayList();
            if (valueList.isEmpty()) {
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
                valueList.add("0");
            }
            data.add(valueList);
            if (keyList.isEmpty()) {
                keyList.add("host_memory_max");
                keyList.add("server_cpu_max");
                keyList.add("host_memory_avg");
                keyList.add("server_memory_max");
                keyList.add("host_cpu_avg");
                keyList.add("server_memory_avg");
                keyList.add("server_cpu_avg");
                keyList.add("host_cpu_max");
            }
            response.put("columns",keyList);
            response.put("data",data);
            response.put("status",0);
            response.put("msg","");
            response.put("total",1);
        } catch (Exception e) {
            response.put("status",-1);
            response.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(response);
    }

    @Value("${cmdb.kg.deviceType.phyServer:X86服务器}")
    private String phyServer;
    @Value("${cmdb.kg.deviceType.vmHost:虚拟机}")
    private String vmHost;
    @Value("${cmdb.kg.deviceType.suHost:宿主机}")
    private String suHost;

    private Map<String, String> getDeviceUtilization(IdcTypePhysicalReq req, String monitor) {
        Map<String, String> response = new HashMap<>();
        try {
            req.setSourceType(monitor);
            req.setDeviceType(phyServer);
            Map<String, Object> serverMap = esIndexPageServiceClient.deviceUtilization(req);
            Map<String, Object> map = (Map)serverMap.get(phyServer);
            response.put("server_"+ monitor +"_max",null == map.get("max") ? "0" : String.valueOf(map.get("max")));
            response.put("server_"+ monitor +"_avg",null == map.get("avg") ? "0" : String.valueOf(map.get("avg")));
            req.setDeviceType(vmHost);
            Map<String, Object> hostMsp = esIndexPageServiceClient.deviceUtilization(req);
            Map<String, Object> map1 = (Map)hostMsp.get(vmHost);
            response.put("host_"+ monitor +"_max",null == map1.get("max") ? "0" : String.valueOf(map1.get("max")));
            response.put("host_"+ monitor +"_avg",null == map1.get("avg") ? "0" : String.valueOf(map1.get("avg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 根据设备id查询监控项列表以及对应阈值
     * @param resourceId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<ItemDto> queryByResourceId(@RequestParam(value = "resourceId") String resourceId,
                                                 @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        PageResult itemPage = zabbixItemServiceClient.query(null, null, null, null, resourceId, pageNum, pageSize);
        List<ItemDto> itemList = PayloadParseUtil.jacksonBaseParse(ItemDto.class, itemPage.getResult());
        if (!CollectionUtils.isEmpty(itemList)) {
            List<String> itemIds = Lists.newArrayList();
            List<String> keyList = Lists.newArrayList();
            String prefix = null;
            for (ItemDto itemDto : itemList) {
                String itemId = itemDto.getItemId();
                if (!StringUtils.isEmpty(itemId)) {
                    itemIds.add(itemId);
                }
                if (!StringUtils.isEmpty(itemDto.getKey())) {
                    itemIds.add(itemDto.getKey());
                }
                if (StringUtils.isEmpty(prefix)) {
                    prefix = itemDto.getPrefix();
                }
            }
            // 查询阈值
            List<Map<String, Object>> thresholdList = zabbixTriggerServiceClient.queryTriggerThreshold(itemIds, null, prefix);
            List<Map<String, Object>> levelList = alertRestfulClient.getItemNewestAlertLevelList(prefix, itemIds);

            List<Map<String, Object>> kpiValueList = esIndexPageServiceClient.getKpiListForItem(resourceId, keyList);
            for (ItemDto itemDto : itemList) {
                String itemId = itemDto.getItemId();
                String key = itemDto.getKey();
                for (Map<String, Object> kpiValue : kpiValueList) {
                    String item = MapUtils.getString(kpiValue, "item");
                    if (!StringUtils.isEmpty(key) && key.equalsIgnoreCase(item)) {
                        itemDto.setValue(MapUtils.getString(kpiValue, "value"));
                        break;
                    }
                }

                if (StringUtils.isEmpty(itemId)) {
                    continue;
                }
                for (Map<String, Object> thresholdMap: thresholdList) {
                    String itemId1 = MapUtils.getString(thresholdMap, "itemId");
                    if (itemId.equals(itemId1)) {
                        itemDto.setThreshold(MapUtils.getString(thresholdMap, "threshold"));
                        break;
                    }
                }

                for (Map<String, Object> level: levelList) {
                    String itemId1 = MapUtils.getString(level, "item_id");
                    if (itemId.equals(itemId1)) {
                        itemDto.setAlertLevel(MapUtils.getString(level, "alert_level"));
                        break;
                    }
                }

            }
        }

        // 组装结果
        PageResult<ItemDto> page = new PageResult<>();
        page.setPageSize(itemPage.getPageSize());
        page.setCurPage(itemPage.getCurPage());
        page.setCount(itemPage.getCount());
        page.setPageCount(page.getPageCount());
        page.setResult(itemList);
        return page;
    }
}
