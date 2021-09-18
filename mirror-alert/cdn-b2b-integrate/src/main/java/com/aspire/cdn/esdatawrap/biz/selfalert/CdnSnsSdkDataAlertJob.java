package com.aspire.cdn.esdatawrap.biz.selfalert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.client.ICdnSnsSdkClient;
import com.aspire.cdn.esdatawrap.biz.domain.CdnSnsQualityDataModel;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;
import com.aspire.cdn.esdatawrap.client.ClientServiceBuilder;
import com.aspire.cdn.esdatawrap.config.model.CdnSnsSdkConfigProps;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.aspire.cdn.esdatawrap.util.Md5Util;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: CdnSnsSdkDataAlertJob
 * <p/>
 *
 * 类功能描述: CDN-SNS-SDK 质量数据抓取及告警
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@ConditionalOnProperty(name="cdnsnssdk.switch", matchIfMissing=false)
final class CdnSnsSdkDataAlertJob {
	private static final String			SIGN_TEMPLATE	= "ts%suser%stoken%s";	// ts{$timestamp}user{$user_name}token{$token}
	
	private static final String			MOBILE_CDN		= "mobile_cdn";	
	private static final String			MOBILE_CARRIER	= "移动";

	@Autowired
	private CdnSnsSdkConfigProps		snsSdkConfig;

	private ScheduledThreadPoolExecutor	executor;

	private ICdnSnsSdkClient			cdnSnsSdkClient;
	
//	@Autowired 
//	private UmsAlertSendHelper 			umsAlertSendHelper;
	
	@Autowired
	private MetricAlertHelper  			metricAlertHelper;
	
	@PostConstruct
	private void scheduleFetchCdnSnsSdkData() {
		List<String> appNameList = snsSdkConfig.getAppNameList();
		if (CollectionUtils.isEmpty(appNameList)) {
			log.warn("There is no appnameList configed for cdnsnssdk client to fetch.");
			return;
		}
		
		cdnSnsSdkClient = ClientServiceBuilder.buildClientService(ICdnSnsSdkClient.class, snsSdkConfig.getUrl());
		
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getClass().getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(appNameList.size());
		appNameList.forEach(appName -> {
			executor.scheduleAtFixedRate(buildTask(appName), 1, snsSdkConfig.getIntervalMinutes(), TimeUnit.MINUTES);
		});
	}
	
	private Runnable buildTask(String appName) {
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
					
					log.info("Begin to fetch cdn sns sdk data for {} at {}.", appName, System.currentTimeMillis());
					DocumentContext dataCtx = fetchCdnSnsSdkData(appName);
					
					if (!checkSnsSdkData(dataCtx, appName)) {
						return;
					}
					
					for (String groupKey : CdnSnsQualityDataModel.GROUP_KEY_LIST) {
						try {
							processCdnSnsSdkDataAlert(dataCtx, appName, groupKey);
						} catch (Throwable e) {
							log.error("Error when process cdn-sns-sdk-datalist for '{}' with groupKey '{}'", appName, groupKey, e);
						}
					}
					
				} catch (Throwable e) {
					log.error("Error when fetch and generate cdn sns sdk data and alerts.", e);
				} finally {
					// runLock.unlock();
				}
			}
		};
	}
	
	private DocumentContext fetchCdnSnsSdkData(String appName) {
		long nowSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		String sign = Md5Util.getMD5String(String.format(SIGN_TEMPLATE, nowSeconds, snsSdkConfig.getUserName(), snsSdkConfig.getToken()));
		Object resp = cdnSnsSdkClient.realtimeQualityData(nowSeconds, snsSdkConfig.getUserName(), sign, appName);
		return JsonUtil.buildDefaultJsonPathContext(resp);
	}
	
	private boolean checkSnsSdkData(DocumentContext dataCtx, String appName) {
		if (dataCtx == null) {
			log.error("The sns sdk data is null or empty for {}.", appName);
			return false;
		}
		Number codeNum = dataCtx.read("$.code", Number.class);
		if (codeNum == null || codeNum.intValue() != CdnSnsQualityDataModel.SUCCESS_CODE) {
			log.error("The code of the sns sdk data is {} for {}, error detail: {}.", codeNum, appName, dataCtx.read("$.message"));
			return false;
		}
		return true;
	}
	
	/** 
	 * 功能描述: 处理生成告警  
	 * <p>
	 * @param dataCtx
	 * @param appName
	 * @param groupKey
	 */
	private void processCdnSnsSdkDataAlert(DocumentContext dataCtx, String appName, String groupKey) {
		TypeRef<List<CdnSnsQualityDataModel>> typeRef = new TypeRef<List<CdnSnsQualityDataModel>>() {};
		List<CdnSnsQualityDataModel> dataList = dataCtx.read("$." + groupKey, typeRef);
		if (dataList != null) {
			dataList = dataList.stream().filter(data -> {
				return MOBILE_CDN.equalsIgnoreCase(data.getCdn()) && MOBILE_CARRIER.equals(data.getCarrier());
			}).collect(Collectors.toList());
		}
		if (dataList == null || dataList.isEmpty()) {
			log.warn("There is no sns sdk data for groupkey {}.", groupKey);
			return;
		}
		// 填充信息
		dataList.forEach(data -> data.poupuMetaInfo(appName, groupKey));
		dataList = dataList.stream().filter(data -> {
			Pair<Boolean, String> checkResult = data.checkDataValid();
			if (!checkResult.getLeft()) {
				log.info("The cdn-sns-sdk data is invalid, datail: " + checkResult.getRight());
			}
			return checkResult.getLeft();
		}).collect(Collectors.toList());
		
		
		List<MetricAlert> alertList = new ArrayList<>();
		dataList.forEach(data -> {
			Double errorPercent = data.getError();
			Double brokenPercent = data.getBrokenCount();
			Double buffPercent = data.getBufferCount();
			errorPercent = errorPercent == null ? 0d : errorPercent;
			brokenPercent = brokenPercent == null ? 0d : brokenPercent;
			buffPercent = buffPercent == null ? 0d : buffPercent;
			
			if (errorPercent + brokenPercent + buffPercent >= snsSdkConfig.getGeneralErrorPercentTherehold()) {
				alertList.add(data.parse2MetricAlert(true, CdnSnsQualityDataModel.ALERT_GENERAL_SERVICE_PECENT));
			} 
			else {
				alertList.add(data.parse2MetricAlert(false, CdnSnsQualityDataModel.ALERT_GENERAL_SERVICE_PECENT));
			}
			
			if (data.getFirstFrameAllTime() >= snsSdkConfig.getFirstFrameDataTimeTherehold()) {
				alertList.add(data.parse2MetricAlert(true, CdnSnsQualityDataModel.ALERT_FIRST_FRAME_TIME_EXCEED));
			} 
			else {
				alertList.add(data.parse2MetricAlert(false, CdnSnsQualityDataModel.ALERT_FIRST_FRAME_TIME_EXCEED));
			}
		});
		
//		umsAlertSendHelper.batchSendUmsAlertMessage(MetricAlert.parse2UmsAlertMsgList(alertList));
		metricAlertHelper.processMetricAlertBatch(alertList);
	}
}
