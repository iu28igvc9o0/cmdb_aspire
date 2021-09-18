package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.elasticsearch.api.dto.AlertEsHistoryReq;
import com.aspire.mirror.elasticsearch.api.dto.CloudPhy;
import com.aspire.mirror.elasticsearch.api.dto.CloudVm;
import com.aspire.mirror.elasticsearch.api.dto.EsHistoryReq;
import com.aspire.mirror.elasticsearch.api.dto.EsHistoryUintReq;
import com.aspire.mirror.elasticsearch.api.service.zabbix.ICloudSysService;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author longfeng
 * @title: ItemController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class CloudSuYanController implements ICloudSysService {
	private static final String INDEX_HISTORY_PRE = "history";
	private static final String INDEX_HISTORY_UINT_PRE = "history_uint";

	@Value("${INDEX_TYPE:doc}")
	private String INDEX_TYPE;
	@Autowired
	private TransportClient transportClient;

	@Override
	public void insertCloudVm(@RequestBody AlertEsHistoryReq vmReq) throws ParseException, IllegalAccessException {
		log.info("***CloudSuYanControllerInsertCloudVm--begin");
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		CloudVm vm = JSON.parseObject(vmReq.getItemListStr(), CloudVm.class);
		vmReq.setSuyanResid(vm.getRes_id());

		String time = vm.getCol_time();
		Date date = DateUtils.parseDate(time, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// Date startTime = vmReq.getDatetime();
		DateFormat returnd = new SimpleDateFormat("yyyyMMdd");
		vmReq.setDatetime(date);
		vmReq.setClock(date.getTime() / 1000);

		String indexLast = returnd.format(date);
		String index = "";
		Float cpu = vm.getCpu_avg_util_percent();
		if (null != cpu) {

			index = this.INDEX_HISTORY_PRE + "-" + indexLast;
			String item = "cpu.usage_average";
			getHis(item, vmReq, cpu, index, bulkRequest);
		}

		Float memPercent = vm.getMem_avg_util_percent();
		if (null != memPercent) {
			// String index = this.INDEX_HISTORY_PRE+"-"+indexLast;
			String item = "mem.usage_average";
			getHis(item, vmReq, memPercent, index, bulkRequest);

		}

		Float diskPercent = vm.getDisk_all_percent();
		if (null != diskPercent) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "diskpused";
			getHis(item, vmReq, diskPercent, index, bulkRequest);
		}

		Float memTotal = vm.getMem_total_size();
		if (null != memTotal) {
			index = this.INDEX_HISTORY_UINT_PRE + "-" + indexLast;
			String item = "vm.memory.size[total]";
			long val = (long) (memTotal * 1024 * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float memUsed = vm.getMem_used_size();
		if (null != memUsed) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "custom.memory.used";
			long val = (long) (memUsed * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float netIn = vm.getNetwork_incoming_bytes_rate();
		if (null != netIn) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.in";
			long val = (long) (netIn * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);

		}

		Float netOut = vm.getNetwork_outgoing_bytes_rate();
		if (null != netOut) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.out";
			long val = (long) (netOut * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskIn = vm.getDisk_read_bytes_rate();
		if (null != diskIn) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.read[,bps]";
			long val = (long) (diskIn * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskOut = vm.getDisk_write_bytes_rate();
		if (null != diskOut) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.write[,bps]";
			long val = (long) (diskOut * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskInIops = vm.getDisk_read_requests_rate();
		if (null != diskInIops) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.read[,ops]";
			long val = (long) (diskInIops * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskOutIops = vm.getDisk_write_requests_rate();
		if (null != diskOutIops) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.write[,ops]";
			long val = (long) (diskOutIops * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskTotal = vm.getDisk_all_total();
		if (null != diskTotal) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "disktotal";
			long val = (long) (diskTotal * 1024 * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskUsed = vm.getDisk_all_used();
		if (null != diskUsed) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "diskused";
			long val = (long) (diskUsed * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		if (bulkRequest.numberOfActions() > 0) {
			// log.info("insert bulkRequest:{}",bulkRequest);
			
			BulkResponse   bulkResponse  = bulkRequest.execute().actionGet();
			if(bulkResponse.hasFailures()) {
				log.info("CloudSuYanControllerInsertCloudVm--failMsg:{}",bulkResponse.buildFailureMessage());
			}
			
		}
		log.info("***CloudSuYanControllerInsertCloudVmEnd");
	}

	private void getHis(String item, AlertEsHistoryReq vmReq, float value, String index, BulkRequestBuilder bulkRequest)
			throws IllegalAccessException {
		EsHistoryReq his = new EsHistoryReq();
		BeanUtils.copyProperties(vmReq, his);
		his.setItem(item);
		his.setValue(value);
		// com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSON(javaObject));
		Map<String, Object> source = objectToMap(his);
		source.put("@timestamp", vmReq.getCurDate());
		source.put("type", "history");
		source.put("suyanResid", vmReq.getSuyanResid());
		// source.remove("datetime");
		source.remove("serialVersionUID");
		source.remove("timestamp");
		bulkRequest.add(transportClient.prepareIndex(index, this.INDEX_TYPE).setSource(source));
	}

	private void getHisUint(String item, AlertEsHistoryReq vmReq, long value, String index,
			BulkRequestBuilder bulkRequest) throws IllegalAccessException {
		EsHistoryUintReq his = new EsHistoryUintReq();
		BeanUtils.copyProperties(vmReq, his);
		his.setItem(item);
		his.setValue(value);
		Map<String, Object> source = objectToMap(his);
		source.put("@timestamp", vmReq.getCurDate());
		source.put("type", "history_uint");
		source.put("suyanResid", vmReq.getSuyanResid());
		// source.remove("datetime");
		source.remove("timestamp");
		source.remove("serialVersionUID");
		bulkRequest.add(transportClient.prepareIndex(index, this.INDEX_TYPE).setSource(source));
		/*
		 * (JSONObject.fromObject(his) .toString(), XContentType.JSON));
		 */
	}

	public static Map<String, Object> objectToMap(Object obj)  {
		/*
		 * Map<String, Object> map = new HashMap<>(); Class<?> clazz = obj.getClass();
		 * //System.out.println(clazz); for (Field field : clazz.getDeclaredFields()) {
		 * field.setAccessible(true); String fieldName = field.getName(); Object value =
		 * field.get(obj); map.put(fieldName, value); }
		 */
		try {
			return InstantUtils.getObjectMap(obj);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IntrospectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		//return map;
	}

	@Override
	public void insertCloudPhy(@RequestBody AlertEsHistoryReq phyReq) throws ParseException, IllegalAccessException {
		log.info("CloudSuYanControlleInsertCloudPhy--begin");
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();

		CloudPhy phy = JSON.parseObject(phyReq.getItemListStr(), CloudPhy.class);
		phyReq.setSuyanResid(phy.getRes_id());

		String time = phy.getCol_time();
		Date date = DateUtils.parseDate(time, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// Date startTime = vmReq.getDatetime();
		DateFormat returnd = new SimpleDateFormat("yyyyMMdd");
		phyReq.setDatetime(date);
		phyReq.setClock(date.getTime() / 1000);
		String indexLast = returnd.format(date);
		String index = "";
		Float cpu = phy.getCpu_avg_util_percent_percent_avg();
		// phyReq.setDeviceClass("");
		if (null != cpu) {
			index = this.INDEX_HISTORY_PRE + "-" + indexLast;
			String item = "custom.cpu.avg5.pused";
			getHis(item, phyReq, cpu, index, bulkRequest);
		}

		Float memPercent = phy.getMem_percent_usedwobufferscaches();
		if (null != memPercent) {
			// String index = this.INDEX_HISTORY_PRE+"-"+indexLast;
			String item = "custom.memory.pused";
			getHis(item, phyReq, memPercent, index, bulkRequest);
		}
		Float memTotal = phy.getMem_mem_total();
		if (null != memTotal) {
			index = this.INDEX_HISTORY_UINT_PRE + "-" + indexLast;
			String item = "vm.memory.size[total]";
			long val = (long) (memTotal * 1024 * 1024 * 1024);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float memUsed = phy.getMem_mem_used();
		if (null != memUsed) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vm.memory.size[total]";
			long val = (long) (memUsed * 1024 * 1024);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float netIn = phy.getInterface_rxrate();
		if (null != netIn) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.in";
			long val = (long) (netIn * 1000);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float netOut = phy.getInterface_txrate();
		if (null != netOut) {
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.out";
			long val = (long) (netOut * 1000);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}
		if (bulkRequest.numberOfActions() > 0) {
			bulkRequest.execute().actionGet();
		}
		log.info("CloudSuYanControlleInsertCloudPhyEnd");
	}

	@Override
	public int insertCloudPhyList(@RequestBody List<AlertEsHistoryReq> phyList) throws ParseException, IllegalAccessException {
		log.info("***CloudSuYanControlle--insertCloudPhyList--begin");
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		for (AlertEsHistoryReq phyReq : phyList) {
			packagePhy(phyReq,bulkRequest);

		}
		int count = bulkRequest.numberOfActions();
		log.info("CloudSuYanControlle--insertCloudPhyList--count:{},all:{}",count,phyList.size());
		if (count > 0) {
			
			BulkResponse   bulkResponse  = bulkRequest.execute().actionGet();
			if(bulkResponse.hasFailures()) {
				log.info("CloudSuYanControlle--insertCloudPhyList--failMsg:{}",bulkResponse.buildFailureMessage());
				//log.info("CloudSuYanControlle-insertCloudPhyList--failMsgDetail:{}",phyList);
			}
			
		}
		log.info("CloudSuYanControlle--insertCloudPhyListEnd");
		return count;
	}

	@Override
	public int insertCloudVmList(@RequestBody List<AlertEsHistoryReq> vmList) throws ParseException, IllegalAccessException {

		log.info("CloudSuYanController--InsertCloudVmList--begin}");
		BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
		for (AlertEsHistoryReq vmReq : vmList) {
			this.packageVm(vmReq, bulkRequest);
		}
		int count = bulkRequest.numberOfActions();
		log.info("CloudSuYanControlle--insertCloudVmList--count:{},all:{}",count,vmList.size());
		if (count > 0) {
			BulkResponse   bulkResponse  = bulkRequest.execute().actionGet();
			if(bulkResponse.hasFailures()) {
				log.info("CloudSuYanControlle-insertCloudVmList-failMsg:{}",bulkResponse.buildFailureMessage());
				//log.info("CloudSuYanControlle-insertCloudVmList-failMsgDetail:{}",vmList);
				//log.info("CloudSuYanControlle-insertCloudVmList-failMsgDetail1:{}",bulkRequest.request().requests());
			}
		}
		log.info("CloudSuYanController-InsertCloudVmListEnd");
		return count;

	}
	
	void packagePhy(AlertEsHistoryReq phyReq,BulkRequestBuilder bulkRequest) throws ParseException, IllegalAccessException {
		CloudPhy phy = JSON.parseObject(phyReq.getItemListStr(), CloudPhy.class);
		phyReq.setSuyanResid(phy.getRes_id());
		boolean flag = false;
		String time = phy.getCol_time();
		Date date = DateUtils.parseDate(time, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// Date startTime = vmReq.getDatetime();
		DateFormat returnd = new SimpleDateFormat("yyyyMMdd");
		phyReq.setCurDate(new Date());
		phyReq.setDatetime(date);
		phyReq.setClock(date.getTime() / 1000);
		String indexLast = returnd.format(date);
		String index = this.INDEX_HISTORY_PRE + "-" + indexLast;
		Float cpu = phy.getCpu_avg_util_percent_percent_avg();
		// phyReq.setDeviceClass("");
		if (null != cpu) {
			flag = true;
			String item = "custom.cpu.avg5.pused";
			getHis(item, phyReq, cpu, index, bulkRequest);
		}

		Float memPercent = phy.getMem_percent_usedwobufferscaches();
		if (null != memPercent) {
			flag = true;
			// String index = this.INDEX_HISTORY_PRE+"-"+indexLast;
			String item = "custom.memory.pused";
			getHis(item, phyReq, memPercent, index, bulkRequest);
		}
		Float memTotal = phy.getMem_mem_total();
		index = this.INDEX_HISTORY_UINT_PRE + "-" + indexLast;
		if (null != memTotal) {
			flag = true;
			String item = "vm.memory.size[total]";
			long val = (long) (memTotal * 1024 * 1024 * 1024);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float memUsed = phy.getMem_mem_used();
		if (null != memUsed) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vm.memory.size[total]";
			long val = (long) (memUsed * 1024 * 1024);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float netIn = phy.getInterface_rxrate();
		if (null != netIn) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.in";
			long val = (long) (netIn * 1000);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}

		Float netOut = phy.getInterface_txrate();
		if (null != netOut) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.out";
			long val = (long) (netOut * 1000);
			getHisUint(item, phyReq, val, index, bulkRequest);
		}
		
		if(!flag) {
			log.info("***CloudSuYanController--packagePhy--数据无效:{}",phyReq);
		}
	}
	
	void packageVm(AlertEsHistoryReq vmReq,BulkRequestBuilder bulkRequest) throws ParseException, IllegalAccessException {
		CloudVm vm = JSON.parseObject(vmReq.getItemListStr(), CloudVm.class);
		vmReq.setSuyanResid(vm.getRes_id());
		boolean flag = false;
		String time = vm.getCol_time();
		Date date = DateUtils.parseDate(time, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// Date startTime = vmReq.getDatetime();
		DateFormat returnd = new SimpleDateFormat("yyyyMMdd");
		vmReq.setDatetime(date);
		vmReq.setCurDate(new Date());
		vmReq.setClock(date.getTime() / 1000);

		String indexLast = returnd.format(date);
		String index = this.INDEX_HISTORY_PRE + "-" + indexLast;
		Float cpu = vm.getCpu_avg_util_percent();
		if (null != cpu) {
			flag = true;
			
			String item = "cpu.usage_average";
			getHis(item, vmReq, cpu, index, bulkRequest);
		}

		Float memPercent = vm.getMem_avg_util_percent();
		if (null != memPercent) {
			flag = true;
			// String index = this.INDEX_HISTORY_PRE+"-"+indexLast;
			String item = "mem.usage_average";
			getHis(item, vmReq, memPercent, index, bulkRequest);

		}

		Float diskPercent = vm.getDisk_all_percent();
		if (null != diskPercent) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "diskpused";
			getHis(item, vmReq, diskPercent, index, bulkRequest);
		}

		Float memTotal = vm.getMem_total_size();
		index = this.INDEX_HISTORY_UINT_PRE + "-" + indexLast;
		if (null != memTotal) {
			flag = true;
			String item = "vm.memory.size[total]";
			long val = (long) (memTotal * 1024 * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float memUsed = vm.getMem_used_size();
		if (null != memUsed) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "custom.memory.used";
			long val = (long) (memUsed * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float netIn = vm.getNetwork_incoming_bytes_rate();
		if (null != netIn) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.in";
			long val = (long) (netIn * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);

		}

		Float netOut = vm.getNetwork_outgoing_bytes_rate();
		if (null != netOut) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "net.if.out";
			long val = (long) (netOut * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskIn = vm.getDisk_read_bytes_rate();
		if (null != diskIn) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.read[,bps]";
			long val = (long) (diskIn * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskOut = vm.getDisk_write_bytes_rate();
		if (null != diskOut) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.write[,bps]";
			long val = (long) (diskOut * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskInIops = vm.getDisk_read_requests_rate();
		if (null != diskInIops) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.read[,ops]";
			long val = (long) (diskInIops * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskOutIops = vm.getDisk_write_requests_rate();
		if (null != diskOutIops) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "vfs.dev.write[,ops]";
			long val = (long) (diskOutIops * 1000);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskTotal = vm.getDisk_all_total();
		if (null != diskTotal) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "disktotal";
			long val = (long) (diskTotal * 1024 * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		Float diskUsed = vm.getDisk_all_used();
		if (null != diskUsed) {
			flag = true;
			// index = this.INDEX_HISTORY_UINT_PRE+"-"+indexLast;
			String item = "diskused";
			long val = (long) (diskUsed * 1024 * 1024);
			getHisUint(item, vmReq, val, index, bulkRequest);
		}

		if(!flag) {
			log.info("***CloudSuYanController--packageVm--数据无效:{}",vmReq);
		}
	}

}
