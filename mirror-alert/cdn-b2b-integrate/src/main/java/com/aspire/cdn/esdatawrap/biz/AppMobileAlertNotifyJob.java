package com.aspire.cdn.esdatawrap.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.generaloperate.model.AppClientInfo;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.AlertAppMobileNotifyConfigProps;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;

import lombok.extern.slf4j.Slf4j;


/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: AlertWexinNotifyJob
 * <p/>
 *
 * 类功能描述: app手机端告警通知任务
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月24日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Order(100)
@Component
@ConditionalOnExpression("${local.config.es-run-action.alert-app-mobile-notify.switch:false} && ${local.config.mobile-notify.switch:false}")
final class AppMobileAlertNotifyJob implements IElasticSearchBizRunAction {
	private static final String			ALERT_SYNC_MARK_INDEX		= "thirdparty_alert_sync_mark";
	private static final String			APP_MOBILE_SYNC_MARK_ID		= "appMobile";
	private static final String			UMS_SYNC_MARK_ID			= "ums";
	private static final String			ALERT_READ_INDEX			= "metric_alert";
	private static final String 		IDX_APP_CLIENT_INFO			= "app_client_info";
	private static final String			DOC_TYPE					= "doc";

	@Autowired
	private LabelContentHolder				labelContentHolder;
	@Autowired
	private RestHighLevelClient				restClient;
	@Autowired
	private AlertAppMobileNotifyConfigProps	alertAppMobileProps;
	@Autowired
	private AppPushHelper					appPushHelper;
	@Autowired
	private LabelContentHolder 				labelHolder;
	@Autowired
	private MetricAlertConfigProps			metricConfig;
	
	private ScheduledThreadPoolExecutor		executor;
	
	@Override
	public void doAction() {
		schedule();
	}
	
	private void schedule() {
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(1);
		executor.scheduleWithFixedDelay(buildTask(), 0, 
				TimeUnit.MILLISECONDS.toSeconds(alertAppMobileProps.getRunInterval().toMillis()), TimeUnit.SECONDS);
	}
	
