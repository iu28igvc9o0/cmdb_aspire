package com.aspire.cdn.esdatawrap.biz;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.config.DomainMapCpNameHolder;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.ReIndexConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: OverrallPerformanceReIndexJob
 * <p/>
 *
 * 类功能描述: 从各省CDN索引复制数据到  集团  overall_performance 索引
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月12日  
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
@ConditionalOnProperty(name="local.config.es-run-action.reindex.switch", matchIfMissing=true)
final class OverrallPerformanceReIndexJob implements IElasticSearchBizRunAction {
	private static final String			FORMAT_PROVINCE_CDN_INDEX			= "%s:cdn_performance_v1*";
	private static final String			OVERALL_PERFORMANCE_INDEX			= "overall_performance";
//	private static final String			OVERALL_PERFORMANCE_INDEX			= "pgh_test_performance";
	private static final String 		STORED_SCRIPT_ID					= "convert_cpname_by_reqdomain";
	private static final String 		PROVINCE_REIDX_TIMEMARK_INDEX		= "province_reidx_timemark";
	private static final String 		ITEM_KEY_PROVINCE_DATA_SYNC			= "province_esdata_sync";
	private static final String 		THEME_PROVINCE_NO_ES_DATA_SYNC		= "未同步到CDN日志数据.";
	private static final String 		CONTENT_PROVINCE_NO_ES_DATA_SYNC	= "%s 从%s 未能同步到CDN日志数据.";
	private static final DateTimeFormatter	TIME_FORMAT						= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private ReIndexConfigProps			reIdxProps;
	
	@Autowired
	private DomainMapCpNameHolder		domainCpNameMap;

	@Autowired
	private RestHighLevelClient			restClient;
	
	@Autowired
	private LabelContentHolder			labelHolder;
	
	@Autowired
	private MetricAlertHelper 			metricAlertHelper; 
	
	private final Map<String, String>	provinceCodeMapName = new HashMap<>();
	
	private ScheduledThreadPoolExecutor	executor;
	
	
	@PostConstruct
	private void init() {
		String json = labelHolder.getContentByLabelKey("province_code_map_name");
		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String,String>>() {};
		provinceCodeMapName.putAll(JsonUtil.jacksonConvert(json, typeRef));
	}
	
