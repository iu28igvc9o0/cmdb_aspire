package com.aspire.mirror.alert.server.task.monthReport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.aspire.mirror.alert.server.vo.monthReport.AlertCloudPhyVo;
import com.aspire.mirror.alert.server.vo.monthReport.AlertCloudVmVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.clientservice.CmdbResfulClient;
import com.aspire.mirror.alert.server.clientservice.EsCloudSysClient;
import com.aspire.mirror.alert.server.dao.monthReport.AlertMonthReportSyncDao;
import com.aspire.mirror.elasticsearch.api.dto.AlertEsHistoryReq;
import com.aspire.ums.cmdb.common.Result;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RestController
public class CloudSysSyncKafkaTask {
	private static final Logger LOGGER = LoggerFactory.getLogger(CloudSysSyncKafkaTask.class);



	@Value("${CloudSysSyncTask.num:100}")
	private int num;

	@Value("${CloudSysSyncTask.token:5245ed1b-6345-11e}")
	private String token;
	
	@Value("${CloudSysSyncTask.condicationCode:alert_index_sync}")
	private String condicationCode;
	
	@Value("${CloudSysSyncTask.delIdcType:呼和浩特资源池}")
	private String delIdcType;
	
	@Value("${CloudSysSyncTask.delPod:POD-3}")
	private String delPod;
	
	@Value("${CloudSysSyncTask.returncode:000}")
	private String returncode;
	
	@Value("${CloudSysSyncTask.VM_URL}")
	private String VM_URL;
	
	@Value("${CloudSysSyncTask.PHY_URL}")
	private String PHY_URL;
	
	@Value("${CloudSysSyncTask.ConnectTimeout:60000}")
	private int ConnectTimeout;
	
	@Value("${CloudSysSyncTask.SocketTimeout:300000}")
	private int SocketTimeout;

	
	@Autowired
	private CmdbResfulClient cmdbCommonRestfulClient;

	@Autowired
	private EsCloudSysClient esCloudSysClient;

	private static ExecutorService EXECUTOR = null;
	
	@Autowired
	private AlertMonthReportSyncDao alertMonthReportSyncDao;
	
