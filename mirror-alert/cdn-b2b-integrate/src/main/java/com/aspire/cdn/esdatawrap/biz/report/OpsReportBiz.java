package com.aspire.cdn.esdatawrap.biz.report;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.aspire.cdn.esdatawrap.anno.CdnReportCacheMark;
import com.aspire.cdn.esdatawrap.biz.report.model.AlertStatisticReportItem;
import com.aspire.cdn.esdatawrap.biz.report.model.CustomOpsReportParam;
import com.aspire.cdn.esdatawrap.biz.report.model.CustomOpsReportResponse;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllNosyncStatistics;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllNosyncStatistics.BarItemVal;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllNosyncStatistics.PieItemVal;
import com.aspire.cdn.esdatawrap.biz.report.model.DistrictWholeInAllSyncStatistics;
import com.aspire.cdn.esdatawrap.biz.report.model.DropDownEntry;
import com.aspire.cdn.esdatawrap.biz.report.model.ReqDomainQueryParams;
import com.aspire.cdn.esdatawrap.biz.report.model.ReqDomainReportByHourParams;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.OpsReportConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: OpsReportBiz
 * <p/>
 *
 * 类功能描述: 运营报表
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年7月20日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Lazy
@Slf4j
@Service
@ConditionalOnExpression("${opsReport.switch:false}")
public class OpsReportBiz {
	private static final Integer		PAST_HOURS					= 24;
	private static final String			SOURCE_INDEX_NAME			= "overall_performance";
//	private static final String			INDEX_FIVE_MINIUTS_COMPRESS	= "compress_overall_fivemin";
	private static final String			METRIC_ALERT_INDEX			= "metric_alert";
	private static final String			CUSTOM_QUERY_KEY_PREFIX		= "customOpsReport_template_";
	private static final String			DOMAIN_QUERY_KEY_PREFIX		= "query_reqdomainList_byHour_";
	

	@Autowired
	private RestHighLevelClient					restClient;

	@Autowired
	private LabelContentHolder					labelHolder;
	
	@Autowired
	private OpsReportCacher						reportCache;
	
	@Autowired
	private	OpsReportConfigProps				reportConfigProps;
	
	private final Map<String, String>			provinceNameMapCode = new HashMap<>();
	private final Map<String, String>			provinceCodeMapName = new HashMap<>();
	
	
	@PostConstruct
	private void init() {
		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String,String>>() {}; 
		Map<String, String> codeMapName = labelHolder.getJacksonBaseObjectByLabelKey(
				"province_code_map_name", null, typeRef);
		provinceCodeMapName.putAll(codeMapName);
		for (Map.Entry<String, String> entry : provinceCodeMapName.entrySet()) {
			provinceNameMapCode.put(entry.getValue(), entry.getKey());
		}
	}
	
	public List<String> getWholeProvinceNamelist() {
		return new ArrayList<String>(provinceNameMapCode.keySet());
	}
	
	/***************************************************************************************************************************/
	/********************************************** 	用 	户	 维	度	 报 	表	************************************************/
	/***************************************************************************************************************************/
	
	/** 
	 * 功能描述: 各省、全国带宽  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchBandwidthReport() {
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.BANDWIDTH_REPORT, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchBandwidthReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.BANDWIDTH_REPORT)
	private DistrictWholeInAllSyncStatistics fetchBandwidthReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("b2bBandwidthReportQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double bandwidth = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].bandwidth.value", 0d);
					response.addDistrictTimeVal(province, timeMark, trimSubnum(bandwidth, 2));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchBandwidthReport method.", e);
		}
	}
	
	private DocumentContext queryReportEsData(String labelKey, Map<String, ?> varibles, String indexName) throws Exception {
		String requestJson = labelHolder.getContentByLabelKey(labelKey, varibles);
		Request request = new Request("Post", "/" + indexName + "/_search");
		request.setJsonEntity(requestJson);
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("QueryReportEsData response: {}", respJson);
		}
		return JsonUtil.buildDefaultJsonPathContext(respJson);
	}
	
	private List<Pair<Long, String>> resolvePastHoursIntervalLabels() {
		List<Pair<Long, String>> resultList = new ArrayList<>();
		long now = System.currentTimeMillis();
		now = now - now % TimeUnit.HOURS.toMillis(1);
		long intervalMill = now - TimeUnit.HOURS.toMillis(PAST_HOURS);
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
		while (intervalMill < now) {
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			LocalDateTime interval = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(intervalMill + TimeUnit.HOURS.toMillis(1)), ZoneId.systemDefault());
			resultList.add(Pair.of(intervalMill, interval.format(timeFormat)));
			intervalMill += TimeUnit.HOURS.toMillis(1);
		}
		return resultList;
	}
	
	private List<Pair<Long, String>> resolvePastHoursIntervalLabels(Long startTime, Long endTime) {
		List<Pair<Long, String>> resultList = new ArrayList<>();
		startTime = startTime - startTime % TimeUnit.HOURS.toMillis(1);
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
		Long intervalMill = startTime;
		while (intervalMill < endTime) {
			LocalDateTime interval = LocalDateTime.ofInstant(Instant.ofEpochMilli(intervalMill), ZoneId.systemDefault());
			resultList.add(Pair.of(intervalMill, interval.format(timeFormat)));
			intervalMill += TimeUnit.HOURS.toMillis(1);
		}
		return resultList;
	}
	
	/** 
	 * 功能描述: 精确到小数位  
	 * <p>
	 * @param source
	 * @return
	 */
	private Double trimSubnum(Double source, int subNum) {
		if (source == null) {
			return 0d;
		}
		Double result = source.doubleValue() * Math.pow(10, subNum);
		Long powResult = result.longValue();
		return powResult / Math.pow(10, subNum);
	}
	