	@Override
	public void doAction() {
		try {
			scheduleReindx();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void scheduleReindx() {
		List<String> provinceList = reIdxProps.getProvinceList();
		if (CollectionUtils.isEmpty(provinceList)) {
			throw new RuntimeException("The 'local.config.reindex.source-provinces' config item is absent.");
		}
		executor = new ScheduledThreadPoolExecutor(provinceList.size(), new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(provinceList.size());
		for (String province : provinceList) {
			executor.scheduleWithFixedDelay(buildTask(province), 0, reIdxProps.getRunInterval().getSeconds(), TimeUnit.SECONDS);
		}
	}
	
	private Runnable buildTask(final String province) {
		// 获取分布式锁
		// String lockName = OVERALL_PERFORMANCE_INDEX + "_reindex_" + province;
		// final Lock runLock = disLockHelper.getLock(lockName);
		return () -> {
			try {
				// 获取分布式锁
				// if (!runLock.tryLock()) {	
				// 	return;
				// }
				String fromIdx = String.format(FORMAT_PROVINCE_CDN_INDEX, province);
				log.info("Begin to reindex {} from {}.", OVERALL_PERFORMANCE_INDEX, fromIdx);
				runReIndexFromProvince(province);
			} catch (Throwable e) {
				log.error("Error to run reindex {} from {}-cdn.", OVERALL_PERFORMANCE_INDEX, province, e);
			} finally {
				// runLock.unlock();
			}
		};
	}
	
	/** 
	 * 功能描述: 针对每个省份CDN运行重建索引  
	 * <p>
	 * @param province
	 */
	private void runReIndexFromProvince(final String province) throws Exception {
		ReindexRequest request = new ReindexRequest();
		request.setMaxRetries(2);
		request.setSlices(3);
		request.setSourceBatchSize(reIdxProps.getSingleReqBatchCount());
		request.setRefresh(false);
		request.setTimeout(TimeValue.timeValueMillis(reIdxProps.getReidxSpanTime().toMillis()));
		
		String fromIdx = String.format(FORMAT_PROVINCE_CDN_INDEX, province);
		request.setSourceIndices(fromIdx);
		RangeQueryBuilder rangeBuilder = new RangeQueryBuilder("recordtime");
		
		Pair<Long, Integer> lastMark = getReindexTimeMark(province);
		Long gtTime = lastMark.getLeft();
		Integer tryCount = lastMark.getRight();
		
		Long limitTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1);
		Long expectTime = gtTime + reIdxProps.getReidxSpanTime().toMillis();
		Long lteTime = expectTime > limitTime ? limitTime : expectTime;

		if (gtTime >= lteTime) {
			return;
		}
		rangeBuilder.gt(gtTime);
		rangeBuilder.lte(lteTime);
		request.setSourceQuery(rangeBuilder);
		String writeIndexName = getTargetWriteIndexName(OVERALL_PERFORMANCE_INDEX);
		request.setDestIndex(writeIndexName);
		request.setDestDocType("doc");
		request.setConflicts("proceed");
		request.setDestOpType("create");
		request.setRefresh(true);
		request.setDestVersionType(VersionType.EXTERNAL);
		
		Map<String, Object> mapParams = new HashMap<>(domainCpNameMap.getDomainCpNameMatch());
		Script parseScript = new Script(ScriptType.STORED, null, STORED_SCRIPT_ID, mapParams);
		request.setScript(parseScript);
		
		if (log.isDebugEnabled()) {
			logRequestBody(request);
		}
		
		BulkByScrollResponse reidxResp = null;
		try {
			reidxResp = restClient.reindex(request, RequestOptions.DEFAULT);
		} catch (Exception e) {
			log.error("Error when reindex from {} to {}.", fromIdx, writeIndexName, e);
			return;
		}
		
		log.info("Reindex from {} to {} with detail: {}.", fromIdx, writeIndexName, reidxResp);
		if (reidxResp.getCreated() > 0) {
			updateReIdxTimeMark(true, province, gtTime, writeIndexName, 0);
			if (tryCount > 0) {
				triggerNoDataSyncMetricAlert(province, gtTime, MetricAlert.MONI_RESULT_REVOKE);
			}
		} 
		else if (tryCount < reIdxProps.getSyncNodataMaxTryCount().intValue()) {
			updateReIdxTimeMark(false, province, gtTime, writeIndexName, tryCount + 1);
		}
		else {
			// 同步时间往后递增, 避免当时间段数据为空时, 反复读取该时间段导致后续的数据也无法读取的问题
			long updateTimeMark = gtTime + tryCount * reIdxProps.getReidxSpanTime().toMillis();
			updateTimeMark = updateTimeMark > System.currentTimeMillis() ? System.currentTimeMillis() : updateTimeMark;
			updateReIdxTimeMark(false, province, updateTimeMark, writeIndexName, 0);
			triggerNoDataSyncMetricAlert(province, gtTime, MetricAlert.MONI_RESULT_ACTIVE);
		}
	}
	
	private void triggerNoDataSyncMetricAlert(String provinceCode, Long syncTime, Integer moniResult) {
		if (!reIdxProps.getSyncNodataAlert()) {
			log.warn("As the configuration switch is off, the no sync data alert will be ignored.");
			return;
		}
		log.info("Begin to trigger no cdn data sync alert for province {} at time {} with moniResult {}.", 
				provinceCode, syncTime, moniResult);
		String businessSource = "B2B";
		String sourceIdenity = "B2BELK";
		String provinceName = provinceCodeMapName.get(provinceCode);
		MetricAlert alert = new MetricAlert();
		alert.setProvince_name(provinceName);
		alert.setSource_identity(sourceIdenity);
		alert.setBusiness_source(businessSource);
		alert.setMoni_target_key(String.join("|", sourceIdenity, businessSource, provinceName, "ES数据同步"));
		Map<String, Object> moniTargetReferObj = new HashMap<>();
		moniTargetReferObj.put("sourceIdentity", sourceIdenity);
		moniTargetReferObj.put("businessSource", businessSource);
		moniTargetReferObj.put("provinceName", provinceName);
		moniTargetReferObj.put("bizKey", "ES数据同步");
		alert.setMoni_target_refer_obj(moniTargetReferObj);
		alert.setItem_key(ITEM_KEY_PROVINCE_DATA_SYNC);
		alert.setAlert_level(MetricAlert.ALERT_LEVEL_HIGH);
		alert.setMoni_result(moniResult);
		alert.setAlert_time(syncTime);
		alert.setLast_alert_time(syncTime);
		alert.setTheme(THEME_PROVINCE_NO_ES_DATA_SYNC);
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(syncTime), ZoneId.systemDefault());
		String timeFormat = TIME_FORMAT.format(localTime);
		alert.setContent(String.format(CONTENT_PROVINCE_NO_ES_DATA_SYNC, timeFormat, provinceName));
		alert.setValue_type(MetricAlert.ItemValueType.VAL_TEXT);
		alert.setValue_text("无数据");
		Map<String, Object> referInfo = new HashMap<>();
		alert.setRefer_info(referInfo);
		metricAlertHelper.processMetricAlert(alert);
	}
	
	/** 
	 * 功能描述: 获取指定省份的reIdx的时间戳  
	 * <p>
	 * @param provinceCode
	 * @param writeIndexName
	 * @return
	 * @throws Exception
	 */
	private Pair<Long, Integer> getReindexTimeMark(String provinceCode) throws Exception {
		Map<String, Object> varibles = Collections.singletonMap("provinceCode", provinceCode);
		DocumentContext respCtx = queryEsDataByLabelKey("getProvinceReidxTimeMark", varibles, PROVINCE_REIDX_TIMEMARK_INDEX);
		Integer hitSize = respCtx.read("$.hits.hits.length()", Number.class).intValue();
		if (hitSize > 0) {
			Integer tryCount = respCtx.read("$.hits.hits[0]['_source'].try_count", Integer.class);
			tryCount = tryCount == null ? 0 : tryCount.intValue();
			Long timeMark = respCtx.read("$.hits.hits[0]['_source'].reidx_timemark", Number.class).longValue();
			return Pair.of(timeMark, tryCount);
		}
		return Pair.of(System.currentTimeMillis() - reIdxProps.getReidxSpanTime().toMillis(), 0);
	}
	
	/** 
	 * 功能描述: 更新指定省份的reIdx的时间戳  
	 * <p>
	 * @param provinceCode
	 * @param lastTimeMark
	 * @param writeIdxName
	 * @throws Exception
	 */
	private void updateReIdxTimeMark(boolean isSyncSucc, String provinceCode, Long lastTimeMark, 
				String writeIdxName, Integer tryCount) throws Exception {
		Long finalUpdateTime = lastTimeMark;
		if (isSyncSucc) {
//			Map<String, Object> varibles = Collections.singletonMap("provinceName", provinceCodeMapName.get(provinceCode));
			Map<String, Object> varibles = new HashMap<>();
			varibles.put("provinceName", provinceCodeMapName.get(provinceCode));
			varibles.put("timeBaseColumn", "recordtime");
			
			DocumentContext respCtx = queryEsDataByLabelKey("fetchProvinceDataLatestTimeMark", varibles, writeIdxName);
			Integer hitsCount = respCtx.read("$.hits.hits.length()", Number.class).intValue();
			if (hitsCount > 0) {
				Long dataLatestTimeMark = respCtx.read("$.aggregations.maxTime.value", Number.class).longValue();
				if (dataLatestTimeMark < lastTimeMark) {
					log.warn("As the newest timemark of province {} is less than the last reidx timemark, "
							+ "the update reidxTimemark will be ignored.", provinceCodeMapName.get(provinceCode));
				} 
				else if (dataLatestTimeMark.longValue() == lastTimeMark.longValue()) {
					log.warn("As the newest timemark of province {} is equal with the last reidx timemark, "
							+ "the update reidxTimemark will be ignored.", provinceCodeMapName.get(provinceCode));
				} else {
					finalUpdateTime = dataLatestTimeMark;
				}
			}
		}
		
		Map<String, Object> varibles = new HashMap<>();
		varibles.put("provinceCode", provinceCode);
		varibles.put("redixTimemark", finalUpdateTime);
		varibles.put("logTime", System.currentTimeMillis());
		varibles.put("tryCount", tryCount);
		log.info("Begin to update reIdxTimeMark for province {} with value {}, tryCount {}.", provinceCode, finalUpdateTime, tryCount);
		String updateJson = labelHolder.getContentByLabelKey("updateProvinceReidxTimeMark", varibles);
		Request request = new Request("Put", "/" + PROVINCE_REIDX_TIMEMARK_INDEX + "/doc/" + provinceCode);
		request.setJsonEntity(updateJson);
		restClient.getLowLevelClient().performRequest(request);
	}
	
	private DocumentContext queryEsDataByLabelKey(String labelKey, Map<String, ?> varibles, String indexName) throws Exception {
		String requestJson = labelHolder.getContentByLabelKey(labelKey, varibles);
		Request request = new Request("Post", "/" + indexName + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("QueryEsData response: {}", respJson);
		}
		return JsonUtil.buildDefaultJsonPathContext(respJson);
	}
	
	private String getTargetWriteIndexName(String indexAlias) throws Exception {
		Request request = new Request("Get", "/" + indexAlias);
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
		Map<String, Object> respMap = JsonUtil.jacksonConvert(respJson, typeRef);
		for (Map.Entry<String, Object> entry : respMap.entrySet()) {
			String indexName = entry.getKey();
			Object detail = entry.getValue();
			DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(detail);
			Boolean isWriteIdx = jsonCtx.read("$.aliases." + indexAlias + ".is_write_index");
			if (isWriteIdx) {
				return indexName;
			}
		}
		return indexAlias;
	}
	
	private void logRequestBody(ReindexRequest request) {
		try {
			BytesRef source = XContentHelper.toXContent(request, XContentType.JSON, ToXContent.EMPTY_PARAMS, false).toBytesRef();
			System.out.println(new String(source.bytes));
		} catch (Exception e) {
			log.error(null, e);
		}
	}
	
	@PreDestroy
	private void preDestroy() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}
}