	public void getData(String startStr,String endStr,String deviceType,String msgType,Date start) {
		LOGGER.info("{}-getData-begintime:{},endtime:{}",msgType,startStr,endStr);
		boolean isVmFlag = true;
		if(deviceType.equals("X86服务器")) {
			isVmFlag = false;
		}
		
		Map<String,Object> mapConfig = Maps.newHashMap();
		mapConfig.put("from_time", startStr);
		mapConfig.put("to_time", endStr);
		mapConfig.put("status", "success");
		mapConfig.put("device_type", deviceType);
		mapConfig.put("exec_time",start);
		
		//String url = this.VM_URL + "?" + "begintime=" + startStr + "&endtime=" + endStr;
		String url = this.VM_URL;
		if(!isVmFlag) {
			url = this.PHY_URL;
		}
		//url = "http://127.0.0.1:28177/v1/alerts/deriveAlert/v1/alert/getPhyData";
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", "application/json");
		JSONObject js1 = new JSONObject();
		js1.put("begintime", startStr);
		js1.put("endtime", endStr);
		final StringEntity entity1 = new StringEntity(js1.toJSONString(), "UTF-8");
		post.setEntity(entity1);
		
		CloseableHttpClient client = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		StringBuilder res = new StringBuilder();
		BufferedReader br = null;
		boolean errorFlag = false;
		String msg = "";
		
		try {
			
			LOGGER.info("{}getDatapostendBegin***********", msgType);
			RequestConfig config = RequestConfig.custom().setConnectTimeout(ConnectTimeout).setSocketTimeout(SocketTimeout).build();
			post.setConfig(config);
			response = client.execute(post);
			LOGGER.info("{}getDatapostend***********", msgType);
			HttpEntity entity = response.getEntity();

			if (entity != null) { // 转换为字节输入流
				br = new BufferedReader(new InputStreamReader(entity.getContent(), Consts.UTF_8));
				String body = null;
				while ((body = br.readLine()) != null) {
					res.append(body);
				}
			}
			JSONObject js = JSON.parseObject(res.toString());
			 
			String syReturncode = js.getString("returncode");
			LOGGER.info("{}returncode:{}",msgType,syReturncode);
			if (syReturncode.equals(this.returncode)) {
				JSONArray arr = js.getJSONArray("rcd");
				if (null != arr) {
					long size = arr.size();
					LOGGER.info("size:"+size);
					mapConfig.put("sum", size);
					//ConstantsUtil.CLOUD_VM_SIZE = size;
					
					int k = 0;
					int devicCount = 0;
					int esCount = 0;
					int delCount = 0;
					while (k < size) {
						JSONArray tem = new JSONArray();
						for (int i = 0; i < this.num; i++) {
							if ((k + i) >= size) {
								k += 300;
								break;
							}
							tem.add(arr.get(i + k));
						}
						Map<String,Object> rsMap = consumeNew(tem.toJSONString(),deviceType,msgType);
						if(rsMap.containsKey("lessDeviceCount")) {
							devicCount +=Integer.parseInt(rsMap.get("lessDeviceCount").toString());
							
						}
						if(rsMap.containsKey("esCount")) {
							esCount +=Integer.parseInt(rsMap.get("esCount").toString());
							
						}
						if(rsMap.containsKey("delCount")) {
							delCount +=Integer.parseInt(rsMap.get("delCount").toString());
							
						}
						k += this.num;
						tem.clear();
					}
					mapConfig.put("less_device_count", devicCount);
					mapConfig.put("esCount", esCount);
					mapConfig.put("delCount", delCount);
					arr.clear();
				}
			} else {
				errorFlag = true;
				msg = msgType+"全网监控中心系统-云资源数据调用虚拟机接口失败,returncode:"+syReturncode;
				LOGGER.error(msg);
				throw new Exception(msg);
			}

		
		}catch (Exception e) {
			errorFlag = true;
			StringBuffer sb = new StringBuffer();
			msg = sb.append(msgType).append(e.getClass().getName())
					.append(":").append(e.getMessage()).toString();
					
			LOGGER.error("{}getData苏研数据错误:{}",msgType,e);
		}finally {
			try {
				if(errorFlag) {
					mapConfig.put("status", "fail");
					mapConfig.put("content", msg);
				}else {
					mapConfig.put("content", msgType+"同步成功");
				}
				
				Date end = new Date();
				mapConfig.put("create_time", end);
				mapConfig.put("exec_duration", (end.getTime()-start.getTime())/1000+"s");
				alertMonthReportSyncDao.insertSuyanConfigLogs(mapConfig);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			IOUtils.closeQuietly(response);
			IOUtils.closeQuietly(client);
		}
		LOGGER.info("*{}CloudSysSyncTaskGetDataEnd***",msgType);
		
	}

	public Map<String,Object> consumeNew(String data,String deviceType,String msgType) throws Exception {
		LOGGER.info("**{}kafkaTOPIC_CLOUD-begin****",msgType);
		Map<String,Object>  rsMap = Maps.newHashMap();
			JSONArray aray = JSON.parseArray(data);
					
					
			Map<String, Map<String, Object>> map = Maps.newHashMap();
			String ids = "";
			
			for(int i = 0; i < aray.size(); i++) {
		           JSONObject obj = aray.getJSONObject(i);
		           String resId = obj.getString("res_id");
		           ids += resId + ",";
			}
			Map<String, Object> params = Maps.newHashMap();
			params.put("suyan_uuid", ids.substring(0, ids.length() - 1));
			params.put("token", token);
			params.put("condicationCode", condicationCode);
			params.put("pageSize", num);
			params.put("currentPage", 1);
			Date from1 = new Date();
			Result<Map<String, Object>> rs = cmdbCommonRestfulClient.getInstanceList(params);
			Date to1 = new Date();
			LOGGER.info("{}cmdb查询苏研VM数据时间:{}ms",msgType, (to1.getTime() - from1.getTime()));
			int dsSize = 0;
			if (null != rs) {
				List<Map<String, Object>> dsList = rs.getData();
				dsSize = dsList.size();
				//LOGGER.info("cmdb查询苏研VM数据dsSize:{},totalSize:{}", dsList.size(),dsSize);
				if (null != dsList) {

					for (Map<String, Object> val : dsList) {
						if(null!= val.get("suyan_uuid")) {
							String resid = val.get("suyan_uuid").toString();
							map.put(resid, val);
						}
						
					}
				}
			}
			int count = aray.size()-dsSize;
			rsMap.put("lessDeviceCount", count);
			LOGGER.info("{}缺失VM苏研数据：{}条", msgType,count);
			List<AlertEsHistoryReq> esList = Lists.newArrayList();
			int delCount = 0;
			for (int i = 0; i < aray.size(); i++) {
				JSONObject obj = aray.getJSONObject(i);
		        String resId = obj.getString("res_id");
		        
				AlertEsHistoryReq es = new AlertEsHistoryReq();
				if (map.containsKey(resId)) {
					Map<String, Object> instance = map.get(resId);
					
					String idcType = instance.get("idcType") == null ? "" : instance.get("idcType").toString();
					String pod_name = instance.get("pod_name") == null ? "" : instance.get("pod_name").toString();
					if(idcType.equals(delIdcType) && pod_name.equals(delPod)) {
						delCount++;
						continue;
					}
					
					es.setHost(instance.get("ip") == null ? "" : instance.get("ip").toString());
					es.setBizSystem(instance.get("bizSystem") == null ? "" : instance.get("bizSystem").toString());
					es.setDepartment1(instance.get("department1") == null ? "" : instance.get("department1").toString());
					es.setDepartment2(instance.get("department2") == null ? "" : instance.get("department2").toString());
					es.setDeviceClass(instance.get("device_class") == null ? "": instance.get("device_class").toString());
					es.setDeviceType(instance.get("device_type") == null ? "" : instance.get("device_type").toString());
					es.setIdcType(idcType);
					es.setRoomId(instance.get("roomId") == null ? "" : instance.get("roomId").toString());
					es.setPodName(pod_name);
				} else {
					LOGGER.error("{}苏研查无该设备信息-苏研resid:{}",msgType, resId);
				}
			
				es.setItemListStr(obj.toJSONString());
				
				
				if (StringUtils.isBlank(es.getDeviceClass())) {
					es.setDeviceClass("服务器");
				}
				if (StringUtils.isBlank(es.getDeviceType())) {
					es.setDeviceType(deviceType);
				}
				esList.add(es);
			}
			LOGGER.info("{}es插入苏研数据-抛弃数量:{}",msgType, delCount);
			rsMap.put("delCount", delCount);
			Date from = new Date();
			int esCount = 0;
			
			if(deviceType.equals("云主机")) {
				esCount = esCloudSysClient.insertCloudVmList(esList);
			}else {
				esCount = esCloudSysClient.insertCloudPhyList(esList);
			}
			rsMap.put("esCount", esCount);
			Date to = new Date();
			LOGGER.info("{}es插入苏研VM数据时间:{}ms",msgType, (to.getTime() - from.getTime()));
			map.clear();
				
		
		LOGGER.info("{}kafkaTOPIC_CLOUD-end*********",msgType);
		return rsMap;
	}
	
	public void  consumePhyNew(String data) throws Exception {
		LOGGER.info("*********kafkaTOPIC_CLOUD_PHYbegin*********");
				try {
					List<AlertCloudPhyVo> phyList = JSON.parseArray(data, AlertCloudPhyVo.class);
					Map<String, Map<String, Object>> map = Maps.newHashMap();
					String ids = "";
					for (AlertCloudPhyVo vm : phyList) {
						String resId = vm.getRes_id();
						ids += resId + ",";
					}
					Map<String, Object> params = Maps.newHashMap();
					params.put("suyan_uuid", ids.substring(0, ids.length() - 1));
					params.put("token", token);
					params.put("condicationCode", condicationCode);
					params.put("pageSize", num);
					params.put("currentPage", 1);
					Date from1 = new Date();
					Result<Map<String, Object>> rs = cmdbCommonRestfulClient.getInstanceList(params);
					Date to1 = new Date();
					LOGGER.info("cmdb查询苏研PHY数据时间:{}ms", (to1.getTime() - from1.getTime()));
					int dsSize = 0;
					if (null != rs) {
						dsSize = rs.getTotalSize();
						List<Map<String, Object>> dsList = rs.getData();
						if (null != dsList) {
							for (Map<String, Object> val : dsList) {
								String resid = val.get("suyan_uuid").toString();
								map.put(resid, val);
							}
						}
					}
					LOGGER.info("缺失PHY苏研数据：{}条", phyList.size()-dsSize);
					List<AlertEsHistoryReq> esList = Lists.newArrayList();
					int delCount = 0;
					for (AlertCloudPhyVo vm : phyList) {
						String resId = vm.getRes_id();
						AlertEsHistoryReq es = new AlertEsHistoryReq();
						if (map.containsKey(resId)) {
							Map<String, Object> instance = map.get(resId);
							
							String idcType = instance.get("idcType") == null ? "" : instance.get("idcType").toString();
							String pod_name = instance.get("pod_name") == null ? "" : instance.get("pod_name").toString();
							if(idcType.equals(delIdcType) && pod_name.equals(delPod)) {
								delCount++;
								continue;
							}
							es.setHost(instance.get("ip") == null ? "" : instance.get("ip").toString());
							es.setBizSystem(instance.get("bizSystem") == null ? "" : instance.get("bizSystem").toString());
							es.setDepartment1(instance.get("department1") == null ? "" : instance.get("department1").toString());
							es.setDepartment2(instance.get("department2") == null ? "" : instance.get("department2").toString());
							es.setDeviceClass(instance.get("device_class") == null ? "": instance.get("device_class").toString());
							es.setDeviceType(instance.get("device_type") == null ? "" : instance.get("device_type").toString());
							es.setIdcType(idcType);
							es.setRoomId(instance.get("roomId") == null ? "" : instance.get("roomId").toString());
							es.setPodName(pod_name);
						} else {
							LOGGER.error("苏研PHY查无该设备信息;苏研resid:{}", resId);
						}
						es.setItemListStr(JSON.toJSONString(vm));
						if (StringUtils.isBlank(es.getDeviceClass())) {
							es.setDeviceClass("服务器");
						}
						if (StringUtils.isBlank(es.getDeviceType())) {
							es.setDeviceType("X86服务器");
						}
						esList.add(es);
					}
					LOGGER.info("es插入苏研PHY数据-抛弃数量:{}", delCount);
					Date from = new Date();
					esCloudSysClient.insertCloudPhyList(esList);
					Date to = new Date();
					LOGGER.info("es插入苏研PHY数据时间:{}ms", (to.getTime() - from.getTime()));
					
					map.clear();
				} catch (ParseException | IllegalAccessException e) {
					LOGGER.error("解析苏研PHY数据错误:{}", data);
					throw new Exception("解析苏研PHY数据错误:{}",e) ;
				} catch (Exception e) {
					LOGGER.error("解析苏研PHY数据错误:{}", data);
					throw new Exception("解析苏研PHY数据错误:{}",e) ;
				}

		LOGGER.info("*********kafkaTOPIC_CLOUD_PHY--end*********");
	}
	public void  consumePhy(String data) throws Exception {
		LOGGER.info("*********kafkaTOPIC_CLOUD_PHYbegin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					List<AlertCloudPhyVo> phyList = JSON.parseArray(data, AlertCloudPhyVo.class);
					Map<String, Map<String, Object>> map = Maps.newHashMap();
					String ids = "";
					for (AlertCloudPhyVo vm : phyList) {
						String resId = vm.getRes_id();
						ids += resId + ",";
					}
					Map<String, Object> params = Maps.newHashMap();
					params.put("suyan_uuid", ids.substring(0, ids.length() - 1));
					params.put("token", token);
					params.put("condicationCode", condicationCode);
					params.put("pageSize", num);
					params.put("currentPage", 1);
					Date from1 = new Date();
					Result<Map<String, Object>> rs = cmdbCommonRestfulClient.getInstanceList(params);
					Date to1 = new Date();
					LOGGER.info("cmdb查询苏研PHY数据时间:{}ms", (to1.getTime() - from1.getTime()));
					int dsSize = 0;
					if (null != rs) {
						dsSize = rs.getTotalSize();
						List<Map<String, Object>> dsList = rs.getData();
						if (null != dsList) {
							for (Map<String, Object> val : dsList) {
								String resid = val.get("suyan_uuid").toString();
								map.put(resid, val);
							}
						}
					}
					LOGGER.info("缺失PHY苏研数据：{}条", phyList.size()-dsSize);
					List<AlertEsHistoryReq> esList = Lists.newArrayList();
					int delCount = 0;
					for (AlertCloudPhyVo vm : phyList) {
						String resId = vm.getRes_id();
						AlertEsHistoryReq es = new AlertEsHistoryReq();
						if (map.containsKey(resId)) {
							Map<String, Object> instance = map.get(resId);
							
							String idcType = instance.get("idcType") == null ? "" : instance.get("idcType").toString();
							String pod_name = instance.get("pod_name") == null ? "" : instance.get("pod_name").toString();
							if(idcType.equals(delIdcType) && pod_name.equals(delPod)) {
								delCount++;
								continue;
							}
							es.setHost(instance.get("ip") == null ? "" : instance.get("ip").toString());
							es.setBizSystem(instance.get("bizSystem") == null ? "" : instance.get("bizSystem").toString());
							es.setDepartment1(instance.get("department1") == null ? "" : instance.get("department1").toString());
							es.setDepartment2(instance.get("department2") == null ? "" : instance.get("department2").toString());
							es.setDeviceClass(instance.get("device_class") == null ? "": instance.get("device_class").toString());
							es.setDeviceType(instance.get("device_type") == null ? "" : instance.get("device_type").toString());
							es.setIdcType(idcType);
							es.setRoomId(instance.get("roomId") == null ? "" : instance.get("roomId").toString());
							es.setPodName(pod_name);
						} else {
							LOGGER.error("苏研PHY查无该设备信息;苏研resid:{}", resId);
						}
						es.setItemListStr(JSON.toJSONString(vm));
						if (StringUtils.isBlank(es.getDeviceClass())) {
							es.setDeviceClass("服务器");
						}
						if (StringUtils.isBlank(es.getDeviceType())) {
							es.setDeviceType("X86服务器");
						}
						esList.add(es);
					}
					LOGGER.info("es插入苏研PHY数据-抛弃数量:{}", delCount);
					Date from = new Date();
					esCloudSysClient.insertCloudPhyList(esList);
					Date to = new Date();
					LOGGER.info("es插入苏研PHY数据时间:{}ms", (to.getTime() - from.getTime()));
					
					map.clear();
				} catch (ParseException | IllegalAccessException e) {
					LOGGER.error("解析苏研PHY数据错误:{}", data);
					e.printStackTrace();
				} catch (Exception e) {
					LOGGER.error("解析苏研PHY数据错误:{}", data);
					e.printStackTrace();
				}
			}
		};
		EXECUTOR.execute(runnable);