	/** 
	 * 功能描述: 各省、全国服务成功率  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchServiceSuccPercentReport() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.SERVICE_SUCC_PERCENT_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchServiceSuccPercentReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.SERVICE_SUCC_PERCENT_REPORT)
	private DistrictWholeInAllNosyncStatistics fetchServiceSuccPercentReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("b2bServiceSuccPercentReportQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			Long wholeCountry5xxCount = 0l;
			Long wholeCountryReqCount = 0l;
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double okPercent = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].okPercent.value", 0d);
					response.addDistrictTimeVal(province, timeMark, trimSubnum(okPercent * 100, 2));
					Number province5xxCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "]['5xxCount'].value");
					Number provinceReqCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value");
					province5xxCount = province5xxCount == null ? 0l : province5xxCount;
					provinceReqCount = provinceReqCount == null ? 0l : provinceReqCount;
					wholeCountry5xxCount += province5xxCount.longValue();
					wholeCountryReqCount += provinceReqCount.longValue();
				}
			}
			double wholeCountry5xxPercent = wholeCountry5xxCount.doubleValue() / wholeCountryReqCount.doubleValue();
			wholeCountry5xxPercent = trimSubnum(wholeCountry5xxPercent * 100, 2);
			response.addWholeInallValueItem(100 - wholeCountry5xxPercent, wholeCountry5xxPercent);
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchServiceSuccPercentReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 各省、全国请求命中率  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchFileHitSucPercentReport() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.FILE_HIT_SUC_PERCENT_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchFileHitSucPercentReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.FILE_HIT_SUC_PERCENT_REPORT)
	private DistrictWholeInAllNosyncStatistics fetchFileHitSucPercentReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("b2bFileHitsSuccPercentReportQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			Double wholeCountryHitCount = 0d;
			Double wholeCountryReqCount = 0d;
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double okPercent = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].hitPercent.value", 0d);
					response.addDistrictTimeVal(province, timeMark, trimSubnum(okPercent * 100, 2));
					wholeCountryHitCount += JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].fileHitCount.value", 0d);
					wholeCountryReqCount += JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value", 0d);
				}
			}
			double wholeCountryHitPercent = wholeCountryHitCount.doubleValue() / wholeCountryReqCount.doubleValue();
			wholeCountryHitPercent = trimSubnum(wholeCountryHitPercent * 100, 2);
			response.addWholeInallValueItem(wholeCountryHitPercent, trimSubnum(100 - wholeCountryHitPercent, 2));
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchFileHitSucPercentReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 各省、全国请求数  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchRequestCountReport() {
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.REQUEST_COUNT_REPORT, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchRequestCountReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.REQUEST_COUNT_REPORT)
	private DistrictWholeInAllSyncStatistics fetchRequestCountReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("b2bRequestCountReportQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double reqCount = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value", 0d);
					response.addDistrictTimeVal(province, timeMark, reqCount);
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchRequestCountReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 各省回包成功率  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchReturnStatusSucPercentReport() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.RETURN_STATUS_SUC_PERCENT_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchReturnStatusSucPercentReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.RETURN_STATUS_SUC_PERCENT_REPORT)
	private DistrictWholeInAllNosyncStatistics fetchReturnStatusSucPercentReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("returnStatusSucPercentQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					String key = "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].statusGroup.buckets";
					Long sucReturnCount = 0l;
					Long totalReturnCount = 0l;
					int statusGroup = respCtx.read(key + ".length()");
					for (int m = 0; m < statusGroup; m++) {
						Number returnCount = respCtx.read(key + "[" + m + "].doc_count", Number.class);
						returnCount = returnCount == null ? new Integer(0) : returnCount; 
						if ("true".equals(respCtx.read(key + "[" + m + "].key_as_string", String.class))) {
							sucReturnCount += returnCount.longValue();
						}
						totalReturnCount += returnCount.longValue();
					}
					double returnSucPercent = totalReturnCount == 0l ? 
							0l : sucReturnCount.doubleValue() / totalReturnCount.doubleValue();
					response.addDistrictTimeVal(province, timeMark, trimSubnum(returnSucPercent * 100, 2));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchReturnStatusSucPercentReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 各省/全国下载速度报表  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchDownloadRateByProvinceReport() {
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.DOWNLOADRATE_BY_PROVINCE_REPORT, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchDownloadRateByProvinceReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.DOWNLOADRATE_BY_PROVINCE_REPORT)
	private DistrictWholeInAllSyncStatistics fetchDownloadRateByProvinceReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("b2bDownloadRateByProvince", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					String provinceDownRateKey = "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].downloadRate.value";
					Double provinceDownRate = JsonUtil.readWithDefault(respCtx, provinceDownRateKey, 0d);
					response.addDistrictTimeVal(province, timeMark, trimSubnum(provinceDownRate, 2));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchDownloadRateByProvinceReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 各省首包延迟  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchInitBitDelayTimeReport() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.INITBIT_DELAY_TIME_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchInitBitDelayTimeReport0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.INITBIT_DELAY_TIME_REPORT)
	private DistrictWholeInAllNosyncStatistics fetchInitBitDelayTimeReport0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			DocumentContext respCtx = queryReportEsData("initBitDelayAvgTimeQuery", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					String key = "$.aggregations.termsProvince.buckets[" + i + "].aggsHours.buckets[" + k + "].initDelayValue.value";
					Double avgInitBitDelayTime = JsonUtil.readWithDefault(respCtx, key, 0d);
					response.addDistrictTimeVal(province, timeMark, trimSubnum(avgInitBitDelayTime, 3));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchInitBitDelayTimeReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 全国状态码占比  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchWholeCountryHttpStatusWeight() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.WHOLE_COUNTRY_HTTP_STATUS_WEIGHT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchWholeCountryHttpStatusWeight0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.WHOLE_COUNTRY_HTTP_STATUS_WEIGHT)
	private DistrictWholeInAllNosyncStatistics fetchWholeCountryHttpStatusWeight0() {
		try {
			long now = System.currentTimeMillis();
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", now - TimeUnit.HOURS.toMillis(PAST_HOURS));
			params.put("timestamp_lte", now);
			DocumentContext respCtx = queryReportEsData("wholeCountryHttpStatusQuery", params, SOURCE_INDEX_NAME);
			Double total200 = JsonUtil.readWithDefault(respCtx, "$.aggregations.total200.value", 0d);
			Double total206 = JsonUtil.readWithDefault(respCtx, "$.aggregations.total206.value", 0d);
			Double total301 = JsonUtil.readWithDefault(respCtx, "$.aggregations.total301.value", 0d);
			Double total302 = JsonUtil.readWithDefault(respCtx, "$.aggregations.total302.value", 0d);
			Double total4xx = JsonUtil.readWithDefault(respCtx, "$.aggregations.total4xx.value", 0d);
			Double total5xx = JsonUtil.readWithDefault(respCtx, "$.aggregations.total5xx.value", 0d);
			
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics();
			response.addWholeInallValueItem(PieItemVal.of("2xx", total200.longValue() + total206.longValue()));
			response.addWholeInallValueItem(PieItemVal.of("3xx", total301.longValue() + total302.longValue()));
			response.addWholeInallValueItem(PieItemVal.of("4xx", total4xx.longValue()));
			response.addWholeInallValueItem(PieItemVal.of("5xx", total5xx.longValue()));
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchInitBitDelayTimeReport method.", e);
		}
	}
	
	/** 
	 * 功能描述: 全国各省5XX排名和占比  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetch5xxPercentRankByProvince() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.PERCENT_5XX_RANK_BY_PROVINCE, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetch5xxPercentRankByProvince0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.PERCENT_5XX_RANK_BY_PROVINCE)
	private DistrictWholeInAllNosyncStatistics fetch5xxPercentRankByProvince0() {
		try {
			long now = System.currentTimeMillis();
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", now - TimeUnit.HOURS.toMillis(PAST_HOURS));
			params.put("timestamp_lte", now);
			DocumentContext respCtx = queryReportEsData("5xxPercentRankByProvince", params, SOURCE_INDEX_NAME);
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics();
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			if (provinceSize == 0) {
				return response;
			}
			
			for (int i = 0; i < provinceSize; i++) {
				String province = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Double provincePercent5xx = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "]['percent_5xx'].value", 0d);
				BarItemVal barItem = new BarItemVal();
				barItem.addAttr("province", province);
				barItem.addAttr("percent", trimSubnum(provincePercent5xx * 100, 6));
				response.addWholeInallValueItem(barItem);
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchInitBitDelayTimeReport method.", e);
		}
	}
	
	/***************************************************************************************************************************/
	/********************************************** 	集 	中	 监	 控	 统 	计	************************************************/
	/***************************************************************************************************************************/
	
