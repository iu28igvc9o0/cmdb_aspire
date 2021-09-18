package com.aspire.cdn.esdatawrap.biz.selfalert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.util.CollectionUtils;

import com.aspire.cdn.esdatawrap.biz.IElasticSearchBizRunAction;
import com.aspire.cdn.esdatawrap.biz.domain.InitDelay5TimesAlertCache;
import com.aspire.cdn.esdatawrap.biz.domain.Raw5xxPercentAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.FocusDomainConfigProps;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.config.model.Req5xxPecentConfigProps;
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
 * 类功能描述: 5xx请求占比计算任务
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
@ConditionalOnProperty(name="local.config.es-run-action.req5xx-percent.switch", matchIfMissing=false)
final class Req5xxPercentRawAlertJob implements IElasticSearchBizRunAction {
	@Autowired
	private LabelContentHolder			labelContentHolder;
	@Autowired
	private RestHighLevelClient			restClient;
	@Autowired(required=false)
	private Req5xxPecentConfigProps		req5xxConfigProps;
	@Autowired(required = false)
	private FocusDomainConfigProps		focusDomainConfigProps;
	@Autowired
	private MetricAlertConfigProps		metricAlertConfigProps;
	@Autowired
	private MetricAlertHelper			metricAlertHelper;
	@Value("${local.config.selfalert.pastLimit:2}")
	private Integer 					alertPastLimit;
	
	private final ConcurrentHashMap<String, InitDelay5TimesAlertCache> initDelayCacheHolder = new ConcurrentHashMap<>(); 

	private ScheduledThreadPoolExecutor	executor;
	private static final String			SOURCE_INDEX_NAME			= "overall_performance";
//	private static final String			SOURCE_INDEX_NAME			= "pgh_test_performance";
	private static final String			AFTER_OBJ_JSON_PATH			= "aggs.composite_group.composite.after";
	
	public static final String  		IP_REG 
						= "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
	public static final Pattern IP_PATTERN = Pattern.compile(IP_REG);
	
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
		executor.setMaximumPoolSize(5);
		executor.scheduleAtFixedRate(buildTask(), 0, req5xxConfigProps.getIntervalMinutes(), TimeUnit.MINUTES);
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
					
