package com.migu.tsg.microservice.atomicservice.composite.controller.alert;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.alert.IComResourceRateScreenService;
import com.aspire.mirror.composite.service.alert.payload.ComTenantRateRequest;
import com.aspire.mirror.composite.service.cmdb.restful.alert.IAlertRestfulAPI;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.TenantRateRequest;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.EsResourceRateScreenServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbHelper;

/**
 * 监控值控制层（拓扑用）
 * <p>
 * 项目名称: mirror平台 包:
 * com.migu.tsg.microservice.atomicservice.composite.controller.alert 类名称:
 * CompMonitorValueController.java 类描述: TODO 创建人: JinSu 创建时间: 2019/10/24 19:39
 * 版本: v1.0
 */
@RestController
public class ComResourceRateScreenController implements IComResourceRateScreenService{
	private Logger logger = LoggerFactory.getLogger(ComResourceRateScreenController.class);
	@Autowired
	private EsResourceRateScreenServiceClient esResourceRateScreenServiceClient;
	@Autowired
	private IAlertRestfulAPI alertRestfulAPI;
	@Value("${IT_COMPANY_INTERNAL:6c1f1415-aa0d-11e9-995c-0242ac110002}")
	private   String IT_COMPANY_INTERNAL;
	private CmdbHelper cmdbHelper;
	
	@Override
	public Map<String, Object> queryData(@RequestParam(value = "timeRange", required = false)String timeRange
			 ,@RequestParam(value = "idcType", required = false)String idcType
			 ,@RequestParam(value = "deviceType", required = false)String deviceType
			 ,@RequestParam(value = "col", required = false)String col
			 ,@RequestParam(value = "colValue", required = false)String colValue
			 ,@RequestParam(value = "granularity", required = false)String granularity )throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date endDate = new Date();
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(endDate);
		 if(timeRange.equals("month")) {
				calendar.add(Calendar.MONTH, -1);
		 }
		 if(timeRange.equals("week")) {
				calendar.add(Calendar.DATE, -7);
		 }
		 if(timeRange.equals("day")) {
				calendar.add(Calendar.DATE, -1);
		 }
		 if(timeRange.equals("hour")) {
				calendar.setTimeInMillis(endDate.getTime()-60*60*1000);
		 }
		 Date startTime = calendar.getTime();
		 
		 MonthReportRequest  rq = new MonthReportRequest();
		 rq.setIdcType(idcType);
		 if(deviceType.equals("裸金属")||deviceType.equals("X86服务器")) {
			 deviceType = "X86服务器";
		 }else {
			 deviceType = "云主机";
		 }
		 rq.setDeviceType(deviceType);
		 rq.setStartTime(sdf.format(startTime));
		 rq.setEndTime(sdf.format(endDate));
		 rq.setCol(col);
		 rq.setColValue(colValue);
		 rq.setGranularity(granularity);
		 Map<String, Object> map = esResourceRateScreenServiceClient.queryData(rq);
		 logger.info("msg:{}",map);
		 return map;
	 }
	 
	@Override
	public List<Map<String, Object>> queryDepartmentData(@RequestBody ComTenantRateRequest request) throws ParseException{
		 TenantRateRequest rq = jacksonBaseParse(TenantRateRequest.class, request);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date endDate = new Date();
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(endDate);
		 calendar.add(Calendar.MONTH, -1);
		 Date startTime = calendar.getTime();
		 rq.setStartTime(sdf.format(startTime));
		 rq.setEndTime(sdf.format(endDate));
		 if(request.getDeviceType().equals("裸金属")||request.getDeviceType().equals("X86服务器")) {
			 request.setDeviceType("X86服务器");
		 }else {
			 request.setDeviceType("云主机");
		 }
		 if(request.getCol().equals("department2")) {
			 rq.setDepartment1(this.IT_COMPANY_INTERNAL);
		 }
		 Map<String, Map<String,String>> map = esResourceRateScreenServiceClient.queryDepartmentData(rq);
		 //request.setCol("department2");
		 if(null!=map && map.size()>0) {
			 List<String> bizIdList = new ArrayList<String>(map.keySet());
			
			 if(request.getCol().equals("department1")) {
				 Map<String, String>  dataMap = cmdbHelper.getDepartmentDataMapByTye(bizIdList,"department");
				 List<Map<String, Object>> list1 = alertRestfulAPI.statisticsDepartmentForAvailability(null);
				 for(Map<String, Object> val :list1) {
					 if(null==val.get("department")) {
						 continue;
					 }
					String department = val.get("department").toString();
					if(map.containsKey(department)) {
						String num = val.get("totalCount").toString();
						 Map<String,String> data = map.get(department);
						 data.put("totalCount", num);
						 if(dataMap.containsKey(department)) {
							 data.put("col", dataMap.get(department));
						 }
					}
				 }
			 }else if(request.getCol().equals("department2")){
				 Map<String, String>  dataMap = cmdbHelper.getDepartmentDataMapByTye(bizIdList,"department");
				 List<Map<String, Object>> list1 = alertRestfulAPI.statisticsDepartmentForAvailability(this.IT_COMPANY_INTERNAL);
				 for(Map<String, Object> val :list1) {
					 if(null==val.get("department")) {
						 continue;
					 }
						String department = val.get("department").toString();
						if(map.containsKey(department)) {
							String num = val.get("totalCount").toString();
							 Map<String,String> data = map.get(department);
							 data.put("totalCount", num);
							 if(dataMap.containsKey(department)) {
								 data.put("col", dataMap.get(department));
							 }
						}
					 }
			 }else {
				 Map<String, Object> mapRq = Maps.newHashMap();
				 List<Map<String, String>> list1 = alertRestfulAPI.statisticsDeviceByBizSystem(mapRq);
				 //logger.info("dd:{}",list1);Map<String, Map<String,String>>
				 Map<String, Map<String, Object>> bizMap = cmdbHelper.getBizDepartmentDatas(bizIdList);
				 for(Map<String, String> val :list1) {
					 if(null==val.get("bizSystem")) {
						 continue;
					 }
						String department = val.get("bizSystem").toString();
						if(map.containsKey(department)) {
							String num = val.get("deviceCount").toString();
							 Map<String,String> data = map.get(department);
							// String department1 = val.get("department1")==null?"":val.get("department1").toString();
							 if(bizMap.containsKey(department)) {
								 Map<String, Object> v = bizMap.get(department);
								 data.put("col", MapUtils.getString(v, "bizSystem"));
								 data.put("department", MapUtils.getString(v, "department2_name"));
							 }
							 if(data.containsKey("totalCount")) {
								 continue;
							 }
							 data.put("totalCount", num);
							// data.put("department", val.get("department2")==null?"":val.get("department2").toString());
						}
					 }
			 }
				 
				 
			 
		 }
		 List<Map<String, Object>> valList = Lists.newArrayList();
		 valList = new ArrayList(map.values());
		 return valList;
	 }
}
