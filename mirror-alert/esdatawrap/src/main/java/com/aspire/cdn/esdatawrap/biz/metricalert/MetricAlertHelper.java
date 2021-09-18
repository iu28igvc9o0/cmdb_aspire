package com.aspire.cdn.esdatawrap.biz.metricalert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: MetricAlertHelper
 * <p/>
 *
 * 类功能描述: 指标项告警帮助类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
public final class MetricAlertHelper {
	private static final String	INDEX_NAME			= "metric_alert";
	private static final String INDEX_TYPE 			= "doc";
	private static final String	UPDATE_REQ_URL		= "%s/doc/%s/_update"; 
	private static final String BULK_CREATE_META	= "{ \"create\" : { \"_index\" : \"%s\", \"_type\" : \"doc\", \"_id\" : \"%s\" } }";
	private static final String BULK_UPDATE_META	= "{ \"update\" : { \"_index\" : \"%s\", \"_type\" : \"doc\", \"_id\" : \"%s\" } }";

	@Autowired
	private LabelContentHolder		labelContentHolder;
	@Autowired
	private MetricAlertConfigProps	metricAlertProps;
	@Autowired
	private RestHighLevelClient		restClient;
	
	/** 
	 * 功能描述: 处理指标告警数据  
	 * <p>
	 * @param metricAlert
	 */
	public void processMetricAlert(MetricAlert metricAlert) {
		if (log.isDebugEnabled()) {
			log.debug("Begin to handle metric alert: {}", metricAlert);
		}
		Pair<Boolean, String> checkResult = metricAlert.selfDataCheck();
		if (!checkResult.getLeft()) {
			log.error("Check Metric alert error, detail: {}.", checkResult.getRight());
		}
		
		DocumentContext currActiveCtx = getCurrActiveMetricAlertList(metricAlert);
		if (MetricAlert.MONI_RESULT_ACTIVE .equals(metricAlert.getMoni_result())) {
			processMetricAlertActive(currActiveCtx, metricAlert);
		}
		else if (MetricAlert.MONI_RESULT_REVOKE.equals(metricAlert.getMoni_result())) {
			processMetricAlertRevoke(currActiveCtx, metricAlert);
		}
	}
	