					// 计算当前时间减掉2分钟后，过去偏移的$timeSpanMinutes分钟的数据
					Long systime = System.currentTimeMillis();
					int timeSpanMinutes = req5xxConfigProps.getIntervalMinutes();
					log.info("Begin to fetch and generate 5xxPercent alerts at {} with iterval {}.", systime, timeSpanMinutes);
					long nowMill = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(alertPastLimit);
					long roundNow = nowMill - nowMill % TimeUnit.MINUTES.toMillis(1); 
					long fromMill = roundNow - TimeUnit.MINUTES.toMillis(timeSpanMinutes);
					String requestJson = getRequestJson(fromMill, roundNow, timeSpanMinutes);
					run5xxPercentRawAlert(requestJson, null);
					log.info("Finish fetch and generate 5xxPercent alerts at {} with iterval {}, cost {}.", 
							systime, timeSpanMinutes, System.currentTimeMillis() - systime);
				} catch (Throwable e) {
					log.error("Error when fetch and generate 5xxPercent alerts.", e);
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
		return labelContentHolder.getContentByLabelKey("template_fetch_5xx_percent_rawalert", varibles);
	}
	
	private void run5xxPercentRawAlert(String requestJson, Object afterObj) throws Exception {
		if (afterObj != null) {
			DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(requestJson);
			jsonCtx.set("$." + AFTER_OBJ_JSON_PATH, afterObj);
			requestJson = jsonCtx.jsonString();
		}
		if (log.isDebugEnabled()) {
			log.debug("5xxPercent request: {}", requestJson);
		}
		
		Request request = new Request("Post", "/" + SOURCE_INDEX_NAME + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("5xxPercent response: {}", respJson);
		}
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		List<Map<String, Object>> bucketList = respCtx.read("$.aggregations.composite_group.buckets");
		if (CollectionUtils.isEmpty(bucketList)) {
			return;
		}
		log.info("bucketList size: {}.", bucketList.size());
		
		final List<MetricAlert> alertList = new ArrayList<>();
		for (Map<String, Object> bucket : bucketList) {
			DocumentContext bucketCtx = JsonUtil.buildDefaultJsonPathContext(bucket);
			Raw5xxPercentAlert rawAlert = Raw5xxPercentAlert.buildFromBucket(bucketCtx, metricAlertConfigProps.getBusinessSource());
			handle5xxPercentRawAlert(rawAlert, alertList);
		}
		if (alertList.size() > 0) {
			executor.submit(() -> metricAlertHelper.processMetricAlertBatch(alertList));
		}
		Object afterKey = respCtx.read("$.aggregations.composite_group.after_key");
		if (afterKey != null) {
			run5xxPercentRawAlert(requestJson, afterKey);
		}
	}
	
	/** 
	 * 功能描述: 处理5xx百分比原始告警  
	 * <p>
	 * @param rawAlert
	 * @throws IOException 
	 */
	private void handle5xxPercentRawAlert(Raw5xxPercentAlert rawAlert, final List<MetricAlert> alertList) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Fetch the 5xx pecent data from es: " + rawAlert.toString());
		}
		
		Pair<Boolean, String> validResult = validReqDomain(rawAlert);
		if (!validResult.getLeft()) {
			log.warn(validResult.getRight());
			return;
		}
		
		Pair<Integer, Integer> alertStatus = rawAlert.resolveRawAlertStatus(req5xxConfigProps, focusDomainConfigProps);
		// 当前未触发告警
		if (alertStatus.getKey() != 0) {
			if (log.isDebugEnabled()) {
				log.info("The raw alert is to be processed, details: " + rawAlert.toString());
			}
			alertList.add(rawAlert.parse2MetricAlert(alertStatus.getKey(), alertStatus.getValue()));
		}
		
		InitDelay5TimesAlertCache nowItem = InitDelay5TimesAlertCache.from(rawAlert.getProvinceName(), rawAlert.getSourceIdentity(), 
				rawAlert.getBusinessSource(), rawAlert.getManufacturer(), rawAlert.getReqDomain(), rawAlert.getInitDelayValue());
		final InitDelay5TimesAlertCache previousItem = initDelayCacheHolder.get(nowItem.getUniqueKey());
		if (previousItem == null) {
			initDelayCacheHolder.put(nowItem.getUniqueKey(), nowItem);
			return;
		}
		
		// 处理首包时延倍增达到5倍告警
		alertStatus = rawAlert.resolveRawInitDelayAlertStatus(req5xxConfigProps, previousItem);
		if (alertStatus.getKey() != 0) {
			if (log.isDebugEnabled()) {
				log.info("The init delay 5 times raw alert is to be processed, details: " + rawAlert.toString());
			}
			alertList.add(rawAlert.parse2InitDelayMetricAlert(alertStatus.getKey(), alertStatus.getValue()));
		}
		
		if (MetricAlert.MONI_RESULT_ACTIVE.equals(alertStatus.getKey())) {
			previousItem.setAlertStatus(alertStatus.getKey());
			// initDelayCacheHolder.put(nowItem.getUniqueKey(), nowItem);
		} else {
			nowItem.setAlertStatus(alertStatus.getKey());
			initDelayCacheHolder.put(nowItem.getUniqueKey(), nowItem);
		}
	}
	
	private Pair<Boolean, String> validReqDomain(Raw5xxPercentAlert rawAlert) {
		String reqDomain = rawAlert.getReqDomain();
		if (StringUtils.isBlank(reqDomain)) {
			return Pair.of(false, "The reqDomain is invalid as it is empty.");
		}
		if (!Pattern.compile("[a-zA-Z]").matcher(reqDomain).find()) {
			return Pair.of(false, "The reqDomain is invalid: " + reqDomain);
		}
		if (reqDomain.indexOf('%') >= 0 || reqDomain.indexOf(',') >= 0 || reqDomain.indexOf('>') >= 0
				|| reqDomain.indexOf('<') >= 0 || reqDomain.indexOf('=') >= 0 || reqDomain.indexOf('/') >= 0) {
			return Pair.of(false, "The reqDomain is invalid as it has special chars: " + reqDomain);
		}
		return Pair.of(true, null);
	}
}
