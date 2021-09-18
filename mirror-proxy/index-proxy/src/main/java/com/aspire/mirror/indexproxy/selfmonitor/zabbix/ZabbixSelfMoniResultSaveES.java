package com.aspire.mirror.indexproxy.selfmonitor.zabbix;

import static com.aspire.mirror.indexproxy.domain.MonitorItemRecord.VALUE_TYPE_FLOAT;
import static com.aspire.mirror.indexproxy.domain.MonitorItemRecord.VALUE_TYPE_UINT;
import static com.aspire.mirror.indexproxy.selfmonitor.zabbix.ZabbixSelfMoniItemCollectExecutor.SYS_TYPE_ZABBIX_SYNC;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.selfmonitor.SelfMoniCollectResultCallbackFacade.ISelfMoniDataCollectResultCallback;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;
import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.aspire.mirror.indexproxy.util.Md5Util;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ZabbixSelfMoniResultSaveES
 * <p/>
 *
 * 类功能描述: ZABBIX监控自采集结果存储到ES
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月4日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnExpression("${selfMoni.elasticsearch.rest.switch:true}")
class ZabbixSelfMoniResultSaveES implements ISelfMoniDataCollectResultCallback {
	@Autowired(required = false)
	private RestHighLevelClient	restHighClient;
	@Autowired
	private ProxyIdentityConfig proxyCfg;
	
	@Override
	public void postProcess(Map<String, Object> callbackParams, List<SelfMoniCollectItemFetchVal> itemFetchValList) {
		Object sourceSysType = callbackParams.get("sourceSysType");
		if (itemFetchValList.isEmpty() || sourceSysType == null || !SYS_TYPE_ZABBIX_SYNC.equals(sourceSysType.toString())) {
			return;
		}
				
		if (restHighClient == null) {
			log.info("As the self-moni elasticsearch configuration is turned off, "
					+ "now the process of save self-moni item history to Es will be aborted.");
			return;
		}
		
		String valueType = String.valueOf(callbackParams.get("valueType"));
		Map<String, Object> data = new HashMap<>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		BulkRequest bulkRequest = new BulkRequest();
		
		try {
			for (SelfMoniCollectItemFetchVal itemVal : itemFetchValList) {
				data.clear();
				String idxPrefix = null;
				if (VALUE_TYPE_FLOAT.equals(valueType)) {
					data.put("type", "history");
					idxPrefix = "history-";
				} else if (VALUE_TYPE_UINT.equals(valueType)) {
					data.put("type", "history_uint");
					idxPrefix = "history_uint-";
				}
				
				String idxName = idxPrefix + format.format(new Date(itemVal.getClockTime() * 1000l));
				String id = Md5Util.getMD5String(itemVal.getGlobalUniqueFetchValId(proxyCfg));
				
				data.put("time", itemVal.getClockTime() * 1000);
				data.put("clock", itemVal.getClockTime());
				data.put("value", itemVal.getCollectVal());
				data.put("item", itemVal.getCollectItem().getMoniItem().getName());
				data.put("metricName", itemVal.getCollectItem().getMoniItem().getName());
//				data.put("@timestamp", Instant.ofEpochMilli(itemVal.getClockTime() * 1000L));
				data.put("datetime", Instant.ofEpochMilli(itemVal.getClockTime() * 1000L));
				data.put("host", itemVal.getCollectItem().getMoniObj().getName());
				data.put("idcType", proxyCfg.getPool());
				data.put("resourceId", itemVal.getCollectItem().getMoniObj().getObjectId());

				IndexRequest idxRequest = new IndexRequest(idxName, "doc", id);
				idxRequest.source(JsonUtil.toJacksonJson(data), XContentType.JSON);
				bulkRequest.add(idxRequest);
			}
		
			if (log.isDebugEnabled()) {
				log.debug("Save self-moni item value with size {} to ES.", itemFetchValList.size());
			}
			
			BulkResponse bulkResp = restHighClient.bulk(bulkRequest, RequestOptions.DEFAULT);
			if (bulkResp.hasFailures()) {
				log.warn(bulkResp.buildFailureMessage());
			}
		} catch (Throwable e) {
			log.error("Error when saveItemHistory2Es.", e);
		}
	}
}