	/** 
	 * 功能描述: 集中监控统计1  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic01Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_01_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic01Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_01_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic01Report0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			
			DocumentContext respCtx = queryReportEsData("queryHourlyDatagramReqCount", params, SOURCE_INDEX_NAME);
			Integer timeItemCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < timeItemCount; i++) {
				Long timeMill = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Long reqCount = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].totalCount.value", 0d).longValue();
				response.addDistrictTimeVal("*", timeMill, reqCount);
			}
			
			Map<String, Object> statisticMap = new HashMap<>();
			respCtx = queryReportEsData("queryTermsByProvinceName", params, SOURCE_INDEX_NAME);
			Integer provinceSize = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			statisticMap.put("totalProvince", provinceSize);
			
			respCtx = queryReportEsData("queryTermsByManufacture", params, SOURCE_INDEX_NAME);
			Integer manuSize = respCtx.read("$.aggregations.termsManufacture.buckets.length()");
			statisticMap.put("totalManufacture", manuSize);

			respCtx = queryReportEsData("queryTermsByReqDomain", params, SOURCE_INDEX_NAME);
			Integer reqDomainSize = respCtx.read("$.aggregations.termsReqDomain.buckets.length()");
			statisticMap.put("totalReqDomain", reqDomainSize);
			
			respCtx = queryReportEsData("queryTermsByNodeIp", params, SOURCE_INDEX_NAME);
			Integer nodeIpSize = respCtx.read("$.aggregations.termsNodeIp.buckets.length()");
			statisticMap.put("totalNodeIp", nodeIpSize);
			
			response.addWholeInallValueItem(statisticMap);
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic01Report0 method.", e);
		}
	}
	
	/** 
	 * 功能描述: 集中监控统计2  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic02Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_02_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic02Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_02_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic02Report0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			
			DocumentContext respCtx = queryReportEsData("queryConcentrate5xxTotalReqCount", params, SOURCE_INDEX_NAME);
			Double req5xxCount = respCtx.read("$.aggregations['5xxCount']['value']");
			Double totalReqCount = respCtx.read("$.aggregations['totalCount']['value']");	
			
			double req5xxPercent = req5xxCount / totalReqCount;
			req5xxPercent = trimSubnum(req5xxPercent * 100, 2);
			double okPercent = trimSubnum(100 - req5xxPercent, 2);
			Map<String, Double> percentMap = new HashMap<>();
			percentMap.put("okPercent", okPercent);
			percentMap.put("req5xxPercent", req5xxPercent);
			response.addWholeInallValueItem(percentMap);
			
			respCtx = queryReportEsData("queryConcentrateDatagramFilesize", params, SOURCE_INDEX_NAME);
			Integer timeItemCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < timeItemCount; i++) {
				Long timeMill = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Double humanReadFileSize = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].humanReadFileSize.value", 0d);
				response.addDistrictTimeVal("*", timeMill, trimSubnum(humanReadFileSize, 2));
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic02Report0 method.", e);
		}
	}
	
	/** 
	 * 功能描述: 集中监控统计3  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic03Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_03_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic03Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_03_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic03Report0() {
		try {
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics();
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			
			DocumentContext respCtx = queryReportEsData("queryConcentrateFileHitsSuccPercent", params, SOURCE_INDEX_NAME);
			Double fileHitCount = respCtx.read("$.aggregations['fileHitCount']['value']");
			Double totalReqCount = respCtx.read("$.aggregations['totalCount']['value']");	
			
			final Map<String, Object> itemMap = new HashMap<>();
			response.addWholeInallValueItem(itemMap);
			
			double fileHitPercent = (totalReqCount == null || totalReqCount <= 0d) ? 0d : trimSubnum(fileHitCount * 100/totalReqCount, 2);
			double unFileHitPercent = trimSubnum(100 - fileHitPercent, 2);
			Map<String, Double> fileHitPercentMap = new HashMap<>();
			fileHitPercentMap.put("fileHitPercent", fileHitPercent);
			fileHitPercentMap.put("unFileHitPercent", unFileHitPercent);
			itemMap.put("fileHit", fileHitPercentMap);
			
			final List<PieItemVal> prov5xxPercentList = new ArrayList<>();
			respCtx = queryReportEsData("queryProvinceCountry5xxPercent", params, SOURCE_INDEX_NAME);
			Integer provinceCount = respCtx.read("$.aggregations.termsProvince.buckets.length()");
			Double wholeCountry5xxCount = respCtx.read("$.aggregations.sumWholeCountry5xxCount.value");
			for (int i = 0; i < provinceCount; i++) {
				String provinceName = respCtx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
				Double prov5xxCount = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsProvince.buckets[" + i + "].sum5xxCount.value", 0d);
				Double prov5xxPercent = trimSubnum(prov5xxCount * 100 / wholeCountry5xxCount, 2);
				if (prov5xxPercent < 0.1) {
					continue;
				}
				prov5xxPercentList.add(new PieItemVal(provinceName, prov5xxPercent));
			}
			itemMap.put("province5xxPercent", prov5xxPercentList);
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic03Report0 method.", e);
		}
	}
	
	/** 
	 * 功能描述: 集中监控统计4  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic04Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_04_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic04Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_04_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic04Report0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			Long startMill = intervalLabelList.get(0).getKey();
			Long endMill = intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1);
			params.put("timestamp_gte", startMill);
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", endMill);
			params.put("timespan", (endMill - startMill) / 1000);
			
			DocumentContext respCtx = queryReportEsData("queryWholeCountryInHourServiceThroughout", params, SOURCE_INDEX_NAME);
			Integer bucketCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < bucketCount; i++) {
				Long timemark = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Double inHourThroughout = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].inHourServiceThroughput.value", 0d);
				response.addDistrictTimeVal("*", timemark, trimSubnum(inHourThroughout, 1));
			}
			
			respCtx = queryReportEsData("queryTop10CpnameByServiceThroughout", params, SOURCE_INDEX_NAME);
			bucketCount = respCtx.read("$.aggregations.termsCpname.buckets.length()");
			for (int i = 0; i < bucketCount; i++) {
				String cpname = respCtx.read("$.aggregations.termsCpname.buckets[" + i + "].key");
				Double filesizeInGB = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCpname.buckets[" + i + "].sumFilesizeInGB.value", 0d);
				BarItemVal barItem = new BarItemVal();
				barItem.addAttr("cpname", cpname);
				barItem.addAttr("filesizeInGB", trimSubnum(filesizeInGB, 2));
				response.addWholeInallValueItem(barItem);
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic04Report0 method.", e);
		}
	}
	
	/** 
	 * 功能描述: 集中监控统计5  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic05Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_05_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic05Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_05_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic05Report0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			
			DocumentContext respCtx = queryReportEsData("queryConcentrateInitBitDelayAvgTime", params, SOURCE_INDEX_NAME);
			Integer bucketCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < bucketCount; i++) {
				Long timemark = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Double initDelayValue = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].initDelayValue.value", 0d);
				response.addDistrictTimeVal("initBitDelay", timemark, trimSubnum(initDelayValue, 2));
			}
			
			respCtx = queryReportEsData("queryConcentrateDayDownloadRateByHour", params, SOURCE_INDEX_NAME);
			bucketCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < bucketCount; i++) {
				Long timemark = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Double inHourDownloadRate = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].downloadRate.value", 0d);
				response.addDistrictTimeVal("downloadRate", timemark, trimSubnum(inHourDownloadRate, 2));
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic05Report0 method.", e);
		}
	}
	
	
	/** 
	 * 功能描述: 集中监控统计6  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics concentrateStatistic06Report() {
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				OpsReportCacher.CONCENTRATE_STATISTIC_06_REPORT, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : concentrateStatistic06Report0();
	}
	
	@CdnReportCacheMark(OpsReportCacher.CONCENTRATE_STATISTIC_06_REPORT)
	private DistrictWholeInAllNosyncStatistics concentrateStatistic06Report0() {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			
			DocumentContext respCtx = queryReportEsData("queryWholeCountryInHourServiceThroughout", params, SOURCE_INDEX_NAME);
			Integer bucketCount = respCtx.read("$.aggregations.aggsHours.buckets.length()");
			for (int i = 0; i < bucketCount; i++) {
				Long timemark = respCtx.read("$.aggregations.aggsHours.buckets[" + i + "].key");
				Double inHourBandwith = JsonUtil.readWithDefault(respCtx, "$.aggregations.aggsHours.buckets[" + i + "].bandwidth.value", 0d);
				response.addDistrictTimeVal("bandwidth", timemark, trimSubnum(inHourBandwith, 2));
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call concentrateStatistic06Report0 method.", e);
		}
	}
	
	
	/***************************************************************************************************************************/
	/********************************************** 	地 	市	 维	度	 报 	表	************************************************/
	/***************************************************************************************************************************/
	
