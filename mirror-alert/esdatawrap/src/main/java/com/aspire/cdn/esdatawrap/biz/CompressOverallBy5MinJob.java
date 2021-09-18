package com.aspire.cdn.esdatawrap.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.cdn.esdatawrap.biz.domain.CompressOverall5MinEsDoc;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.CompressOverall5minProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: CompressOverallBy5MinJob.java
 * <p/>
 *
 * 类功能描述: 按5分钟粒度汇聚数据，维度为： province_name + req_domain + service_type
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
@ConditionalOnProperty(name="local.config.es-run-action.compress-overall5min.switch", matchIfMissing=true)
final class CompressOverallBy5MinJob implements IElasticSearchBizRunAction {
	private static final String			SOURCE_INDEX_NAME			= "overall_performance";
	private static final String			AFTER_OBJ_JSON_PATH			= "aggs.composite_group.composite.after";

	@Autowired
	private LabelContentHolder			labelContentHolder;
	@Autowired
	private RestHighLevelClient			restClient;
	@Autowired 
	private CompressOverall5minProps	configProps;
	
	private ScheduledThreadPoolExecutor	executor;
	
	
	@Override
	public void doAction() {
		schedule();
	}
	
	private void schedule() {
		List<String> provinceList = configProps.getProvinceList();
		executor = new ScheduledThreadPoolExecutor(provinceList.size(), new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(provinceList.size());
		for (String provinceName : provinceList) {
			executor.scheduleAtFixedRate(buildTask(provinceName), 0, configProps.getCompressSpan().toMinutes(), TimeUnit.MINUTES);
		}
	}
	
	private Runnable buildTask(final String provinceName) {
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
					
					log.info("Begin to compress overall-peformance data for province {}.", provinceName);
					// 计算当前时间减掉2分钟后，过去偏移的汇聚时长的数据
					long timeSpanMinutes = configProps.getCompressSpan().toMinutes();
					long nowMill = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2);
					long roundNow = nowMill / (1000 * 60 * timeSpanMinutes) * (1000 * 60 * timeSpanMinutes);  
					long fromMill = roundNow - TimeUnit.MINUTES.toMillis(timeSpanMinutes);
					String requestJson = getRequestJson(provinceName, fromMill, roundNow);
					processCompressDataList(provinceName, fromMill, requestJson, null);
				} catch (Throwable e) {
					log.info("Error when  compress overall-peformance data for province {}.", provinceName, e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private String getRequestJson(String provinceName, long gteMill, long ltMill) {
		Map<String, Object> varibles = new HashMap<>();
		varibles.put("timestamp_gte", gteMill);
		varibles.put("timestamp_lt", ltMill);
		varibles.put("province_name", provinceName);
		return labelContentHolder.getContentByLabelKey("compress_overall_by5min", varibles);
	}
	
	private void processCompressDataList(String provinceName, long fromMill, String requestJson, Object afterObj) throws Exception {
		if (afterObj != null) {
			DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(requestJson);
			jsonCtx.set("$." + AFTER_OBJ_JSON_PATH, afterObj);
			requestJson = jsonCtx.jsonString();
		}
		if (log.isDebugEnabled()) {
			log.debug("compress_overall_by5min request: {}", requestJson);
		}
		
		Request request = new Request("Post", "/" + SOURCE_INDEX_NAME + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("compress_overall_by5min response: {}", respJson);
		}
		DocumentContext respCtx = JsonUtil.buildDefaultJsonPathContext(respJson);
		List<Map<String, Object>> bucketList = respCtx.read("$.aggregations.composite_group.buckets");
		if (CollectionUtils.isEmpty(bucketList)) {
			return;
		}
		
		List<CompressOverall5MinEsDoc> compressList = new ArrayList<>();
		for (Map<String, Object> bucket : bucketList) {
			DocumentContext bucketCtx = JsonUtil.buildDefaultJsonPathContext(bucket);
			compressList.add(CompressOverall5MinEsDoc.popupFromBucket(provinceName, fromMill, bucketCtx));
		}
		save2Index(compressList);
		
		Object afterKey = respCtx.read("$.aggregations.composite_group.after_key");
		if (afterKey != null) {
			processCompressDataList(provinceName, fromMill, requestJson, afterKey);
		}
	}
	
	private void save2Index(List<CompressOverall5MinEsDoc> compressList) throws IOException {
		if (CollectionUtils.isEmpty(compressList)) {
			return;
		}
		StringBuilder sendContent = new StringBuilder();
		String meta = "{\"index\" : { \"_index\" : \"compress_overall_fivemin\", \"_type\" : \"doc\"}}";
		compressList.stream().forEach(data -> {
			sendContent.append(meta).append('\n').append(data.toEsDoc()).append('\n');
		});
		
		Request request = new Request("Post", "/_bulk");
		request.setJsonEntity(sendContent.toString());
		if (log.isDebugEnabled()) {
			log.debug("Begin to bulk submit to index compress_overall_fivemin with data size {}.", compressList.size());
		}
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		if (StringUtils.isNotBlank(respJson) && respJson.contains("\"errors\":true")) {
			log.error("Got the bulk submit response for index {}, but may be some errors, detail: {}", 
					"compress_overall_fivemin", respJson);
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Got the response for bulk submit to index compress_overall_fivemin: {}", respJson);
		}
	}
}
