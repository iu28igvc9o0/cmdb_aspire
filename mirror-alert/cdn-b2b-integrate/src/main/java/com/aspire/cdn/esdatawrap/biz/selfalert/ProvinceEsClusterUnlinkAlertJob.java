package com.aspire.cdn.esdatawrap.biz.selfalert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.IElasticSearchBizRunAction;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.config.LabelContentHolder;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;
import com.aspire.cdn.esdatawrap.config.model.ProvinceEsUnlinkConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;


/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: ProvinceEsClusterUnlinkAlertJob
 * <p/>
 *
 * 类功能描述: 各省es集群断连检测及告警
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年9月3日  
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
@ConditionalOnProperty(name="local.config.es-run-action.province-es-unlink.switch", matchIfMissing=false)
final class ProvinceEsClusterUnlinkAlertJob implements IElasticSearchBizRunAction {
	@Autowired
	private LabelContentHolder				labelHolder;
	@Autowired
	private RestHighLevelClient				restClient;
	@Autowired(required = false)
	private ProvinceEsUnlinkConfigProps		provinceEsUnlinkConfigProps;
	@Autowired
	private MetricAlertConfigProps			metricAlertConfigProps;
	@Autowired
	private MetricAlertHelper				metricAlertHelper;

	private ScheduledThreadPoolExecutor		executor;

	private static final DateTimeFormatter	TIME_FORMAT				= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final String				ITEM_KEY				= "province_es_cluster_unlink";
	public static final String				THEME					= "远程ES集群通讯断连";
	public static final String				ALERT_CONTENT_TEMPLATE	= "%s 检测到 %sES集群与集团ES通讯断连.";
	
	@Override
	public void doAction() {
		scheduleProvinceEsUnlinkRawAlert();
	}
	
	private void scheduleProvinceEsUnlinkRawAlert() {
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(1);
		executor.scheduleWithFixedDelay(buildTask(), 0, provinceEsUnlinkConfigProps.getIntervalMinutes(), TimeUnit.MINUTES);
	}
	
	private Runnable buildTask() {
		// 获取分布式锁
		// String lockName = "lock_province_es_unlink";
		// final Lock runLock = disLockHelper.getLock(lockName);
		return new Runnable() {
			@Override
			public void run() {
				try {
					// 获取分布式锁
					// if (!runLock.tryLock()) {	
					// 	return;
					// }
					
					runProvinceEsUnlinkAlert();
				} catch (Throwable e) {
					log.error("Error when check provinces of es cluster unlink status alerts.", e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private void runProvinceEsUnlinkAlert() throws Exception {
		log.info("Begin to check provinces of es cluster unlink status alerts.");
		Request request = new Request("Get", "/_remote/info");
		Response response = restClient.getLowLevelClient().performRequest(request);
		String respJson = EntityUtils.toString(response.getEntity());
		if (log.isDebugEnabled()) {
			log.debug("Get provinces es cluster info response: {}", respJson);
		}
		
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String,Object>>() {};
		Map<String, Object> repsMap = JsonUtil.jacksonConvert(respJson, typeRef);
		for (Map.Entry<String, Object> entry : repsMap.entrySet()) {
			String provinceCode = entry.getKey();
			DocumentContext clusterInfoCtx = JsonUtil.buildDefaultJsonPathContext(entry.getValue());
			
			MetricAlert metricAlert = null;
			if (!clusterInfoCtx.read("$.connected", Boolean.class).booleanValue()) {
				metricAlert = generateProvinceEsUnlinkMetricAlert(provinceCode, MetricAlert.MONI_RESULT_ACTIVE);
			} else {
				metricAlert = generateProvinceEsUnlinkMetricAlert(provinceCode, MetricAlert.MONI_RESULT_REVOKE);
			}
			metricAlertHelper.processMetricAlert(metricAlert);
		}
	}
	
	private MetricAlert generateProvinceEsUnlinkMetricAlert(String provinceCode, Integer moniResult) {
		TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String,String>>() {}; 
		Map<String, String> provinceCodeMapName = labelHolder.getJacksonBaseObjectByLabelKey(
				"province_code_map_name", null, typeRef);
		MetricAlert alert = new MetricAlert();
		alert.setProvince_name(provinceCodeMapName.get(provinceCode));
		String businessSource = metricAlertConfigProps.getBusinessSource();
		alert.setBusiness_source(businessSource);
		alert.setSource_identity("B2BELK");
		if ("OTT".equalsIgnoreCase(businessSource)) {
			alert.setSource_identity("OTTELK");
		}
		alert.setMoni_target_key(String.join("|", alert.getSource_identity(), businessSource, provinceCode));
		Map<String, Object> moniTargetReferObj = new HashMap<>();
		moniTargetReferObj.put("sourceIdentity", alert.getSource_identity());
		moniTargetReferObj.put("businessSource", businessSource);
		moniTargetReferObj.put("provinceCode", provinceCode);
		alert.setMoni_target_refer_obj(moniTargetReferObj);
		alert.setItem_key(ITEM_KEY);
		alert.setAlert_level(MetricAlert.ALERT_LEVEL_HIGH);
		alert.setMoni_result(moniResult);
		long currTime = System.currentTimeMillis();
		alert.setAlert_time(currTime);
		alert.setLast_alert_time(currTime);
		alert.setTheme(THEME);
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currTime), ZoneId.systemDefault());
		String timeFormat = TIME_FORMAT.format(localTime);
		alert.setContent(String.format(ALERT_CONTENT_TEMPLATE, timeFormat, provinceCodeMapName.get(provinceCode)));
		alert.setValue_type(MetricAlert.ItemValueType.VAL_TEXT);
		alert.setValue_text("断连");
		return alert;
	}
}