	private Runnable buildTask() {
		// 获取分布式锁
		// String lockName = "rawAlert_5xxPercent_" + provinceName;
		// final Lock runLock = disLockHelper.getLock(lockName);
		return new Runnable() {
			@Override
			public void run() {
				try {
					// 获取分布式锁
					// if (!runLock.tryLock()) {	
					// 	return;
					// }
					log.info("Begin to run app mobile alert notify job.");
					runAppMobileAlertNotify();
				} catch (Throwable e) {
					log.error("Error when run app mobile alert notify job.", e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private Long getLastReadMark(String markId) {
		try {
			Request request = new Request("Get", ALERT_SYNC_MARK_INDEX + "/" + DOC_TYPE + "/" + markId);
			Response response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
			Boolean found = respCtx.read("$.found");
			if (!found) {
				return System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5);
			}
			Long lastMarkMill = respCtx.read("$._source.last_sync_mill", Long.class);
			return lastMarkMill;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * 功能描述: APP手机端告警通知
	 * <p>
	 * @throws Exception
	 */
	private void runAppMobileAlertNotify() throws Exception {
		Map<String, Object> varibles = new HashMap<>();
		Long syncMarkMill = getLastReadMark(APP_MOBILE_SYNC_MARK_ID);
		Long syncUmsMark = getLastReadMark(UMS_SYNC_MARK_ID);
		if (syncUmsMark < syncMarkMill) {
			log.info("Ums syncmark is less than appmobile syncmark, the app alert notity will ignore this time.");
			return;
		}
		Long endTimeLimit = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2);
		endTimeLimit = Math.min(syncUmsMark, endTimeLimit);
		varibles.put("syncMarkMill", syncMarkMill);
		varibles.put("syncMarkMillEnd", endTimeLimit);
		varibles.put("businessSource", metricConfig.getBusinessSource());
		String queryJson = labelContentHolder.getContentByLabelKey("readAppMobileB2BAlertListFromSyncMark", varibles);
		Request request = new Request("Post", ALERT_READ_INDEX + "/" + DOC_TYPE + "/_search?scroll=1m");
		request.setJsonEntity(queryJson);
		
		log.info("Begin to read alert list from index {} from scan-mark-time {}.", ALERT_READ_INDEX, syncMarkMill);
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		Integer hitCount = respCtx.read("$.hits.hits.length()");
		if (hitCount == 0) {
			return;
		}
		
		List<AppClientInfo> appClientList = fetchAppClientList();
		
		Map<String, Object> params = new HashMap<>();
		params.put("timeout", "1m");
		params.put("scrollId", respCtx.read("$._scroll_id"));
		
		boolean continueFlag = processScrollDataList(respCtx, appClientList);
		while (continueFlag) {
			request = new Request("Post", "/_search/scroll");
			queryJson = labelHolder.getContentByLabelKey(LabelContentHolder.KEY_SCROLL_FETCH_DATA, params);
			request.setJsonEntity(queryJson);
			response = restClient.getLowLevelClient().performRequest(request);
			respCtx = JsonUtil.buildDefaultJsonPathContext(EntityUtils.toString(response.getEntity()));
			continueFlag = processScrollDataList(respCtx, appClientList);
		}
	}
	
	private List<AppClientInfo> fetchAppClientList() {
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.size(2000);
		RangeQueryBuilder queryBuilder = new RangeQueryBuilder("refresh_time");
		queryBuilder.gt(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
		sourceBuilder.query(queryBuilder);
		SearchRequest searchReq = new SearchRequest(new String[] {IDX_APP_CLIENT_INFO}, sourceBuilder);
		
		SearchResponse response = null;
		try {
			response = restClient.search(searchReq, RequestOptions.DEFAULT);
			SearchHit[] hits = response.getHits().getHits();
			return Arrays.asList(hits).stream().filter(hit -> StringUtils.isNotBlank(hit.getSourceAsString())).map(hit -> {
				return JsonUtil.jacksonConvert(hit.getSourceAsString(), AppClientInfo.class);
			}).collect(Collectors.toList());
		} catch (IOException e) {
			log.error(null, e);
		}
		return new ArrayList<>();
	}
	
	private boolean processScrollDataList(DocumentContext respCtx, List<AppClientInfo> appClientList) throws IOException {
		Integer readCount = respCtx.read("$.hits.hits.length()", Integer.class);
		if (readCount.intValue() == 0) {
			return false;
		}
		TypeRef<List<MetricAlert>> typeRef = new TypeRef<List<MetricAlert>>() {};
		List<MetricAlert> alertList = respCtx.read("$.hits.hits[*]['_source']", typeRef);
		
		alertList.stream().forEach(alert -> {
			try {
				// 只推送告警
				if (alert.getMoni_result() != MetricAlert.MONI_RESULT_ACTIVE) {
					return;
				}
				String rAlertId = alert.getAlertKey() + "_" + alert.getAlert_level();
				Map<String, Object> params = new HashMap<>();
				params.put("actionType", "alert_push");
				params.put("rAlertId", rAlertId);
				appPushHelper.pushListMessage(appClientList, alert.getTheme(), alert.getContent(), params);
			} catch (Throwable e) {
				log.error("Error when try to push app mobile alert notify.", e);
			}
		});
		
		updateAlertSyncMark(alertList.get(alertList.size() - 1).getScan_mark_time());
		return true;
	}
	
	private void updateAlertSyncMark(Long lastMarkTime) throws IOException {
		log.info("Begin to refresh '{}' of docId '{}' with value {}.", ALERT_SYNC_MARK_INDEX, APP_MOBILE_SYNC_MARK_ID, lastMarkTime);
		IndexRequest indexReq = new IndexRequest(ALERT_SYNC_MARK_INDEX, DOC_TYPE, APP_MOBILE_SYNC_MARK_ID);
		indexReq.source(Collections.singletonMap("last_sync_mill", lastMarkTime));
		this.restClient.index(indexReq, RequestOptions.DEFAULT);
	}
}
