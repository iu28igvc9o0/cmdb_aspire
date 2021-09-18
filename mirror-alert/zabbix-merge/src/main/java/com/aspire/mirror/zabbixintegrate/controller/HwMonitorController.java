package com.aspire.mirror.zabbixintegrate.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.zabbixintegrate.bean.HwResponse;
import com.aspire.mirror.zabbixintegrate.bean.HwSyncLog;
import com.aspire.mirror.zabbixintegrate.biz.HwMonitorSyncService;
import com.aspire.mirror.zabbixintegrate.biz.alert.HwMonitorTask;
import com.aspire.mirror.zabbixintegrate.config.HWProperties;
import com.aspire.mirror.zabbixintegrate.daoCmdb.AlertRestfulDao;
import com.aspire.mirror.zabbixintegrate.util.DateUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class HwMonitorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(HwMonitorController.class);
	@Value("${HwMonitorTask.moniotrConfigFlag:false}")
	private boolean moniotrConfigFlag;
	@Value("${HwMonitorTask.deviceFlag:false}")
	private boolean deviceFlag;
	@Value("${HwMonitorTask.monitorDataFlag:false}")
	private boolean monitorDataFlag;
	@Value("${HwMonitorTask.reMonitorDataFlag:false}")
	private volatile boolean reMonitorDataFlag;
	
	@Autowired
	private HWProperties hWProperties;

	
	//private int tag_type = hWProperties.getTag_type();

	

	@Autowired
	private AlertRestfulDao alertRestfulDao;

	@Autowired
	private HwMonitorSyncService hwMonitorSyncService;

	@GetMapping(value = "/v1/alert/scanMoniotrConfig")
	public void scanMoniotrConfig() throws Exception {
		log.info("**scanMoniotrConfig--begin**************");
		if (moniotrConfigFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "scanMoniotrConfig");// 设置日志信息
				hwMonitorSyncService.syncMonitorConfigData();
			} catch (Exception e) {
				log.error("scanMoniotrConfig报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
				//throw e;
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanMoniotrConfig--end**************");
	}

	@GetMapping(value = "/v1/alert/scanDeviceDatas")
	public void scanDeviceDatas() throws Exception {
		log.info("**scanDeviceDatas--begin**************");
		if (deviceFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				formLog(syncLog, null, "scanDeviceDatas");// 设置日志信息
				hwMonitorSyncService.syncDeviceData();
			} catch (Exception e) {
				log.error("scanDeviceDatas报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanDeviceDatas--end**************");
	}

	

	private void formLog(HwSyncLog syncLog, String url, String config_type) {
		syncLog.setUrl(url);
		syncLog.setConfigType(config_type);
		syncLog.setIdcTypeTag(hWProperties.getIdcType()+"_"+hWProperties.getTag_type());
		syncLog.setExecTime(new Date());
	}

	@GetMapping(value = "/v1/alert/reScanMonitorDatas")
	public void reScanMonitorDatas(boolean flag,String startTime,String  endTime) throws Exception {
		log.info("**reScanMonitorDatas--begin**************");
		if (reMonitorDataFlag) {
			
			HwMonitorTask.setReScanMonitorTime(flag, startTime, endTime);
		}

		log.info("******reScanMonitorDatas--end**************");
	}
	
	@GetMapping(value = "/v1/alert/ScanMonitorDatas")
	public void ScanMonitorDatas() throws Exception {
		log.info("**scanDeviceDatas--begin**************");
		if (monitorDataFlag) {
			HwSyncLog syncLog = new HwSyncLog();// 记录日志
			try {
				List<Date> dateList = DateUtil.getExecDuration(10, hWProperties.getDelayMinute(), hWProperties.getIntervalMinute());
				Date endTime = dateList.get(1);
				Date startTime = dateList.get(0);
				formLog(syncLog, null, "ScanMonitorDatas");// 设置日志信息
				hwMonitorSyncService.syncMonitorDatas(startTime, endTime);
			} catch (Exception e) {
				log.error("scanDeviceDatas报错",e);
				syncLog.setStatusFail();
				String message = e.getMessage()==null?"":e.getMessage().toString();
				syncLog.setContent(e.getClass().getName() + ":" + message);
			}finally {
				syncLog.setCreateTime(new Date());
				alertRestfulDao.insertHwSyncLog(syncLog);
			}
		}

		log.info("******scanDeviceDatas--end**************");
	}
	
	
	
	@PutMapping(value = "/rest/plat/smapp/v1/oauth/token")
	public String testToken(@RequestBody JSONObject ob) throws Exception {
		JSONObject obToken = new JSONObject();
		obToken.put("accessSession", "ddddddd");
		
		return obToken.toJSONString();
	}
	
	
	@GetMapping(value = "rest/performance/v1/mgr-svc/obj-types")
	public HwResponse testMonitorType() throws Exception {
		String ss = "[ \r\n" + 
				"    {\r\n" + 
				"            \"obj_type_id\": 562958543355904,\r\n" + 
				"            \"parent_obj_type_id\": 1407379178520576,\r\n" + 
				"            \"resource_category\": \"CLOUD_VM\",\r\n" + 
				"            \"resource_provider\": \"ict-resource\",\r\n" + 
				"            \"en_us\": \"Elastic Cloud Server\",\r\n" + 
				"            \"zh_cn\": \"弹性云服务器\",\r\n" + 
				"            \"group_en_us\": \"Cloud Resource\",\r\n" + 
				"            \"group_zh_cn\": \"云资源\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"obj_type_id\": 562967133290496,\r\n" + 
				"            \"parent_obj_type_id\": 0,\r\n" + 
				"            \"resource_category\": \"CLOUD_VOLUME\",\r\n" + 
				"            \"resource_provider\": \"ict-resource\",\r\n" + 
				"            \"en_us\": \"Elastic Volume Server\",\r\n" + 
				"            \"zh_cn\": \"云硬盘\",\r\n" + 
				"            \"group_en_us\": \"Cloud Resource\",\r\n" + 
				"            \"group_zh_cn\": \"云资源\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"obj_type_id\": 562971428257792,\r\n" + 
				"            \"parent_obj_type_id\": 0,\r\n" + 
				"            \"resource_category\": \"CLOUD_FLOATING_IPS\",\r\n" + 
				"            \"resource_provider\": \"ict-resource\",\r\n" + 
				"            \"en_us\": \"Floating IP\",\r\n" + 
				"            \"zh_cn\": \"弹性IP\",\r\n" + 
				"            \"group_en_us\": \"Cloud Resource\",\r\n" + 
				"            \"group_zh_cn\": \"云资源\"\r\n" + 
				"        } \r\n" + 
				"  ]";
		JSONArray sss = JSONObject.parseArray(ss);
		HwResponse  s = new HwResponse();
		s.setStatus_code(200);
		s.setData(sss);
		return s;
	}
	
	
	@GetMapping(value = "/rest/performance/v1/mgr-svc/obj-types/{obj-type-id}/indicators")
	public HwResponse indicatorRel() throws Exception {
		String ss = "{\r\n" + 
				"    \"status_code\": 200,\r\n" + 
				"    \"error_code\": 0,\r\n" + 
				"    \"error_msg\": \"Successful\",\r\n" + 
				"    \"data\": {\r\n" + 
				"        \"indicator_ids\": [\r\n" + 
				"            562958543421441,\r\n" + 
				"        	   562958543486979\r\n" + 
				" ]\r\n" + 
				"    }\r\n" + 
				"} ";
		//JSONObject aa = JSONObject.parseObject(ss);
		//JSONArray sss = JSONObject.parseArray(aa.getString("indicator_ids"));
		//List<Integer> sss = aa.getObject("indicator_ids", List.class);
		HwResponse  s = JSONObject.parseObject(ss, HwResponse.class);
		//s.setStatus_code(200);
		//s.setData(sss);
		return s;
	}
	
	@PostMapping(value = "/rest/performance/v1/mgr-svc/indicators")
	public HwResponse indicator(@RequestBody List<Long> ids) throws Exception {
		String ss = "{\r\n" + 
				"        \"562958543486979\": {\r\n" + 
				"            \"kpi\": 1,\r\n" + 
				"            \"data_type\": \"float\",\r\n" + 
				"            \"data_unit\": \"%\",\r\n" + 
				"            \"en_us\": \"Memory Usage\",\r\n" + 
				"            \"zh_cn\": \"内存使用率\",\r\n" + 
				"            \"group_en_us\": \"OS Monitoring\",\r\n" + 
				"            \"group_zh_cn\": \"操作系统监控\",\r\n" + 
				"            \"tag\": \"CLOUD_VM_USAGE\",\r\n" + 
				"            \"alarm_id\": null,\r\n" + 
				"            \"alarm_desc_en_us\": null,\r\n" + 
				"            \"alarm_desc_zh_cn\": null,\r\n" + 
				"            \"indicator_name\": \"memoryUsage\"\r\n" + 
				"        },\r\n" + 
				"        \"562958543421441\": {\r\n" + 
				"            \"kpi\": 1,\r\n" + 
				"            \"data_type\": \"float\",\r\n" + 
				"            \"data_unit\": \"%\",\r\n" + 
				"            \"en_us\": \"CPU Usage\",\r\n" + 
				"            \"zh_cn\": \"CPU使用率\",\r\n" + 
				"            \"group_en_us\": \"Basic Monitoring\",\r\n" + 
				"            \"group_zh_cn\": \"基础监控\",\r\n" + 
				"            \"tag\": \"CLOUD_VM_USAGE\",\r\n" + 
				"            \"alarm_id\": null,\r\n" + 
				"            \"alarm_desc_en_us\": null,\r\n" + 
				"            \"alarm_desc_zh_cn\": null,\r\n" + 
				"            \"indicator_name\": \"cpuUsage\"\r\n" + 
				"        }\r\n" + 
				"    }";
		JSONObject sss = JSONObject.parseObject(ss);
		HwResponse  s = new HwResponse();
		s.setStatus_code(200);
		s.setData(sss);
		return s;
	}
	
	
	
	@GetMapping(value = "/rest/tenant-resource/v1/instances/{class-name}")
	public String getDevices(int pageNo,int pageSize,String condition,String contentSelector) throws Exception {
		String ss = "{\r\n" + 
				"    \"objList\": [\r\n" + 
				"        {\r\n" + 
				"            \"extraSpecs\": \"{\\\"reqUri\\\":\\\"/service/sites/3FAF07CB/vms/i-000007B7\\\",\\\"cpuReservation\\\":0,\\\"memorySize\\\":28672,\\\"vCPU\\\":16,\\\"memoryWeight\\\":286720,\\\"cpuLimit\\\":0,\\\"osVersiontype\\\":\\\"Redhat Linux Enterprise 6.6 64bit\\\",\\\"cpuWeight\\\":16000,\\\"memoryLimit\\\":28672,\\\"memoryReservation\\\":0}\",\r\n" + 
				"            \"createdAt\": \"2015-03-31 05:02:46\",\r\n" + 
				"            \"privateIps\": \"@192.168.216.186@0.0.0.0@\",\r\n" + 
				"            \"name\": \"mail10_vmdmz_30\",\r\n" + 
				"            \"hostId\": \"F0FAAC8BA728341BBC4EC478D1F2DD95\",\r\n" + 
				"            \"id\": \"42B7D94F74193982B339D5BAB29539D1\",\r\n" + 
				"            \"nativeId\": \"urn:sites:3FAF07CB:vms:i-000007B7\",\r\n" + 
				"            \"bizRegionId\": \"EDC784392480303A8C1FD1A188E377D0\",\r\n" + 
				"            \"status\": \"active\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"extraSpecs\": \"{\\\"reqUri\\\":\\\"/service/sites/3FAF07CB/vms/i-000007AF\\\",\\\"cpuReservation\\\":0,\\\"memorySize\\\":16384,\\\"vCPU\\\":8,\\\"memoryWeight\\\":163840,\\\"cpuLimit\\\":0,\\\"osVersiontype\\\":\\\"Redhat Linux Enterprise 6.6 64bit\\\",\\\"cpuWeight\\\":8000,\\\"memoryLimit\\\":16384,\\\"memoryReservation\\\":0}\",\r\n" + 
				"            \"createdAt\": \"2015-03-31 03:24:03\",\r\n" + 
				"            \"privateIps\": \"@192.168.216.179@0.0.0.0@\",\r\n" + 
				"            \"name\": \"mail10_vmdmz_23\",\r\n" + 
				"            \"hostId\": \"F0FAAC8BA728341BBC4EC478D1F2DD95\",\r\n" + 
				"            \"id\": \"13AC69196566369A9DE6F48D69DB5C28\",\r\n" + 
				"            \"nativeId\": \"urn:sites:3FAF07CB:vms:i-000007AF\",\r\n" + 
				"            \"bizRegionId\": \"EDC784392480303A8C1FD1A188E377D0\",\r\n" + 
				"            \"status\": \"active\"\r\n" + 
				"        }\r\n" + 
				"],\r\n" + 
				"    \"totalNum\": 2,\r\n" + 
				"    \"pageSize\": 20,\r\n" + 
				"    \"totalPageNo\": 1,\r\n" + 
				"    \"currentPage\": 1\r\n" + 
				"}";
		return ss;
	}

	@GetMapping(value = "/rest/cmdb/v1/instances/{className}")
	public String getRegion(int pageNo,int pageSize,String contentSelector,String condition) throws Exception {
		String ss = "{\r\n" + 
				"    \"objList\": [\r\n" + 
				"        {\r\n" + 
				"            \"locales\": \"{\\\"en_us\\\":\\\"JiLin-POD-01\\\",\\\"zh_cn\\\":\\\"吉林移动-POD-01\\\"}\",\r\n" + 
				"            \"class_Name\": \"SYS_BusinessRegion\",\r\n" + 
				"            \"id\": \"0B208D022AAE324193E246813F5CEA61\",\r\n" + 
				"            \"nativeId\": \"jl-fc-1\",\r\n" + 
				"            \"resId\": \"0B208D022AAE324193E246813F5CEA61\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"locales\": \"{\\\"en_us\\\":\\\"JiLin-POD-02\\\",\\\"zh_cn\\\":\\\"吉林移动-POD-02\\\"}\",\r\n" + 
				"            \"class_Name\": \"SYS_BusinessRegion\",\r\n" + 
				"            \"id\": \"6CCFD74F87333EF2B93789DC1885BC81\",\r\n" + 
				"            \"nativeId\": \"jl-fc-2\",\r\n" + 
				"            \"resId\": \"6CCFD74F87333EF2B93789DC1885BC81\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"locales\": \"{\\\"en_us\\\":\\\"nanji-POD-01\\\",\\\"zh_cn\\\":\\\"南方基地-POD-01\\\"}\",\r\n" + 
				"            \"class_Name\": \"SYS_BusinessRegion\",\r\n" + 
				"            \"id\": \"EDC784392480303A8C1FD1A188E377D0\",\r\n" + 
				"            \"nativeId\": \"nj-fc-1\",\r\n" + 
				"            \"resId\": \"EDC784392480303A8C1FD1A188E377D0\"\r\n" + 
				"        },\r\n" + 
				"        {\r\n" + 
				"            \"locales\": \"{\\\"en_us\\\":\\\"pod3\\\",\\\"zh_cn\\\":\\\"呼和浩特资源池-POD3\\\"}\",\r\n" + 
				"            \"class_Name\": \"SYS_BusinessRegion\",\r\n" + 
				"            \"id\": \"667699C8CE263E9A807DE2FF72DF0088\",\r\n" + 
				"            \"nativeId\": \"cn-hhht-1\",\r\n" + 
				"            \"resId\": \"667699C8CE263E9A807DE2FF72DF0088\"\r\n" + 
				"        }\r\n" + 
				"    ],\r\n" + 
				"    \"totalNum\": 4,\r\n" + 
				"    \"pageSize\": 100,\r\n" + 
				"    \"totalPageNo\": 1,\r\n" + 
				"    \"currentPage\": 1\r\n" + 
				"} ";
		return ss;
	}
	
	@PostMapping(value = "/rest/performance/v1/data-svc/history-data/action/query")
	public HwResponse getMonitorData(@RequestBody JSONObject ob) throws Exception {
		String ss = " {\r\n" + 
				"        \"42B7D94F74193982B339D5BAB29539D1\": {\r\n" + 
				"            \"562958543486979\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"63.880001068115234\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"63.880001068115234\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"63.880001068115234\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"63.880001068115234\"\r\n" + 
				"                }\r\n" + 
				"            },\r\n" + 
				"            \"562958543421441\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"3.7799999713897705\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"3.7799999713897705\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"3.7799999713897705\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"3.7799999713897705\"\r\n" + 
				"                }\r\n" + 
				"            }\r\n" + 
				"        },\r\n" + 
				"        \"B0B6AA837DAE305EBD9AC2B6CD3DDBEA\": {\r\n" + 
				"            \"562958543486979\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"1.7899999618530273\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"1.7899999618530273\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"1.7899999618530273\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"1.7899999618530273\"\r\n" + 
				"                }\r\n" + 
				"            },\r\n" + 
				"            \"562958543421441\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"0.0\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"0.0\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"0.0\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"0.0\"\r\n" + 
				"                }\r\n" + 
				"            }\r\n" + 
				"        },\r\n" + 
				"        \"842F4E002AB630F4BD9E761C9DFE35BA\": {\r\n" + 
				"            \"562958543486979\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"9.640000343322754\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"9.640000343322754\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"9.640000343322754\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"9.640000343322754\"\r\n" + 
				"                }\r\n" + 
				"            },\r\n" + 
				"            \"562958543421441\": {\r\n" + 
				"                \"series\": [\r\n" + 
				"                    {\r\n" + 
				"                        \"1606102200000\": \"0.9599999785423279\"\r\n" + 
				"                    }\r\n" + 
				"                ],\r\n" + 
				"                \"min\": {\r\n" + 
				"                    \"1606102200000\": \"0.9599999785423279\"\r\n" + 
				"                },\r\n" + 
				"                \"max\": {\r\n" + 
				"                    \"1606102200000\": \"0.9599999785423279\"\r\n" + 
				"                },\r\n" + 
				"                \"avg\": {\r\n" + 
				"                    \"1606102500000\": \"0.9599999785423279\"\r\n" + 
				"                }\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    }";
		JSONObject sss = JSONObject.parseObject(ss);
		HwResponse  s = new HwResponse();
		s.setStatus_code(200);
		s.setData(sss);
		return s;
	}
}
