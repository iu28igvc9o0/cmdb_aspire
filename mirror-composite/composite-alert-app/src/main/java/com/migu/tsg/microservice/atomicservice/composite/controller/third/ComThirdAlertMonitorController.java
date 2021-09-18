package com.migu.tsg.microservice.atomicservice.composite.controller.third;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.api.dto.third.AlertThirdRequest;
import com.aspire.mirror.composite.payload.third.ComAlertThirdRequest;
import com.aspire.mirror.composite.service.third.IComThirdAlertMonitorService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.third.AlertThirdMonitorServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Maps;


@RestController
public class ComThirdAlertMonitorController implements IComThirdAlertMonitorService {

	@Autowired
	private AlertThirdMonitorServiceClient alertsThirdServiceClient;
   
	//直真接口
	/**
	 * p1.1:4资源池设备数量统计
	 */
	@Override
	public Map<String,List<Map<String,Object>>>  idcTypeUserRate() throws ParseException{
		
	   return alertsThirdServiceClient.idcTypeUserRate();
		 
	}
	
	//p2.6资源利用率
	@Override
	public List<Map<String,Object>>  bizSystemTopUseRate(@RequestParam(value="idcType",required = false)String idcType) throws ParseException {
		
	   return alertsThirdServiceClient.bizSystemTopUseRate(idcType);
		 
	}
	
	//p1.2:4低利用率应用系统
	@Override
	public List<Map<String,Object>>  bizSystemLowUseRate(@RequestBody ComAlertThirdRequest params) {
		
		AlertThirdRequest req = PayloadParseUtil.jacksonBaseParse(AlertThirdRequest.class,params);
		return alertsThirdServiceClient.bizSystemLowUseRate(req);
	}
	
	//p1.2:4/1低利用率应用系统
	@Override
	public List<Map<String,Object>>  bizSystemLowUseRateTopN(@RequestBody ComAlertThirdRequest params) {
		AlertThirdRequest req = PayloadParseUtil.jacksonBaseParse(AlertThirdRequest.class,params);
		   return alertsThirdServiceClient.bizSystemLowUseRateTopN(req);
	}
	
	
	//p1.2:3/1低利用率应用系统
	@Override
	public List<Map<String,Object>>  bizSysDayType2TopN(@RequestBody ComAlertThirdRequest params) {
		AlertThirdRequest req = PayloadParseUtil.jacksonBaseParse(AlertThirdRequest.class,params);
		   return alertsThirdServiceClient.bizSysDayType2TopN(req);
	}
	
	//p1.2:3低利用率应用系统
	@Override
	public List<Map<String,Object>>  bizSysDayType2UseRate(@RequestBody ComAlertThirdRequest params) throws Exception {
		AlertThirdRequest req = PayloadParseUtil.jacksonBaseParse(AlertThirdRequest.class,params);
		   return alertsThirdServiceClient.bizSysDayType2UseRate(req);
	}

	//查询性能打分数据
	@Override
	public Map<String,Object> getPoorEfficiencyDevice(@RequestParam("start")String start
			,@RequestParam("end") String end, @RequestParam("page_no")Integer page_no,
			@RequestParam("page_size")Integer page_size) {
		//设置返回信息
		Map<String,Object> returnMap = Maps.newHashMap();
		returnMap.put("success", true);
		 returnMap.put("total",null);
		 returnMap.put("data",null);
		 returnMap.put("errMsg",null);
		try {
			Map<String,Object> params = Maps.newHashMap();
			params.put("start", start);
			params.put("end", end);
			if(null!=page_no && null!=page_size && page_no!=0) {
				if(page_no==1) {
					params.put("pageNo", 0);
				}else {
					page_no--;
					params.put("pageNo", page_no*page_size);
				}
			}else {
				params.put("pageNo", page_no);
			}
			
			params.put("pageSize", page_size);
			params.put("type", 1);
			 PageResponse<Map<String, Object>> data = alertsThirdServiceClient.getPoorEfficiencyDevice(params);
			 returnMap.put("total", data.getCount());
			 returnMap.put("data", data.getResult());
		}catch(Exception e) {
			returnMap.put("success", false);
			StringBuffer sb = new StringBuffer();
			sb.append(start).append(end).append(page_no).append(page_size);
			String msg = sb.append(e.getClass().getName())
					.append(":").append(e.getMessage()).toString();
			returnMap.put("errMsg", msg.toString());
		}
		return returnMap;
	}
	

}
