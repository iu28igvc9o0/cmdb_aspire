package com.aspire.cdn.esdatawrap.biz;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.client.model.WeixinMessageSendRequest;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.AlertWeixinNotifyConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
 * 类功能描述: 告警上报微信通知定时任务
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
@ConditionalOnProperty(name="local.config.es-run-action.alert-weixin-notify.switch", matchIfMissing=false)
final class AlertWexinNotifyJob implements IElasticSearchBizRunAction {
	private static final String			PROVINCE_MAP_AGENTSECRET	= "province_map_agentidSecret";
	private static final String			ALERT_SYNC_MARK_INDEX		= "thirdparty_alert_sync_mark";
	private static final String			WEIXIN_SYNC_MARK_ID			= "weixin";
	private static final String			ALERT_READ_INDEX			= "metric_alert";
	private static final String			DOC_TYPE					= "doc";

	@Autowired
	private LabelContentHolder				labelContentHolder;
	@Autowired
	private RestHighLevelClient				restClient;
	@Autowired
	private AlertWeixinNotifyConfigProps	alertWexinProps;
	@Autowired
	private WeixinAcessHelper				weixinHelper;
	@Autowired
	private LabelContentHolder 				labelHolder;
	
	private final Map<String, Map<String, String>>	provinceMap		= new HashMap<>();
	private ScheduledThreadPoolExecutor	executor;
	
	@Override
	public void doAction() {
		prepare();
		schedule();
	}
	
	private void prepare() {
		String mapJson = labelContentHolder.getContentByLabelKey(PROVINCE_MAP_AGENTSECRET);
		TypeReference<Map<String, Map<String, String>>> typeRef = new TypeReference<Map<String, Map<String, String>>>() {};
		provinceMap.putAll(JsonUtil.jacksonConvert(mapJson, typeRef));
		if (provinceMap.isEmpty()) {
			throw new RuntimeException("The province map agentId-secret should not be empty.");
		}
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
				TimeUnit.MINUTES.toSeconds(alertWexinProps.getRunInterval().toMinutes()), TimeUnit.SECONDS);
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
					log.info("Begin to run alert weixin notify.");
					runAlertWeixinNotify();
				} catch (Throwable e) {
					log.error("Error when run alert weixin notify.", e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private Long getLastReadMark() {
		try {
			Request request = new Request("Get", ALERT_SYNC_MARK_INDEX + "/" + DOC_TYPE + "/" + WEIXIN_SYNC_MARK_ID);
			Response response = restClient.getLowLevelClient().performRequest(request);
			String respJson = EntityUtils.toString(response.getEntity());
			DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
			Boolean found = respCtx.read("$.found");
			if (!found) {
				return 0L;
			}
			Long lastMarkMill = respCtx.read("$._source.last_sync_mill", Long.class);
			return lastMarkMill;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * 功能描述: 告警微信通知  
	 * <p>
	 * @throws Exception
	 */
	private void runAlertWeixinNotify() throws Exception {
		Map<String, Object> varibles = new HashMap<>();
		Long syncMarkMill = getLastReadMark();
		varibles.put("syncMarkMill", syncMarkMill);
		varibles.put("syncMarkMillEnd", System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1));
		String queryJson = labelContentHolder.getContentByLabelKey("readAlertListFromSyncMark", varibles);
		Request request = new Request("Post", ALERT_READ_INDEX + "/" + DOC_TYPE + "/_search?scroll=1m");
		request.setJsonEntity(queryJson);
		
		log.info("Begin to read alert list from index {} from scan-mark-time {}.", ALERT_READ_INDEX, syncMarkMill);
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		
		Map<String, Object> params = new HashMap<>();
		params.put("timeout", "1m");
		params.put("scrollId", respCtx.read("$._scroll_id"));
		
		boolean continueFlag = processScrollDataList(respCtx);
		while (continueFlag) {
			request = new Request("Post", "/_search/scroll");
			queryJson = labelHolder.getContentByLabelKey(LabelContentHolder.KEY_SCROLL_FETCH_DATA, params);
			request.setJsonEntity(queryJson);
			response = restClient.getLowLevelClient().performRequest(request);
			respCtx = JsonUtil.buildDefaultJsonPathContext(EntityUtils.toString(response.getEntity()));
			continueFlag = processScrollDataList(respCtx);
		}
	}
	
	private boolean processScrollDataList(DocumentContext respCtx) throws IOException {
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
				String agentId = weixinHelper.getAgentIdByProvinceName(alert.getProvince_name());
				if (StringUtils.isBlank(agentId)) {
					return;
				}
				WeixinMessageSendRequest alertNotify = new WeixinMessageSendRequest();
				alertNotify.setInnerContent(alert.getTheme() + ":", alert.getContent());
				alertNotify.setAgentId(agentId);
				String accssToken = weixinHelper.getWeixinCorpAccessTokenByProvince(alert.getProvince_name());
				weixinHelper.notifyAlertByWeixinCorp(accssToken, alertNotify);
			} catch (Throwable e) {
				log.error(null, e);
			}
		});
		
		updateAlertSyncMark(alertList.get(alertList.size() - 1).getScan_mark_time());
		return true;
	}
	
	private void updateAlertSyncMark(Long lastMarkTime) throws IOException {
		log.info("Begin to refresh '{}' of docId '{}' with value {}.", ALERT_SYNC_MARK_INDEX, WEIXIN_SYNC_MARK_ID, lastMarkTime);
		IndexRequest indexReq = new IndexRequest(ALERT_SYNC_MARK_INDEX, DOC_TYPE, WEIXIN_SYNC_MARK_ID);
		indexReq.source(Collections.singletonMap("last_sync_mill", lastMarkTime));
		this.restClient.index(indexReq, RequestOptions.DEFAULT);
	}
}