	/** 
	 * 功能描述: 省内各市服务成功率  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchCitybaseServiceSuccPercent(String provinceName) {
		String provinceCode = provinceNameMapCode.get(provinceName);
		if (provinceCode == null) {
			throw new RuntimeException("There is no province code attached to the province with name: " + provinceName);
		}
		String cacheKey = String.format(OpsReportCacher.CITY_BASE_SERVICE_SUCC_PERCENT_REPORT, provinceCode);
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				cacheKey, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchCitybaseServiceSuccPercent0(provinceCode);
	}
	
	@CdnReportCacheMark(value=OpsReportCacher.CITY_BASE_SERVICE_SUCC_PERCENT_REPORT,
			loopKeys={"ah","bj","fj","gs","gd","gx","gz","hi","he","ha","hlj","hb","hn","jl","js",
					"jx","ln","nm","nx","sd","sx","sn","sh","sc","tj","xz","xj","yn","zj","cq","qh"})
	private DistrictWholeInAllNosyncStatistics fetchCitybaseServiceSuccPercent0(String provinceCode) {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("citybaseServiceSuccPercent", params, SOURCE_INDEX_NAME);
			Integer citySize = respCtx.read("$.aggregations.termsCities.buckets.length()");
			if (citySize == 0) {
				return response;
			}
			
			Long wholeProvince5xxCount = 0l;
			Long wholeProvinceReqCount = 0l;
			for (int i = 0; i < citySize; i++) {
				String city = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double okPercent = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].okPercent.value", 0d);
					response.addDistrictTimeVal(city, timeMark, trimSubnum(okPercent * 100, 2));
					Number city5xxCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "]['5xxCount'].value");
					Number cityReqCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value");
					city5xxCount = city5xxCount == null ? 0l : city5xxCount;
					cityReqCount = cityReqCount == null ? 0l : cityReqCount;
					wholeProvince5xxCount += city5xxCount.longValue();
					wholeProvinceReqCount += cityReqCount.longValue();
				}
			}
			double wholeProvince5xxPercent = wholeProvince5xxCount.doubleValue() / wholeProvinceReqCount.doubleValue();
			wholeProvince5xxPercent = trimSubnum(wholeProvince5xxPercent * 100, 2);
			response.addWholeInallValueItem(100 - wholeProvince5xxPercent, wholeProvince5xxPercent);
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchCitybaseServiceSuccPercent0 method with param: " + provinceCode, e);
		}
	}
	
	/** 
	 * 功能描述: 省内各市请求命中率  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllNosyncStatistics fetchCitybaseFileHitSucPercentReport(String provinceName) {
		String provinceCode = provinceNameMapCode.get(provinceName);
		if (provinceCode == null) {
			throw new RuntimeException("There is no province code attached to the province with name: " + provinceName);
		}
		String cacheKey = String.format(OpsReportCacher.CITY_BASE_FILE_HIT_SUC_PERCENT_REPORT, provinceCode);
		DistrictWholeInAllNosyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				cacheKey, DistrictWholeInAllNosyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchCitybaseFileHitSucPercentReport0(provinceCode);
	}
	
	@CdnReportCacheMark(value=OpsReportCacher.CITY_BASE_FILE_HIT_SUC_PERCENT_REPORT,
	loopKeys={"ah","bj","fj","gs","gd","gx","gz","hi","he","ha","hlj","hb","hn","jl","js",
			"jx","ln","nm","nx","sd","sx","sn","sh","sc","tj","xz","xj","yn","zj","cq","qh"})
	private DistrictWholeInAllNosyncStatistics fetchCitybaseFileHitSucPercentReport0(String provinceCode) {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("citybaseFileHitsSuccPercentReportQuery", params, SOURCE_INDEX_NAME);
			Integer citySize = respCtx.read("$.aggregations.termsCities.buckets.length()");
			if (citySize == 0) {
				return response;
			}
			
			Double wholeProvinceHitCount = 0d;
			Double wholeProvinceReqCount = 0d;
			for (int i = 0; i < citySize; i++) {
				String city = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double okPercent = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].hitPercent.value", 0d);
					response.addDistrictTimeVal(city, timeMark, trimSubnum(okPercent * 100, 2));
					wholeProvinceHitCount += JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].fileHitCount.value", 0d);
					wholeProvinceReqCount += JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value", 0d);
				}
			}
			double wholeProvinceHitPercent = wholeProvinceHitCount.doubleValue() / wholeProvinceReqCount.doubleValue();
			wholeProvinceHitPercent = trimSubnum(wholeProvinceHitPercent * 100, 2);
			response.addWholeInallValueItem(wholeProvinceHitPercent, trimSubnum(100 - wholeProvinceHitPercent, 2));
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchCitybaseFileHitSucPercentReport0 method with param: " + provinceCode, e);
		}
	}
	
	/** 
	 * 功能描述: 省内各市、全省带宽  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchCitybaseBandwidthReport(String provinceName) {
		String provinceCode = provinceNameMapCode.get(provinceName);
		if (provinceCode == null) {
			throw new RuntimeException("There is no province code attached to the province with name: " + provinceName);
		}
		String cacheKey = String.format(OpsReportCacher.CITY_BASE_BANDWIDTH_REPORT, provinceCode);
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				cacheKey, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchCitybaseBandwidthReport0(provinceCode);
	}
	
	@CdnReportCacheMark(value=OpsReportCacher.CITY_BASE_BANDWIDTH_REPORT,
	loopKeys={"ah","bj","fj","gs","gd","gx","gz","hi","he","ha","hlj","hb","hn","jl","js",
			"jx","ln","nm","nx","sd","sx","sn","sh","sc","tj","xz","xj","yn","zj","cq","qh"})
	private DistrictWholeInAllSyncStatistics fetchCitybaseBandwidthReport0(String provinceCode) {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("citybaseBandwidthReportQuery", params, SOURCE_INDEX_NAME);
			Integer citySize = respCtx.read("$.aggregations.termsCities.buckets.length()");
			if (citySize == 0) {
				return response;
			}
			
			for (int i = 0; i < citySize; i++) {
				String city = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double bandwidth = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].bandwidth.value", 0d);
					response.addDistrictTimeVal(city, timeMark, trimSubnum(bandwidth, 2));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchCitybaseBandwidthReport0 method with param: " + provinceCode, e);
		}
	}
	
	/** 
	 * 功能描述: 地市/全省下载速度报表  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchCitybaseDownloadRateReport(String provinceName) {
		String provinceCode = provinceNameMapCode.get(provinceName);
		if (provinceCode == null) {
			throw new RuntimeException("There is no province code attached to the province with name: " + provinceName);
		}
		String cacheKey = String.format(OpsReportCacher.CITY_BASE_DOWNLOADRATE_BY_PROVINCE_REPORT, provinceCode);
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				cacheKey, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchCitybaseDownloadRateReport0(provinceCode);
	}
	
	@CdnReportCacheMark(value=OpsReportCacher.CITY_BASE_DOWNLOADRATE_BY_PROVINCE_REPORT,
	loopKeys={"ah","bj","fj","gs","gd","gx","gz","hi","he","ha","hlj","hb","hn","jl","js",
			"jx","ln","nm","nx","sd","sx","sn","sh","sc","tj","xz","xj","yn","zj","cq","qh"})
	private DistrictWholeInAllSyncStatistics fetchCitybaseDownloadRateReport0(String provinceCode) {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("citybaseDownloadRateByProvince", params, SOURCE_INDEX_NAME);
			Integer citySize = respCtx.read("$.aggregations.termsCities.buckets.length()");
			if (citySize == 0) {
				return response;
			}
			
			for (int i = 0; i < citySize; i++) {
				String city = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					String cityDownRateKey = "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].downloadRate.value";
					Double cityDownRate = JsonUtil.readWithDefault(respCtx, cityDownRateKey, 0d);
					response.addDistrictTimeVal(city, timeMark, trimSubnum(cityDownRate, 2));
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchCitybaseDownloadRateReport0 method with param: " + provinceCode, e);
		}
	}
	

	/** 
	 * 功能描述: 各市、全省请求数  <br/> 
	 * </pre>
	 */
	public DistrictWholeInAllSyncStatistics fetchCitybaseReqCountReport(String provinceName) {
		String provinceCode = provinceNameMapCode.get(provinceName);
		if (provinceCode == null) {
			throw new RuntimeException("There is no province code attached to the province with name: " + provinceName);
		}
		String cacheKey = String.format(OpsReportCacher.CITY_BASE_REQUEST_COUNT_REPORT, provinceCode);
		DistrictWholeInAllSyncStatistics cacheResult = reportCache.getCacheReportResultByKey(
				cacheKey, DistrictWholeInAllSyncStatistics.class);
		return cacheResult != null ? cacheResult : fetchCitybaseReqCountReport0(provinceCode);
	}
	