		LOGGER.info("*********kafkaTOPIC_CLOUD_PHY--end*********");
	}

	private void formEsData(AlertEsHistoryReq es, String resId, boolean phyFlag) throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		params.put("suyan_uuid", resId);
		params.put("token", token);
		Date from = new Date();

		// cmdbCommonRestfulClient.getInstanceList(params);
		Map<String, Object> instance = cmdbCommonRestfulClient.getInstanceDetail(params);
		Date to = new Date();
		LOGGER.info("cmdb请求suyan设备时间:{}ms", (to.getTime() - from.getTime()));
		if (null == instance || instance.size() == 0) {
			if (phyFlag) {
				LOGGER.error("苏研PHY数据查无该设备信息;设备查询条件:{}", params);
			} else {
				LOGGER.error("苏研VM查无该设备信息;设备查询条件:{}", params);
			}

			// es.setSuyanResid(resId);
			// LOGGER.error("全网监控中心系统-云资源数据,查无该设备信息");
			// throw new Exception("全网监控中心系统-云资源数据,查无该设备信息");
		} else {
			es.setHost(instance.get("ip") == null ? "" : instance.get("ip").toString());
			es.setBizSystem(instance.get("bizSystem") == null ? "" : instance.get("bizSystem").toString());
			es.setDepartment1(instance.get("department1") == null ? "" : instance.get("department1").toString());
			es.setDepartment2(instance.get("department2") == null ? "" : instance.get("department2").toString());
			es.setDeviceClass(instance.get("device_class") == null ? "" : instance.get("device_class").toString());
			es.setDeviceType(instance.get("device_type") == null ? "" : instance.get("device_type").toString());
			es.setIdcType(instance.get("idcType") == null ? "" : instance.get("idcType").toString());
			es.setRoomId(instance.get("roomId") == null ? "" : instance.get("roomId").toString());
			es.setPodName(instance.get("pod_name") == null ? "" : instance.get("pod_name").toString());
		}

	}
	
	
	

	public void consumeVm(String data) throws Exception {
		LOGGER.info("*********kafkaTOPIC_CLOUD_VMbegin*********");
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					List<AlertCloudVmVo> phyList = JSON.parseArray(data, AlertCloudVmVo.class);
					Map<String, Map<String, Object>> map = Maps.newHashMap();
					String ids = "";
					for (AlertCloudVmVo vm : phyList) {
						String resId = vm.getRes_id();
						ids += resId + ",";
					}
					Map<String, Object> params = Maps.newHashMap();
					params.put("suyan_uuid", ids.substring(0, ids.length() - 1));
					params.put("token", token);
					params.put("condicationCode", condicationCode);
					params.put("pageSize", num);
					params.put("currentPage", 1);
					Date from1 = new Date();
					Result<Map<String, Object>> rs = cmdbCommonRestfulClient.getInstanceList(params);
					Date to1 = new Date();
					LOGGER.info("cmdb查询苏研VM数据时间:{}ms", (to1.getTime() - from1.getTime()));
					int dsSize = 0;
					if (null != rs) {
						List<Map<String, Object>> dsList = rs.getData();
						dsSize = rs.getTotalSize();
						LOGGER.info("cmdb查询苏研VM数据dsSize:{},totalSize:{}", dsList.size(),dsSize);
						if (null != dsList) {

							for (Map<String, Object> val : dsList) {
								String resid = val.get("suyan_uuid").toString();
								
								map.put(resid, val);
							}
						}
					}
					LOGGER.info("缺失VM苏研数据：{}条", phyList.size()-dsSize);
					List<AlertEsHistoryReq> esList = Lists.newArrayList();
					int delCount = 0;
					for (AlertCloudVmVo vm : phyList) {
						String resId = vm.getRes_id();
						AlertEsHistoryReq es = new AlertEsHistoryReq();
						if (map.containsKey(resId)) {
							Map<String, Object> instance = map.get(resId);
							
							String idcType = instance.get("idcType") == null ? "" : instance.get("idcType").toString();
							String pod_name = instance.get("pod_name") == null ? "" : instance.get("pod_name").toString();
							if(idcType.equals(delIdcType) && pod_name.equals(delPod)) {
								delCount++;
								continue;
							}
							
							es.setHost(instance.get("ip") == null ? "" : instance.get("ip").toString());
							es.setBizSystem(instance.get("bizSystem") == null ? "" : instance.get("bizSystem").toString());
							es.setDepartment1(instance.get("department1") == null ? "" : instance.get("department1").toString());
							es.setDepartment2(instance.get("department2") == null ? "" : instance.get("department2").toString());
							es.setDeviceClass(instance.get("device_class") == null ? "": instance.get("device_class").toString());
							es.setDeviceType(instance.get("device_type") == null ? "" : instance.get("device_type").toString());
							es.setIdcType(idcType);
							es.setRoomId(instance.get("roomId") == null ? "" : instance.get("roomId").toString());
							es.setPodName(pod_name);
						} else {
							LOGGER.error("苏研VM查无该设备信息;苏研resid:{}", resId);
						}
						es.setItemListStr(JSON.toJSONString(vm));
						if (StringUtils.isBlank(es.getDeviceClass())) {
							es.setDeviceClass("服务器");
						}
						if (StringUtils.isBlank(es.getDeviceType())) {
							es.setDeviceType("云主机");
						}
						esList.add(es);
					}
					LOGGER.info("es插入苏研VM数据-抛弃数量:{}", delCount);
					Date from = new Date();
					esCloudSysClient.insertCloudVmList(esList);
					Date to = new Date();
					LOGGER.info("es插入苏研VM数据时间:{}ms", (to.getTime() - from.getTime()));
					map.clear();
				} catch (ParseException | IllegalAccessException e) {
					LOGGER.error("解析苏研数据错误:{}", data);
					e.printStackTrace();
				} catch (Exception e) {
					LOGGER.error("解析苏研数据错误:{}", data);
					e.printStackTrace();
				}
			}
		};
		EXECUTOR.execute(runnable);

		LOGGER.info("*********kafkaTOPIC_CLOUD_VMend*********");
	}
	
	
	public List<String> getExecDuration() {
		// log.info("查询设备利用率es脚本： {}", JSONUtils.valueToString(request));
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Calendar calendar = Calendar.getInstance();
		    	calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE))/10*10);
		    	calendar.set(Calendar.SECOND,0);
		    	Date late = calendar.getTime();
		    	calendar.add(Calendar.MINUTE, -10);
		    	Date early = calendar.getTime();
		    	List<String> list =  Arrays.asList(sdf.format(early),
		    			sdf.format(late));
		    	return list;

	}
}
