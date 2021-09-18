package com.aspire.cdn.esdatawrap.biz.selfalert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.IElasticSearchBizRunAction;
import com.aspire.cdn.esdatawrap.biz.domain.Raw404PercentAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.FocusDoaminReq404PecentConfigProps;
import com.aspire.cdn.esdatawrap.config.model.FocusDomainConfigProps;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: Req5xxPercentRawAlertJob
 * <p/>
 *
 * 类功能描述: 商业域名404请求占比计算任务
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月15日  
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
@ConditionalOnProperty(name="local.config.es-run-action.focus-domain-req404-percent.switch", matchIfMissing=false)
final class BizDomainReq404PercentRawAlertJob implements IElasticSearchBizRunAction {
	@Autowired
	private LabelContentHolder					labelContentHolder;
	@Autowired
	private RestHighLevelClient					restClient;
	@Autowired(required = false)
	private FocusDoaminReq404PecentConfigProps	bizDomainReq404ConfigProps;
	@Autowired(required = false)
	private FocusDomainConfigProps				focusDomainConfigProps;
	@Autowired
	private MetricAlertConfigProps				metricAlertConfigProps;
	@Autowired
	private MetricAlertHelper					metricAlertHelper;
	@Value("${local.config.selfalert.pastLimit:2}")
	private Integer 							alertPastLimit;

	private ScheduledThreadPoolExecutor			executor;
	private static final String					SOURCE_INDEX_NAME	= "overall_performance";
	// private static final String SOURCE_INDEX_NAME = "pgh_test_performance";
	private static final String					AFTER_OBJ_JSON_PATH	= "aggs.composite_group.composite.after";

	
	@Override
	public void doAction() {
		schedule5xxPercentRawAlert();
	}
	
	private void schedule5xxPercentRawAlert() {
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(4);
		executor.scheduleAtFixedRate(
				buildTask(), 0, bizDomainReq404ConfigProps.getIntervalMinutes(), TimeUnit.MINUTES);
	}
	
	private Runnable buildTask() {
		// 获取分布式锁
		// String lockName = "lock_rawAlert_5xxPercent";
		// final Lock runLock = disLockHelper.getLock(lockName);
		return new Runnable() {
			@Override
			public void run() {
				try {
					// 获取分布式锁
					// if (!runLock.tryLock()) {	
					// 	return;
					// }
					
					log.info("Begin to fetch and generate focus domain 404 percent alerts.");
					// 计算当前时间减掉2分钟后，过去偏移的5分钟的数据
					int timeSpanMinutes = bizDomainReq404ConfigProps.getIntervalMinutes();
					long nowMill = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(alertPastLimit);
					long roundNow = nowMill - nowMill % TimeUnit.MINUTES.toMillis(1); 
					long fromMill = roundNow - TimeUnit.MINUTES.toMillis(timeSpanMinutes);
					String requestJson = getRequestJson(fromMill, roundNow, timeSpanMinutes);
					List<String> focusDomainList = new ArrayList<>(focusDomainConfigProps.getFocusPriorityDomainList());
					List<String> excludeDomainList = bizDomainReq404ConfigProps.getExcludeDomainList();
					focusDomainList.removeAll(excludeDomainList);
					run404PercentRawAlert(requestJson, focusDomainList, null);
				} catch (Throwable e) {
					log.error("Error when fetch and generate focus domain 404 percent alerts.", e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private String getRequestJson(long gteMill, long ltMill, int timeSpanMinutes) {
		Map<String, Object> varibles = new HashMap<>();
		varibles.put("timestamp_gte", gteMill);
		varibles.put("timestamp_lt", ltMill);
		varibles.put("span_seconds", TimeUnit.MINUTES.toSeconds(timeSpanMinutes));
		return labelContentHolder.getContentByLabelKey("template_fetch_focus_domain_404_percent_rawalert", varibles);
	}
	
	private void run404PercentRawAlert(String requestJson, List<String> businessDomainList, Object afterObj) throws Exception {
		DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(requestJson);
		jsonCtx.set("$.query.bool.must[1].terms.req_domain", businessDomainList);
		if (afterObj != null) {
			jsonCtx.set("$." + AFTER_OBJ_JSON_PATH, afterObj);
		}
		requestJson = jsonCtx.jsonString();
		if (log.isDebugEnabled()) {
			log.debug("By province business-domain 404 Percent request: {}", requestJson);
		}
		
		Request request = new Request("Post", "/" + SOURCE_INDEX_NAME + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("Business domain 404 percent response: {}", respJson);
		}
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		List<Map<String, Object>> bucketList = respCtx.read("$.aggregations.composite_group.buckets");
		if (CollectionUtils.isEmpty(bucketList)) {
			return;
		}
		log.info("Business domain 404 percent response bucketList size: {}.", bucketList.size());
		
		final List<MetricAlert> alertList = new ArrayList<>();
		for (Map<String, Object> bucket : bucketList) {
			DocumentContext bucketCtx = JsonUtil.buildDefaultJsonPathContext(bucket);
			Raw404PercentAlert rawAlert = Raw404PercentAlert.buildFromBucket(bucketCtx, metricAlertConfigProps.getBusinessSource());
			handle404PercentRawAlert(rawAlert, alertList);
		}
		if (alertList.size() > 0) {
			executor.submit(() -> metricAlertHelper.processMetricAlertBatch(alertList));
		}
		Object afterKey = respCtx.read("$.aggregations.composite_group.after_key");
		if (afterKey != null) {
			run404PercentRawAlert(requestJson, businessDomainList, afterKey);
		}
	}
	
	/** 
	 * 功能描述: 处理5xx百分比原始告警  
	 * <p>
	 * @param rawAlert
	 * @throws IOException 
	 */
	private void handle404PercentRawAlert(Raw404PercentAlert rawAlert, final List<MetricAlert> alertList) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Fetch the 5xx pecent data from es: " + rawAlert.toString());
		}
		
		Pair<Integer, Integer> alertStatus = rawAlert.resolveRawAlertStatus(bizDomainReq404ConfigProps, focusDomainConfigProps);
		// 当前未触发告警
		if (alertStatus.getKey() == 0) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.info("The 404 percent raw alert is to be processed, details: " + rawAlert.toString());
		}
		alertList.add(rawAlert.parse2MetricAlert(alertStatus.getKey(), alertStatus.getValue()));
	}
}