	@CdnReportCacheMark(value=OpsReportCacher.CITY_BASE_REQUEST_COUNT_REPORT,
	loopKeys={"ah","bj","fj","gs","gd","gx","gz","hi","he","ha","hlj","hb","hn","jl","js",
			"jx","ln","nm","nx","sd","sx","sn","sh","sc","tj","xz","xj","yn","zj","cq","qh"})
	private DistrictWholeInAllSyncStatistics fetchCitybaseReqCountReport0(String provinceCode) {
		try {
			List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
			DistrictWholeInAllSyncStatistics response = new DistrictWholeInAllSyncStatistics(intervalLabelList);
			
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 date histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("citybaseRequestCountReportQuery", params, SOURCE_INDEX_NAME);
			Integer citySize = respCtx.read("$.aggregations.termsCities.buckets.length()");
			if (citySize == 0) {
				return response;
			}
			
			for (int i = 0; i < citySize; i++) {
				String city = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].key");
				Integer timeItemCount = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					Double reqCount = JsonUtil.readWithDefault(respCtx, "$.aggregations.termsCities.buckets[" + i + "].aggsHours.buckets[" + k + "].totalCount.value", 0d);
					response.addDistrictTimeVal(city, timeMark, reqCount);
				}
			}
			return response;
		} catch (Exception e) {
			throw new RuntimeException("Error when call fetchCitybaseReqCountReport0 method with param: " + provinceCode, e);
		}
	}
	
	
	/***************************************************************************************************************************/
	/********************************************** 	告 	警	 统	计	 报 	表	************************************************/
	/***************************************************************************************************************************/
	
	/** 
	 * 功能描述: 根据请求域名统计的top10数量告警  
	 * <p>
	 * @return
	 */
	public List<AlertStatisticReportItem> queryTop10AlertStatisticByReqDomain() {
		List<AlertStatisticReportItem> resultList = new ArrayList<>();
		try {
			DocumentContext respCtx = queryReportEsData("queryTop10AlertStatisticByReqDomain", null, METRIC_ALERT_INDEX);
			Integer bucketSize = respCtx.read("$.aggregations.termsReqDomain.buckets.length()");
			if (bucketSize == 0) {
				return resultList;
			}
			
			for (int i = 0; i < bucketSize; i++) {
				Number occurCount = respCtx.read("$.aggregations.termsReqDomain.buckets[" + i + "].doc_count");
				String reqDomain = respCtx.read("$.aggregations.termsReqDomain.buckets[" + i + "].key");
				String cpName = respCtx.read("$.aggregations.termsReqDomain.buckets[" + i + "].termsCpname.buckets[0].key");
				AlertStatisticReportItem reportItem = new AlertStatisticReportItem();
				reportItem.setCpName(cpName);
				reportItem.setReqDomain(reqDomain);
				reportItem.setOccurCount(occurCount.intValue());
				resultList.add(reportItem);
			}
			return resultList;
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryTop10AlertStatisticByReqDomain method.", e);
		}
	}
	
	public List<AlertStatisticReportItem> queryTop10AlertStatisticReport() {
		List<AlertStatisticReportItem> resultList = new ArrayList<>();
		try {
			DocumentContext respCtx = queryReportEsData("queryTop10AlertStatisticReport", null, METRIC_ALERT_INDEX);
			Integer bucketSize = respCtx.read("$.aggregations.termsAlertKey.buckets.length()");
			if (bucketSize == 0) {
				return resultList;
			}
			
			for (int i = 0; i < bucketSize; i++) {
				String businessSource = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsBusinessSource.buckets[0].key");
				String province = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsProvince.buckets[0].key"); 
				String manufacture = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsManufacture.buckets[0].key"); 
				String cpName = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsCpname.buckets[0].key"); 
				String reqDomain = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsReqDomain.buckets[0].key"); 
				String alertTheme = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].termsAlertTheme.buckets[0].key"); 
				Number occurCount = respCtx.read("$.aggregations.termsAlertKey.buckets[" + i + "].doc_count"); 
				AlertStatisticReportItem reportItem = new AlertStatisticReportItem();
				reportItem.setBusinessSource(businessSource);
				reportItem.setProvinceName(province);
				reportItem.setManufacture(manufacture);
				reportItem.setCpName(cpName);
				reportItem.setReqDomain(reqDomain);
				reportItem.setAlertTitle(alertTheme);
				reportItem.setOccurCount(occurCount.intValue());
				resultList.add(reportItem);
			}
			return resultList;
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryTop10AlertStatisticReport method.", e);
		}
	}
	
	public List<AlertStatisticReportItem> queryTop10AlertStatisticByManufacture() {
		List<AlertStatisticReportItem> resultList = new ArrayList<>();
		try {
			DocumentContext respCtx = queryReportEsData("queryTop10AlertStatisticByManufacture", null, METRIC_ALERT_INDEX);
			Integer bucketSize = respCtx.read("$.aggregations.termsManufacture.buckets.length()");
			if (bucketSize == 0) {
				return resultList;
			}
			
			for (int i = 0; i < bucketSize; i++) {
				String manufacture = respCtx.read("$.aggregations.termsManufacture.buckets[" + i + "].key");
				Number occurCount = respCtx.read("$.aggregations.termsManufacture.buckets[" + i + "].doc_count");
				AlertStatisticReportItem reportItem = new AlertStatisticReportItem();
				reportItem.setManufacture(manufacture);
				reportItem.setOccurCount(occurCount.intValue());
				resultList.add(reportItem);
			}
			return resultList;
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryTop10AlertStatisticByManufacture method.", e);
		}
	}
	
	/***************************************************************************************************************************/
	/************************************************* 	自 	定	 义	 报 	表	****************************************************/
	/***************************************************************************************************************************/
	public List<DropDownEntry> loadCustomReportKeyValList() {
		List<DropDownEntry> resultList = new ArrayList<>();
		for (Map.Entry<String, String> entry : reportConfigProps.getCustomReportDefineList().entrySet()) {
			resultList.add(new DropDownEntry(entry.getKey(), entry.getValue()));
		}
		return resultList;
	}
	
	public List<DropDownEntry> loadProvinceKeyValList() {
		List<DropDownEntry> resultList = new ArrayList<>();
		for (Map.Entry<String, String> entry : provinceCodeMapName.entrySet()) {
			resultList.add(new DropDownEntry(entry.getKey(), entry.getValue()));
		}
		return resultList;
	}
	
	public List<DropDownEntry> loadProvinceCityKeyValList(String provinceCode) {
		List<DropDownEntry> resulList = new ArrayList<>();
		try	{
			Map<String, String> varibles = Collections.singletonMap("provinceName", provinceCodeMapName.get(provinceCode));
			DocumentContext respCtx = queryReportEsData("queryProvinceCityKeyValList", varibles, SOURCE_INDEX_NAME);
			int bucketSize = respCtx.read("$.aggregations.termsCity.buckets.length()", Number.class).intValue();
			for (int i = 0; i < bucketSize; i++) {
				String cityName = respCtx.read("$.aggregations.termsCity.buckets[" + i + "].key");
				resulList.add(new DropDownEntry(cityName, cityName));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error when call loadProvinceCityKeyValList method.", e);
		}
		return resulList;
	}
	
	public CustomOpsReportResponse queryCustomReportDataList(CustomOpsReportParam queryParam) {
		try {
			String labelKey = CUSTOM_QUERY_KEY_PREFIX + queryParam.getReportKey();
			Map<String, Object> varibles = new HashMap<>();
			varibles.put("timestamp_gte", queryParam.getStartTimeMill());
			varibles.put("timestamp_lt", queryParam.getEndTimeTimeMill());
			varibles.put("span_seconds", (queryParam.getEndTimeTimeMill() - queryParam.getStartTimeMill()) / 1000);
			varibles.put("districtFiled", "province_name");
			varibles.put("provinceMatchCondition", "");
			if (StringUtils.isNotBlank(queryParam.getProvinceCode())) {
				varibles.put("provinceMatchCondition", ",\n" + queryParam.getProvinceQueryCondStr(provinceCodeMapName));
				varibles.put("districtFiled", "city_name");
			}
			varibles.put("cityMatchCondition", "");
			if (StringUtils.isNotBlank(queryParam.getCity())) {
				varibles.put("cityMatchCondition", ",\n" + queryParam.getCityQueryCondStr());
			}
			DocumentContext respCtx = queryReportEsData(labelKey, varibles, SOURCE_INDEX_NAME);
			return CustomOpsReportResponse.resolveQueryResult(queryParam, respCtx);
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryCustomReportDataList method.", e);
		}
	}
	
	public List<String> queryReqDomainListByKey(ReqDomainQueryParams queryObj) {
		List<String> resultList = new ArrayList<>();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("query_keyword", queryObj.getQueryKeyword());
			if (StringUtils.isBlank(queryObj.getStartTime())) {
				List<Pair<Long, String>> intervalLabelList = resolvePastHoursIntervalLabels();
				params.put("timestamp_gte", intervalLabelList.get(0).getKey());
				// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
				params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			} else {
				params.put("timestamp_gte", queryObj.getStartTimeMills());
				params.put("timestamp_lt", queryObj.getEndTimeMills());
			}
			DocumentContext respCtx = queryReportEsData("query_domain_list_by_keyword", params, SOURCE_INDEX_NAME);
			Integer size = respCtx.read("$.aggregations.termsReqDomain.buckets.length()");
			for (int i = 0; i < size; i++) {
				resultList.add(respCtx.read("$.aggregations.termsReqDomain.buckets[" + i + "].key", String.class));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryReqDomainListByKey method.", e);
		}
		return resultList;
	}
	
	/***************************************************************************************************************************/
	/************************************************* 	域  名	指   标    ****************************************************/
	/***************************************************************************************************************************/
	public List<DropDownEntry> loadReqDomainReportKeyValList() {
		List<DropDownEntry> resultList = new ArrayList<>();
		for (Map.Entry<String, String> entry : reportConfigProps.getDomainReportDefineList().entrySet()) {
			resultList.add(new DropDownEntry(entry.getKey(), entry.getValue()));
		}
		return resultList;
	}
	
	public DistrictWholeInAllNosyncStatistics queryReqDomainListCompareReportByKey(ReqDomainReportByHourParams queryParam) {
		List<Pair<Long, String>> intervalLabelList = null;
		if (StringUtils.isNoneBlank(queryParam.getStartTime())) {
			intervalLabelList = resolvePastHoursIntervalLabels(queryParam.getStartTimeMills(), queryParam.getEndTimeMills());
		} else {
			intervalLabelList = resolvePastHoursIntervalLabels();
		}
		DistrictWholeInAllNosyncStatistics response = new DistrictWholeInAllNosyncStatistics(intervalLabelList);
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("timestamp_gte", intervalLabelList.get(0).getKey());
			// 由于ES中 histogram 是从每个小时开始算一个点，所以需要往后延迟一个小时
			params.put("timestamp_lt", intervalLabelList.get(intervalLabelList.size() - 1).getKey() + TimeUnit.HOURS.toMillis(1));
			params.put("join_reqDomain_list", queryParam.getJoinReqdomainArrText());
			String reportKey = queryParam.getReportKey();
			DocumentContext respCtx = queryReportEsData(DOMAIN_QUERY_KEY_PREFIX + reportKey, params, SOURCE_INDEX_NAME);
			Integer domainSize = respCtx.read("$.aggregations.compositeGroup.buckets.length()");
			if (domainSize == 0) {
				return response;
			}
			for (int i = 0; i < domainSize; i++) {
				String domain = respCtx.read("$.aggregations.compositeGroup.buckets[" + i + "].key.reqDomainGroup");
				Integer timeItemCount = respCtx.read("$.aggregations.compositeGroup.buckets[" + i + "].aggsHours.buckets.length()");
				for (int k = 0; k < timeItemCount; k++) {
					Long timeMark = respCtx.read("$.aggregations.compositeGroup.buckets[" + i + "].aggsHours.buckets[" + k + "].key"); 
					
                    if ("returnStatusSucPercent".equals(reportKey)) {
                    	String key = "$.aggregations.compositeGroup.buckets[" + i + "].aggsHours.buckets[" + k + "].statusGroup.buckets";
    					Long sucReturnCount = 0l;
    					Long totalReturnCount = 0l;
    					int statusGroup = respCtx.read(key + ".length()");
    					for (int m = 0; m < statusGroup; m++) {
    						Number returnCount = respCtx.read(key + "[" + m + "].doc_count", Number.class);
    						returnCount = returnCount == null ? new Integer(0) : returnCount; 
    						if ("true".equals(respCtx.read(key + "[" + m + "].key_as_string", String.class))) {
    							sucReturnCount += returnCount.longValue();
    						}
    						totalReturnCount += returnCount.longValue();
    					}
    					double returnSucPercent = totalReturnCount == 0l ? 
    							0l : sucReturnCount.doubleValue() / totalReturnCount.doubleValue();
    					response.addDistrictTimeVal(domain, timeMark, trimSubnum(returnSucPercent * 100, 2));
                    } else {
                    	Double itemVal = JsonUtil.readWithDefault(respCtx, "$.aggregations.compositeGroup.buckets[" + i + "].aggsHours.buckets[" + k + "]['" + reportKey + "'].value", 0d);
                    	response.addDistrictTimeVal(domain, timeMark, trimSubnum(itemVal, 2));
                    }
				}
				
			}
		} catch (Exception e) {
			throw new RuntimeException("Error when call queryReqDomainReportByKey() method.", e);
		}
		return response;
	}
	
	public static void main(String[] args) throws Exception {
//		Instant now = Instant.now();
//		LocalDateTime localTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
//		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
//		System.out.println(timeFormat.format(now));
//		System.out.println(localTime.format(timeFormat));
		
//		LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//	    System.out.println(beginTime.toEpochSecond(OffsetDateTime.now().getOffset()));
		
		
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		
//	    //获取当前日期
//	    LocalDate nowDate = LocalDate.now();
//	    //设置零点
//	    LocalDateTime beginTime = LocalDateTime.of(nowDate,LocalTime.MIN);
//	    //将时间进行格式化
//	    String time1= beginTime.format(dtf);
//
//	    //设置当天的结束时间
//	    LocalDateTime endTime = LocalDateTime.of(nowDate,LocalTime.MAX);
//	    //将时间进行格式化
//	    String time2 =dtf.format(endTime);
//
//	    System.out.println("今天开始的时间beginTime："+time1);
//	    System.out.println("今天结束的时间endTime："+time2);
		
//		List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\pengguihua\\Desktop\\result.json"));
//		DocumentContext ctx = JsonUtil.buildDefaultJsonPathContext(StringUtils.join(lines, '\n'));
//		
//		int size = ctx.read("$.aggregations.termsProvince.buckets.length()");
//		for (int i = 0; i < size; i++) {
//			String provinceName = ctx.read("$.aggregations.termsProvince.buckets[" + i + "].key");
//			Double percent = ctx.read("$.aggregations.termsProvince.buckets[" + i + "].okPercent.value");
//			System.out.println(provinceName + "\t" + trimSubnum2(percent * 100, 2) + "%");
//		}
	}
	
	private static Double trimSubnum2(Double source, int subNum) {
		Double result = source.doubleValue() * Math.pow(10, subNum);
		Long powResult = result.longValue();
		return powResult / Math.pow(10, subNum);
	}
}
