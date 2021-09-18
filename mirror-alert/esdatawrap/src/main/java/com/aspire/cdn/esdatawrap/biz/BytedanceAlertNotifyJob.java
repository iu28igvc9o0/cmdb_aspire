package com.aspire.cdn.esdatawrap.biz;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PreDestroy;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.cdn.esdatawrap.biz.client.IBytedanceAlertNotifyClient;
import com.aspire.cdn.esdatawrap.client.ClientServiceBuilder;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.BytedanceAlertConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.google.common.base.Joiner;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;


/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: BytedanceAlertNotifyJob
 * <p/>
 *
 * 类功能描述: 头条告警通知
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月10日  
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
@ConditionalOnProperty(name="local.config.es-run-action.bytedance-alert.switch", matchIfMissing=false)
final class BytedanceAlertNotifyJob implements IElasticSearchBizRunAction {
	private static final String			SOURCE_INDEX_NAME	= "overall_performance";

	@Autowired
	private RestHighLevelClient			restClient;
	@Autowired
	private BytedanceAlertConfigProps	byteDanceProps;
	@Autowired
	private LabelContentHolder			labelContentHolder;
	
	private ScheduledThreadPoolExecutor	executor;
	
	
	@Override
	public void doAction() {
		try {
			schedule();
		} catch (Exception e) {
			throw new RuntimeException(e);
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
		executor.setCorePoolSize(1);
		executor.setMaximumPoolSize(1);
		executor.scheduleAtFixedRate(buildTask(), 0, TimeUnit.MINUTES.toSeconds(5), TimeUnit.SECONDS);
	}
	
	private Runnable buildTask() {
		// 获取分布式锁
		// String lockName = "rawAlert_5xxPercent_" + provinceName;
		// final Lock runLock = disLockHelper.getLock(lockName);
		return new Runnable() {
			@Override
			public void run() {
				String joinDomains = Joiner.on(",").join(byteDanceProps.getDomainList());
				try {
					// 获取分布式锁
					// if (!runLock.tryLock()) {	
					// 	return;
					// }
					log.info("Begin to process bytedance domain alerts for domainList {}.", joinDomains);
					// 计算当前时间减掉2分钟后，过去偏移的5分钟的数据
					int timeSpanMinutes = 5;
					long nowMill = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2);
					long roundNow = nowMill / (1000 * 60 * timeSpanMinutes) * (1000 * 60 * timeSpanMinutes);  
					long fromMill = roundNow - TimeUnit.MINUTES.toMillis(timeSpanMinutes);
					List<String> domainList = byteDanceProps.getDomainList();
					String requestJson = getRequestJson(domainList, fromMill, roundNow);
					runBytedanceDomainListAlerts(requestJson);
				} catch (Throwable e) {
					log.error("Error when process bytedance domain alerts for domainList {}.", joinDomains, e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private String getRequestJson(List<String> domainList, long gteMill, long ltMill) {
		Map<String, Object> varibles = new HashMap<>();
		varibles.put("timestamp_gte", gteMill);
		varibles.put("timestamp_lt", ltMill);
		varibles.put("reqDomainSize", domainList.size());
		
		String rawJson = labelContentHolder.getContentByLabelKey("template_bytedance_domainReq_percent", varibles);
		DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);
		List<Object> shouldArr = jsonCtx.read("$.query.bool.must[1].bool.should");
		
		domainList.forEach(domain -> {
			Map<String, Object> shouldItem = new HashMap<>();
			shouldItem.put("match", Collections.singletonMap("req_domain", domain));
			shouldArr.add(shouldItem);
		});
		return JsonUtil.toJacksonPrettyJson(jsonCtx);
	}
	
	private void runBytedanceDomainListAlerts(final String requestJson) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Bytedance fetch alert items request: {}", requestJson);
		}
		
		Request request = new Request("Post", "/" + SOURCE_INDEX_NAME + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("Bytedance fetch alert items response: {}", respJson);
		}
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		List<Map<String, Object>> bucketList = respCtx.read("$.aggregations.req_domain_group.buckets");
		if (CollectionUtils.isEmpty(bucketList)) {
			log.info("There is no records for the the bytedance domainlist.");
			return;
		}
		
		Float baseline4xx = byteDanceProps.getTriggerPercentFloor4xx();
		Float baseline5xx = byteDanceProps.getTriggerPercentFloor5xx();
		Integer minTotalCount = byteDanceProps.getMinReqTotalCount();
		
		for (Map<String, Object> bucket : bucketList) {
			DocumentContext bucketCtx = JsonUtil.buildDefaultJsonPathContext(bucket);
			String reqDomain = bucketCtx.read("$.key");
			String logTimeStr = bucketCtx.read("$.last_log_time.value_as_string");
			Instant lastLogTime = Instant.parse(logTimeStr);
			Number sum_4xx_count = bucketCtx.read("$.sum_4xx_count.value");
			Number sum_5xx_count = bucketCtx.read("$.sum_5xx_count.value");
			Number total_req_count = bucketCtx.read("$.total_req_count.value");
			notifyAlert(reqDomain, "test_4xx", minTotalCount, baseline4xx, sum_4xx_count, total_req_count, lastLogTime);
			notifyAlert(reqDomain, "test_5xx", minTotalCount, baseline5xx, sum_5xx_count, total_req_count, lastLogTime);
		}
	}
	
	private void notifyAlert(String reqDomain, String classify, 
				Integer minTotalCount, Float baseline, Number statusCount, Number totalCount, Instant lastLogTime) {
		log.info("Prepare to compute bytedance alert: reqDomain={}, classify={}, minTotalCount={}, baseline={},"
				+ "statusCount={}, totalCount={}, lastlogTime={}.", reqDomain, classify, minTotalCount, baseline, 
				statusCount, totalCount, lastLogTime.toString());
		if (totalCount.intValue() <= minTotalCount) {
			return;
		}
		float percent = statusCount.floatValue() / totalCount.floatValue();
		percent = Math.round(percent * 10000) / 10000f;
		if (percent < baseline) {
			return;
		}
		
		Map<String, Object> varibles = new HashMap<>();
		varibles.put("reqDomain", reqDomain);
		varibles.put("baseline", baseline);
		varibles.put("rate", percent);
		varibles.put("alertTime", lastLogTime);
		varibles.put("classify", classify);
		String body = labelContentHolder.getContentByLabelKey("bytedance_alert_notify_body", varibles);
		if (log.isDebugEnabled()) {
			log.debug("The bytedance alert notify detail: body={}", body);
		}
		
		String baseUrl = byteDanceProps.getNotifyUrlBase();
		String signMd5 = byteDanceProps.getBytedanceSignMd5(lastLogTime);
		IBytedanceAlertNotifyClient client = ClientServiceBuilder.buildClientService(IBytedanceAlertNotifyClient.class, baseUrl);
		String notifyResp = client.notifyBytedanceAlert(byteDanceProps.getApiUsername(), lastLogTime.toString(), signMd5, body);
		log.info("Received response for bytedance domain alert notfiy: domain={}, classify={}, repsonse={}",
				reqDomain, classify, notifyResp);
	}
	
	@PreDestroy
	private void preDestroy() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}
}