	/** 
	 * 功能描述: 1. 获取和当前待处理告警相同的告警;   2. 过滤出告警级别等于或大于当前待处理告警的告警级别;   3. 过滤出当前未消除的活动告警;  4. 按告警级别升序
	 * <p>
	 * @param metricAlert
	 * @return
	 */
	private DocumentContext getCurrActiveMetricAlertList(MetricAlert metricAlert) {
		try {
			Map<String, Object> varibles = new HashMap<>();
			varibles.put("alert_key", metricAlert.getAlertKey());				// 1. 获取和当前待处理告警相同key的告警;
			varibles.put("alert_level", metricAlert.getAlert_level());			// 2. 过滤出告警级别等于或大于当前待处理告警的告警级别;
			varibles.put("moni_result", MetricAlert.MONI_RESULT_ACTIVE);		// 3. 过滤出当前的活动告警;
			varibles.put("history_flag", MetricAlert.HISTORY_FLAG_CURRENT);		// 3. 过滤出当前未消除的;
			String queryJson = labelContentHolder.getContentByLabelKey("readMetricAlertListByAlertkeyAndLevel", varibles);
			Request request = new Request("Post", "/" + INDEX_NAME + "/_search");
			request.setJsonEntity(queryJson);
			Response response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			return JsonUtil.buildDefaultJsonPathContext(respJson);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void processMetricAlertActive(DocumentContext currActiveCtx, MetricAlert metricAlert) {
		try {
			final Integer currAlertLevel = metricAlert.getAlert_level().intValue(); 
			final Integer aboveTotal = currActiveCtx.read("$.hits.total", Integer.class).intValue();  // 当前级别或以上级别的告警数
			boolean isCurrLevelExist = false;
			if (aboveTotal.intValue() > 0 
					&& currAlertLevel.equals(currActiveCtx.read("$.hits.hits[0]['_source']['alert_level']"))) {
				isCurrLevelExist = true;
			}
			
			// 已经存在当前级别的告警则更新
			if (isCurrLevelExist) {
				// 只更新第一条, 即告警级别匹配当前处理告警的那条数据
				String docId = currActiveCtx.read("$.hits.hits[0]['_id']");
				Integer previousStepCount = currActiveCtx.read("$.hits.hits[0]['_source']['step_count']");
				log.info("Begin to update metric-alert, detail: docId: {}, alertKey {}, alert: {}.", 
						docId, metricAlert.getAlertKey(), metricAlert.toString());
				metricAlert.setStep_count(previousStepCount + 1);	// 重复告警次数累加1
				metricAlert.setScan_mark_time(currActiveCtx.read("$.hits.hits[0]['_source']['scan_mark_time']", Long.class));
				// 如果为UMS反向同步的可见告警，或者为跳过告警收敛处理，或者为每次达到收敛次数, 重新让告警进入可扫描
				if (metricAlert.getReverse_sync() || metricAlert.getSkip_step()
						|| metricAlert.getStep_count() % metricAlertProps.getStepcountCutdown() == 0) {
					metricAlert.refreshScanMarkTime();
				}
				Request request = new Request("Post", String.format(UPDATE_REQ_URL, INDEX_NAME, docId));
				String queryJson = labelContentHolder.getContentByLabelKey(
						"updateActiveMetricAlertByDocId", metricAlert.resolveActiveAlertUpdateParams());
				request.setJsonEntity(queryJson);
				restClient.getLowLevelClient().performRequest(request);
			} 
			// 不存在匹配当前告警级别的告警, 则新增
			else {
				String docId = metricAlert.generateDocId();
				log.info("Begin to insert metric-alert, detail: docId: {}, alertKey {}, alert: {}.", 
						docId, metricAlert.getAlertKey(), metricAlert.toString());
				// 处理scan_mark_time
				if (!metricAlert.getReverse_sync() && !metricAlert.getSkip_step()
						&& metricAlertProps.isMetricAlertStepCutDown()) {
					metricAlert.set2NoScanMarkTime();
				} else {
					metricAlert.refreshScanMarkTime();
				}
				IndexRequest idxReq = new IndexRequest(INDEX_NAME, INDEX_TYPE, docId);
				idxReq.source(metricAlert.toEsDoc(), XContentType.JSON);
				restClient.index(idxReq, RequestOptions.DEFAULT);
			}
			
			// 针对当前告警级别以上级别的告警，触发产生消警
			if (aboveTotal.intValue() == 0 
					|| currAlertLevel.intValue() == MetricAlert.ALERT_LEVEL_SERIOUS.intValue()) {
				return;
			}
			metricAlert.set2NoScanMarkTime();   						// 重置			
			metricAlert.setAlert_level(currAlertLevel.intValue() + 1);	// 告警级别加一个级别
			if (isCurrLevelExist) {
				currActiveCtx.delete("$.hits.hits[0]");
			}
			processMetricAlertRevoke00(currActiveCtx, metricAlert);
		} catch (Exception e) {
			log.error("Error when processMetricAlertActive.", e);
		}
	}
	
	/** 
	 * 功能描述: 处理消警; 1. 当前告警级别以上的告警全部清除	2. 为匹配的每一条告警新增一条清除记录
	 * <p>
	 * @param currActiveCtx
	 * @param metricAlert
	 */
	private void processMetricAlertRevoke(DocumentContext currActiveCtx, MetricAlert metricAlert) {
		try {
			Integer size = currActiveCtx.read("$.hits.hits.length()");
			if (size == 0) {
				if (log.isDebugEnabled()) {
					log.debug("As there is no metric alert active, the revoke action will just return.");
				}
				return;
			}
			log.info("Begin to revoke metric-alert, alertkey: {}, detail: {}.", 
					metricAlert.getAlertKey(), metricAlert.toString());
			processMetricAlertRevoke00(currActiveCtx, metricAlert);
		} catch (Exception e) {
			log.error("Error when processMetricAlertActive.", e);
		}
	}
	
	private void processMetricAlertRevoke00(DocumentContext currActiveCtx, MetricAlert metricAlert) throws Exception {
		Integer size = currActiveCtx.read("$.hits.hits.length()");
		if (size == 0) {
			return;
		}	
		List<String> bulkLines = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			String docId = currActiveCtx.read("$.hits.hits[" + i + "]['_id']");
			bulkLines.add(String.format(BULK_UPDATE_META, INDEX_NAME, docId));
			MetricAlert alert = currActiveCtx.read("$.hits.hits[" + i + "]['_source']", MetricAlert.class);
			bulkLines.add(JsonUtil.toJacksonJson(alert.resolveActiveAlertRevokeUpdateParams()));
			
			MetricAlert revokeAlert = metricAlert.copy();
			revokeAlert.setAlert_level(alert.getAlert_level());
			revokeAlert.setHistory_flag(MetricAlert.HISTORY_FLAG_HISTORY);
			revokeAlert.setStep_count(alert.getStep_count());   // 消警时，step_count保持和告警一致, step_count作为告警收敛的查询字段，非常重要
			// 如果消警对应的告警之前已经进入可扫描, 则消警也需要进入可扫描
			if (metricAlert.getReverse_sync() || metricAlert.getSkip_step() 
					|| alert.getStep_count() >= metricAlertProps.getStepcountCutdown()) {
				revokeAlert.refreshScanMarkTime();					
			}
			bulkLines.add(String.format(BULK_CREATE_META, INDEX_NAME, revokeAlert.generateDocId()));
			bulkLines.add(JsonUtil.toJacksonJson(revokeAlert));
		}
		bulkLines.add("\n");
		
		Request request = new Request("Post", "_bulk");
		request.setJsonEntity(String.join("\n", bulkLines));
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		if (StringUtils.isNotBlank(respJson) && respJson.contains("\"errors\":true")) {
			log.error("Got the bulk submit response for metric alert revoke, but may be some errors, detail: {}", respJson);
		}
	}
}
