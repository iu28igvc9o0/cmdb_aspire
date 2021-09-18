package com.aspire.mirror.alert.server.controller.alert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspire.mirror.alert.api.dto.alert.AlertKpiBook;
import com.aspire.mirror.alert.api.dto.alert.KpiData;
import com.aspire.mirror.alert.api.service.alert.IAlertRestfulService;
import com.aspire.mirror.alert.server.biz.alert.AlertRestfulBiz;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.dashboard.DeviceItemInfoDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardization;
import com.aspire.mirror.alert.server.dao.alert.po.KpiBook;
import com.aspire.mirror.alert.server.dao.alert.po.KpiListData;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.dao.cmdbInstance.CmdbInstanceMapper;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: AlertDeriveController
 * @projectName mirror-alert
 * @description: TODO
 * @date 2019/8/1614:14
 */
@RestController
@Slf4j
public class AlertRestfulController implements IAlertRestfulService {
    @Autowired
	private AlertRestfulBiz alertRestfulBiz;
    
    @Autowired
	private AlertRestfulDao alertRestfulDao;
    
    @Autowired
	private DeviceItemInfoDao deviceItemInfoDao;
    @Autowired
    private AuthHelper authHelper;
    @Autowired
    private CmdbInstanceMapper cmdbInstanceMapper;
    @Autowired
	private AlertsBizV2 alertsBizV2;

	@Override
	public String bookAlerts(@RequestBody com.aspire.mirror.alert.api.dto.alert.AlertStandardization stand) {
		if (stand == null) {
			log.warn("bookAlerts param stand is null");
			return null;
		}
		if (StringUtils.isBlank(stand.getDisplayCols()) || StringUtils.isBlank(stand.getTopic())) {
			log.warn("bookAlerts param displayCols:{};topic:{}",stand.getDisplayCols(),stand.getTopic());
			return null;
		}
		
		AlertStandardization standDTO = new AlertStandardization();
		BeanUtils.copyProperties(stand, standDTO);
		if(null==standDTO.getStatus()) {
			standDTO.setStatus(1);
		}
		
		alertRestfulBiz.insertBookAlerts(standDTO);
		return JSON.toJSONString(new ResponseEntity<String>("success", HttpStatus.OK));
	}
	@Override
	public List<HashMap<String,String>> getMonitorList(@RequestParam(value = "device_class",required=false)String deviceClass
			,@RequestParam(value = "device_type",required=false)String deviceType) {
		List<HashMap<String,String>> datalist = deviceItemInfoDao.getMonitorList(deviceClass, deviceType);
			for(HashMap<String, String> o:datalist){
				o.put("key", o.remove("moniter_item_key"));
			}
		return datalist;
	}
	
	@Override
	public String bookKpiList(@RequestBody AlertKpiBook regData) {
		if (regData == null) {
			log.warn("bookKpiList param regData is null");
			return null;
		}
		List<KpiData> list = regData.getKpiList();
		if (null==list || list.size()==0 ) {
			log.warn("bookAlerts param kpiList is null or empty");
			return null;
		}
		String interval = regData.getInterval();
		if(StringUtils.isBlank(interval)) {
			interval = "10";
		}
		
		KpiBook dto = new KpiBook();
		if(null == regData.getStatus()) {
			dto.setStatus(1);
		}else {
			dto.setStatus(regData.getStatus());
		}
		dto.setInterval(Integer.parseInt(interval));
		dto.setSource(regData.getSource());
		dto.setSubTopic(regData.getSubTopic());
		
		List<KpiListData> data = Lists.newArrayList();
		for(KpiData k:list) {
			KpiListData kk = new KpiListData();
			kk.setIdc_type(k.getIdc_type());
			List<String> pod = k.getPod();
			List<String> rooId = k.getRoomId();
			List<String> keys = k.getKeys();
			if(null!=pod &&pod.size()>0) {
				kk.setPod(pod.stream().collect(Collectors.joining(",")));
			}
			if(null!=rooId &&rooId.size()>0) {
				kk.setRoomId(rooId.stream().collect(Collectors.joining(",")));
			}
			if(null!=keys &&keys.size()>0) {
				kk.setKeys_value(keys.stream().collect(Collectors.joining(",,/;")));
			}
			data.add(kk);
		}
		dto.setKpiList(data);
		alertRestfulBiz.insertBookKpiList(dto);
		return JSON.toJSONString(new ResponseEntity<String>("success", HttpStatus.OK));
	}

	@Override
	public Map<String,List<String>> getAuthField(@RequestParam(value = "type",required=false)Integer type)  {
		Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
		
		return authHelper.packCondition(resFilterMap,type);
	}
	
	@Override
	public PageResponse<Map<String,Object>> getCmdbPageList(@RequestBody Map<String,Object> params)  {
		String bizSystem = MapUtils.getString(params, "bizSystem");
		if(StringUtils.isNotBlank(bizSystem)) {
			params.put("bizSystemList", bizSystem.split(","));
		}
		int pageNo = params.get("pageNo")==null?1:Integer.parseInt(params.get("pageNo").toString());
		int pageSize = params.get("pageSize")==null?50:Integer.parseInt(params.get("pageSize").toString());
		Page page = new Page();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		page.setParams(params);
        int count = cmdbInstanceMapper.pageCount(page);
        PageResponse<Map<String,Object>> pageResponse = new PageResponse< Map<String,Object>>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<Map<String,Object>> listAlertFilter = cmdbInstanceMapper.pageList(page);
            pageResponse.setResult(listAlertFilter);
        }
		return pageResponse;
	}
	
	

	/**
	 * 根据设备信息获取设备最新告警
	 * @param deviceIds
	 * @return
	 */
	public List<Map<String, Object>> getDeviceNewestAlertLevelList(@RequestBody List<String> deviceIds) {

		return alertsBizV2.getDeviceNewestAlertLevelList(deviceIds);
	}

	/**
	 * 根据监控项获取监控项最新告警
	 * @param itemIds
	 * @return
	 */
	public List<Map<String, Object>> getItemNewestAlertLevelList(@PathVariable(name = "prefix") String prefix,  @RequestBody List<String> itemIds) {

		return alertsBizV2.getItemNewestAlertLevelList(prefix, itemIds);
	}
	
	@Override
	public List<Map<String, Object>> getIdcTypePerformanceData(@RequestBody Map<String,String> params) {
		return alertRestfulDao.getIdcTypePerformanceData(params.get("idcType"),params.get("startTime") 
				,params.get("endTime") ,params.get("item"),params.get("deviceType") );
	}
}
